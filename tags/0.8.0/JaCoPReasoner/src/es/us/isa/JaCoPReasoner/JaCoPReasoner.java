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
package es.us.isa.JaCoPReasoner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import JaCoP.And;
import JaCoP.Constraint;
import JaCoP.Delete;
import JaCoP.FD;
import JaCoP.FDstore;
import JaCoP.IfThen;
import JaCoP.IfThenElse;
import JaCoP.In;
import JaCoP.LargestDomain;
import JaCoP.LargestMin;
import JaCoP.MaxRegret;
import JaCoP.MinDomainOverDegree;
import JaCoP.MostConstrainedDynamic;
import JaCoP.MostConstrainedStatic;
import JaCoP.PrimitiveConstraint;
import JaCoP.SelectVariable;
import JaCoP.SmallestDomain;
import JaCoP.SmallestMax;
import JaCoP.SmallestMin;
import JaCoP.Sum;
import JaCoP.Variable;
import JaCoP.XeqC;
import JaCoP.XeqY;
import JaCoP.XgtC;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.FeatureModelReasoner;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.models.featureModel.Cardinality;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.GenericRelation;

public class JaCoPReasoner extends FeatureModelReasoner {
	private Map<String,GenericFeature> features;
	private GenericFeature root;
	private FDstore store;
	private Map<String,Variable> variables;
	private ArrayList<Constraint> constraints;
	private Map<Constraint,GenericRelation> relationConstraintMap;
	protected boolean consistent;
	protected boolean reify;
	protected SelectVariable heuristics;
	protected static Map<String,SelectVariable> heuristicsMap;
	
	public JaCoPReasoner() {
		store = new FDstore();
		variables = new HashMap<String,Variable>();
		constraints = new ArrayList<Constraint>();
		relationConstraintMap = new HashMap<Constraint,GenericRelation>();
		consistent = true;
		reify = false;
		heuristics = null;
		heuristicsMap = new HashMap<String,SelectVariable>();
		heuristicsMap.put("MCD",new Delete(new MostConstrainedDynamic()));
		heuristicsMap.put("MCS",new Delete(new MostConstrainedStatic()));
		heuristicsMap.put("LD",new Delete(new LargestDomain()));
		heuristicsMap.put("LM",new Delete(new LargestMin()));
		heuristicsMap.put("MR",new Delete(new MaxRegret()));
		heuristicsMap.put("MDOD",new Delete(new MinDomainOverDegree()));
		heuristicsMap.put("SDOM",new Delete(new SmallestDomain()));
		heuristicsMap.put("SMAX",new Delete(new SmallestMax()));
		heuristicsMap.put("SMIN",new Delete(new SmallestMin()));
		features = new HashMap<String,GenericFeature>();
	}
	
	@Override
	public void reset() {
		store = new FDstore();
		variables = new HashMap<String,Variable>();
		constraints = new ArrayList<Constraint>();
		relationConstraintMap = new HashMap<Constraint,GenericRelation>();
		consistent = true;
		reify = false;
		root = null;
		features = new HashMap<String,GenericFeature>();
	}
	
	@Override
	public void addFeature(GenericFeature f, Collection<Cardinality> cards) {
		features.put(f.getName(), f);
		Iterator<Cardinality> cardIt = cards.iterator();
		Variable var = new Variable(store,f.getName(),0,1);
		while (cardIt.hasNext()) {
			Cardinality card = cardIt.next();
			var.addDom(card.getMin(),card.getMax());
		}
		variables.put(f.getName(),var);
	}

	@Override
	public void addRoot(GenericFeature feature) {
		Variable root = variables.get(feature.getName());
		PrimitiveConstraint constraint = new XeqC(root,1);
		constraints.add(constraint);
		store.impose(constraint);
		//relationConstraintMap.put(null,constraint);
	}

	@Override
	public void addMandatory(GenericRelation rel,GenericFeature child, GenericFeature parent) {
		Variable childVar  = variables.get(child.getName());
		Variable parentVar = variables.get(parent.getName());
		PrimitiveConstraint constraint = new XeqY(childVar,parentVar);
		constraints.add(constraint);
		store.impose(constraint);
		relationConstraintMap.put(constraint,rel);
	}

	@Override
	public void addOptional(GenericRelation rel,GenericFeature child, GenericFeature parent) {
		Variable childVar  = variables.get(child.getName());
		Variable parentVar = variables.get(parent.getName());
		PrimitiveConstraint constraint = new IfThen(new XeqC(parentVar,0),new XeqC(childVar,0));;
		constraints.add(constraint);
		store.impose(constraint);
		relationConstraintMap.put(constraint,rel);
	}

	@Override
	public void addCardinality(GenericRelation rel,GenericFeature child, GenericFeature parent,
			Iterator<Cardinality> cardinalities) {
		Variable childVar  = variables.get(child.getName());
		Variable parentVar = variables.get(parent.getName());
		
		FD domain = new FD(0,0);
		if (cardinalities.hasNext())
			domain.remove(0);
		while(cardinalities.hasNext()) {
			Cardinality card = cardinalities.next();
			domain.addDom(card.getMin(),card.getMax());
		}
		
		//FIXME no s� si funcionar� correctamente esta nueva expresi�n, creo que s�
		PrimitiveConstraint constraint = new IfThenElse(new XeqC(parentVar,0),new XeqC(childVar,0),new In(childVar,domain));
		constraints.add(constraint);
		store.impose(constraint);
		relationConstraintMap.put(constraint,rel);
		//FIXME guarrada a petici�n del Bena Loperini
		PrimitiveConstraint constraintGuarra = new IfThen(new XeqC(childVar,0),new XeqC(parentVar,0));
		constraints.add(constraintGuarra);
		store.impose(constraintGuarra);
		relationConstraintMap.put(constraintGuarra,rel);
	}

	@Override
	public void addSet(GenericRelation rel,GenericFeature parent, Collection<GenericFeature> children,
			Collection<Cardinality> cardinalities) {
		Variable parentVar = variables.get(parent.getName());
		// create the sum variable
		Variable setVar = new Variable(store,rel.getName());
		setVar.addDom(0,children.size());
		FD domain = new FD(0,0);
		domain.remove(0);
		Iterator<Cardinality> itc = cardinalities.iterator();
		while(itc.hasNext()) {
			Cardinality card = itc.next();
			domain.addDom(card.getMin(),card.getMax());
		}
		variables.put(rel.getName(),setVar);
		
		// create a set of variables to sum them
		ArrayList<Variable> varsList = new ArrayList<Variable>();
		Iterator<GenericFeature> it = children.iterator();
		while (it.hasNext()) {
			varsList.add(variables.get(it.next().getName()));
		}
		
		// creates the sum constraint with the cardinality variable
		Constraint suma = new Sum(varsList, setVar);
		// it is not a primitive constraint and cannot be reified
		constraints.add(suma);
		store.impose(suma);
		
		// creates the arraylist of constraints for the And constraint
		ArrayList<PrimitiveConstraint> localConstraints = new ArrayList<PrimitiveConstraint>();
		it = children.iterator();
		while (it.hasNext()) {
			Variable childVar = variables.get(it.next().getName());
			localConstraints.add(new XeqC(childVar, 0));
		}
		
		// adding the second constraint to the store
		//TODO cambiar In por XeqC(setVar,0)
		PrimitiveConstraint constraint = new IfThenElse(
				new XgtC(parentVar, 0), new In(setVar, domain),
				//new In(setVar,new FD(0,0)));
				new And(localConstraints));
		constraints.add(constraint);
		store.impose(constraint);
		relationConstraintMap.put(constraint,rel);
		
		//FIXME guarrada a petici�n del Bena Loperini
		PrimitiveConstraint constraintGuarra = new IfThen(new XeqC(setVar,0),new XeqC(parentVar,0));
		constraints.add(constraintGuarra);
		store.impose(constraintGuarra);
		relationConstraintMap.put(constraintGuarra,rel);
	}

	@Override
	public void addExcludes(GenericRelation rel,GenericFeature origin, GenericFeature destination) {
		Variable originVar  = variables.get(origin.getName());
		Variable destVar = variables.get(destination.getName());
		//FIXME no s� si funcionar� correctamente esta nueva expresi�n, creo que s�
		PrimitiveConstraint constraint = new IfThen(new XgtC(originVar,0),new XeqC(destVar,0));
		constraints.add(constraint);
		store.impose(constraint);
		relationConstraintMap.put(constraint,rel);
	}

	@Override
	public void addRequires(GenericRelation rel,GenericFeature origin, GenericFeature destination) {
		Variable originVar  = variables.get(origin.getName());
		Variable destVar = variables.get(destination.getName());
		//FIXME no s� si funcionar� correctamente esta nueva expresi�n, creo que s�
		PrimitiveConstraint constraint = new IfThen(new XgtC(originVar,0),new XgtC(destVar,0));
		constraints.add(constraint);
		store.impose(constraint);
		relationConstraintMap.put(constraint,rel);
	}

	public PerformanceResult ask(Question q) {
		PerformanceResult res;
		JaCoPQuestion jq = (JaCoPQuestion)q;
		if (heuristics != null)
			jq.setHeuristics(heuristics);
		jq.preAnswer(this);
		res = jq.answer(this);
		jq.postAnswer(this);
		consistent = true;
		return res;
	}
	
	public void setReify (boolean reify) {
		this.reify = reify;
	}
	
	GenericRelation getRelation(Constraint c) {
		return relationConstraintMap.get(c);
	}
	
	public boolean consistency() {
		if (consistent)
			consistent = store.consistency();
		return consistent;
	}
	
	public ArrayList<Variable> getVariables() {
		ArrayList<Variable> res = new ArrayList<Variable>();
		Iterator<Entry<String,Variable>> it = variables.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String,Variable> elem = it.next();
			res.add(elem.getValue());
		}
		return res;
	}
	
	public FDstore getStore(){
		return store;
	}

	public GenericFeature searchFeatureByName(String id) {
		return features.get(id);
	}
	
	public GenericFeature getRoot() {
		return root;
	}
	
	public Collection<GenericFeature> getAllFeatures(){
		return this.features.values();
	}
}
