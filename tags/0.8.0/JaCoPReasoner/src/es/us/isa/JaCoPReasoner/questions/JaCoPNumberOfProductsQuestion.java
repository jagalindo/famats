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
package es.us.isa.JaCoPReasoner.questions;

import java.util.ArrayList;

import JaCoP.Delete;
import JaCoP.FDstore;
import JaCoP.Indomain;
import JaCoP.IndomainMin;
import JaCoP.MostConstrainedDynamic;
import JaCoP.SearchAll;
import JaCoP.Variable;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.JaCoPReasoner.JaCoPQuestion;
import es.us.isa.JaCoPReasoner.JaCoPReasoner;
import es.us.isa.JaCoPReasoner.JaCoPResult;

public class JaCoPNumberOfProductsQuestion extends JaCoPQuestion implements
		NumberOfProductsQuestion {

	protected long NoP;
	
	/**
	 * 
	 */
	public JaCoPNumberOfProductsQuestion() {
		super();
		NoP = 0;
		heuristics = new Delete(new MostConstrainedDynamic());
	}

	/* (non-Javadoc)
	 * @see tdg.SPL.Reasoner.NumberOfProductsQuestion#getNumberOfProducts()
	 */
	public long getNumberOfProducts() {
		return NoP;
	}

	/* (non-Javadoc)
	 * @see tdg.SPL.Reasoner.Question#preReason(tdg.SPL.Reasoner.Reasoner)
	 */
	public PerformanceResult answer(JaCoPReasoner r) {
		JaCoPReasoner jacop = (JaCoPReasoner)r;
		JaCoPResult res = new JaCoPResult();
		if (jacop.consistency()) {
			jacop.setReify(false);
			FDstore store = jacop.getStore();
			ArrayList<Variable> vars = jacop.getVariables();
			
			SearchAll sa = new SearchAll();
			Indomain indomain = new IndomainMin();
			sa.setOnlyCount(true);
			sa.labeling(store, vars, indomain, heuristics);
			NoP = sa.solutionsNo;
			res.fillFields(sa);
		} else {
			NoP = 0;
		}
		
		return res;
	}
	
	public String toString() {
		return "Number of Products (JaCoP)= " + getNumberOfProducts();
	}

}
