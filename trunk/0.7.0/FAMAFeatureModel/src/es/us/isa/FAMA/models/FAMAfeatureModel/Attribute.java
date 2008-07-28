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
package es.us.isa.FAMA.models.FAMAfeatureModel;


/**
 * @author   Dani
 */
public class Attribute {
	public final static int ATT_INTEGER = 0;
	public final static int ATT_REAL = 1;
	public final static int ATT_NATURAL = 2;
	public final static int ATT_BOOLEAN = 3;
	 
	/**
	 * @uml.property  name="name"
	 */
	protected String name;
	
	
	
	/**
	 * @uml.property  name="domain"
	 */
	protected int domain;
	protected Constraint definition;
	protected Constraint defValue;
	
	/**
	 * @return
	 * @uml.property  name="definition"
	 */
	public Constraint getDefinition() {
		return definition;
	}
	/**
	 * @param definition
	 * @uml.property  name="definition"
	 */
	public void setDefinition(Constraint definition) {
		this.definition = definition;
	}
	/**
	 * @return
	 * @uml.property  name="defValue"
	 */
	public Constraint getDefValue() {
		return defValue;
	}
	/**
	 * @param defValue
	 * @uml.property  name="defValue"
	 */
	public void setDefValue(Constraint defValue) {
		this.defValue = defValue;
	}
	/**
	 * @return
	 * @uml.property  name="domain"
	 */
	public int getDomain() {
		return domain;
	}
	/**
	 * @param domain
	 * @uml.property  name="domain"
	 */
	public void setDomain(int domain) {
		this.domain = domain;
	}
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}	
}
