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
package es.us.isa.ChocoReasoner.questions;

import es.us.isa.ChocoReasoner.ChocoQuestion;
import es.us.isa.ChocoReasoner.ChocoReasoner;
import es.us.isa.ChocoReasoner.ChocoResult;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;


public class ChocoCommonalityQuestion extends ChocoQuestion implements CommonalityQuestion {

	private long commonality;
	GenericFeature feature;
	
	public ChocoCommonalityQuestion(){
		this.feature=null;
	}
	
	public ChocoCommonalityQuestion(GenericFeature f){
		this.feature=f;
	}
	
	@Override
	public long getCommonality() {
		return commonality;
	}

	@Override
	public void setFeature(GenericFeature f) {
		this.feature = f;	
	}

	public void preAnswer(Reasoner r) {
		commonality = 0;
	}
	
	public PerformanceResult answer(Reasoner r) {
		PerformanceResult res = new ChocoResult();
		if (feature != null) {
			ChocoFilterQuestion filter = new ChocoFilterQuestion();
			ChocoNumberOfProductsQuestion number = new ChocoNumberOfProductsQuestion();
			ChocoSetQuestion set = new ChocoSetQuestion();
			filter.addFeature(feature,1);
			set.addQuestion(filter);
			set.addQuestion(number);
			res = r.ask(set);
			commonality = number.getNumberOfProducts();
		}
		return res;
	}
}
