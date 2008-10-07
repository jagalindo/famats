/*
	This file is part of FaMaTS.

    FaMaTS is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FaMaTS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FaMaTS.  If not, see <http://www.gnu.org/licenses/>.

 */
package es.us.isa.Sat4jReasoner.questions;

import java.util.Collection;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.DetectErrorsQuestion;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.Reasoner.questions.defaultImpl.DefaultDetectErrorsQuestion;
import es.us.isa.FAMA.errors.Error;
import es.us.isa.FAMA.errors.Observation;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jDetectErrorsQuestion extends Sat4jQuestion implements
		DetectErrorsQuestion {

	private DefDetectErrorsQuestion dtq;
	
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
	
	public String toString(){
		return this.dtq.toString();
	}
	
	class DefDetectErrorsQuestion extends DefaultDetectErrorsQuestion{

		@Override
		public FilterQuestion filterQuestionFactory() {
			return new Sat4jFilterQuestion();
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
