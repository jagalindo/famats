package main;

import es.us.isa.FAMA.Reasoner.QuestionTrader;
import es.us.isa.FAMA.Reasoner.questions.CommonalityQuestion;
import es.us.isa.FAMA.Reasoner.questions.FilterQuestion;
import es.us.isa.FAMA.Reasoner.questions.NumberOfProductsQuestion;
import es.us.isa.FAMA.Reasoner.questions.SetQuestion;
import es.us.isa.FAMA.Reasoner.questions.ValidQuestion;
import es.us.isa.FAMA.models.FAMAfeatureModel.Feature;
import es.us.isa.FAMA.models.featureModel.GenericFeatureModel;

public class FAMAFirstTime {

	public static void main(String[] args) {
		
		//The main class is instantiated 
		QuestionTrader qt = new QuestionTrader();
		
		//A feature model from an XML file is loaded 
		GenericFeatureModel fm = null;
		fm = (GenericFeatureModel) qt.openFile("fm-samples/test.fama");
		qt.setVariabilityModel(fm);
		
		////////// VALID QUESTION + NUMBER PRODUCTS QUESTION ///////////
		ValidQuestion vq = (ValidQuestion) qt.createQuestion("Valid");
		qt.ask(vq);
		if (vq.isValid()) {
			System.out.println("Your feature model is valid");
			NumberOfProductsQuestion npq = (NumberOfProductsQuestion) qt
					.createQuestion("#Products");
			qt.ask(npq);
			System.out.println("The number of products is: "
					+ npq.getNumberOfProducts());
		} else {
			System.out.println("Your feature model is not valid");
		}

		////////// COMMONALITY QUESTION ///////////

		CommonalityQuestion cq = (CommonalityQuestion) qt
				.createQuestion("Commonality");
		cq.setFeature(new Feature("child01"));
		qt.ask(cq);
		System.out.println("Commonality of the selected: "
				+ cq.getCommonality());

		////////// FILTER QUESTION ///////////

		FilterQuestion fq = (FilterQuestion) qt.createQuestion("Filter");
		fq.addValue(new Feature("child02"), 1);

		NumberOfProductsQuestion npq = (NumberOfProductsQuestion) qt
				.createQuestion("#Products");

		// You need to create a set question to see the results of applying the filter defined before
		SetQuestion sq = (SetQuestion) qt.createQuestion("Set");
		sq.addQuestion(fq);
		sq.addQuestion(npq);

		qt.ask(sq);
		System.out.println("Number of products after applying the filter: "
				+ npq.getNumberOfProducts());

	}

}
