package es.us.isa.Sat4jReasoner.questions;

import java.util.Collection;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.ExtendedFilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidConfigurationQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultValidConfigurationQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jValidConfigurationQuestion extends Sat4jQuestion implements
		ValidConfigurationQuestion {

	private DefValidConfigurationQuestion vcq;
	
	public Sat4jValidConfigurationQuestion() {
		super();
		vcq = new DefValidConfigurationQuestion();
	}

	@Override
	public boolean isValid() {
		return vcq.isValid();
	}

	@Override
	public void setProduct(Product p) {
		vcq.setProduct(p);
	}
	
	public PerformanceResult answer(Reasoner r){
		Sat4jReasoner sat = (Sat4jReasoner) r;
		vcq.setAllFeatures(sat.getAllFeatures());
		return this.vcq.answer(r);
	}
	
	public String toString(){
		return this.vcq.toString();
	}
	
	class DefValidConfigurationQuestion extends DefaultValidConfigurationQuestion{

		Collection<GenericFeature> c;
		
		@Override
		public ExtendedFilterQuestion filterQuestionFactory() {
			return new Sat4jExtendedFilterQuestion();
		}

		@Override
		public Collection<GenericFeature> getAllFeatures() {
			return this.c;
		}
		
		public void setAllFeatures(Collection<GenericFeature> c){
			this.c = c;
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
		public ValidQuestion validQuestionFactory() {
			return new Sat4jValidQuestion();
		}

		@Override
		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}
		
	}

}
