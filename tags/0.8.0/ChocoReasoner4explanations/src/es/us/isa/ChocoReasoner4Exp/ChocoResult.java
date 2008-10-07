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
package es.us.isa.ChocoReasoner4Exp;

import choco.kernel.solver.Solver;
import choco.kernel.solver.search.AbstractGlobalSearchStrategy;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;



public class ChocoResult extends PerformanceResult{
	
	protected int depth;
	protected int nodes;
	protected int time;
	
	public ChocoResult() {
	}

	public void fillFields(Solver s) {
		AbstractGlobalSearchStrategy S2=s.getSearchStrategy();
		nodes=S2.getNodeCount();
		depth=S2.getLoggingMaxDepth();
		time=S2.getTimeCount();
	}
	
	public void addFields(Solver s){
		AbstractGlobalSearchStrategy S2=s.getSearchStrategy();
		nodes +=S2.getNodeCount();
		depth+=S2.getLoggingMaxDepth();
		time+=S2.getTimeCount();
	}
	
	public void addFields(ChocoResult c){
		nodes += c.nodes;
		depth += c.depth;
		time += c.time;
	}
	
}
