package es.us.isa.JavaBDDReasoner.questions;

import java.util.Collection;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.DetectErrorsQuestion;
import es.us.isa.FAMA.Reasoner.questions.ExtendedFilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultDetectErrorsQuestion;
import es.us.isa.FAMA.errors.Error;
import es.us.isa.FAMA.errors.Observation;
import es.us.isa.JavaBDDReasoner.JavaBDDQuestion;
import es.us.isa.JavaBDDReasoner.JavaBDDResult;


public class JavaBDDDetectErrorsQuestion extends JavaBDDQuestion implements
		DetectErrorsQuestion {

	DefDetectErrorsQuestion deq;
	
	public JavaBDDDetectErrorsQuestion() {
		super();
		deq = new DefDetectErrorsQuestion();
	}

	public Collection<Error> getErrors() {
		return deq.getErrors();
	}

	public void setObservations(Collection<Observation> observations) {
		deq.setObservations(observations);
	}
	
	@Override
	public PerformanceResult answer(Reasoner r){
		return deq.answer(r);
	}
	
	class DefDetectErrorsQuestion extends DefaultDetectErrorsQuestion{

		@Override
		public ExtendedFilterQuestion extendedFilterQuestionFactory() {
			return new JavaBDDExtendedFilterQuestion();
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
