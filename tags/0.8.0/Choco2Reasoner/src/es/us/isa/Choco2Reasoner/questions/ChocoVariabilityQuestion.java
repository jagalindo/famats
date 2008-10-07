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
package es.us.isa.Choco2Reasoner.questions;

import es.us.isa.Choco2Reasoner.ChocoQuestion;
import es.us.isa.Choco2Reasoner.ChocoReasoner;
import es.us.isa.Choco2Reasoner.ChocoResult;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.VariabilityQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultVariabilityQuestion;

public class ChocoVariabilityQuestion extends ChocoQuestion implements
		VariabilityQuestion {

	private DefVariabilityQuestion vq;
	
	public ChocoVariabilityQuestion(){
		vq = new DefVariabilityQuestion();
	}
	
	@Override
	public float getVariability() {
		return vq.getVariability();
	}
	
	public PerformanceResult answer(Reasoner r){
		ChocoReasoner c = (ChocoReasoner) r;
		vq.setNumberOfFeatures(c.getAllFeatures().size());
		return vq.answer(r);
	}
	
	public String toString(){
		return this.vq.toString();
	}

	class DefVariabilityQuestion extends DefaultVariabilityQuestion{

		private long nFeatures;
		
		@Override
		public long getNumberOfFeatures() {
			return nFeatures;
		}
		
		public void setNumberOfFeatures(long n){
			nFeatures = n;
		}

		@Override
		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}

		@Override
		public NumberOfProductsQuestion numberOfProductsQuestionFactory() {
			return new ChocoNumberOfProductsQuestion();
		}

		@Override
		public PerformanceResult performanceResultFactory() {
			return new ChocoResult();
		}
		
	}
}
