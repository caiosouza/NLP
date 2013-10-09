package main;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ArquivoUtils;
import utils.ContaPalavras;
import utils.GeraTF_IDF;
import utils.ProcessaCSVtoTextFiles;

public class Exec {
	
	 private static final String stopListNameFile = "enStopList.txt";
	 public static final int frequenciaMin = 0;

	public static String csvToTxt(String nomeArquivo, Boolean removeStopWords, Boolean reduzStemme, String corpusTxtPath) {
		
		ProcessaCSVtoTextFiles.processCSVFile(removeStopWords, reduzStemme, nomeArquivo, stopListNameFile, corpusTxtPath);
		return null;
	}

	public static Map<String, Integer> montaAllTokensFrequencia(String nomePath, int minFrequenciaArquivo, int minFrequenciaGlobal){
		
		List<File> arquivos = ArquivoUtils.getAllFilesRecursive(nomePath);
		Map<String, Integer> dicionarioFrequencia = new HashMap<String, Integer>();
		Map<String, Integer> arquivoFrequencia = new HashMap<String, Integer>();
		
		for (File arquivo : arquivos) {
			
			List<String> linhas = ArquivoUtils.abreArquivo(arquivo);
			arquivoFrequencia = ContaPalavras.contaFrequencia(linhas, minFrequenciaArquivo, null);
			dicionarioFrequencia = ContaPalavras.consolidaTermoFrequencia(arquivoFrequencia, dicionarioFrequencia);
			
		}
		
		return filtraFrequenciaGlobal(dicionarioFrequencia, minFrequenciaGlobal);
		
	}

	private static Map<String, Integer> filtraFrequenciaGlobal(Map<String, Integer> dicionarioFrequencia, int minFrequenciaGlobal) {
		
		Map<String, Integer> mapFiltrado = new HashMap<String,Integer>();
		
		for (Map.Entry<String, Integer> entry : dicionarioFrequencia.entrySet()) {
    		if(entry.getValue() > minFrequenciaGlobal){
    			mapFiltrado.put(entry.getKey(), entry.getValue());
    		}
			
    	}
		return mapFiltrado;		

	}

	public static String[] getDicHead(Map<String, Integer> dicionarioFrequencia) {
	
		String[] dicHead = new String[dicionarioFrequencia.size()];

		int i = 0;
		for (Map.Entry<String, Integer> entry : dicionarioFrequencia.entrySet()) {
    		dicHead[i] = entry.getKey();
    		i++;
		}
			
    	return dicHead;	
    }

	public static String[][] montaTF_IDF(String nomePasta, int minArquivoFrequencia, int minDicionarioFrequencia) {
		
		//PROCESSA TODOS OS TEXTOS DE UMA PASTA
		//MONTA O DICIONARIO - TOKEN FREQUENCIA
		Map<String, Integer> dicionarioFrequencia = Exec.montaAllTokensFrequencia(nomePasta,minArquivoFrequencia, minDicionarioFrequencia);
		
		//SALVA O DICIONARIO
		String dicName = nomePasta.replace("corpusTxt", "dicFrequencia") + ".txt";
		ArquivoUtils.salvaArquivo((ContaPalavras.imprimeFrequencias(dicionarioFrequencia)),dicName);
		
		//pega a lista de tokens
		//String [] dicHead = Exec.getDicHead(dicionarioFrequencia);
		
		// TODO Auto-generated method stub
		List<File> arquivos = ArquivoUtils.getAllFilesRecursive(nomePasta);
		String nomeTF_IDF = nomePasta.replace("corpusTxt", "TfIdf") + ".txt";
		//nomeTF_IDF = nomeTF_IDF.replace("/", "_");
		GeraTF_IDF.exec(arquivos, nomeTF_IDF, minArquivoFrequencia, dicionarioFrequencia);
		
		return null;
	}
	
	
	
}
