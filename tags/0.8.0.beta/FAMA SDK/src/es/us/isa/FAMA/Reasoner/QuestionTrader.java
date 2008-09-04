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

package es.us.isa.FAMA.Reasoner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import spitz.ayal.jarjar.JarJarClassLoader;
import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Factory.QuestionAbstractFactory;
import es.us.isa.FAMA.models.ModelTrader;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;


/**
 * @author   trinidad  
 */
public class QuestionTrader {
	
	protected ModelTrader mt;
	protected VariabilityModel fm;
	protected ArrayList<QuestionAbstractFactory> questionFactoriesList;
	protected Map<Reasoner,QuestionAbstractFactory> reasonersMap;
	protected Map<String,Reasoner> reasonersIdMap;
	protected Map<String,Class<Question>> questionsMap;
	protected CriteriaSelector selector;
	protected Map<String, CriteriaSelector> selectorsMap;
	protected ClassLoader famaloader;
	
	
	public QuestionTrader () {
		
		reasonersMap = new HashMap<Reasoner, QuestionAbstractFactory>();
		selectorsMap = new HashMap<String, CriteriaSelector>();
		questionsMap = new HashMap<String, Class<Question>>();
		reasonersIdMap = new HashMap<String,Reasoner> ();
		
		selector = new DefaultCriteriaSelector(this);
		selectorsMap.put("Default",selector);
		
		mt = null;
		parseConfigFile();
	}
	
	
	private void parseConfigFile() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document configDocument = builder.parse("FAMAconfig.xml");
			reasonersMap.clear();
			if (configDocument == null) return;
			NodeList rootList = configDocument.getChildNodes();
			if (rootList.getLength() == 1) {
				Node root = rootList.item(0);
				if (root.getNodeName().equalsIgnoreCase("questionTrader") && root.hasChildNodes()) {
					NodeList questionTraderNL = root.getChildNodes();
					for (int i = 0; i < questionTraderNL.getLength(); i++) {
						Node questionTraderNode = questionTraderNL.item(i);
						if (questionTraderNode.getNodeType() == Node.ELEMENT_NODE) {
							if (questionTraderNode.getNodeName().equalsIgnoreCase("reasoner")) {
								processReasonerNode(questionTraderNode);
							}
							else if (questionTraderNode.getNodeName().equalsIgnoreCase("criteriaSelector")) {
								processCriteriaSelectorNode(questionTraderNode);
							}
							else if (questionTraderNode.getNodeName().equalsIgnoreCase("question")) {
								processQuestionNode(questionTraderNode);
							}
							else if (questionTraderNode.getNodeName().equalsIgnoreCase("models")) {
								processModelsNode(questionTraderNode);
							}
						}
					}
				}
			}
		} catch (ParserConfigurationException e1){
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			
		} 				
	}

	private void processModelsNode(Node modelTraderNode) {
		mt = new ModelTrader(modelTraderNode);		
	}


	@SuppressWarnings("unchecked")
	private void processQuestionNode(Node questionTraderNode) {
		try {
			Node idNode = questionTraderNode.getAttributes().getNamedItem("id");
			Node classNode = questionTraderNode.getAttributes().getNamedItem("interface");
			Node fileNode = questionTraderNode.getAttributes().getNamedItem("file");
			if (idNode != null && classNode != null && fileNode != null) {
				String interfaceName = classNode.getNodeValue();
				String fileName = fileNode.getNodeValue();
				try {
					URL url = new URL("jar:file:"+fileName + "!/");
					url.openConnection();
					ClassLoader loader = new JarJarClassLoader(fileName);
					Class<Question> qcl = (Class<Question>) loader.loadClass(interfaceName);
					if (qcl != null){
						questionsMap.put(idNode.getNodeValue(), qcl );
					}
				} catch (ClassNotFoundException e) {}
				  catch (ClassCastException e) {}									
			}	
		} catch (Exception e) {e.printStackTrace();}
	} 


	@SuppressWarnings("unchecked")
	private void processCriteriaSelectorNode(Node csNode) {
		Node fileNode = csNode.getAttributes().getNamedItem("file");
		Node classNode = csNode.getAttributes().getNamedItem("class");
		Node nameNode = csNode.getAttributes().getNamedItem("name");
		try {
			if (fileNode != null && classNode != null) {
				String className = classNode.getNodeValue();
				String fileName = fileNode.getNodeValue();
				String nameName = nameNode.getNodeValue();
				
				URL url = new URL("jar:file:"+fileName + "!/");
				url.openConnection();
				URL []urlArray = new URL[]{url};
				URLClassLoader loader = new URLClassLoader(urlArray);
				Class<CriteriaSelector>csc = (Class<CriteriaSelector>) loader.loadClass(className);
				if (csc != null) {
					CriteriaSelector cs = csc.newInstance();
					this.selectorsMap.put(nameName,cs);
				}			
			} else if (fileNode == null && classNode != null) {
				String className = classNode.getNodeValue();
				String nameName = nameNode.getNodeValue();
				Class<CriteriaSelector> csc = (Class<CriteriaSelector>) Class.forName(className);
				if (csc != null) {
					Constructor<CriteriaSelector>[] cons = (Constructor<CriteriaSelector>[]) csc.getConstructors();
					CriteriaSelector cs = null;
					// i'm searching for the constructor having a QuestionTrader as a parameter. Instead of
					// checking all the constructors for their parameters type, I just try the newInstance method
					// to work and in case it fails (we're calling another constructor) we capture the 
					// InstantiationException so it makes no noise and keeps on searching for the right constructor
					for (int i = 0; i < cons.length && cs == null; i++) {  
						try {
							cs = cons[i].newInstance(this);
						} catch (InstantiationException ie) {}
					}
					if (cs != null)
						this.selectorsMap.put(nameName, cs);
				}
			}
		} catch (Exception e) {e.printStackTrace();}
	}

	
	@SuppressWarnings("unchecked")
	private void processReasonerNode(Node reasonerNode) {
		Node fileNode = reasonerNode.getAttributes().getNamedItem("file");
		Node classNode = reasonerNode.getAttributes().getNamedItem("class");
		Node idNode = reasonerNode.getAttributes().getNamedItem("id");
		if (fileNode != null && classNode != null) {
			String className = classNode.getNodeValue();
			String fileName = fileNode.getNodeValue();
			String idName = className;
			if (idNode != null)
				idName = idNode.getNodeValue();
				
			try {
				//URL url = new URL("jar:file:"+ fileName + "!/");
				//url.openConnection();
				ClassLoader loader = new JarJarClassLoader(fileName);
				Class<Reasoner>cl = (Class<Reasoner>) loader.loadClass(className);
				InputStream configStream = loader.getResourceAsStream("config.xml");
				if (cl != null) {
					Reasoner reasoner = cl.newInstance();
					reasoner.setConfigFile(configStream,loader);
					this.reasonersMap.put(reasoner, reasoner.getFactory());
					reasonersIdMap.put(idName, reasoner);
				}
			} catch (Exception e) {e.printStackTrace();}			
		}
	}

	public PerformanceResult ask (Question q) {
		PerformanceResult res = null;
		if (q != null) {
			Class<? extends Reasoner> reasonerClass = q.getReasonerClass();
			Iterator<Reasoner> itr = reasonersMap.keySet().iterator();
			while (itr.hasNext() && res == null) {
				Reasoner r = itr.next();
				if (reasonerClass.isInstance(r)) {
					fm.transformTo(r);
					res = r.ask(q);
					selector.registerResults(q, fm, res);
				}
			}
		}
		return res;
	}
	
	public Question createQuestion (String questionType) {
		// Receive a question name and return its 
		// associated interface
		
		Class<Question> q = questionsMap.get(questionType);
		
		if ( q != null){
			return selector.createQuestion(q, fm);
		}
		
		return null;

	}

	
	public void setVariabilityModel (VariabilityModel fm) {
		this.fm = fm;		
	}
		
	public Iterator<String> getReasonerIds() {
		return this.reasonersIdMap.keySet().iterator();
	}
	
	public Reasoner getReasonerById (String id) {
		return reasonersIdMap.get(id);
	}
	
	public Iterator<String> getQuestionsIds() {
		return this.questionsMap.keySet().iterator();
	}
	
	public Class<Question> getQuestionById (String id) {
		return questionsMap.get(id);
	}
	
	public Iterator<String> getCriteriaSelectorNames() {
		return this.selectorsMap.keySet().iterator();
	}

	public CriteriaSelector getCriteriaSelector(String selectorName) {
		return this.selectorsMap.get(selectorName);
	}
	
	public boolean setCriteriaSelector(String criteriaName) {
		CriteriaSelector newSelector = selectorsMap.get(criteriaName);
		boolean res = (newSelector != null);
		if (res) {
			this.selector = newSelector;
		}
		return res;
	}
	
	public VariabilityModel openFile(String filename) {	
		return mt.openFile(filename);
	}
	
	public void writeFile(String filename, VariabilityModel vm){
		mt.writeFile(filename,vm);
	}
}
