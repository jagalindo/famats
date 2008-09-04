package es.us.isa.FAMA.tests;

import org.junit.Test;
import org.junit.Assert;

import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.models.FAMAfeatureModel.Feature;
import es.us.isa.FAMA.models.featureModel.*;
import es.us.isa.FAMA.models.featureModel.Product;

public class ValidProductQuestionTest {


	Question q;
	GenericFeatureModel fm;
	QuestionTrader qt;
	
	
	private void validProduct(String inputName, Product p, boolean expectedOutput)
	{
		// Read input
		qt = new QuestionTrader();
		fm = (GenericFeatureModel) qt.openFile("test-inputs/" + inputName);
		qt.setVariabilityModel(fm);
		qt.setCriteriaSelector("selected"); // We select a specific solver
		
		// Perform question
		

	}
	
	
	// ERROR-FREE INPUTS
	
	@Test
	public void ValidProductMandatory()
	{
		System.out.println("========= MANDATORY (Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("relationships/mandatory/mandatory.fama",p,true);
	}
	
	@Test
	public void NoValidProductMandatory()
	{
		System.out.println("========= MANDATORY (No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		
		this.validProduct("relationships/mandatory/mandatory.fama",p,false);
	}
	
	@Test
	public void ValidProductOptional()
	{
		System.out.println("========= OPTIONAL (Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		
		this.validProduct("relationships/optional/optional.fama",p,true);
	}
	
	
	@Test
	public void ValidProductAlternative()
	{
		System.out.println("========= ALTERNATIVE (Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("relationships/alternative/alternative.fama",p,true);
	}
	
	@Test
	public void NoValidProductAlternative()
	{
		System.out.println("========= ALTERNATIVE (No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		
		this.validProduct("relationships/alternative/alternative.fama",p,false);
	}
	
	@Test
	public void ValidProductOr()
	{
		System.out.println("========= OR (Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		
		this.validProduct("relationships/or/or.fama",p,true);
	}
	
	@Test
	public void NoValidProductOr()
	{
		System.out.println("========= OR (No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		
		this.validProduct("relationships/or/or.fama",p,false);
	}
	
	@Test
	public void ValidProductExcludes()
	{
		System.out.println("========= EXCLUDES (Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		
		this.validProduct("relationships/excludes/excludes.fama",p,true);
	}
	
	@Test
	public void NoValidProductExcludes()
	{
		System.out.println("========= EXCLUDES (No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("relationships/excludes/excludes.fama",p,false);
	}
	
	@Test
	public void ValidProductRequires()
	{
		System.out.println("========= REQUIRES (Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("relationships/requires/requires.fama",p,true);
	}
	
	
	@Test
	public void NoValidProductRequires()
	{
		System.out.println("========= REQUIRES (No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		
		this.validProduct("relationships/requires/requires.fama",p,false);
	}
	
	// INPUT FMS WITH DEAD FEATURES
	
	
	@Test
	public void ValidProductDFCase1()
	{
		System.out.println("========= DEAD FEATURES (Case 1 - Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		p.addFeature(new Feature("E"));
		
		this.validProduct("dead-features/case1/df-case1.fama",p,true);
	}
	
	@Test
	public void NoValidProductDFCase1()
	{
		System.out.println("========= DEAD FEATURES (Case 1 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		p.addFeature(new Feature("D"));
		
		this.validProduct("dead-features/case1/df-case1.fama",p,false);
	}
	
	@Test
	public void ValidProductDFCase2()
	{
		System.out.println("========= DEAD FEATURES (Case 2 - Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		p.addFeature(new Feature("D"));
		
		this.validProduct("dead-features/case2/df-case2.fama",p,true);
	}
	
	@Test
	public void NoValidProductDFCase2()
	{
		System.out.println("========= DEAD FEATURES (Case 2 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		p.addFeature(new Feature("E"));
		
		this.validProduct("dead-features/case2/df-case2.fama",p,false);
	}
	
	@Test
	public void ValidProductDFCase3()
	{
		System.out.println("========= DEAD FEATURES (Case 3 - Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		p.addFeature(new Feature("E"));
		
		this.validProduct("dead-features/case3/df-case3.fama",p,true);
	}
	
	@Test
	public void NoValidProductDFCase3()
	{
		System.out.println("========= DEAD FEATURES (Case 3 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		p.addFeature(new Feature("D"));
		
		this.validProduct("dead-features/case3/df-case3.fama",p,false);
	}
	
	@Test
	public void ValidProductDFCase4()
	{
		System.out.println("========= DEAD FEATURES (Case 4 - Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("dead-features/case4/df-case4.fama",p,true);
	}
	
	@Test
	public void NoValidProductDFCase4()
	{
		System.out.println("========= DEAD FEATURES (Case 4 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		
		this.validProduct("dead-features/case4/df-case4.fama",p,false);
	}
	
	@Test
	public void NoValidProductDFCase5()
	{
		System.out.println("========= DEAD FEATURES (Case 5 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		p.addFeature(new Feature("C"));
		
		this.validProduct("dead-features/case5/df-case5.fama",p,false);
	}
	
	@Test
	public void ValidProductDFCase6()
	{
		System.out.println("========= DEAD FEATURES (Case 6 - Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("C"));
		
		this.validProduct("dead-features/case6/df-case6.fama",p,true);
	}
	
	@Test
	public void NoValidProductDFCase6()
	{
		System.out.println("========= DEAD FEATURES (Case 6 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("dead-features/case6/df-case6.fama",p,false);
	}
	
	@Test
	public void NoValidProductDFCase7()
	{
		System.out.println("========= DEAD FEATURES (Case 7 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("dead-features/case7/df-case7.fama",p,false);
	}
	
	@Test
	public void ValidProductDFCase8()
	{
		System.out.println("========= DEAD FEATURES (Case 8 - Valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("C"));
		
		this.validProduct("dead-features/case8/df-case8.fama",p,true);
	}
	
	@Test
	public void NoValidProductDFCase8()
	{
		System.out.println("========= DEAD FEATURES (Case 8 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("B"));
		
		this.validProduct("dead-features/case8/df-case8.fama",p,false);
	}
	
	@Test
	public void NoValidProductDFCase9()
	{
		System.out.println("========= DEAD FEATURES (Case 9 - No valid product) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		
		this.validProduct("dead-features/case9/df-case9.fama",p,false);
	}
	
	// ERRONEUS INPUT
	
	@Test
	public void ValidProductErroneousInput()
	{
		System.out.println("========= MANDATORY (Erroneous input) ===========");
		
		Product p = new Product();
		p.addFeature(new Feature("A"));
		p.addFeature(new Feature("D"));
		
		this.validProduct("relationships/mandatory/mandatory.fama",p,true);
	}
}
