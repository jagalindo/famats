package es.us.isa.FAMA.Reasoner.questions.defaultImpl;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.VariabilityQuestion;

public abstract class DefaultVariabilityQuestion implements VariabilityQuestion {

	private float vFactor;
	
	public float getVariability() {
		// TODO Auto-generated method stub
		return this.vFactor;
	}
	
	public PerformanceResult answer(Reasoner r){
		PerformanceResult pr = this.performanceResultFactory();
		NumberOfProductsQuestion npq = this.numberOfProductsQuestionFactory();
		r.ask(npq);
		long n = npq.getNumberOfProducts();
		long f = this.getNumberOfFeatures();
		long aux = (long)Math.pow(2, f) - 1;
		this.vFactor = (float)n / aux;
		return pr;
	}

	public abstract Class<? extends Reasoner> getReasonerClass();
	
	public abstract NumberOfProductsQuestion numberOfProductsQuestionFactory();
	
	public abstract PerformanceResult performanceResultFactory();
	
	public abstract long getNumberOfFeatures();

}
