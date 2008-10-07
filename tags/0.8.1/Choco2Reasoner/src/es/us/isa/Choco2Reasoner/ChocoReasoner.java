/*
	This file is part of FaMaTS.

    FaMaTS is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FaMaTS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FaMaTS.  If not, see <http://www.gnu.org/licenses/>.

 */
package es.us.isa.Choco2Reasoner;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static choco.Choco.*;
import choco.cp.model.CPModel;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.FeatureModelReasoner;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.models.featureModel.Cardinality;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.GenericRelation;


public class ChocoReasoner extends FeatureModelReasoner{
	
	protected Map<String,GenericFeature> features;
	protected HashMap<String, IntegerVariable> variables;
	protected Map<String,Constraint> dependencies;
	protected Map<String,IntegerExpressionVariable> setRelations;
	protected Model problem;
	protected boolean reify;
	
	
	public ChocoReasoner() {
		reset();
	}
	
	@Override
	public void reset() {
		
		this.features = new HashMap<String, GenericFeature >();
		this.variables= new HashMap<String, IntegerVariable>();
		this.problem = new CPModel();
		this.dependencies = new HashMap<String,Constraint>();
		this.setRelations = new HashMap<String,IntegerExpressionVariable>();
		this.reify=false;
		
	}
	
	public Model getProblem() {
		return problem;
	}
	
	public void setProblem(Model problem) {
		this.problem = problem;
	}
	
	@Override
	public void addRoot(GenericFeature feature) {
		IntegerVariable root = variables.get(feature.getName());
		problem.addConstraint(eq(root,1));
	}
	
	@Override
	public void addMandatory(GenericRelation rel, GenericFeature child,
			GenericFeature parent) {
		
		IntegerVariable childVar  = variables.get(child.getName());
		IntegerVariable parentVar = variables.get(parent.getName());
		Constraint mandatoryConstraint = ifOnlyIf(eq(parentVar, 1), eq(childVar, 1));
		dependencies.put(rel.getName(),mandatoryConstraint);
		problem.addConstraint(mandatoryConstraint);
	
	}
	
	@Override
	public void addOptional(GenericRelation rel, GenericFeature child,
			GenericFeature parent) {
		
		IntegerVariable childVar  = variables.get(child.getName());
		IntegerVariable parentVar = variables.get(parent.getName());
		Constraint optionalConstraint = implies(eq(parentVar,0),eq(childVar,0));
		dependencies.put(rel.getName(),optionalConstraint);
		problem.addConstraint(optionalConstraint);
		
	}
	
	@Override
	public void addCardinality(GenericRelation rel, GenericFeature child,
			GenericFeature parent, Iterator<Cardinality> cardinalities) {
		
		IntegerVariable childVar  = variables.get(child.getName());
		IntegerVariable parentVar = variables.get(parent.getName());
		
		SortedSet<Integer> cardValues = new TreeSet<Integer>();
		Iterator<Cardinality> itc = cardinalities;
		while(itc.hasNext()) {
			Cardinality card = itc.next();
			for (int i = card.getMin(); i <= card.getMax();i++)
				cardValues.add(i);
		}
		int[] cardValuesArray = new int[cardValues.size()];
		Iterator<Integer> itcv = cardValues.iterator();
		int pos = 0;
		while (itcv.hasNext()) {
			cardValuesArray[pos] = itcv.next();
			pos++;
		}
		
		IntegerVariable cardinalityVar = makeIntVar(rel.getName()+"_card",cardValuesArray,"cp:no_decision");
		Constraint cardConstraint = ifThenElse(gt(parentVar,0),eq(childVar,cardinalityVar),eq(childVar,0));
		problem.addConstraint(cardConstraint);
		dependencies.put(rel.getName(), cardConstraint);

	}

	@Override
	public void addRequires(GenericRelation rel, GenericFeature origin,
			GenericFeature destination) {
		
		IntegerVariable originVar  = variables.get(origin.getName());
		IntegerVariable destinationVar = variables.get(destination.getName());
		Constraint requieresConstraint = implies(gt(originVar,0),gt(destinationVar,0));
		problem.addConstraint(requieresConstraint);
		dependencies.put(rel.getName(),requieresConstraint);
		
	}

	@Override
	public void addExcludes(GenericRelation rel, GenericFeature origin,
			GenericFeature dest) {
	
		IntegerVariable originVar  = variables.get(origin.getName());
		IntegerVariable destVar = variables.get(dest.getName());
		Constraint excludesConstraint = implies(gt(originVar,0), eq(destVar, 0));
		problem.addConstraint(excludesConstraint);
		dependencies.put(rel.getName(),excludesConstraint);
		
	}

	@Override
	public void addFeature(GenericFeature f, Collection<Cardinality> cards) {
		
		features.put(f.getName(), f);	//Save the feature
		Iterator<Cardinality> cardIt = cards.iterator();//Looks for all the cardinality and save it
		IntegerVariable var;
		SortedSet<Integer> vals = new TreeSet<Integer>();
		
		while(cardIt.hasNext()){
			Cardinality card = cardIt.next();
			int min = card.getMin();
			int max = card.getMax();
			for (int i = min; i <= max; i++){
				vals.add(i);
			}
		}
		
		// we don't have to check if it is already inserted into the set, because
		// no repeated elements are allowed.
		vals.add(0);
		// we convert the ordered set to an array of ints
		int[] domain = new int[vals.size()];
		Iterator<Integer> itv = vals.iterator();
		int pos = 0;
		while (itv.hasNext()) {
			domain[pos] = itv.next();
			pos++;
		}
		var=makeIntVar(f.getName(), domain);
		problem.addVariable(var);
		this.variables.put(f.getName(),var);
		
	}
	

	@Override
	
	public void addSet(GenericRelation rel, GenericFeature parent,
			Collection<GenericFeature> children, Collection<Cardinality> cardinalities) {

		Cardinality card = null;
		//This constraint should be as ifThenElse(A>0;sum(B,C) in {n,m};B=0,C=0)
		//Save the parent to check the value
		IntegerVariable parentVar = variables.get(parent.getName());
		
		//Save the cardninality if exist from the parameter cardinalities 
		SortedSet<Integer> cardValues = new TreeSet<Integer>();
		Iterator<Cardinality> itc = cardinalities.iterator();
		while(itc.hasNext()) {
			card = itc.next();
			for (int i = card.getMin(); i <= card.getMax();i++)
				cardValues.add(i);
		}
		int[] cardValuesArray = new int[cardValues.size()];
		Iterator<Integer> itcv = cardValues.iterator();
		int pos = 0;
		while (itcv.hasNext()) {
			cardValuesArray[pos] = itcv.next();
			pos++;
		}
		
		IntegerVariable cardinalityVar = makeIntVar(rel.getName()+"_card", cardValuesArray,"cp:no_decision");//cp:no_decision
		problem.addVariable(cardinalityVar);
		// Save all children to have the posiblitily of sum them
		ArrayList<IntegerVariable> varsList = new ArrayList<IntegerVariable>();
		Iterator<GenericFeature> it = children.iterator();
		
		while (it.hasNext()) {
			varsList.add(variables.get(it.next().getName()));
		}
		
		// creates the sum constraint with the cardinality variable 
		//If parent var is equal to 0 then he sum of children has to be 0
		IntegerVariable[] aux = {};
		aux = varsList.toArray(aux);

		//If parent is greater than 0, then apply the restriction ifThenElse(A>0;sum(B,C) in {n,m};B=0,C=0)
		Constraint setConstraint = ifThenElse(gt(parentVar,0),eq(sum(aux),cardinalityVar),eq(sum(aux),0));
		problem.addConstraint(setConstraint);//add only this constraint
		dependencies.put(rel.getName(), setConstraint);
		setRelations.put(rel.getName(), sum(aux));
		
	}

	@Override
	public PerformanceResult ask(Question q) {
		
		PerformanceResult res;
		ChocoQuestion chq = (ChocoQuestion)q;
		chq.preAnswer(this);
		res = chq.answer(this);
		chq.postAnswer(this);
		return res;
		
	}

	public void createProblem() {
		this.problem = new CPModel();
	}
	
	public void setReify(boolean b) {
		this.reify=false;
	}
	
	public HashMap<String, IntegerVariable> getVariables() {
		return variables;
	}
	
	public Map<String, IntegerExpressionVariable> getSetRelations(){
		return setRelations;
	}
	
	public Map<String, Constraint> getRelations(){
		return dependencies;
	}
	
	public GenericFeature searchFeatureByName(String id) {
		return features.get(id);
	}

	public Collection<GenericFeature> getAllFeatures(){
		return this.features.values();
	}
	
}
