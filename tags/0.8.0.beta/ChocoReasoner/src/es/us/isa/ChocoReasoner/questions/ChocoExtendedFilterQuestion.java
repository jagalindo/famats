package es.us.isa.ChocoReasoner.questions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import choco.Constraint;
import choco.Problem;
import choco.integer.IntDomainVar;
import choco.integer.IntExp;

import es.us.isa.ChocoReasoner.ChocoQuestion;
import es.us.isa.ChocoReasoner.ChocoReasoner;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.ExtendedFilterQuestion;
import es.us.isa.FAMA.models.variabilityModel.VariabilityElement;

public class ChocoExtendedFilterQuestion extends ChocoQuestion implements
		ExtendedFilterQuestion {

	private Map<VariabilityElement,Integer> elemsSet;
	
	public ChocoExtendedFilterQuestion(){
		super();
		elemsSet = new HashMap<VariabilityElement,Integer>();
	}
	
	@Override
	public void addValue(VariabilityElement ve, int value) {
		if (!elemsSet.containsKey(ve))
			elemsSet.put(ve,value);
	}

	@Override
	public void removeValue(VariabilityElement ve) {
		// TODO Auto-generated method stub
		Iterator<Entry<VariabilityElement,Integer>> it = elemsSet.entrySet().iterator();
		while (it.hasNext()) {
			Entry<VariabilityElement,Integer> e = it.next();
			if (e.getKey().getName().equalsIgnoreCase(ve.getName()))
				it.remove();
		}
	}
	
	public void preAnswer(Reasoner choco){
		ChocoReasoner r = (ChocoReasoner)choco;
		Iterator<Entry<VariabilityElement,Integer>> it = elemsSet.entrySet().iterator();
		HashMap<String, IntDomainVar> vars = r.getVariables();
		while (it.hasNext()) {
			Entry<VariabilityElement,Integer> e = it.next();
			VariabilityElement f = e.getKey();//FIXME f == null :(
			Problem p = r.getProblem();
			IntExp arg0 = vars.get(f.getName());
			int arg1 = e.getValue().intValue();
			Constraint aux = p.eq(arg0, arg1);
			p.post(aux);
			//r.getProblem().post(r.getProblem().eq(vars.get(f.getName()),e.getValue().intValue()));
		}
				
		
		
	}
	
	public void postAnswer(ChocoReasoner r) {
	}

}
