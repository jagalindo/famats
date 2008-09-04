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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import choco.Problem;
import choco.integer.IntDomainVar;
import choco.integer.IntVar;


import es.us.isa.ChocoReasoner.ChocoQuestion;
import es.us.isa.ChocoReasoner.ChocoReasoner;
import es.us.isa.ChocoReasoner.ChocoResult;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.Reasoner.questions.ProductsQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.Product;


public class ChocoProductsQuestion extends ChocoQuestion implements ProductsQuestion{
	
	private List<Product> products;
	
	public ChocoProductsQuestion() {
		products = new LinkedList<Product>();
	}
	
	@Override
	public long getNumberOfProducts() {
		if(products!=null){
		return products.size();
		}else{
			return 0;
		}
	}

	public void preAnswer(Reasoner r) {
		products = new LinkedList<Product>();
	}
	public PerformanceResult answer(Reasoner choco) {
		ChocoReasoner r = (ChocoReasoner)choco;
		ChocoResult res = new ChocoResult();
		Map<IntVar,Integer> sols = new HashMap<IntVar, Integer>();
		List<IntVar> vars= new ArrayList<IntVar>();
		List<Integer> values= new ArrayList<Integer>();
		Problem Chocoproblem=r.getProblem();
		if (Chocoproblem.solve() == Boolean.TRUE) {
			  do {
			    for(int i = 0; i < Chocoproblem.getNbIntVars(); i ++) {
			    	vars.add((Chocoproblem.getIntVar(i)));
			    	values.add(((IntDomainVar) Chocoproblem.getIntVar(i)).getVal());
			    }
			  } while(Chocoproblem.nextSolution() == Boolean.TRUE);
			}
			processMap(vars,values, r);
			res.fillFields(r.getProblem().getSolver());
			return res;
	}
	
	private void processMap(List<IntVar> vars, List<Integer> values,ChocoReasoner reasoner) {
		Map <IntVar,GenericFeature> varFeat = new HashMap<IntVar,GenericFeature>();
		Iterator<IntVar> it1=vars.iterator();
		while (it1.hasNext()) {
			IntVar var = (IntVar)it1.next();
			String temp=new String(var.toString().substring(0,var.toString().indexOf(":")));
			GenericFeature f = reasoner.searchFeatureByName(temp);
			if (f != null){
				varFeat.put(var,f);
			}
			
		}
		
	for(int a=0;a<values.size();a++){//FIXME posible error de cuenteo
		Product product=new Product();
		for(int b=0;b<varFeat.size();b++){
		if(values.get(a)>0){
				product.addFeature(varFeat.get(vars.get(a)));
			}
			a++;
		}
		products.add(product);

	}
			
		
		
		
		
	}	
	

	@Override
	public Collection<Product> getAllProducts() {
		return products;
	}
		
	}



