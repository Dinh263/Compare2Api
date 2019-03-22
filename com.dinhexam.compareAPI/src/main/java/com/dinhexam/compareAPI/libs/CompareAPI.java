package com.dinhexam.compareAPI.libs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class CompareAPI implements ICompareApi {

	/**
	 * This functio is used for compare 2 api response in the file.
	 * Step 1: read the 1st file and get all api then add to the list ls01.
	 * Step 2: read the 2nd file and get all api then add to the list ls02.
	 * Step 3: loop each of api in each file and compare the response of the request.
	 * first, get the content of the request and format of response: xml or json.
	 * then we compare the content of response.
	 * 1. if the format of 1st response is "json"
	 *   1a. check the format of 2nd response, if the format is "json"  then we compare 2 json string.
	 *   1b. check the format of 2nd response, if the format is "xml", we have to convert the format of 2nd response to "json" then we compare 2 json string
	 * 2. if the format of 1st response is "xml"
	 *   2a. check the format of 2nd response, if format is "xml", then we compare 2 xml string.
	 *   2b. if format of 2nd response is "json", then we have to convert the format of 1st response to "json" then we compare 2 json string.
	 * @param fileName1 the 1st file contains list of api.
	 * @param fileName2 the 2nd file contains list of api.
	 * 
	 */
	public void compare2Api(File fileName1, File fileName2) {
		ArrayList<String> ls01 = readFile(fileName1);
		ArrayList<String> ls02 = readFile(fileName2);
		for(int i=0;i<ls01.size();i++) {
			try {
			ResponseObject resObj01 = getRequestResponse(ls01.get(i));
			ResponseObject resObj02 = getRequestResponse(ls02.get(i));
			boolean result = false;
			if(resObj01.getFormatType().equals("json")) {
				if(resObj02.getFormatType().equals("json")) {
					result = CompareUtils.compare2Json(resObj01.getContent(), resObj02.getContent());
				}
				else {
					String json02 = convertXMLToJson(resObj02.getContent());
					result = CompareUtils.compare2Json(resObj01.getContent(), json02);
				}
			}
			else {
				if(resObj02.getFormatType().equals("xml")) {
					result = CompareUtils.compare2XML(resObj01.getContent(), resObj02.getContent());
				}
				else {
					String json01 = convertXMLToJson(resObj01.getContent());
					result = CompareUtils.compare2Json(json01, resObj02.getContent());
				}
			}
			
			System.out.println(ls01.get(i) + (result==true?" euqal ":" not equal ")+ls02.get(i));
			}
			catch(ClientProtocolException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * This function is used for read content of the file. 
	 * each of line will be added to a list
	 * @param fileName content list of api.
	 * @return list of api.
	 */
	public ArrayList<String> readFile(File fileName){
		ArrayList<String> ls = new ArrayList<String>();
		BufferedReader buffet;
		try {
			buffet = new BufferedReader(new FileReader(fileName));
			String line = "";
			while((line = buffet.readLine())!=null) {
				ls.add(line);
			}
			buffet.close();
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return ls;
	}
	
	/**
	 * This function is used for get the response content of request and format of response (json, xml)
	 * @param apiUrl the url of the request
	 * @return ResponseObject 
	 */
	private  ResponseObject getRequestResponse(String apiUrl){
		ResponseObject res = new ResponseObject();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(apiUrl);
		HttpResponse response;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			res.setContent(EntityUtils.toString(entity));
			res.setFormatType(entity.getContentType().toString().contains("application/json")?"json":"xml");		
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return res;
		
	}
	
	/**
	 * This function is used for convert XML to Json 
	 * @param xmlStr xml string
	 * @return json string
	 * @throws IOException
	 */
	private String convertXMLToJson(String xmlStr) throws IOException {
		String jsonStr = "";
		XmlMapper mapper = new XmlMapper();
		JsonNode node = mapper.readTree(xmlStr.getBytes());
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonStr = jsonMapper.writeValueAsString(node);
		return jsonStr;
	}
}
