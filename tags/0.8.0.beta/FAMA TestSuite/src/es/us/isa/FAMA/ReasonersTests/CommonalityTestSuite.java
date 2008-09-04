package es.us.isa.FAMA.ReasonersTests;

import java.util.Iterator;

import org.junit.Test;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.models.featureModel.GenericFeature;


/**
 * This jUnit test case tests the commonality question for anyone reasoner.
 * @author Jesús
 *
 */
public class CommonalityTestSuite extends TestSuite {

	@Test
	public void testCommonalityQuestion(){
		this.fm.getFeatures().iterator();
		Question q = qt.createQuestion("Commonality");
		CommonalityQuestion	pq = (CommonalityQuestion) q;
		Iterator<? extends GenericFeature> it = this.fm.getFeatures().iterator();
		System.out.println("---- COMMONALITY QUESTION TEST ----");
		while (it.hasNext()){
			GenericFeature f = it.next();
			pq.setFeature(f);
			@SuppressWarnings("unused")
			PerformanceResult pr = qt.ask(pq);
			Long l = pq.getCommonality();
			System.out.println("\nFeature " + f.getName() + ", Commonality: " + l + "\n");
		}
	}
	
}
