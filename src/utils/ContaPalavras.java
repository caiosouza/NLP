package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
//import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class ContaPalavras {
	
	public static final int frequenciaMin = 0;

    public static void main(String[] args) throws Exception {
 
    	ContaPalavras contaPalavras = new ContaPalavras();
    	contaPalavras.exec(args);
    }
    
    public void exec(String[] args){
    	
    	List<String> linhas = new ArrayList<String>();
    	
    	try {
			linhas = leArquivo(args);
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao ler arquivo");
			System.exit(-1);
		}
    	Map<String,Integer> mapPalavras = contaFrequencia(linhas, frequenciaMin, null);
    	imprimeFrequencias(mapPalavras);
    }
    
	public List<String> leArquivo(String[] args) throws Exception{
    	
    	List<String> linhas = new ArrayList<String>();
    	
    	String nomeArq;     
        
        //testa se nome do arq. texto foi passado na chamada do programa
        
        if (args.length != 1) {
        	Scanner s = new Scanner(System.in);
        	System.out.println("Entre com o nome do arquivo.");
        	nomeArq = s.next();
        	
        }
        else {
        	nomeArq = args[0];
        }
        nomeArq = "data/100001000044.txt";
     
        //abre o arquivo
       
        BufferedReader txtBuffer = new BufferedReader(new InputStreamReader(
        	    new FileInputStream(nomeArq), "UTF-8"));
        
        //pega a primeira linha do arquivo
        String curLine; //recebe cada linha lida do arquivo texto
        
        curLine = txtBuffer.readLine();
         
        while (curLine != null) {
        	linhas.add(curLine);
            //pega a proxima linha do arquivo
            curLine = txtBuffer.readLine();
        }
         
        txtBuffer.close();
 
    	return linhas;
    }
    	
	
	public static Map<String, Integer> filtraFrequencia(Map<String, Integer> mapOriginal, int minFrequencia){
		
		Map<String, Integer> mapFiltrado = new HashMap<String,Integer>();
		
		for (Map.Entry<String, Integer> entry : mapOriginal.entrySet()) {
    		if(entry.getValue() > minFrequencia){
    			mapFiltrado.put(entry.getKey(), entry.getValue());
    		}
			
    	}
		return mapFiltrado;
	}
	
	public static Map<String, Integer> contaFrequencia(String linha, int minFrequencia, Set<String> filtroTermos) {
		
		List<String> linhas = new ArrayList<String>();
		linhas.add(linha);
		return contaFrequencia(linhas, minFrequencia, filtroTermos);
			
	}	
		
	public static Map<String, Integer> contaFrequencia(List<String> linhas, int minFrequencia, Set<String> filtroTermos) {

		Map<String,Integer> mapPalavras = new HashMap<String,Integer>(); 
    	for (String curLine : linhas) {
    		String minusculo = curLine.toLowerCase();
            
            //limpa removendo acentos, cedilhas etc...
            minusculo = limpacaracteres(minusculo);
            minusculo = removeNumeros(minusculo);
        	Pattern p = Pattern.compile("([a-záéíóúçãõôê]+)");
            //Pattern p = Pattern.compile("(\\d+)|([a-z]+)");
            Matcher m = p.matcher(minusculo);
            
            while(m.find())
            {
            	String token = m.group(); //pega um token
            	if(token.length()>1){
	            	Integer freq = mapPalavras.get(token); //verifica se esse token ja esta no mapa   
	            	//totalTermos = totalTermos +1; 
	            	if(filtroTermos!= null) {
	            		if (filtroTermos.contains(token)){
	            			if (freq != null) { //se palavra existe, atualiza a frequencia
	      	            	  mapPalavras.put(token, freq+1);
	      	            	}
	      	                else { // se palavra nao existe, insiro com um novo id e freq=1.
	      	                    mapPalavras.put(token,1);
	      	                }
	            		}
	            	} else {
		            	if (freq != null) { //se palavra existe, atualiza a frequencia
		            	  mapPalavras.put(token, freq+1);
		            	}
		                else { // se palavra nao existe, insiro com um novo id e freq=1.
		                    mapPalavras.put(token,1);
		                }
	            	}
            	}
            }
        }

        return filtraFrequencia(mapPalavras,minFrequencia);
    }

	public static Map<String, Integer> contaPresenca(List<String> linhas, int minFrequencia, Set<String> filtroTermos) {

		Map<String,Integer> mapPalavrasConsolidado = new HashMap<String,Integer>(); 
    	
		
		for (String curLine : linhas) {
			
			Map<String,Integer> mapPalavrasAtual = new HashMap<String,Integer>(); 
			String minusculo = curLine.toLowerCase();
            
            //limpa removendo acentos, cedilhas etc...
            minusculo = limpacaracteres(minusculo);
            minusculo = removeNumeros(minusculo);
        	Pattern p = Pattern.compile("([a-záéíóúçãõôê]+)");
            //Pattern p = Pattern.compile("(\\d+)|([a-z]+)");
            Matcher m = p.matcher(minusculo);
            
            while(m.find())
            {
            	String token = m.group(); //pega um token
            	Integer freq = null;
            	if(token.length()>1){
	            	freq = mapPalavrasAtual.get(token); //verifica se esse token ja esta no mapa   
	            	
	            	if(filtroTermos!= null) {
	            		if (filtroTermos.contains(token)){
	            			if (freq != null) { // se palavra nao existe, insiro com um novo id e freq=1.
	            				//mapPalavrasAtual.put(token, freq+1);
	      	                } else{ //se palavra existe, nao faz nada
	      	                	mapPalavrasAtual.put(token,1);
	      	            	}
	      	                
	            		}
	            	} 
	            	else {
		            	if (freq != null) { // se palavra nao existe, insiro com um novo id e freq=1.
		            		//mapPalavrasAtual.put(token, freq+1);
		                } else { //se palavra existe, nao faz nada
		            		mapPalavrasAtual.put(token,1);
		            	}
	            	}
            	}
            }
            
            //consolita map de presencas
            consolidaTermoFrequencia(mapPalavrasAtual, mapPalavrasConsolidado);
        }

        return filtraFrequencia(mapPalavrasConsolidado,minFrequencia);
    }

	
	
    public static String removeNumeros(String passa){
    	
    	passa = passa.replaceAll("[1234567890]", "");
    	return passa;
    }
    
    public static String limpacaracteres(String passa) {
    		
    	passa = passa.replaceAll("[ÂÀÁÄÃ]","A");  
        passa = passa.replaceAll("[âãàáä]","a");  
        passa = passa.replaceAll("[ÊÈÉË]","E");  
        passa = passa.replaceAll("[êèéë]","e");  
        passa = passa.replaceAll("ÎÍÌÏ","I");  
        passa = passa.replaceAll("îíìï","i");  
        passa = passa.replaceAll("[ÔÕÒÓÖ]","O");  
        passa = passa.replaceAll("[ôõòóö]","o");  
        passa = passa.replaceAll("[ÛÙÚÜ]","U");  
        passa = passa.replaceAll("[ûúùü]","u");  
        passa = passa.replaceAll("Ç","C");  
        passa = passa.replaceAll("ç","c");   
        passa = passa.replaceAll("[ýÿ]","y");  
        passa = passa.replaceAll("Ý","Y");  
        passa = passa.replaceAll("ñ","n");  
        passa = passa.replaceAll("Ñ","N");  
        passa = passa.replaceAll("['<>|/]","");  
        return passa;  
    	
    }
    
	public static List<String> imprimeFrequencias(Map<String, Integer> mapPalavras) {
		
		List<String> output = new ArrayList<String>();
		
    	for (Map.Entry<String, Integer> entry : mapPalavras.entrySet()) {
    		System.out.println(entry.getKey() + ";" + entry.getValue());
    		output.add(entry.getKey() + ";" + entry.getValue());
    	}
    	return output;
    }

	public static Map<String, Integer> consolidaTermoFrequencia(Map<String, Integer> termoFrequenciaDocumento,
			Map<String, Integer> termoFrequenciaCategoria) {

		if (termoFrequenciaCategoria.isEmpty()){
			
			//para toda chave no mapa do documento
			for (Map.Entry<String, Integer> entry : termoFrequenciaDocumento.entrySet()) {
				String termo = entry.getKey();
				int freqDoc = entry.getValue();
				termoFrequenciaCategoria.put(termo, freqDoc);
			}
			//termoFrequenciaCategoria = termoFrequenciaDocumento;
		}
		else 
		{
			//para toda chave no mapa do documento
			for (Map.Entry<String, Integer> entry : termoFrequenciaDocumento.entrySet()) {
				String termo = entry.getKey();
				int freqDoc = entry.getValue();
				//verifica se ela estao no mapa da categoria
				if (termoFrequenciaCategoria.containsKey(termo))
				{
					//se estiver atualiza o valor com a soma dos valores
					int freqCategoria = termoFrequenciaCategoria.get(termo);
					termoFrequenciaCategoria.put(termo, freqCategoria+ freqDoc);
				} else {
					//se nao estiver adiciona com o valor do documento
					termoFrequenciaCategoria.put(termo, freqDoc);
				}
			}
			
		}
		return termoFrequenciaCategoria;

	}
    
}
