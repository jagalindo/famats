package es.us.isa.Sat4jReasoner.questions;

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
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jDetectErrorsQuestion extends Sat4jQuestion implements
		DetectErrorsQuestion {

	DefDetectErrorsQuestion dtq;
	
	public Sat4jDetectErrorsQuestion() {
		super();
		dtq = new DefDetectErrorsQuestion();
	}

	@Override
	public Collection<Error> getErrors() {
		return dtq.getErrors();
	}

	@Override
	public void setObservations(Collection<Observation> observations) {
		dtq.setObservations(observations);
		
	}
	
	@Override
	public PerformanceResult answer(Reasoner r){
		return dtq.answer(r);
	}
	
	class DefDetectErrorsQuestion extends DefaultDetectErrorsQuestion{

		@Override
		public ExtendedFilterQuestion extendedFilterQuestionFactory() {
			return new Sat4jExtendedFilterQuestion();
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
