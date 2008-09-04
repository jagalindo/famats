package es.us.isa.FAMA.tests;

import org.junit.Test;
import org.junit.Assert;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.models.featureModel.*;

public class NumberOfProductsQuestionTest {


	Question q;
	GenericFeatureModel fm;
	QuestionTrader qt;
	
	
	private void numberOfProducts(String inputName, long expectedOutput)
	{
		// Read input
		qt = new QuestionTrader();
		fm = (GenericFeatureModel) qt.openFile("test-inputs/" + inputName);
		qt.setVariabilityModel(fm);
		qt.setCriteriaSelector("selected"); // We select a specific solver
		
		// Perform question
		Question q = qt.createQuestion("#Products");
		@SuppressWarnings("unused")
		PerformanceResult pr = qt.ask(q);
		NumberOfProductsQuestion pq = (NumberOfProductsQuestion) q;
		long np = pq.getNumberOfProducts();
		
		// Show result
		System.out.println("Number of products: " + np);
		
		// Check result
		Assert.assertEquals("Different number of produts",expectedOutput,np);
	}
	
	
	// ERROR-FREE INPUTS
	
	@Test
	public void NumberOfProductsMandatory()
	{
		System.out.println("========= MANDATORY ===========");
		this.numberOfProducts("relationships/mandatory/mandatory.fama",1);
	}
	
	@Test
	public void NumberOfProductsOptional()
	{
		System.out.println("========= OPTIONAL ===========");
		this.numberOfProducts("relationships/optional/optional.fama",2);
	}
	
	@Test
	public void NumberOfProductsAlternative()
	{
		System.out.println("========= ALTERNATIVE ===========");
		this.numberOfProducts("relationships/alternative/alternative.fama",2);
	}
	
	@Test
	public void NumberOfProductsOr()
	{
		System.out.println("========= OR ===========");
		this.numberOfProducts("relationships/or/or.fama",3);
	}
	
	@Test
	public void NumberOfProductsExcludes()
	{
		System.out.println("========= EXCLUDES ===========");
		this.numberOfProducts("relationships/excludes/excludes.fama",3);
	}
	
	@Test
	public void NumberOfProductsRequires()
	{
		System.out.println("========= REQUIRES ===========");
		this.numberOfProducts("relationships/requires/requires.fama",3);
	}
	
	
	// INPUT FMS WITH DEAD FEATURES
	
	
	@Test
	public void NumberOfProductsDFCase1()
	{
		System.out.println("========= DEAD FEATURES (Case 1) ===========");
		this.numberOfProducts("dead-features/case1/df-case1.fama",1);
	}
	
	@Test
	public void NumberOfProductsDFCase2()
	{
		System.out.println("========= DEAD FEATURES (Case 2) ===========");
		this.numberOfProducts("dead-features/case2/df-case2.fama",1);
	}
	
	@Test
	public void NumberOfProductsDFCase3()
	{
		System.out.println("========= DEAD FEATURES (Case 3) ===========");
		this.numberOfProducts("dead-features/case3/df-case3.fama",1);
	}
	
	@Test
	public void NumberOfProductsDFCase4()
	{
		System.out.println("========= DEAD FEATURES (Case 4) ===========");
		this.numberOfProducts("dead-features/case4/df-case4.fama",1);
	}
	
	@Test
	public void NumberOfProductsDFCase5()
	{
		System.out.println("========= DEAD FEATURES (Case 5) ===========");
		this.numberOfProducts("dead-features/case5/df-case5.fama",0);
	}
	
	@Test
	public void NumberOfProductsDFCase6()
	{
		System.out.println("========= DEAD FEATURES (Case 6) ===========");
		this.numberOfProducts("dead-features/case6/df-case6.fama",1);
	}
	
	@Test
	public void NumberOfProductsDFCase7()
	{
		System.out.println("========= DEAD FEATURES (Case 7) ===========");
		this.numberOfProducts("dead-features/case7/df-case7.fama",0);
	}
	
	@Test
	public void NumberOfProductsDFCase8()
	{
		System.out.println("========= DEAD FEATURES (Case 8) ===========");
		this.numberOfProducts("dead-features/case8/df-case8.fama",2);
	}
	
	@Test
	public void NumberOfProductsDFCase9()
	{
		System.out.println("========= DEAD FEATURES (Case 9) ===========");
		this.numberOfProducts("dead-features/case9/df-case9.fama",0);
	}
}
