package es.us.isa.Sat4jReasoner.questions;

import java.util.Collection;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.Reasoner.questions.ExtendedFilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultCommonalityQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jDefaultCommonalityQuestion extends Sat4jQuestion implements
		CommonalityQuestion {

	private DefCommonalityQuestion cq;
	
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
	
	public void preAnswer(Reasoner r){
		this.cq.preAnswer(r);
	}
	
	public PerformanceResult answer(Reasoner r){
		Sat4jReasoner sat = (Sat4jReasoner) r;
		this.cq.setFeatures(sat.getAllFeatures());
		return this.cq.answer(r);
	}
	
	public String toString(){
		return this.cq.toString();
	}
	
	class DefCommonalityQuestion extends DefaultCommonalityQuestion{

		Collection<GenericFeature> c;
		
		@Override
		public ExtendedFilterQuestion filterQuestionFactory() {
			return new Sat4jExtendedFilterQuestion();
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

		public void setFeatures(Collection<GenericFeature> c){
			this.c = c;
		}
		
		@Override
		public boolean isValid(GenericFeature f) {
			// TODO Auto-generated method stub
			return c.contains(f);
		}
		
	}

}
