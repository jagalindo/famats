package es.us.isa.FAMA.ReasonersTests;

import org.junit.Test;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;

/**
 * This jUnit test case tests the Valid question for anyone reasoner
 * @author Jesús
 *
 */
public class ValidTestSuite extends TestSuite {

	@Test
	public void testValidQuestion(){
		Question q = qt.createQuestion("Valid");
		@SuppressWarnings("unused")
		PerformanceResult pr = qt.ask(q);
		ValidQuestion	pq = (ValidQuestion) q;
		System.out.println("---- VALID MODEL QUESTION TEST ----");
		System.out.println("Is the model valid? "+pq.isValid());
	}
	
}
