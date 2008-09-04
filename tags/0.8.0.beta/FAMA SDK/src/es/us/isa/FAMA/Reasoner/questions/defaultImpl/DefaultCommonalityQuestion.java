package es.us.isa.FAMA.Reasoner.questions.defaultImpl;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;

public abstract class DefaultCommonalityQuestion implements CommonalityQuestion {

	private GenericFeature feature;
	private long commonality;
	
	public DefaultCommonalityQuestion() {
		feature = null;
	}
		
	public DefaultCommonalityQuestion(GenericFeature f) {
		this.feature = f;
	}
	
	public void setFeature(GenericFeature f) {
		this.feature = f;
	}

	public long getCommonality() {
		return commonality;
	}
	
	public void preAnswer(Reasoner r) {
		commonality = 0;
	}
	
	public PerformanceResult answer(Reasoner r) {
		PerformanceResult res = this.performanceResultFactory();
		if (feature != null) {
			FilterQuestion filter = this.filterQuestionFactory();
			NumberOfProductsQuestion number = this.numberOfProductsQuestionFactory();
			SetQuestion set = this.setQuestionFactory();
			filter.addFeature(feature,1);
			set.addQuestion(filter);
			set.addQuestion(number);
			res = r.ask(set);
			commonality = number.getNumberOfProducts();
		}
		return res;
	}
	
	public String toString() {
		GenericFeature f = (GenericFeature) feature;
		String res = "Commonality of " + f.getName() + " is " + commonality;
		return res;
	}
	
	public abstract NumberOfProductsQuestion numberOfProductsQuestionFactory();
	
	public abstract SetQuestion setQuestionFactory();
	
	public abstract FilterQuestion filterQuestionFactory();

	public abstract PerformanceResult performanceResultFactory();

}
