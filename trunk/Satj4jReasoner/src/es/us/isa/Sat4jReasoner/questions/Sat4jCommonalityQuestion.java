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

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.Sat4jReasoner.Sat4jQuestion;
import es.us.isa.Sat4jReasoner.Sat4jResult;

public class Sat4jCommonalityQuestion extends Sat4jQuestion implements
		CommonalityQuestion {

	private GenericFeature feature;
	/**
	 * @uml.property  name="commonality"
	 */
	private long commonality;
	
	public Sat4jCommonalityQuestion() {
		feature = null;
	}
		
	public Sat4jCommonalityQuestion(GenericFeature f) {
		this.feature = f;
	}
	
	/**
	 * @param f
	 * @uml.property  name="feature"
	 */
	public void setFeature(GenericFeature f) {
		this.feature = f;
	}

	/**
	 * @return
	 * @uml.property  name="commonality"
	 */
	public long getCommonality() {
		return commonality;
	}

	
	//Consultar este fallo ¿Quitar override?
	@Override
	public void preAnswer(Reasoner r) {
		commonality = 0;
	}
	
	@Override
	public PerformanceResult answer(Reasoner r) {
		PerformanceResult res = new Sat4jResult();
		if (feature != null) {
			Sat4jFilterQuestion filter = new Sat4jFilterQuestion();
			Sat4jNumberOfProductsQuestion np = new Sat4jNumberOfProductsQuestion();
			Sat4jSetQuestion set = new Sat4jSetQuestion();
			filter.addFeature(feature,1);
			set.addQuestion(filter);
			set.addQuestion(np);
			res = r.ask(set);
			commonality = np.getNumberOfProducts();
		}
		return res;
	}
	
	public String toString() {
		String res = "Commonality of " + feature.getName() + " is " + commonality;
		return res;
	}
}
