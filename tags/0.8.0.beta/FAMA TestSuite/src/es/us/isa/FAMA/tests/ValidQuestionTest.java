package es.us.isa.FAMA.tests;

import org.junit.Test;
import org.junit.Assert;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.featureModel.*;

public class ValidQuestionTest {


	Question q;
	GenericFeatureModel fm;
	QuestionTrader qt;
	
	
	private void validFM(String inputName, boolean expectedOutput)
	{
		// Read input
		qt = new QuestionTrader();
		fm = (GenericFeatureModel) qt.openFile("test-inputs/" + inputName);
		qt.setVariabilityModel(fm);
		qt.setCriteriaSelector("selected"); // We select a specific solver
		
		// Perform question
		Question q = qt.createQuestion("Valid");
		PerformanceResult pr = qt.ask(q);
		ValidQuestion	pq = (ValidQuestion) q;

		// Show result
		System.out.println("Is the model valid? "+ pq.isValid());
		
		// Check result
		Assert.assertTrue("Wrong answer", pq.isValid()== expectedOutput);

	}
	
	
	// ERROR-FREE INPUTS
	
	@Test
	public void validFMMandatory()
	{
		System.out.println("========= MANDATORY ===========");
		this.validFM("relationships/mandatory/mandatory.fama",true);
	}
	
	@Test
	public void validFMOptional()
	{
		System.out.println("========= OPTIONAL ===========");
		this.validFM("relationships/optional/optional.fama",true);
	}
	
	@Test
	public void validFMAlternative()
	{
		System.out.println("========= ALTERNATIVE ===========");
		this.validFM("relationships/alternative/alternative.fama",true);
	}
	
	@Test
	public void validFMOr()
	{
		System.out.println("========= OR ===========");
		this.validFM("relationships/or/or.fama",true);
	}
	
	@Test
	public void validFMExcludes()
	{
		System.out.println("========= EXCLUDES ===========");
		this.validFM("relationships/excludes/excludes.fama",true);
	}
	
	@Test
	public void validFMRequires()
	{
		System.out.println("========= REQUIRES ===========");
		this.validFM("relationships/requires/requires.fama",true);
	}
	
	
	// INPUT FMS WITH DEAD FEATURES

	
	@Test
	public void validFMDFCase5()
	{
		System.out.println("========= DEAD FEATURES (Case 5) ===========");
		this.validFM("dead-features/case5/df-case5.fama",false);
	}

	@Test
	public void validFMDFCase7()
	{
		System.out.println("========= DEAD FEATURES (Case 7) ===========");
		this.validFM("dead-features/case7/df-case7.fama",false);
	}
	
	@Test
	public void validFMDFCase9()
	{
		System.out.println("========= DEAD FEATURES (Case 9) ===========");
		this.validFM("dead-features/case9/df-case9.fama",false);
	}
}
