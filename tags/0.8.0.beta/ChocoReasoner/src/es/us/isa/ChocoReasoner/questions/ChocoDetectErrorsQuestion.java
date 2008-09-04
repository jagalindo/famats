package es.us.isa.ChocoReasoner.questions;

import java.util.Collection;

import es.us.isa.ChocoReasoner.ChocoQuestion;
import es.us.isa.ChocoReasoner.ChocoResult;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.DetectErrorsQuestion;
import es.us.isa.FAMA.Reasoner.questions.ExtendedFilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultDetectErrorsQuestion;
import es.us.isa.FAMA.errors.Error;
import es.us.isa.FAMA.errors.Observation;

public class ChocoDetectErrorsQuestion extends ChocoQuestion implements
		DetectErrorsQuestion {

	DefDetectErrorsQuestion deq;
	
	public ChocoDetectErrorsQuestion() {
		super();
		deq = new DefDetectErrorsQuestion();
	}

	@Override
	public Collection<Error> getErrors() {
		return deq.getErrors();
	}

	@Override
	public void setObservations(Collection<Observation> observations) {
		deq.setObservations(observations);
	}
	
	public PerformanceResult answer(Reasoner r){
		return this.deq.answer(r);
	}
	
	class DefDetectErrorsQuestion extends DefaultDetectErrorsQuestion{

		@Override
		public ExtendedFilterQuestion extendedFilterQuestionFactory() {
			return new ChocoExtendedFilterQuestion();
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
