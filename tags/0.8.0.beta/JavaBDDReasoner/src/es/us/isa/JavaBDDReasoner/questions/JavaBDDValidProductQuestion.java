package es.us.isa.JavaBDDReasoner.questions;

import java.util.Collection;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidProductQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultValidProductQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;
import es.us.isa.JavaBDDReasoner.JavaBDDQuestion;
import es.us.isa.JavaBDDReasoner.JavaBDDReasoner;
import es.us.isa.JavaBDDReasoner.JavaBDDResult;
import es.us.isa.JavaBDDReasoner.questions.JavaBDDValidConfigurationQuestion.DefValidConfigurationQuestion;

public class JavaBDDValidProductQuestion extends JavaBDDQuestion implements
		ValidProductQuestion {

	DefValidProductQuestion vpq;
	
	public JavaBDDValidProductQuestion() {
		super();
		vpq = new DefValidProductQuestion();
	}

	public boolean isValid() {
		return vpq.isValid();
	}

	public void setProduct(Product p) {
		vpq.setProduct(p);
	}
	
	public PerformanceResult answer(Reasoner r) {
		JavaBDDReasoner bdd = (JavaBDDReasoner) r;
		this.vpq.setAllFeatures(bdd.getAllFeatures());
		return this.vpq.answer(r);
	}
	
	class DefValidProductQuestion extends DefaultValidProductQuestion{

		Collection<GenericFeature> c;
		
		@Override
		public FilterQuestion filterQuestionFactory() {
			return new JavaBDDFilterQuestion();
		}

		@Override
		public Collection<GenericFeature> getAllFeatures() {
			return c;
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
