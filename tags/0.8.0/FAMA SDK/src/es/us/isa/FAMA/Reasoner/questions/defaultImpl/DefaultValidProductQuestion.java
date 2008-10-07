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

import java.util.Collection;
import java.util.Iterator;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidProductQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;


public abstract class DefaultValidProductQuestion implements
		ValidProductQuestion {

	private boolean valid;
	
	private Product p;
	
	public void setProduct(Product p){
		this.p = p;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public PerformanceResult answer(Reasoner r) {
		valid = false;
		PerformanceResult res = this.performanceResultFactory();
		
		FilterQuestion fq = this.filterQuestionFactory();
		ValidQuestion vq = this.validQuestionFactory();
		
		Collection<GenericFeature> prodFeats = this.p.getFeatures();
		Collection<GenericFeature> excludeFeats = this.getAllFeatures();
		
		//if prodFeats contains features that are not on the Feature Model,
		//the product is not valid
		if (excludeFeats.containsAll(prodFeats)){
			excludeFeats.removeAll(prodFeats);
			
			Iterator<GenericFeature> it1 = prodFeats.iterator();
			while(it1.hasNext()){
				GenericFeature f = it1.next();
				fq.addValue(f, 1);
			}
			
			Iterator<GenericFeature> it2 = excludeFeats.iterator();
			while(it2.hasNext()){
				GenericFeature f = it2.next();
				fq.addValue(f, 0);
			}
			
			SetQuestion sq = this.setQuestionFactory();
			sq.addQuestion(fq);
			sq.addQuestion(vq);
			res =  r.ask(sq);
			if (vq.isValid()){
				valid = true;
			}
		}
		
		
		return res;
	}
	
	public abstract SetQuestion setQuestionFactory();
	
	public abstract FilterQuestion filterQuestionFactory();
	
	public abstract ValidQuestion validQuestionFactory();
	
	public abstract Collection<GenericFeature> getAllFeatures();
	
	public abstract PerformanceResult performanceResultFactory();
	
}
