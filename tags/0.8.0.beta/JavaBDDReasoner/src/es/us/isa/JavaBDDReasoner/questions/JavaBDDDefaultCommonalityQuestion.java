package es.us.isa.JavaBDDReasoner.questions;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultCommonalityQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.JavaBDDReasoner.JavaBDDQuestion;
import es.us.isa.JavaBDDReasoner.JavaBDDReasoner;
import es.us.isa.JavaBDDReasoner.JavaBDDResult;

public class JavaBDDDefaultCommonalityQuestion extends JavaBDDQuestion
		implements CommonalityQuestion {

	DefCommonalityQuestion cq;
	
	public JavaBDDDefaultCommonalityQuestion() {
		super();
		cq = new DefCommonalityQuestion();
	}

	public long getCommonality() {
		return cq.getCommonality();
	}

	public void setFeature(GenericFeature f) {
		cq.setFeature(f);
	}
	
	@Override
	public PerformanceResult answer(Reasoner r){
		return this.cq.answer(r);
	}
	
	class DefCommonalityQuestion extends DefaultCommonalityQuestion{

		@Override
		public FilterQuestion filterQuestionFactory() {
			return new JavaBDDFilterQuestion();
		}

		@Override
		public NumberOfProductsQuestion numberOfProductsQuestionFactory() {
			return new JavaBDDNumberOfProductsQuestion();
		}

		@Override
		public PerformanceResult performanceResultFactory() {
			return new JavaBDDResult();
		}

		@Override
		public SetQuestion setQuestionFactory() {
			return new JavaBDDSetQuestion();
		}

		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}
		
	}

}
