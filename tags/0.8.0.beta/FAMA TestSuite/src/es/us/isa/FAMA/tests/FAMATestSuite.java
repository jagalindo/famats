package es.us.isa.FAMA.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={NumberOfProductsQuestionTest.class, ValidQuestionTest.class, ProductsQuestionTest.class, FilterQuestionTest.class})
public class FAMATestSuite {


}
