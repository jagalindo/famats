package es.us.isa.FAMA.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import es.us.isa.FAMA.Benchmarking.PerformanceResult;
import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.ProductsQuestion;
import es.us.isa.FAMA.models.FAMAfeatureModel.Feature;
import es.us.isa.FAMA.models.featureModel.*;

public class ProductsQuestionTest {


	Question q;
	GenericFeatureModel fm;
	QuestionTrader qt;
	
	// Features
	private final Feature A = new Feature("A");
	private final Feature B = new Feature("B");
	private final Feature C = new Feature("C");
	private final Feature D = new Feature("D");
	private final Feature E = new Feature("E");
	
	
	private void products(String inputName, List<Product> products)
	{
		// Read input
		qt = new QuestionTrader();
		fm = (GenericFeatureModel) qt.openFile("test-inputs/" + inputName);
		qt.setVariabilityModel(fm);
		qt.setCriteriaSelector("selected"); // We select a specific solver
		
		// Perform question
		Question q = qt.createQuestion("Products");
		@SuppressWarnings("unused")
		PerformanceResult pr = qt.ask(q);
		ProductsQuestion pq = (ProductsQuestion) q;

		// Show result
		int np = (int) pq.getNumberOfProducts();
		Iterator<Product> it = pq.getAllProducts().iterator();
		int i = 0;
		while (it.hasNext()){
			Product p = it.next();
			System.out.print("PRODUCT "+i+": ");
			int jmax = p.getNumberOfFeatures();
			System.out.println("Number of features = " + jmax);
			for (int j = 0; j < jmax; j++){
				System.out.println("Feature " + j + " = " + p.getFeature(j).getName());
				System.out.print(p.getFeature(j).getName() + ", ");
			}
			System.out.println("");
			i++;
		}
		/*for (int i = 0; i < np; i++){
			Product p = pq.getProduct(i);
			System.out.print("PRODUCT "+i+": ");
			int jmax = p.getNumberOfFeatures();
			System.out.println("Number of features = " + jmax);
			for (int j = 0; j < jmax; j++){
				System.out.println("Feature " + j + " = " + p.getFeature(j).getName());
				System.out.print(p.getFeature(j).getName() + ", ");
			}
			System.out.println("");
		}*/
		
		// Check result
		if (pq.getNumberOfProducts()!= products.size())
			Assert.fail("The operation returns a wrong number of products");
		
		it = pq.getAllProducts().iterator();
		while (it.hasNext()){
			Product p = it.next();
			int j=0;
			boolean found=false;
			while (j<np && !found) {
				Product pe = products.get(j);
				if (p.equals(pe))
					found=true;
				else
					j++;
			}
			if (!found)
				Assert.fail("The operation returns an unexpected product");
		}
		
		
		/*for (int i = 0; i < np; i++){
			Product p = pq.getProduct(i);
			int j=0;
			boolean found=false;
			while (j<np && !found) {
				Product pe = products.get(j);
				if (equalsProducts(p,pe))
					found=true;
				else
					j++;
			}
			if (!found)
				Assert.fail("The operation returns an unexpected product");
		}*/
	}
	
	// Check if two products are equals (i.e. they have the same set of features)
	// TODO: Implement the method equals in the Product class.
	private boolean equalsProducts (Product p, Product ep) {
		
		int pf,epf;
		
		pf = p.getNumberOfFeatures();
		epf = ep.getNumberOfFeatures();
		
		if (pf != epf)
			return false;
		
		for (int i=0; i < pf; i++) {
			int j=0;
			boolean found = false;
			while (j<epf && !found) {
				if (p.getFeature(i).getName().equals(ep.getFeature(j).getName()))
					found = true;
				else
					j++;
			}
			if (!found)
				return false;
		}
			
		return true;
	}
	
	// ERROR-FREE INPUTS
	
	@Test
	public void productsMandatory()
	{
		System.out.println("========= MANDATORY ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(B);
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		
		this.products("relationships/mandatory/mandatory.fama",products);
	}
	
	@Test
	public void productsOptional()
	{
		System.out.println("========= OPTIONAL ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		
		// Product 2
		Product p2= new Product();
		p2.addFeature(A);
		p2.addFeature(B);
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		products.add(p2);
		
		this.products("relationships/optional/optional.fama",products);
	}
	
	@Test
	public void productsAlternative()
	{
		System.out.println("========= ALTERNATIVE ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(B);
		
		// Product 2
		Product p2= new Product();
		p2.addFeature(A);
		p2.addFeature(C);
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		products.add(p2);
		
		this.products("relationships/alternative/alternative.fama",products);
	}
	
	@Test
	public void productsOr()
	{
		System.out.println("========= OR ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(B);
		
		// Product 2
		Product p2= new Product();
		p2.addFeature(A);
		p2.addFeature(C);
		
		// Product 3
		Product p3= new Product();
		p3.addFeature(A);
		p3.addFeature(B);
		p3.addFeature(C);
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		products.add(p2);
		products.add(p3);
		
		this.products("relationships/or/or.fama",products);
	}
	
	@Test
	public void productsExcludes()
	{
		System.out.println("========= EXCLUDES ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		
		// Product 2
		Product p2= new Product();
		p2.addFeature(A);
		p2.addFeature(B);
		
		// Product 3
		Product p3= new Product();
		p3.addFeature(A);
		p3.addFeature(C);
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		products.add(p2);
		products.add(p3);
		
		this.products("relationships/excludes/excludes.fama",products);
	}
	
	@Test
	public void productsRequires()
	{
		System.out.println("========= REQUIRES ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		
		// Product 2
		Product p2= new Product();
		p2.addFeature(A);
		p2.addFeature(C);
		
		// Product 3
		Product p3= new Product();
		p3.addFeature(A);
		p3.addFeature(B);
		p3.addFeature(C);
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		products.add(p2);
		products.add(p3);
		
		this.products("relationships/requires/requires.fama",products);
	}
	
	
	// INPUT FMS WITH DEAD FEATURES
	
	
	@Test
	public void productsDFCase1()
	{
		System.out.println("========= DEAD FEATURES (Case 1) ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(B);
		p.addFeature(C);
		p.addFeature(E);
	
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		
		this.products("dead-features/case1/df-case1.fama",products);
	}
	
	@Test
	public void productsDFCase2()
	{
		System.out.println("========= DEAD FEATURES (Case 2) ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(B);
		p.addFeature(C);
		p.addFeature(D);
	
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		
		this.products("dead-features/case2/df-case2.fama",products);
	}
	
	@Test
	public void productsDFCase3()
	{
		System.out.println("========= DEAD FEATURES (Case 3) ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(B);
		p.addFeature(C);
		p.addFeature(E);
	
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		
		this.products("dead-features/case3/df-case3.fama",products);
	}
	
	@Test
	public void productsDFCase4()
	{
		System.out.println("========= DEAD FEATURES (Case 4) ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(B);
	
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		
		this.products("dead-features/case4/df-case4.fama",products);
	}
	
	@Test
	public void productsDFCase5()
	{
		System.out.println("========= DEAD FEATURES (Case 5) ===========");
	
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		
		this.products("dead-features/case5/df-case5.fama",products);
	}
	
	@Test
	public void productsDFCase6()
	{
		System.out.println("========= DEAD FEATURES (Case 6) ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		p.addFeature(C);
	
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		
		this.products("dead-features/case6/df-case6.fama",products);
	}
	
	@Test
	public void productsDFCase7()
	{
		System.out.println("========= DEAD FEATURES (Case 7) ===========");
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		
		this.products("dead-features/case7/df-case7.fama",products);
	}
	
	@Test
	public void productsDFCase8()
	{
		System.out.println("========= DEAD FEATURES (Case 8) ===========");
		
		// Product 1
		Product p= new Product();
		p.addFeature(A);
		
		// Product 2
		Product p2= new Product();
		p2.addFeature(A);
		p2.addFeature(C);
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		products.add(p);
		products.add(p2);
		
		this.products("dead-features/case8/df-case8.fama",products);
	}
	
	@Test
	public void productsDFCase9()
	{
		System.out.println("========= DEAD FEATURES (Case 9) ===========");
		
		// Prepare list of products
		List<Product> products = new ArrayList<Product>();
		
		this.products("dead-features/case9/df-case9.fama",products);
	}
}
