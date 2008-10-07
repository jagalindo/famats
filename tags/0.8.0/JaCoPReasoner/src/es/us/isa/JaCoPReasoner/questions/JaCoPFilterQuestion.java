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
package es.us.isa.JaCoPReasoner.questions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import JaCoP.FDstore;
import JaCoP.Variable;
import JaCoP.XeqC;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.models.variabilityModel.VariabilityElement;
import es.us.isa.JaCoPReasoner.JaCoPQuestion;
import es.us.isa.JaCoPReasoner.JaCoPReasoner;

public class JaCoPFilterQuestion extends JaCoPQuestion implements
		FilterQuestion {

	private Map<VariabilityElement,Integer> elementsSet;
	int storeLevel;
	boolean consistent;
	
	public JaCoPFilterQuestion() {
		elementsSet = new HashMap<VariabilityElement,Integer>();
		storeLevel = 0;
		consistent = true;
	}
	
	public void addValue(VariabilityElement ve, int value) {
		if (!elementsSet.containsKey(ve))
			elementsSet.put(ve,value);
	}

	public void removeValue(VariabilityElement ve) {
		Iterator<Entry<VariabilityElement,Integer>> it = elementsSet.entrySet().iterator();
		while (it.hasNext()) {
			Entry<VariabilityElement,Integer> e = it.next();
			if (e.getKey().getName().equalsIgnoreCase(ve.getName()))
				it.remove();
		}
	}
	
	/* (non-Javadoc)
	 * @see tdg.SPL.Reasoner.JaCoP.JaCoPQuestion#preAnswer(tdg.SPL.Reasoner.Reasoner)
	 */
	@Override
	public void preAnswer(JaCoPReasoner r) {
		JaCoPReasoner jacop = (JaCoPReasoner)r;
		FDstore store = jacop.getStore();
		storeLevel = store.level;
		// and apply consistency to make the changes available to the next level
		consistent = jacop.consistency();
		if (consistent) {
			store.setLevel(storeLevel+1);
			
			// now we impose the new constraints
			Iterator<Entry<VariabilityElement,Integer>> it = elementsSet.entrySet().iterator();
			List<Variable> vars = jacop.getVariables();
			while (it.hasNext()) {
				Entry<VariabilityElement,Integer> e = it.next();
				VariabilityElement f = e.getKey();
				Iterator<Variable> it2 = vars.iterator();
				boolean varFound = false;
				while (it2.hasNext() && !varFound) {
					Variable v = it2.next();
					if (v.id().equalsIgnoreCase(f.getName())) {
						store.impose(new XeqC(v,e.getValue().intValue()));
						varFound = true;
					}
					
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see tdg.SPL.Reasoner.JaCoP.JaCoPQuestion#postAnswer(tdg.SPL.Reasoner.Reasoner)
	 */
	@Override
	public void postAnswer(JaCoPReasoner r) {
		// going back to the previous level
		if (consistent) {
			JaCoPReasoner jacop = (JaCoPReasoner)r;
			FDstore store = jacop.getStore();
			store.removeLevel(storeLevel+1);
			store.setLevel(storeLevel);
		}
	}

}
