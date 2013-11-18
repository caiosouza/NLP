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
	
	public static String[] limpaTokens(String[] tokens) {
		
		List<String> tokensArray = new ArrayList<String>();
		for (String token : tokens) {
			if (!token.trim().contentEquals("")){
				tokensArray.add(token);
			}
		}
		
		String [] tokensFiltrados = new String[tokensArray.size()];
		for (int i = 0; i < tokensArray.size(); i++) {
			tokensFiltrados[i] = tokensArray.get(i);
		}
		
		return tokensFiltrados;
	}

}
