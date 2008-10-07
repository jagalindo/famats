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
package es.us.isa.JaCoPReasoner;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import JaCoP.Search;

/**
 * @author  Dani
 */
public class JaCoPResult extends PerformanceResult {
	protected int backtracks;
	protected int decisions;
	protected int depth;
	protected int nodes;
	protected int wrongDecisions;
	
	public JaCoPResult() {
	}
	
	public JaCoPResult(Search s) {
		fillFields(s);
	}
	
	public void fillFields(Search s) {
		backtracks = s.getBacktracks();
		decisions = s.getDecisions();
		depth = s.getDepth();
		nodes = s.getNodes();
		wrongDecisions = s.getWrongDecisions();
	}
	
	public void addFields(Search s) {
		backtracks += s.getBacktracks();
		decisions += s.getDecisions();
		depth += s.getDepth();
		nodes += s.getNodes();
		wrongDecisions += s.getWrongDecisions();	
	}
	
	public void addFields(JaCoPResult jr) {
		this.backtracks += jr.backtracks;
		this.decisions += jr.decisions;
		this.depth += jr.depth;
		this.nodes += jr.nodes;
		this.wrongDecisions += jr.wrongDecisions;
	}
	
	public String toString() {
		String res = String.valueOf(backtracks);
		return res;
	}

	/**
	 * @return  Returns the backtracks.
	 * @uml.property  name="backtracks"
	 */
	public int getBacktracks() {
		return backtracks;
	}

	/**
	 * @return  Returns the decisions.
	 * @uml.property  name="decisions"
	 */
	public int getDecisions() {
		return decisions;
	}

	/**
	 * @return  Returns the depth.
	 * @uml.property  name="depth"
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @return  Returns the nodes.
	 * @uml.property  name="nodes"
	 */
	public int getNodes() {
		return nodes;
	}

	/**
	 * @return  Returns the wrongDecisions.
	 * @uml.property  name="wrongDecisions"
	 */
	public int getWrongDecisions() {
		return wrongDecisions;
	}
}
