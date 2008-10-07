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
package es.us.isa.FAMA.models;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import spitz.ayal.jarjar.JarJarClassLoader;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;
import es.us.isa.FAMA.models.variabilityModel.parsers.IReader;
import es.us.isa.FAMA.models.variabilityModel.parsers.IWriter;
import es.us.isa.FAMA.models.variabilityModel.parsers.ParserFactory;
import es.us.isa.FAMA.models.variabilityModel.parsers.Factory.ParserAbstractFactory;

/**
 * @author Alberto
 * 
 */
public class ModelTrader {

	private ParserFactory pFactory;

	protected Map<String, IReader> readersMap;
	protected Map<String, IWriter> writersMap;

	public ModelTrader(Node modelTraderNode) {
		readersMap = new HashMap<String, IReader>();
		writersMap = new HashMap<String, IWriter>();

		pFactory = new ParserFactory();

		parseConfigFile(modelTraderNode);
	}

	private void parseConfigFile(Node modelTraderNode) {

		readersMap.clear();
		writersMap.clear();
		if (modelTraderNode == null)
			return;
		NodeList rootList = modelTraderNode.getChildNodes();

		for (int j = 0; j < rootList.getLength(); j++) {
			Node modelNode = rootList.item(j);
			if (modelNode.getNodeType() == Node.ELEMENT_NODE) {
				if (modelNode.getNodeName().equalsIgnoreCase("reader")) {
					processReaderNode(modelNode);
				} else if (modelNode.getNodeName().equalsIgnoreCase("writer")) {
					processWriterNode(modelNode);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processReaderNode(Node readerNode) {
		try {
			Node extNode = readerNode.getAttributes()
					.getNamedItem("extensions");
			Node classNode = readerNode.getAttributes().getNamedItem("class");
			Node fileNode = readerNode.getAttributes().getNamedItem("file");
			if (extNode != null && classNode != null && fileNode != null) {
				String fileName = fileNode.getNodeValue();
				String className = classNode.getNodeValue();
				try {
					//URL url = new URL("jar:file:" + fileName + "!/");
					//url.openConnection();
					ClassLoader loader = new JarJarClassLoader(fileName);
					Class<IReader> rcl = (Class<IReader>) loader
							.loadClass(className);
					if (rcl != null) {
						IReader reader = rcl.newInstance();
						StringTokenizer st = new StringTokenizer(extNode
								.getNodeValue(), ",");
						while (st.hasMoreTokens()) {
							readersMap.put(st.nextToken(), reader);
						}
					}
				} catch (ClassNotFoundException e) {
				} catch (ClassCastException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void processWriterNode(Node writerNode) {
		try {
			Node extNode = writerNode.getAttributes()
					.getNamedItem("extensions");
			Node classNode = writerNode.getAttributes().getNamedItem("class");
			Node fileNode = writerNode.getAttributes().getNamedItem("file");
			if (extNode != null && classNode != null && fileNode != null) {
				String fileName = fileNode.getNodeValue();
				String className = classNode.getNodeValue();
				try {
					//URL url = new URL("jar:file:" + fileName + "!/");
					//url.openConnection();
					JarJarClassLoader loader = new JarJarClassLoader(fileName);
					Class<IWriter> wcl = (Class<IWriter>) loader
							.loadClass(className);
					if (wcl != null) {
						IWriter writer = wcl.newInstance();
						StringTokenizer st = new StringTokenizer(extNode
								.getNodeValue(), ",");
						while (st.hasMoreTokens()) {
							writersMap.put(st.nextToken(), writer);
						}
					}
				} catch (ClassNotFoundException e) {
				} catch (ClassCastException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ParserAbstractFactory getFactory() {
		return pFactory;
	}

	public VariabilityModel openFile(String filename) {

		try {
			String extension = filename
					.substring(filename.lastIndexOf('.') + 1);
			if (hasReader(extension)) {
				IReader reader = readersMap.get(extension);
				if (reader != null) {
					//IReader reader = pFactory.createReader(r);
					return reader.parseFile(filename);
				}
			}
		} catch (Exception e) {
			System.out.println("File " + filename + " is not a valid FM");
		}

		return null;
	}

	public void writeFile(String filename, VariabilityModel vm) {

		try {
			String extension = filename
					.substring(filename.lastIndexOf('.') + 1);
			if (hasWriter(extension)) {
				IWriter writer = writersMap.get(extension);
				if (writer != null) {
					//IWriter writer = pFactory.createWriter(writer);
					writer.writeFile(filename, vm);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean hasReader(String extension) {
		return readersMap.containsKey(extension);
	}

	public boolean hasWriter(String extension) {
		return writersMap.containsKey(extension);
	}

}
