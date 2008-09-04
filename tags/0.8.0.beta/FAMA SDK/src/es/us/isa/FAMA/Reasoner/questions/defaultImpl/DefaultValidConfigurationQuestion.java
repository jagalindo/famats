package es.us.isa.FAMA.Reasoner.questions.defaultImpl;

import java.util.Collection;
import java.util.Iterator;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidConfigurationQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;


public abstract class DefaultValidConfigurationQuestion implements
		ValidConfigurationQuestion {

private boolean valid;
	
	private Product p;
	
	public boolean isValid() {
		return valid;
	}

	public void setProduct(Product p) {
		this.p = p;
	}
	
	public PerformanceResult answer(Reasoner r){
		valid = false;
		PerformanceResult res = this.performanceResultFactory();
		
		FilterQuestion fq = this.filterQuestionFactory();
		ValidQuestion vq = this.validQuestionFactory();
		
		Collection<GenericFeature> prodFeats = this.p.getFeatures();
		
		if (this.getAllFeatures().containsAll(prodFeats)){
			Iterator<GenericFeature> it1 = prodFeats.iterator();
			while(it1.hasNext()){
				GenericFeature f = it1.next();
				fq.addFeature(f, 1);
			}
			
			SetQuestion sq = this.setQuestionFactory();
			sq.addQuestion(fq);
			sq.addQuestion(vq);
			res = r.ask(sq);
			if (vq.isValid()){
				valid = true;
			}
		}
		
		return res;
	}
	
	public abstract SetQuestion setQuestionFactory();
	
	public abstract FilterQuestion filterQuestionFactory();
	
	public abstract ValidQuestion validQuestionFactory();
	
	public abstract Collection<GenericFeature> getAllFeatures();
	
	public abstract PerformanceResult performanceResultFactory();

}
