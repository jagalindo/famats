package es.us.isa.ChocoReasoner.questions;

import es.us.isa.ChocoReasoner.ChocoQuestion;
import es.us.isa.ChocoReasoner.ChocoResult;
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

	DefCommonalityQuestion cq;
	
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
	
	public PerformanceResult answer(Reasoner r){
		return cq.answer(r);
	}
	
	class DefCommonalityQuestion extends DefaultCommonalityQuestion{

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
		
	}

}
