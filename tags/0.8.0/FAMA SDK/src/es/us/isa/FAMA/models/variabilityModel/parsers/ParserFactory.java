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
package es.us.isa.FAMA.models.variabilityModel.parsers;

import es.us.isa.FAMA.models.variabilityModel.parsers.Factory.ParserAbstractFactory;

public class ParserFactory implements ParserAbstractFactory {

	public ParserFactory() {}
	
	@SuppressWarnings("unused")
	public IReader createReader(Class<IReader> readerClass) {
		IReader res = null;
		
		if (readerClass != null) {
			try {
				ClassLoader cl1 = this.getClass().getClassLoader();
				ClassLoader cl2 = readerClass.getClassLoader();
				res = readerClass.newInstance();
				if (!readerClass.isInstance(res))
					res = null;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	@SuppressWarnings("unused")
	public IWriter createWriter(Class<IWriter> writerClass) {
		IWriter res = null;
		
		if (writerClass != null) {
			try {
				ClassLoader cl1 = this.getClass().getClassLoader();
				ClassLoader cl2 = writerClass.getClassLoader();
				res = writerClass.newInstance();
				if (!writerClass.isInstance(res))
					res = null;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
}
