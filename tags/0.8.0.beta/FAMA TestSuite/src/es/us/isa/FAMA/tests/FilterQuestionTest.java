package es.us.isa.FAMA.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;


import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.ProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.models.FAMAfeatureModel.Feature;
import es.us.isa.FAMA.models.featureModel.*;
import es.us.isa.FAMA.models.featureModel.Product;

public class FilterQuestionTest {

	Question q;
	GenericFeatureModel fm;
	QuestionTrader qt;
	
	// Output products
	private Product p1;
	private Product p2;
	private Product p3;
	private Product p4;
	private Product p5;
	private Product p6;
	private Product p7;
	private Product p8;
	
	@Before
	public void setUp(){
		
		// Features
		Feature a = new Feature("A");
		Feature b = new Feature("B");
		Feature c = new Feature("C");
		Feature d = new Feature("D");
		Feature e = new Feature("E");
		Feature f = new Feature("F");
		Feature g = new Feature("G");
		
		
		// Product 1 ({A,C,D})
		p1 = new Product();
		p1.addFeature(a);
		p1.addFeature(c);
		p1.addFeature(d);
		
		// Product 2 ({A,C,D,E,F})
		p2 = new Product();
		p2.addFeature(a);
		p2.addFeature(c);
		p2.addFeature(d);
		p2.addFeature(e);
		p2.addFeature(f);
		
		// Product 3 ({A,C,E,F})
		p3 = new Product();
		p3.addFeature(a);
		p3.addFeature(c);
		p3.addFeature(e);
		p3.addFeature(f);
		
		// Product 4 ({A,B,C,D,E,G})
		p4 = new Product();
		p4.addFeature(a);
		p4.addFeature(b);
		p4.addFeature(c);
		p4.addFeature(d);
		p4.addFeature(e);
		p4.addFeature(g);
		
		// Product 5 ({A,C,D,E,G})
		p5 = new Product();
		p5.addFeature(a);
		p5.addFeature(c);
		p5.addFeature(d);
		p5.addFeature(e);
		p5.addFeature(g);
		
		// Product 6 ({A,B,C,E,G})
		p6 = new Product();
		p6.addFeature(a);
		p6.addFeature(b);
		p6.addFeature(c);
		p6.addFeature(e);
		p6.addFeature(g);
		
		// Product 7  ({A,C,E,G})
		p7 = new Product();
		p7.addFeature(a);
		p7.addFeature(c);
		p7.addFeature(e);
		p7.addFeature(g);
		
		// Product 8 ({A,B})
		p8 = new Product();
		p8.addFeature(a);
		p8.addFeature(b);
		
		
	}
	
	private void filter(String inputName, List<String> selected, List<String> unselected, List<Product> expectedProducts)
	{
		// Read input
		qt = new QuestionTrader();
		fm = (GenericFeatureModel) qt.openFile("test-inputs/" + inputName);
		qt.setVariabilityModel(fm);
		qt.setCriteriaSelector("selected"); // We select a specific solver
		
		// Perform question
		SetQuestion sq = (SetQuestion) qt.createQuestion("Set");
		FilterQuestion fq = (FilterQuestion) qt.createQuestion("Filter");
		ProductsQuestion pq = (ProductsQuestion) qt.createQuestion("Products");
		
		// Select features
		Iterator<String> it;
		if (selected!=null) {
			it = selected.iterator();
			while (it.hasNext())
				fq.addFeature(new Feature((String)it.next()), 1);
		}
		
		// Unselect features
		if (unselected!=null) {
			it = unselected.iterator();
			while (it.hasNext())
				fq.removeFeature(new Feature((String)it.next()));
		}
		
		sq.addQuestion(fq);
		sq.addQuestion(pq);
		qt.ask(sq);
		
		// Show results
		int np = (int) pq.getNumberOfProducts();
/*		for (int i = 0; i < np; i++){
			Product p = pq.getProduct(i);
			System.out.print("PRODUCT "+i+": ");
			int jmax = p.getNumberOfFeatures();
			for (int j = 0; j < jmax; j++){
				System.out.print(p.getFeature(j).getName() + ", ");
			}
			System.out.println("");
		}*/
		
		// Check results
		if (pq.getNumberOfProducts()!= expectedProducts.size())
			Assert.fail("The operation returns a wrong number of products");
		
		Iterator<Product> itP = pq.getAllProducts().iterator();
		
		while (itP.hasNext()){
			Product p = itP.next();
			int j=0;
			boolean found=false;
			while (j<np && !found) {
				Product pe = expectedProducts.get(j);
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
				Product pe = expectedProducts.get(j);
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
	public void filterTC1()
	{
		System.out.println("========= TEST CASE 1 ===========");
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("A");
		selected.add("C");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		products.add(p1);
		products.add(p2);
		products.add(p3);
		products.add(p4);
		products.add(p5);
		products.add(p6);
		products.add(p7);
		
		this.filter("filter/case1/filter-case1.fama",selected,null,products);
	}
	
	@Test
	public void filterTC2()
	{
		System.out.println("========= TEST CASE 2 ===========");
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("D");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		products.add(p1);
		products.add(p2);
		products.add(p4);
		products.add(p5);
		
		this.filter("filter/case1/filter-case1.fama",selected,null,products);
	}
	
	@Test
	public void filterTC3()
	{
		System.out.println("========= TEST CASE 3 ===========");
		
		// Unselected features
		List<String> unselected = new ArrayList<String>();
		unselected.add("B");
		unselected.add("F");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		products.add(p1);
		products.add(p5);
		products.add(p7);
		
		this.filter("filter/case1/filter-case1.fama",null,unselected,products);
	}
	
	@Test
	public void filterTC4()
	{
		System.out.println("========= TEST CASE 4 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("A");
		selected.add("E");
		selected.add("F");
		
		// Unselected features
		List<String> unselected = new ArrayList<String>();
		unselected.add("B");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		products.add(p2);
		products.add(p3);
		
		this.filter("filter/case1/filter-case1.fama",selected,unselected,products);
	}
	
	@Test
	public void filterTC5()
	{
		System.out.println("========= TEST CASE 5 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("B");
		selected.add("C");
		selected.add("F");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		
		this.filter("filter/case1/filter-case1.fama",selected,null,products);
	}
	
	@Test
	public void filterTC6()
	{
		System.out.println("========= TEST CASE 6 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("E");
		selected.add("F");
		selected.add("G");
		
		// Unselected features
		List<String> unselected = new ArrayList<String>();
		unselected.add("B");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		
		this.filter("filter/case1/filter-case1.fama",selected,unselected,products);
	}
	
	// INPUT FMS WITH DEAD FEATURES
	
	@Test
	public void filterTC7()
	{
		System.out.println("========= TEST CASE 7 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("A");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		products.add(p6);
		products.add(p8);
		
		this.filter("filter/case2/filter-case2.fama",selected,null,products);
	}
	
	@Test
	public void filterTC8()
	{
		System.out.println("========= TEST CASE 8 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("B");
		
		// Unselected features
		List<String> unselected = new ArrayList<String>();
		unselected.add("C");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		products.add(p8);
		
		this.filter("filter/case2/filter-case2.fama",selected,unselected,products);
	}
	
	@Test
	public void filterTC9()
	{
		System.out.println("========= TEST CASE 9 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("A");
		selected.add("C");
		selected.add("E");
		selected.add("G");
		
		// Unselected features
		List<String> unselected = new ArrayList<String>();
		unselected.add("F");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		products.add(p6);
		
		this.filter("filter/case2/filter-case2.fama",selected,unselected,products);
	}
	
	@Test
	public void filterTC10()
	{
		System.out.println("========= TEST CASE 10 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("D");
		selected.add("F");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		
		this.filter("filter/case2/filter-case2.fama",selected,null,products);
	}
	
	// ERRONEOUS INPUTS
	
	@Test
	public void filterTC11()
	{
		System.out.println("========= TEST CASE 11 ===========");
		
		
		// Selected features
		List<String> selected = new ArrayList<String>();
		selected.add("H");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		
		this.filter("filter/case2/filter-case2.fama",selected,null,products);
	}
	
	@Test
	public void filterTC12()
	{
		System.out.println("========= TEST CASE 12 ===========");
		
		
		// Selected features
		List<String> unselected = new ArrayList<String>();
		unselected.add("H");
		
		// Output products
		List<Product> products = new ArrayList<Product>();
		
		this.filter("filter/case2/filter-case2.fama",null,unselected,products);
	}
	
}
