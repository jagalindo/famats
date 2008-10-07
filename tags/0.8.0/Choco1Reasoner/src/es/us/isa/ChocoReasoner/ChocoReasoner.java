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
package es.us.isa.ChocoReasoner;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import choco.*;
import choco.integer.IntDomainVar;
import choco.integer.var.IntDomain;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.FeatureModelReasoner;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.models.featureModel.Cardinality;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.GenericRelation;


public class ChocoReasoner extends FeatureModelReasoner{
	
	private Map<String,GenericFeature> features;
	private HashMap<String, IntDomainVar> variables;
	private Map<String,Constraint> dependencies;
	private Problem problem;
	private boolean feasible;
	@SuppressWarnings("unused")
	private boolean reify;
	
	
	public ChocoReasoner() {
		reset();
	}
	
	@Override
	public void reset() {
		
		this.features = new HashMap<String, GenericFeature >();
		this.variables= new HashMap<String, IntDomainVar>();
		this.problem = new Problem();
		this.dependencies = new HashMap<String,Constraint>();
		this.reify=false;
		this.feasible = true;
		
	}
	
	public Problem getProblem() {
		return problem;
	}
	
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	@Override
	public void addRoot(GenericFeature feature) {
		IntDomainVar root = variables.get(feature.getName());
		//Constraint rootConstraint = problem.eq(root,1);
		//problem.post(rootConstraint);
		try {
			root.setInf(1);
			root.setSup(1);
		} catch (ContradictionException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void addMandatory(GenericRelation rel, GenericFeature child,
			GenericFeature parent) {
		IntDomainVar childVar  = variables.get(child.getName());
		IntDomainVar parentVar = variables.get(parent.getName());
		//Constraint mandatoryConstraint = problem.eq(childVar, parentVar);
		Constraint mandatoryConstraint = problem.ifThen(problem.eq(parentVar, 1), problem.eq(childVar, 1));
		Constraint mandatoryConstraint2 = problem.ifThen(problem.eq(parentVar, 0), problem.eq(childVar, 0));
		problem.post(mandatoryConstraint2);
		problem.post(mandatoryConstraint);
		dependencies.put(rel.getName(),mandatoryConstraint);
		
	
	}
	
	@Override
	public void addOptional(GenericRelation rel, GenericFeature child,
			GenericFeature parent) {
		
		IntDomainVar childVar  = variables.get(child.getName());
		IntDomainVar parentVar = variables.get(parent.getName());
		Constraint optionalConstraint = problem.ifThen(problem.eq(parentVar,0),problem.eq(childVar,0));
		problem.post(optionalConstraint);
		dependencies.put(rel.getName(),optionalConstraint);
	}
	
	@Override
	public void addCardinality(GenericRelation rel, GenericFeature child,
			GenericFeature parent, Iterator<Cardinality> cardinalities) {
		
		IntDomainVar childVar  = variables.get(child.getName());
		IntDomainVar parentVar = variables.get(parent.getName());
		Cardinality card;
		
		IntDomain domain= childVar.getDomain();
		//FIXME: The possibility of having more than one set of cardinalities is not contemplated, e.g.: [1..3],[5..9]
		if (cardinalities.hasNext())
			domain.remove(0);
		while(cardinalities.hasNext()) {
			card = cardinalities.next();
			domain.updateInf(card.getMin()-childVar.getInf());
			domain.updateSup(card.getMax()-childVar.getSup());
		}
		
		Constraint constraint = problem.ifThen(problem.eq(parentVar,0),problem.eq(childVar,0));
		if(!constraint.isSatisfied()){
			childVar =problem.makeBoundIntVar(child.getName(), domain.getInf(),domain.getSup());
			variables.put(child.getName(),childVar);
		}
		Constraint constraintGuarra = problem.ifThen(problem.eq(childVar,0),problem.eq(parentVar,0));
		
		
		
		problem.post(constraint);
		problem.post(constraintGuarra);
		dependencies.put(rel.getName(),constraintGuarra);
		dependencies.put(rel.getName(),constraint);
	}

	@Override
	public void addRequires(GenericRelation rel, GenericFeature origin,
			GenericFeature destination) {
		IntDomainVar originVar  = variables.get(origin.getName());
		IntDomainVar destinationVar = variables.get(destination.getName());
		Constraint requieresConstraint = problem.ifThen(problem.gt(originVar,0),problem.gt(destinationVar,0));
		problem.post(requieresConstraint);
		dependencies.put(rel.getName(),requieresConstraint);
	}

	@Override
	public void addExcludes(GenericRelation rel, GenericFeature origin,
			GenericFeature dest) {
	
		IntDomainVar originVar  = variables.get(origin.getName());
		IntDomainVar destVar = variables.get(dest.getName());
		Constraint excludesConstraint = problem.ifThen(problem.gt(originVar,0), problem.eq(destVar, 0));
		problem.post(excludesConstraint);
		dependencies.put(rel.getName(),excludesConstraint);
	}

	@Override
	public void addFeature(GenericFeature f, Collection<Cardinality> cards) {
		features.put(f.getName(), f);	//Save the feature
		Iterator<Cardinality> cardIt = cards.iterator();//Looks for all the cardinality and save it
		IntDomainVar var;
		if(cardIt.hasNext()){
			Cardinality card = cardIt.next();
			var=problem.makeBoundIntVar(f.getName(), 0, card.getMax());
			problem.post(problem.or(problem.leq(var, 1),problem.geq(var, card.getMin())));
						
			//FIXME Consultar con Pablo que no haya cardinalidades multiples [0..1]U[3..6]U[8..9] de momento solo contempla 2	
		
		}else{
			var=problem.makeBoundIntVar(f.getName(), 0, 1);
		}	
		
		this.variables.put(f.getName(),var);
		
	}
	

	@Override
	
	public void addSet(GenericRelation rel, GenericFeature parent,
			Collection<GenericFeature> children, Collection<Cardinality> cardinalities) {
		
		Cardinality card = null;
		//This constraint should be as ifThenElse(A>0;sum(B,C) in {n,m};B=0,C=0)
		//Save the parent to check the value
		IntDomainVar parentVar = variables.get(parent.getName());
		
		//Create a temp variable for the relation
		
		//Save the cardninality if exist from the parameter cardinalities 
		Iterator<Cardinality> itc = cardinalities.iterator();
		while(itc.hasNext()) {
			card = itc.next();
			
		}
		
		// Save all children to have the posiblitily of sum them
		ArrayList<IntDomainVar> varsList = new ArrayList<IntDomainVar>();
		Iterator<GenericFeature> it = children.iterator();
		
		while (it.hasNext()) {
			varsList.add(variables.get(it.next().getName()));
		}
		
		
		// creates the arraylist of constraints for the And constraint,
		//saving all children with 0 value for later use.
		ArrayList<Constraint> localConstraints = new ArrayList<Constraint>();
		it = children.iterator();
		while (it.hasNext()) {
			IntDomainVar childVar = variables.get(it.next().getName());
			//when you create the constraint, it
			
			Constraint hijos=problem.ifThen(problem.eq(parentVar,0),problem.eq(childVar,0));
			localConstraints.add(hijos);
			problem.post(hijos);
		}
		// creates the sum constraint with the cardinality variable 
		//If parent var is equal to 0 then he sum of children has to be 0
		IntDomainVar[] aux = {};
		aux = varsList.toArray(aux);
		Constraint suma = problem.ifThen(problem.eq(parentVar, 0),problem.eq(problem.sum(aux),0));
		problem.post(suma);//add the constraint
		
		//If parent is greater than 0, then apply the restriction ifThenElse(A>0;sum(B,C) in {n,m};B=0,C=0)
		
		Constraint constraint1 = problem.ifThen(problem.gt(parentVar, 0),problem.and(problem.gt(problem.sum(aux), 0),problem.leq(problem.sum(aux), card.getMax()),problem.or(problem.leq(problem.sum(aux),1),(problem.geq(problem.sum(aux), card.getMin())))));
		problem.post(constraint1);
					
		dependencies.put(rel.getName(),constraint1);// save the constraint for later use if needed
		
	
			
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
		this.problem = new Problem();
	}
	
	public boolean isFeasible(){
		if (feasible){
			feasible = this.problem.isFeasible();
		}
		return feasible;
	}
	
	public void setReify(boolean b) {
		this.reify=false;
	}
	
	public HashMap<String, IntDomainVar> getVariables() {
		return variables;
	}
	
	public GenericFeature searchFeatureByName(String id) {
		return features.get(id);
	}

	public Collection<GenericFeature> getAllFeatures(){
		return this.features.values();
	}
	
}
