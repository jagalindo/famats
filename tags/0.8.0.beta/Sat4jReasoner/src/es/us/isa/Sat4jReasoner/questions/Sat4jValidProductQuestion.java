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
package es.us.isa.Sat4jReasoner.questions;

import java.util.Collection;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidProductQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultValidProductQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jValidProductQuestion extends Sat4jQuestion implements
		ValidProductQuestion {

	DefValidProductQuestion vpq;
	
	public Sat4jValidProductQuestion() {
		super();
		this.vpq = new DefValidProductQuestion();
	}

	@Override
	public boolean isValid() {
		return vpq.isValid();
	}

	@Override
	public void setProduct(Product p) {
		vpq.setProduct(p);
	}
	
	public PerformanceResult answer(Reasoner r){
		Sat4jReasoner sat = (Sat4jReasoner)r;
		vpq.setAllFeatures(sat.getAllFeatures());
		return this.vpq.answer(r);
	}

	class DefValidProductQuestion extends DefaultValidProductQuestion {

		Collection<GenericFeature> feats;
		
		@Override
		public FilterQuestion filterQuestionFactory() {
			return new Sat4jFilterQuestion();
		}

		@Override
		public Collection<GenericFeature> getAllFeatures() {
			return this.feats;
		}
		
		public void setAllFeatures(Collection<GenericFeature> f){
			this.feats = f;
		}

		@Override
		public PerformanceResult performanceResultFactory() {
			return new Sat4jResult();
		}

		@Override
		public SetQuestion setQuestionFactory() {
			return new Sat4jSetQuestion();
		}

		@Override
		public ValidQuestion validQuestionFactory() {
			return new Sat4jValidQuestion();
		}

		@Override
		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}
		

	}

}


