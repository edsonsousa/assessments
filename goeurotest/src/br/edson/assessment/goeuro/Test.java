package br.edson.assessment.goeuro;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Test {

	private final String URL_GO_EURO_TEST_JSON = "http://api.goeuro.com/api/v2/position/suggest/en/";
	private final List<JSONObject> listCities;

	public static void main(String[] args) {

		if(args.length == 0){
			System.out.println("Please inform a least 1 City as parameter.");
			return;
		}

		Test test = new Test();

		for (int i = 0; i < args.length; i++) {
			test.listCities.add(test.returnCityJSON(args[i]));

		}

		test.writeCSV(test.listCities);

	}

	public Test() {
		listCities = new ArrayList<JSONObject>();
	}

	private void writeCSV(List<JSONObject> citiesJSON) {
		if (citiesJSON != null){
			System.out.println(citiesJSON.toString());
		}

	}

	private JSONObject returnCityJSON(String city) {
		URI uri;
		try {

			//			DefaultHttpClient httpclient = new DefaultHttpClient();
			//
			//			HttpHost proxy = new HttpHost("10.0.220.11", 8080);
			//			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			String ipAddress = "104.155.44.104";
			InetAddress inet = InetAddress.getByName(ipAddress);

			System.out.println("Sending Ping Request to " + ipAddress);
			System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
			//	        String genreJson = IOUtils.toString(new URL(URL_GO_EURO_TEST_JSON));
			//	        JSONObject json = new JSONObject(genreJson);
			InputStream is = new URL(URL_GO_EURO_TEST_JSON).openStream();
			uri = new URI(URL_GO_EURO_TEST_JSON + city);

			JSONTokener tokener = new JSONTokener(uri.toURL().openStream());

			return new JSONObject(tokener);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
