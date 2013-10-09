package utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static Boolean getBoolean(String string) {
		
		return string.contentEquals("1");
	}
	
	public static List<String> splitToArray(String file, String separador){
		String[] parts = file.split(separador);			
		List<String> arrayParts = new ArrayList<String>();
		for(String part : parts){
			arrayParts.add(part);
		}		
		return arrayParts;
	}
}
