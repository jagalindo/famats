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
/*
 * Created on 10-Jan-2005
 *
 */
package es.us.isa.FAMA.models.featureModel;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * @author trinidad, Manuel Nieto Ucl�s, Jose
 *
 */
public class Product {
	//TODO getAllFeatures(): Collection ---> ask Pablo
	private List<GenericFeature> listOfFeatures;
	
	public Product () {
		listOfFeatures = new ArrayList<GenericFeature>();
	}
	
	public int getNumberOfFeatures() {
		return listOfFeatures.size();
	}
	
	public GenericFeature getFeature(int index) throws IndexOutOfBoundsException{
		if ( index >= 0 && index < listOfFeatures.size())
			return (GenericFeature)(listOfFeatures.get(index));
		else
			throw new IndexOutOfBoundsException("Feature does not exist in this product");
	}
	
	public void addFeature (GenericFeature f) {
		listOfFeatures.add(f);
	}
	
	public Collection<GenericFeature> getFeatures(){
		return listOfFeatures;
	}
	
	public boolean equals(Object p){
		boolean eq=false;
		if (p instanceof Product){
			Collection<GenericFeature> listOfFeat1=((Product) p).getFeatures();
			if(listOfFeat1.containsAll(listOfFeatures)&&listOfFeatures.containsAll(listOfFeat1))
				eq=true;
		}
		
		return eq;
	}
}
