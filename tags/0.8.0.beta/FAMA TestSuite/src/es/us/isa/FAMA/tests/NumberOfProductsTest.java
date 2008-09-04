package es.us.isa.FAMA.tests;

import org.junit.Test;
import org.junit.Assert;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.models.featureModel.*;

public class NumberOfProductsTest {


	Question q;
	GenericFeatureModel fm;
	QuestionTrader qt;
	
	
	public void numberOfProducts(String inputName, long expectedOutput)
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
		this.numberOfProducts("relationships/excludes/excludes.fama",2);

	}
	
	@Test
	public void NumberOfProductsRequires()
	{
		System.out.println("========= REQUIRES ===========");
		this.numberOfProducts("relationships/requires/requires.fama",2);

	}
	
	
	// INPUT FMS WITH DEAD FEATURES
	
	
	
}
