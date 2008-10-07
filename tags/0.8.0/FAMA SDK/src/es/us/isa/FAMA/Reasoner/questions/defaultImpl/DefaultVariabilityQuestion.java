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
package es.us.isa.FAMA.Reasoner.questions.defaultImpl;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.VariabilityQuestion;

public abstract class DefaultVariabilityQuestion implements VariabilityQuestion {

	private float vFactor;
	
	public float getVariability() {
		return this.vFactor;
	}
	
	public PerformanceResult answer(Reasoner r){
		PerformanceResult pr = this.performanceResultFactory();
		NumberOfProductsQuestion npq = this.numberOfProductsQuestionFactory();
		r.ask(npq);
		long n = npq.getNumberOfProducts();
		long f = this.getNumberOfFeatures();
		long aux = (long)Math.pow(2, f) - 1;
		this.vFactor = (float)n / aux;
		return pr;
	}

	public abstract Class<? extends Reasoner> getReasonerClass();
	
	public abstract NumberOfProductsQuestion numberOfProductsQuestionFactory();
	
	public abstract PerformanceResult performanceResultFactory();
	
	public abstract long getNumberOfFeatures();

}
