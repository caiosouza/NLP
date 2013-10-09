package main;

import java.util.Map;

import utils.ArquivoUtils;
import utils.ContaPalavras;
import utils.Utils;

public class NLP {

	public static final int arquivoFrequenciaMin = 0;
	public static final int dicionarioFrequenciaMin = 20;
	public static final String experimentoFolder = "experimentos/Top25/";
	private static String corpusTxtPath = experimentoFolder + "corpusTxt/TrainTop100/";
	
	
	public static void main(String[] args) {
		System.out.println("1");
		
		NLP nlp = new NLP();
		System.out.println("1");
		
		//nlp.exec(args);
		nlp.teste();
		System.out.println("1");
		
	}
	
	public void teste(){
		
		String[] args1 = {"0", "1", "1", experimentoFolder + "reutersCSV/TrainTop100.csv", corpusTxtPath};
		exec(args1);
		//String[] args2 = {"0", "0", "0", "reuters_test.csv"};
		//exec(args2);
		String[] args3 = {"1", corpusTxtPath+ "Stop"};
		//exec(args3);
	}

	private void exec(String[] args){

		int opCode = validaEntrada(args);
		
		//pre-processa csv para txt files
		if (opCode == 0) {
		  Boolean removeStop = Utils.getBoolean(args[1]);
		  Boolean execStemmer = Utils.getBoolean(args[2]);
		  String nomeArquivo = args[3];
		  String corpusTxtPath = args[4];

		  Exec.csvToTxt(nomeArquivo, removeStop, execStemmer, corpusTxtPath);
		  
		}
		
		if (opCode == 1){
			
			String nomePasta = args[1];
		
			
			//MONTA MATRIZ TERMO/ DOCUMENTO, COLOCANDO CADA DOCUMENTO NO ESPA‚O
			String [][] tf_idf = Exec.montaTF_IDF(nomePasta, arquivoFrequenciaMin,dicionarioFrequenciaMin );
			
			//INICIA OS K CLUSTERS
			//RODA O K-MEANS
			
			
		}
		
	}
	
	private int validaEntrada(String[] args) {
		
		// 0: preprocessa csv para gerar arquivos do corpus como txt
		int opCode = -1;
		if (args.length == 0){
			imprimeUsage();
		}
		else {
			opCode = Integer.parseInt(args[0]);
			if (((opCode == 0) && (args.length != 5))|| ((opCode == 1) && (args.length != 2))||
				((opCode == 2) && (args.length != 7))|| ((opCode == 3) && (args.length <  5))||
				((opCode == 4) && (args.length <  3))|| ((opCode == 5) && (args.length <  4))){
				imprimeUsage();
			}
		}
		
		return opCode;
	}
	
	private void imprimeUsage() {
		
		System.out.println("Opcoes de Uso:");
		// 0: preprocessa csv para gerar arquivos do corpus como txt
		
		System.exit(-1);
	}
		
		
		
	
}
