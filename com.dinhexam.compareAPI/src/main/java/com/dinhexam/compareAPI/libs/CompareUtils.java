package com.dinhexam.compareAPI.libs;

import java.io.IOException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CompareUtils {

	/**
	 * This function is used for compare 2 xml 
	 * @param xml01
	 * @param xml02
	 * @return true if equal otherwise false will return
	 */
	public static boolean compare2XML(String xml01, String xml02) {
			XMLUnit.setIgnoreWhitespace(true);
			boolean equal = false;
			try {
				Diff diff = new Diff(xml01, xml02);
				equal = diff.similar();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			catch (SAXException e) {
				e.printStackTrace();
			}
			return equal;
		}
		
	/**
	 * This function is used for compare 2 json
	 * @param json01
	 * @param json02
	 * @return true if equal otherwise false will return
	 */
		public static boolean compare2Json(String json01, String json02) {
			ObjectMapper mapper = new ObjectMapper();
			boolean equal =false;
			try {
				JsonNode js01 = mapper.readTree(json01);
				JsonNode js02 = mapper.readTree(json02);
				if(js01.equals(js02)) {
					equal = true;
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return equal;
	 
		}
	
}
