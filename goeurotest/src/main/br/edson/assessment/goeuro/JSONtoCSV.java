package br.edson.assessment.goeuro;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.opencsv.CSVWriter;


public class JSONtoCSV {

	private static final String SPACE = "%20";
	private static final String URL_GO_EURO_TEST_JSON = "http://api.goeuro.com/api/v2/position/suggest/en/";
	public static final String ERROR_PARAMETER_NULL = "Please inform 1 City as parameter";
	private static final String KEY_ID = "_id";
	private static final String JSON_ERROR = "JSON Error";
	private static final String URL_ERROR = "URL Error";
	private static final String ZERO_RESULTS = "No results found for ";
	private static final String IO_QUERY_ERROR = "Stream IO Error";
	private static final String FILE_IO_ERROR = "IO File Error";
	private static final String KEY_NAME = "name";
	private static final String KEY_TYPE = "type";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_GEO_POSITION = "geo_position";
	private static final String SEPARATOR = ",";
	public static final String CSV_GENERATED = "Sucessfull generated CVS";


	public  String run(String city) {
		JSONArray arrayReturn;
		List<JSONObject> objectsReturn;

		if(city == null){
			return ERROR_PARAMETER_NULL;
		}

		try {

			arrayReturn = returnCityJSON(city);

		} catch (JSONException e) {
			return JSON_ERROR;
		} catch (URISyntaxException e) {
			return URL_ERROR;
		} catch (MalformedURLException e) {
			return URL_ERROR;
		} catch (IOException e) {
			return IO_QUERY_ERROR;
		}
		if(arrayReturn != null && arrayReturn.length() > 0 ){
			System.out.println("Found "+arrayReturn.length()+" lines for \""+ city+"\"");
			objectsReturn = new ArrayList<JSONObject>();

			for (int i = 0; i < arrayReturn.length(); i++) {
				objectsReturn.add(arrayReturn.getJSONObject(i));
			}
			try {
				writeCSV(city, jsonTOCSV(objectsReturn));
				return CSV_GENERATED;
			} catch (IOException e) {
				return FILE_IO_ERROR;
			}
		} else{
			return ZERO_RESULTS + "\""+ city+"\"";
		}
	}

	private String[] jsonTOCSV(List<JSONObject> objectsReturn) {
		String line;

		String[] result = new String[objectsReturn.size()];
		int cont = 0;
		for (JSONObject o : objectsReturn) {
			//_id, name, type, latitude, longitude
			JSONObject geo_position = ((JSONObject) o.get(KEY_GEO_POSITION));
			line = o.get(KEY_ID).toString() + SEPARATOR
					+ o.get(KEY_NAME).toString() + SEPARATOR
					+ o.get(KEY_TYPE).toString() + SEPARATOR
					+ geo_position.get(KEY_LATITUDE).toString() + SEPARATOR
					+ geo_position.get(KEY_LONGITUDE).toString();
			result[cont] = line;
			cont++;
		}
		return result;
	}

	private void writeCSV(String city, String[] citiesCSV) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter("resultAPI_go_euro_"+city+".csv"), ',');
		//write a header
		writer.writeNext((KEY_ID+SEPARATOR+KEY_NAME+SEPARATOR+KEY_TYPE+SEPARATOR+KEY_LATITUDE+SEPARATOR+KEY_LONGITUDE).split(SEPARATOR));

		String[] line;
		for (int i = 0; i < citiesCSV.length; i++) {
			line = citiesCSV[i].split(",");
			writer.writeNext(line);
		}

		writer.close();

	}

	private JSONArray returnCityJSON(String city) throws URISyntaxException, JSONException, MalformedURLException, IOException {

		//I need to do this because of encoding of a possible city like 'São Paulo'
		URI uri = new URI(new URI(URL_GO_EURO_TEST_JSON + city.replaceAll(" ",SPACE)).toASCIIString());

		JSONTokener tokener = new JSONTokener(uri.toURL().openStream());

		return new JSONArray(tokener);
	}

	public static void main(String[] args) {
		String city = null;

		if(args.length > 0){
			city = args[0];
		}
		//System.out.println("Parameter: "+city);
		System.out.println(new JSONtoCSV().run(city));

	}

}
