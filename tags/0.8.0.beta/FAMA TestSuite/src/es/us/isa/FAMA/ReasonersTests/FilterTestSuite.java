package es.us.isa.FAMA.ReasonersTests;

import java.util.Iterator;

import org.junit.Test;

import es.us.isa.FAMA.Reasoner.Question;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.ProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.featureModel.Product;

/**
 * This jUnit test case tests the Filter question for anyone reasoner
 * @author Jesús
 *
 */
@SuppressWarnings("unused")
public class FilterTestSuite extends TestSuite{

	@Test
	public void testFilterQuestion(){
		SetQuestion q1 = (SetQuestion) qt.createQuestion("Set");
		FilterQuestion q2 = (FilterQuestion) qt.createQuestion("Filter");
		ProductsQuestion q3 = (ProductsQuestion) qt.createQuestion("Products");
		
		//ADD AND REMOVE FEATURES
		//q2.addFeature(f, cardinality)
		//q2.removeFeature(f);
		
		q1.addQuestion(q2);
		q1.addQuestion(q3);
		qt.ask(q1);
		
		System.out.println("\n---- FILTER QUESTION TEST (WITH PRODUCTS QUESTION) ----");
		long imax = q3.getNumberOfProducts();
		Iterator<Product> it = q3.getAllProducts().iterator();
		int i = 0;
		while (it.hasNext()){
			i++;
			Product p = it.next();
			System.out.print("PRODUCT "+i+": ");
			int jmax = p.getNumberOfFeatures();
			for (int j = 0; j < jmax; j++){
				System.out.print(p.getFeature(j).getName() + ", ");
			}
			System.out.println("");
		}
		
		System.out.println("\n---- FILTER QUESTION TEST (WITH VALID QUESTION) ----");
		q1 = (SetQuestion) qt.createQuestion("Set");
		q2 = (FilterQuestion) qt.createQuestion("Filter");
		ValidQuestion q4 = (ValidQuestion) qt.createQuestion("Valid");
		
		q2.addFeature(this.fm.searchFeatureByName("FIRE"), 1);
		q2.addFeature(this.fm.searchFeatureByName("HIS"), 0);
		
		q1.addQuestion(q2);
		q1.addQuestion(q4);
		qt.ask(q1);
		System.out.println("Is the model valid? "+q4.isValid());
	}
	
}
