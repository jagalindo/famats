package es.us.isa.Sat4jReasoner.questions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.ExtendedFilterQuestion;
import es.us.isa.FAMA.models.variabilityModel.VariabilityElement;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;

public class Sat4jExtendedFilterQuestion extends Sat4jQuestion implements
		ExtendedFilterQuestion {

	private ArrayList<String> addedFeatures;		// Added Features
	private ArrayList<String> removedFeatures;      // Removed Features
	private ArrayList<String> addedClauses;         // Added clauses
	
	public Sat4jExtendedFilterQuestion() {
		super();
		addedFeatures = new ArrayList<String>();
		removedFeatures = new ArrayList<String>();
		addedClauses = new ArrayList<String>();
	}

	@Override
	public void addValue(VariabilityElement ve, int value) {
		if (!addedFeatures.contains(ve.getName()) && !removedFeatures.contains(ve.getName())){
			if (value > 0){
				addedFeatures.add(ve.getName());
			}
			else{
				removedFeatures.add(ve.getName());
			}
		}
	}

	@Override
	public void removeValue(VariabilityElement ve) {
		if (!addedFeatures.remove(ve.getName())){
			removedFeatures.remove(ve.getName());
		}
	}
	
	public void preAnswer(Reasoner r) {
		
		Sat4jReasoner sr=(Sat4jReasoner)r;
		
		// ADD CLAUSES
		
		// Added Features
		Iterator<String> it = addedFeatures.iterator();
		while (it.hasNext()) {
			String cnf_var = sr.getCNFVar(it.next());
			String clause = cnf_var + " 0";
			sr.getClauses().add(clause);
			addedClauses.add(clause);
		}
		
		// Removed Features
		it = removedFeatures.iterator();
		while (it.hasNext()) {
			String cnf_var = sr.getCNFVar((String)it.next());
			String clause ="-" + cnf_var + " 0";
			sr.getClauses().add(clause);
			addedClauses.add(clause);
		}
		
		// Create CNF file
		super.preAnswer(r);
	}
		
	public void postAnswer(Reasoner r) {
		//Remove filter
		Iterator<String> it = addedClauses.iterator();
		while (it.hasNext())
			((Sat4jReasoner)r).getClauses().remove(it.next());
	}

}
