package es.us.isa.JavaBDDReasoner.questions;

import java.util.Collection;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidConfigurationQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultValidConfigurationQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;
import es.us.isa.JavaBDDReasoner.JavaBDDQuestion;
import es.us.isa.JavaBDDReasoner.JavaBDDReasoner;
import es.us.isa.JavaBDDReasoner.JavaBDDResult;

public class JavaBDDValidConfigurationQuestion extends JavaBDDQuestion
		implements ValidConfigurationQuestion {

	DefValidConfigurationQuestion vcq;
	
	
	
	public JavaBDDValidConfigurationQuestion() {
		super();
		vcq = new DefValidConfigurationQuestion();
	}

	public boolean isValid() {
		return vcq.isValid();
	}

	public void setProduct(Product p) {
		vcq.setProduct(p);
	}
	
	public PerformanceResult answer(Reasoner r) {
		JavaBDDReasoner bdd = (JavaBDDReasoner) r;
		vcq.setAllFeatures(bdd.getAllFeatures());
		return vcq.answer(r);
	}
	
	class DefValidConfigurationQuestion extends DefaultValidConfigurationQuestion{

		Collection<GenericFeature> c;
		
		@Override
		public FilterQuestion filterQuestionFactory() {
			return new JavaBDDFilterQuestion();
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
			return new JavaBDDResult();
		}

		@Override
		public SetQuestion setQuestionFactory() {
			return new JavaBDDSetQuestion();
		}

		@Override
		public ValidQuestion validQuestionFactory() {
			return new JavaBDDValidQuestion();
		}

		public Class<? extends Reasoner> getReasonerClass() {
			return null;
		}
		
	}

}
