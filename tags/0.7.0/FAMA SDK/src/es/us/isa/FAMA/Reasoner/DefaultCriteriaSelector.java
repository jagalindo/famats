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

package es.us.isa.FAMA.Reasoner;

import java.util.Iterator;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;

public class DefaultCriteriaSelector extends CriteriaSelector {
	
	public DefaultCriteriaSelector (QuestionTrader qt) {
		super(qt);
	}
	
	public Question createQuestion(Class<Question> questionClass, VariabilityModel vm) {
		if (qt != null) {
			Iterator<String> reasoners = qt.getReasonerIds();
			Question res = null;
			while (reasoners.hasNext() && res == null) {
				String id = reasoners.next();
				Reasoner r = qt.getReasonerById(id);
				res = r.getFactory().createQuestion(questionClass);
			}
			return res;
				
		}
		return null;
	}
	
	public void registerResults(Question q, VariabilityModel fm, PerformanceResult pr) {
		
	}
}
