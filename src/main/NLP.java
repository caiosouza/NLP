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
		
		NLP nlp = new NLP();
		
		//nlp.exec(args);
		nlp.teste();
		
	}
	
	public void teste(){
		
		//String[] args1 = {"0", "1", "1", experimentoFolder + "reutersCSV/TrainTop100.csv", corpusTxtPath};
		//exec(args1);
		//String[] args2 = {"0", "0", "0", "reuters_test.csv"};
		//exec(args2);
		//String[] args3 = {"1", corpusTxtPath+ "Stop"};
		//exec(args3);
		String[] args2 = {"2"};
		//exec(args2);
		String[] args3 = {"3"};
		exec(args3);
		String[] args4 = {"4"};
		exec(args4);
		
	}

	private void exec(String[] args){

		int opCode = validaEntrada(args);
		
		getPorperties();
		
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
		
		if(opCode == 2){
			
			int topWordsNum = 2000000000;
			String experimentoFolder = "experimentos/Balanceado/ClusterKmeans/EntropiaFull/";
			
			/*saida*/
			String termosEntropiaTXT = experimentoFolder + "termoEntropia/"+"termosEntropia.txt";
			
			/*Entrada*/
			//String clusterOrderFile = experimentoFolder + "ArquivosEntrada/clusterDominio.csv";
			String clusterOrderFile = experimentoFolder + "ArquivosEntrada/cluster_ReutersTrainTop100SS.csv";
			String allTextBaseTxtFile = experimentoFolder  + "ArquivosEntrada/allTextBaseTxtFile/ReutersTrainTop100SS.txt";
			
			InformationEntropy infoEntropy = new InformationEntropy(topWordsNum, experimentoFolder, termosEntropiaTXT, clusterOrderFile, allTextBaseTxtFile);
			
			infoEntropy.exec();
		}
		
		if(opCode == 3){
			
			Integer [] topNs = {1,2,3,4,5,10,15,20,25,30,35,40,45,50,60,70,80,90,100,200,300,400,500,600,700,800,900,1000};
			int topN = 0;
			
			String experimentoFolder = "experimentos/Desbalanceado/ClusterGabarito/EntropiaFull/";
			boolean balanceado = false;
			
			/*Entrada*/
			String baseDocumentos = experimentoFolder+"ArquivosEntrada/allTextBaseTxtFile/ReutersTrainTop100SS.txt";
			String termosOrdenadosTXT = experimentoFolder + "termoEntropia/termosOrdenados.txt";
			String termosCategoriaFull = experimentoFolder + "ArquivosEntrada/termoCategoriaFull.txt";
			
			String[] categorias = {"acq","bop","cocoa","coffee","corn","cpi","crude","dlr","earn","gnp","gold","grain","interest",
					"livestock","money-fx","money-supply","nat-gas","oilseed","reserves","ship","soybean","sugar","trade","veg-oil","wheat"};
			
			for (int i = 0; i < topNs.length; i++) {
				
				topN = topNs[i];
				
				/*Saida*/
				String termoCategoriaTXT = experimentoFolder+"termoCategoria/termoCategoria"+topN+".txt";
				String categoriasEncontradas = experimentoFolder+"classificationFiles/categoriasEncontradas"+topN+".txt";
				
					
				ClusterEntropyClassifier classificador = new ClusterEntropyClassifier(topN, balanceado, termoCategoriaTXT, 
						categoriasEncontradas, baseDocumentos, termosOrdenadosTXT, termosCategoriaFull, categorias);
						
				classificador.montaDicionario();
				classificador.rodaHeuristica();
			}
		}
		
		if (opCode == 4){
			Integer [] topNs = {1,2,3,4,5,10,15,20,25,30,35,40,45,50,60,70,80,90,100,200,300,400,500,600,700,800,900,1000};
			int topN = 0;
			
			String experimentoFolder = "experimentos/Desbalanceado/ClusterGabarito/EntropiaFull/";
			String categoriasOriginaisTXT = experimentoFolder+ "ArquivosEntrada/ordemCategoriasTrainTop100SS.txt";
			String[] categorias = {"acq","bop","cocoa","coffee","corn","cpi","crude","dlr","earn","gnp","gold","grain","interest",
					"livestock","money-fx","money-supply","nat-gas","oilseed","reserves","ship","soybean","sugar","trade","veg-oil","wheat"};
			
			for (int i = 0; i < topNs.length; i++) {
				
				topN = topNs[i];
				
				String categoriasEncontradasTXT = experimentoFolder+ "classificationFiles/categoriasEncontradas"+topN+".txt";
				String resultados = experimentoFolder+"resultados/"+topN+"resultado.txt";
				
				CalculaAcerto calc = new CalculaAcerto(categoriasEncontradasTXT, categoriasOriginaisTXT, resultados, categorias);
				calc.exec();
				
			}	
			
		}
		
		
	}
	
	private void getPorperties() {
		// TODO Auto-generated method stub
		
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
				((opCode == 2) && (args.length != 1))|| ((opCode == 3) && (args.length !=  1))||
				((opCode == 4) && (args.length != 1))|| ((opCode == 5) && (args.length <  4))){
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
