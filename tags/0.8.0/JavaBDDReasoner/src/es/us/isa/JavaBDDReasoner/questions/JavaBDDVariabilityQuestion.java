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
package es.us.isa.JavaBDDReasoner.questions;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.VariabilityQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultVariabilityQuestion;
import es.us.isa.JavaBDDReasoner.JavaBDDQuestion;
import es.us.isa.JavaBDDReasoner.JavaBDDReasoner;
import es.us.isa.JavaBDDReasoner.JavaBDDResult;

public class JavaBDDVariabilityQuestion extends JavaBDDQuestion implements
		VariabilityQuestion {

	private DefVariabilityQuestion vq;
	
	public JavaBDDVariabilityQuestion(){
		vq = new DefVariabilityQuestion();
	}
	
	public float getVariability() {
		return vq.getVariability();
	}
	
	public PerformanceResult answer(Reasoner r){
		JavaBDDReasoner c = (JavaBDDReasoner) r;
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
			return new JavaBDDNumberOfProductsQuestion();
		}

		@Override
		public PerformanceResult performanceResultFactory() {
			return new JavaBDDResult();
		}
		
	}
}
