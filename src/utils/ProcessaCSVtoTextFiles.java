package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import utils.PreProcessaCorpus;
import utils.ArquivoUtils;
import utils.CSVReader;

public class ProcessaCSVtoTextFiles {
	
	//private static String corpusTxtPath = "corpusTxt/";
	private static String codificacao = "UTF-8";
	//private static int minNumLinhas = 10;

	public static List<String> processCSVFile(Boolean removeStopWords, Boolean reduzStemme, String nomeCSV,	String stopListNameFile, String corpusTxtPath) {
		File csvFile = new File(nomeCSV);
		return processCSVFile(removeStopWords, reduzStemme, csvFile, stopListNameFile, corpusTxtPath);
	}  
	
	public static List<String> processCSVFile(Boolean removeStopWords, Boolean reduzStemme, File csvFile, String stopListNameFile, String corpusTxtPath) {
		
		List<String> outPut = new ArrayList<String>();
		
		System.out.println("Processando arquivos: "+ csvFile.getName());
		outPut.add("Processando arquivos: "+ csvFile.getName());
		try {
			BufferedReader arq = new BufferedReader (new InputStreamReader (new FileInputStream(csvFile), codificacao)); 
			
			CSVReader reader = new CSVReader(arq,';'); 
			String[] linhas = reader.getLine();
			int count=1;
			int bratLines = 0;
			//primeira linha é o cabecalho: id, content, ...
			while ((linhas= reader.getLine()) !=null ) {
		
				if (linhas.length>=4)
					bratLines += save(removeStopWords, reduzStemme, linhas[0], linhas[1], linhas[2],linhas[3], stopListNameFile, corpusTxtPath);
				else continue;
				count++;				
			}
			System.out.println(bratLines+ " linhas em "+count+" arquivos extraidos.");
			outPut.add(bratLines+ " linhas em "+count+" arquivos extraidos.");
		} catch (IOException e) {
			System.err.printf("[ERROR] Erro ao abrir arquivo: " + csvFile.getAbsolutePath());
			System.exit(-1);
		}
		
		return outPut;
	}

/*

	public void exec(Boolean removeStopWords, Boolean reduzStemme, File orgDirPath) { 
		
		boolean isEmpty = true;
		File[] csvFiles = orgDirPath.listFiles();
		String localPath = "";
		String stopListNameFile = "StopList.txt";
		StemmerType stemmerType = StemmerType.ORENGO;
		Integer numSerie = 1;
		Integer minNumLinhas = 10;
		for (File csvFile:csvFiles) {
			String filename = csvFile.getName();
			if (filename.contains(".csv")) {
				isEmpty = false;
				
				//String tgtDirPathName = tgtDirPath + File.separator + filename.replace(".csv", "");
				processCSVFile(removeStopWords, reduzStemme, csvFile, localPath, stopListNameFile, stemmerType, numSerie, minNumLinhas);
			}
		}
		
		if (isEmpty) {
			try {
				System.out.println("Nao foram encontrados arquivos CSVs em " + orgDirPath.getCanonicalPath());
				System.exit(-1);
			}
			catch(Exception e) {
				System.out.println("Nao foram encontrados arquivos CSVs." );
				System.exit(-1);
			}
		}
			
	}
*/
	
	private static int save(Boolean removeStopWords, Boolean reduzStemme, String id, String reutersFileName, String content, 
			String category, String stopListNameFile, String corpusTxtPath) {
		
	    List<String> linhas = new ArrayList<String>();
	    
	    content = content.toLowerCase();
	    content = ContaPalavras.limpacaracteres(content);
	    content = ContaPalavras.removeNumeros(content);
	    
	    content = PreProcessaCorpus.preProcessaLinha(content, removeStopWords, reduzStemme, stopListNameFile);
	    
        String [] contents = content.split("[.!?] ");
	    	
	  	content = content.replace("? ","? \n");
	    content = content.replace("! ","! \n");
	    content = content.replace(". ",". \n");
	    linhas.add(content);
		    	
	    String tipoPreprocessamento = getTipoPreprocessamento(reduzStemme,removeStopWords);
	    String filename = corpusTxtPath + tipoPreprocessamento + "/" + category + "/" + id + "_" + reutersFileName + ".txt";
	    ArquivoUtils.salvaArquivo(linhas, filename);
	    return contents.length;
	    
	}

private static String getTipoPreprocessamento(Boolean reduzStemme, Boolean removeStopWords) {
	
	String tipoPreProcessamento = "";
	if (reduzStemme){
		if (removeStopWords) tipoPreProcessamento = "StemmeStop";
		else tipoPreProcessamento = "Steme";
	}
	else {
		if (removeStopWords) tipoPreProcessamento = "Stop";
		else tipoPreProcessamento = "Original";
	}
	
	return tipoPreProcessamento;

}
	    
	    
		
}