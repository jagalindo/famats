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
package es.us.isa.ChocoReasoner.questions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import java.util.Map.Entry;

import choco.integer.IntDomainVar;

import es.us.isa.ChocoReasoner.ChocoQuestion;
import es.us.isa.ChocoReasoner.ChocoReasoner;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;


public class ChocoFilterQuestion extends ChocoQuestion implements FilterQuestion{

	private Map<GenericFeature,Integer> featuresSet;
	
	public ChocoFilterQuestion(){
		featuresSet = new HashMap<GenericFeature,Integer>();
	}

	@Override
	public void addFeature(GenericFeature f, int cardinality) {
		if (!featuresSet.containsKey(f))
			featuresSet.put(f,cardinality);
		
	}
	
	@Override
	public void removeFeature(GenericFeature f) {
		Iterator<Entry<GenericFeature,Integer>> it = featuresSet.entrySet().iterator();
		while (it.hasNext()) {
			Entry<GenericFeature,Integer> e = it.next();
			if (e.getKey().getName().equalsIgnoreCase(f.getName()))
				it.remove();
		}
		
	}

	
	public void preAnswer(Reasoner choco){
		ChocoReasoner r = (ChocoReasoner)choco;
		Iterator<Entry<GenericFeature,Integer>> it = featuresSet.entrySet().iterator();
		HashMap<String, IntDomainVar> vars = r.getVariables();
		while (it.hasNext()) {
			Entry<GenericFeature,Integer> e = it.next();
			GenericFeature f = e.getKey();
			r.getProblem().post(r.getProblem().eq(vars.get(f.getName()),e.getValue().intValue()));
		}
				
		
		
	}
	
	public void postAnswer(ChocoReasoner r) {
	}
	

}
