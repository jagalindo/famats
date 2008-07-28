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
package es.us.isa.FAMA.models.featureModel;

import java.util.List;


import es.us.isa.FAMA.models.variabilityModel.VariabilityElement;

public abstract class GenericFeature extends VariabilityElement {

	protected String name;
	protected GenericRelation parent_relation;
	protected List<GenericRelation> relations;
	
		
	
	/* Name ***************************************************************/	
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName( String name ){
		this.name = name;		
	}
	
	/* Others *************************************************************/
	public String toString() {
		return name;
	}

	/*
	 * @Override
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean res = false;
		if (obj instanceof GenericFeature) {
			GenericFeature f = (GenericFeature)obj;
			return this.name.equalsIgnoreCase(f.getName());
		}
		return res;
	}
	
}
