package es.us.isa.FAMA.Reasoner.questions.defaultImpl;

import java.util.Collection;
import java.util.Iterator;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidProductQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;


public abstract class DefaultValidProductQuestion implements
		ValidProductQuestion {

	private boolean valid;
	
	private Product p;
	
	public void setProduct(Product p){
		this.p = p;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public PerformanceResult answer(Reasoner r) {
		valid = false;
		PerformanceResult res = this.performanceResultFactory();
		
		FilterQuestion fq = this.filterQuestionFactory();
		ValidQuestion vq = this.validQuestionFactory();
		
		Collection<GenericFeature> prodFeats = this.p.getFeatures();
		Collection<GenericFeature> excludeFeats = this.getAllFeatures();
		
		//if prodFeats contains features that are not on the Feature Model,
		//the product is not valid
		if (excludeFeats.containsAll(prodFeats)){
			excludeFeats.removeAll(prodFeats);
			
			Iterator<GenericFeature> it1 = prodFeats.iterator();
			while(it1.hasNext()){
				GenericFeature f = it1.next();
				fq.addFeature(f, 1);
			}
			
			Iterator<GenericFeature> it2 = excludeFeats.iterator();
			while(it2.hasNext()){
				GenericFeature f = it2.next();
				fq.addFeature(f, 0);
			}
			
			SetQuestion sq = this.setQuestionFactory();
			sq.addQuestion(fq);
			sq.addQuestion(vq);
			res =  r.ask(sq);
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
