package es.us.isa.Sat4jReasoner.questions;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.VariabilityQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultVariabilityQuestion;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jVariabilityQuestion extends Sat4jQuestion implements
		VariabilityQuestion {

	private DefVariabilityQuestion vq;
	
	public Sat4jVariabilityQuestion(){
		vq = new DefVariabilityQuestion();
	}
	
	@Override
	public float getVariability() {
		// TODO Auto-generated method stub
		return vq.getVariability();
	}
	
	public PerformanceResult answer(Reasoner r){
		Sat4jReasoner c = (Sat4jReasoner) r;
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
			return new Sat4jNumberOfProductsQuestion();
		}

		@Override
		public PerformanceResult performanceResultFactory() {
			return new Sat4jResult();
		}
		
	}
	
}
