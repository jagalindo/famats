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

import java.util.ArrayList;

import JaCoP.FDstore;
import JaCoP.Indomain;
import JaCoP.IndomainMin;
import JaCoP.Search;
import JaCoP.Variable;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.JaCoPReasoner.JaCoPQuestion;
import es.us.isa.JaCoPReasoner.JaCoPReasoner;
import es.us.isa.JaCoPReasoner.JaCoPResult;

public class JaCoPValidQuestion extends JaCoPQuestion implements ValidQuestion {
	boolean valid;
	
	public JaCoPValidQuestion() {
		valid = false;
	}
	
	public boolean isValid() {
		return valid;
	}

	@Override
	public PerformanceResult answer(JaCoPReasoner r) {
		JaCoPReasoner jacop = (JaCoPReasoner)r;
		JaCoPResult res = new JaCoPResult();
		valid = jacop.consistency();
		if (valid) {
			jacop.setReify(false);
			FDstore store = jacop.getStore();
			ArrayList<Variable> vars = jacop.getVariables();
			
			Search sa = new Search();
			Indomain indomain = new IndomainMin();	
			valid = sa.labeling(store, vars, indomain, heuristics);
			res.fillFields(sa);
		}
		return res;
	}

	public String toString() {
		if (valid)
			return "Feature model is valid";
		else
			return "Feature model is not valid";
	}

}
