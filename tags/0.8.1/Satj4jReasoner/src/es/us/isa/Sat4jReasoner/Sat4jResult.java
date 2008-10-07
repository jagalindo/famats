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
package es.us.isa.Sat4jReasoner;

import java.util.Map;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;

/**
 * @author  Sergio
 */
public class Sat4jResult extends PerformanceResult {
	
	/**
	 * @uml.property  name="propagations"
	 */
	private int propagations;
	private int learnedliterals;
	private int changedreason;
	private int reduceddb;
	/**
	 * @uml.property  name="starts"
	 */
	private int starts;
	private int learnedbinaryclauses;
	private int learnedclauses;
	private int reducedliterals;
	private int learnedternaryclauses;
	/**
	 * @uml.property  name="conflicts"
	 */
	private int conflicts;
	/**
	 * @uml.property  name="inspects"
	 */
	private int inspects;
	private int rootsimplifications;
	/**
	 * @uml.property  name="decisions"
	 */
	private int decisions;
	
	public Sat4jResult() {
		propagations = 0;
		learnedliterals = 0;
		changedreason = 0;
		reduceddb = 0;
		starts = 0;
		learnedbinaryclauses = 0;
		learnedclauses = 0;
		reducedliterals = 0;
		learnedternaryclauses = 0;
		conflicts = 0;
		inspects = 0;
		rootsimplifications = 0;
		decisions = 0;
	}
	
	/**
	 * @return
	 * @uml.property  name="propagations"
	 */
	public int getPropagations() {
		return propagations;
	}
	
	public int getLearnedLiterals() {
		return learnedliterals;
	}
	
	public int getChangedReason() {
		return changedreason;
	}
	
	public int getReducedDB() {
		return reduceddb;
	}
	
	/**
	 * @return
	 * @uml.property  name="starts"
	 */
	public int getStarts() {
		return starts;
	}
	
	public int getLearnedBinaryClauses() {
		return learnedbinaryclauses;
	}
	
	public int getLearnedClauses() {
		return learnedclauses;
	}
	
	public int getReducedLiterals() {
		return reducedliterals;
	}
	
	public int getLearnedTernaryClauses() {
		return learnedternaryclauses;
	}
	
	/**
	 * @return
	 * @uml.property  name="conflicts"
	 */
	public int getConflicts() {
		return conflicts;
	}
	
	/**
	 * @return
	 * @uml.property  name="inspects"
	 */
	public int getInspects() {
		return inspects;
	}
	
	public int getRootSimplications() {
		return rootsimplifications;
	}
	
	/**
	 * @return
	 * @uml.property  name="decisions"
	 */
	public int getDecisions() {
		return decisions;
	}
	
	public void fillFields(Map<String,Number> stats) {
		propagations = (stats.get("propagations")).intValue();
		learnedliterals = (stats.get("learnedliterals")).intValue();
		changedreason = (stats.get("changedreason")).intValue();
		reduceddb = (stats.get("reduceddb")).intValue();
		starts = (stats.get("starts")).intValue();
		learnedbinaryclauses = (stats.get("learnedbinaryclauses")).intValue();
		learnedclauses = (stats.get("learnedclauses")).intValue();
		reducedliterals = (stats.get("reducedliterals")).intValue();
		learnedternaryclauses = (stats.get("learnedternaryclauses")).intValue();
		conflicts = (stats.get("conflicts")).intValue();
		inspects = (stats.get("inspects")).intValue();
		rootsimplifications = (stats.get("rootSimplifications")).intValue();
		decisions = (stats.get("decisions")).intValue();
	}
	
	public void addFields(Sat4jResult sr) {
		propagations += sr.getPropagations();
		learnedliterals += sr.getLearnedLiterals();
		changedreason += sr.getChangedReason();
		reduceddb += sr.getReducedDB();
		starts += sr.getStarts();
		learnedbinaryclauses += sr.getLearnedBinaryClauses();
		learnedclauses += sr.getLearnedClauses();
		reducedliterals += sr.getReducedLiterals();
		learnedternaryclauses += sr.getLearnedTernaryClauses();
		conflicts += sr.getConflicts();
		inspects += sr.getInspects();
		rootsimplifications += sr.getRootSimplications();
		decisions += sr.getDecisions();
	}
	
	public String toString() {
		String res = "Main SAT Statistics:\n" +
				     "Time (ms): " + this.getTime() + "\n" +
				     "Conflicts: " + String.valueOf(conflicts) + "\n" +
				     "Decisions: " + String.valueOf(decisions);
		
		return res;
	}
	
}
