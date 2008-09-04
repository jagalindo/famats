package es.us.isa.Sat4jReasoner.questions;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultCommonalityQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jDefaultCommonalityQuestion extends Sat4jQuestion implements
		CommonalityQuestion {

	DefaultCommonalityQuestion cq;
	
	public Sat4jDefaultCommonalityQuestion() {
		super();
		cq = new DefCommonalityQuestion();
	}

	@Override
	public long getCommonality() {
		return this.cq.getCommonality();
	}

	@Override
	public void setFeature(GenericFeature f) {
		this.cq.setFeature(f);
	}
	
	public PerformanceResult answer(Reasoner r){
		return this.cq.answer(r);
	}
	
	class DefCommonalityQuestion extends DefaultCommonalityQuestion{

		@Override
		public FilterQuestion filterQuestionFactory() {
			return new Sat4jFilterQuestion();
		}

		@Override
		public NumberOfProductsQuestion numberOfProductsQuestionFactory() {
			return new Sat4jNumberOfProductsQuestion();
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
		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}
		
	}

}
