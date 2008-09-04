package es.us.isa.ChocoReasoner.questions;

import java.util.Collection;

import es.us.isa.ChocoReasoner.ChocoQuestion;
import es.us.isa.ChocoReasoner.ChocoReasoner;
import es.us.isa.ChocoReasoner.ChocoResult;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidProductQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultValidProductQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;

public class ChocoValidProductQuestion extends ChocoQuestion implements
		ValidProductQuestion {

	DefValidProductQuestion vpq;
	
	public ChocoValidProductQuestion(){
		super();
		vpq = new DefValidProductQuestion();
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
		ChocoReasoner choco = (ChocoReasoner) r;
		this.vpq.setAllFeatures(choco.getAllFeatures());
		return this.vpq.answer(r);
	}

	class DefValidProductQuestion extends DefaultValidProductQuestion{

		Collection<GenericFeature> c;
		
		@Override
		public FilterQuestion filterQuestionFactory() {
			return new ChocoFilterQuestion();
		}

		public void setAllFeatures(Collection<GenericFeature> c){
			this.c = c;
		}
		
		@Override
		public Collection<GenericFeature> getAllFeatures() {
			return c;
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
		public ValidQuestion validQuestionFactory() {
			return new ChocoValidQuestion();
		}

		@Override
		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}
		
	}
	
}
