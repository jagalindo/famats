package es.us.isa.FAMA.ReasonersTest.CriteriaSelector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.CriteriaSelector;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.Reasoner;
import es.us.isa.FAMA.models.variabilityModel.VariabilityModel;

/**
 * With this class, we choose the reasoner specified on reasoner.properties
 * @author Jesús
 *
 */
public class SelectedCriteriaSelector extends CriteriaSelector {
	
	public SelectedCriteriaSelector(QuestionTrader qt) {
		super(qt);
	}

	@Override
	public Question createQuestion(Class<Question> questionInt,
			VariabilityModel vm) {

		Question q = null;
		if (qt != null){
			Reasoner r = getSelectedReasoner();
			if (r != null){
				q = r.getFactory().createQuestion(questionInt);
			}
		}
		return q;
		
	}

	private Reasoner getSelectedReasoner(){
		
		Reasoner r = null;
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("reasoner.properties"));
			String rName = p.getProperty("reasoner");
			r = qt.getReasonerById(rName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
		
	}
	
	@Override
	public void registerResults(Question q, VariabilityModel vm,
			PerformanceResult pr) {

	}

}
