package br.edson.assessment.goeuro;

import org.junit.Assert;
import org.junit.Test;

public class JSONtoCSVTest {

	@Test
	public void withoutParameterTest() {

		Assert.assertEquals(JSONtoCSV.ERROR_PARAMETER_NULL, new JSONtoCSV().run(null));
	}

	@Test
	public void singleParameterTest() {

		Assert.assertEquals(JSONtoCSV.CSV_GENERATED, new JSONtoCSV().run("Fortaleza"));
	}

}
