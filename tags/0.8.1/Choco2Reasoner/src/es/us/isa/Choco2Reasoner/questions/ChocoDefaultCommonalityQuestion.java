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

import java.util.Collection;

import es.us.isa.Choco2Reasoner.ChocoQuestion;
import es.us.isa.Choco2Reasoner.ChocoReasoner;
import es.us.isa.Choco2Reasoner.ChocoResult;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultCommonalityQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;

public class ChocoDefaultCommonalityQuestion extends ChocoQuestion implements
		CommonalityQuestion {

	private DefCommonalityQuestion cq;
	
	public ChocoDefaultCommonalityQuestion() {
		super();
		cq = new DefCommonalityQuestion();
	}

	@Override
	public long getCommonality() {
		return cq.getCommonality();
	}

	@Override
	public void setFeature(GenericFeature f) {
		cq.setFeature(f);
	}
	
	public void preAnswer(Reasoner r){
		this.cq.preAnswer(r);
	}
	
	public PerformanceResult answer(Reasoner r){
		ChocoReasoner choco = (ChocoReasoner) r;
		this.cq.setFeatures(choco.getAllFeatures());
		return cq.answer(r);
	}
	
	public String toString(){
		return this.cq.toString();
	}
	
	class DefCommonalityQuestion extends DefaultCommonalityQuestion{

		Collection<GenericFeature> c;
		
		@Override
		public FilterQuestion filterQuestionFactory() {
			return new ChocoFilterQuestion();
		}

		@Override
		public NumberOfProductsQuestion numberOfProductsQuestionFactory() {
			return new ChocoNumberOfProductsQuestion();
		}

		@Override
		public PerformanceResult performanceResultFactory() {
			return new ChocoResult();
		}

		@Override
		public SetQuestion setQuestionFactory() {
			return new ChocoSetQuestion();
		}

		@Override
		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}

		public void setFeatures(Collection<GenericFeature> c){
			this.c = c;
		}
		
		@Override
		public boolean isValid(GenericFeature f) {
			return c.contains(f);
		}
		
	}

}
