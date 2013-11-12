package main;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

public class NLP {

	public static final int arquivoFrequenciaMin = 0;
	public static final int dicionarioFrequenciaMin = 20;
	
	private int topWordsNum;
	private String experimentoFolder;
	private String nomeExperimento;
	private String termosEntropiaTXT;
	private String clusterOrderFileName;
	private String allTextBaseTxtFileName;
	private String clusterCategoriaFileName;
	private String categoriasFileName;
	
	private String categoriasCorretasFileName;
	private String resultados;
	private boolean balanceado;
	private String termosOrdenadosTXT;
	private String termosCategoriaFull;
	private int tipoOrdenacao;
	private int numHeuristica;
	
	private Integer[] topNs = {1,2,3,4,5,10,15,20,25,30,35,40,45,50,60,70,80,90,100,200};//,300,400,500,600,700,800,900,1000};
	private String[] categorias = {"acq","bop","cocoa","coffee","corn","cpi","crude","dlr","earn","gnp","gold","grain","interest",
			"livestock","money-fx","money-supply","nat-gas","oilseed","reserves","ship","soybean","sugar","trade","veg-oil","wheat"};
	private int tipoMapeamentoTermo;
	private String termosCategoria;
	private String pathBase;
	
	
	
	public static void main(String[] args) {
		
		NLP nlp = new NLP();
		
		//nlp.exec(args);
		nlp.teste();
		
	}

	
	public void teste(){
		
		List<String[]> experimentos = new ArrayList<String[]>();
		
		//String[] args1 = {"0", "1", "1", experimentoFolder + "reutersCSV/TrainTop100.csv", corpusTxtPath};
		//exec(args1);
		//String[] args2 = {"0", "0", "0", "reuters_test.csv"};
		//exec(args2);
		//String[] args3 = {"1", corpusTxtPath+ "Stop"};
		//exec(args3);
		/*String[] args2 = {"2"};
		exec(args2);
		/*/
		//String[] args3 = {"3"};
		//exec(args3);
		//String[] args4 = {"4"};
		//exec(args4);
		
		//String[] args5 ={"5","1","0","0","0","0","0","0"};
		
		/*
		experimentos.add(new String[]{"6","1"});
		experimentos.add(new String[]{"6","2"});
		experimentos.add(new String[]{"6","3"});
		*/
		
		/*
		// base reuters 25
		experimentos.add(new String[]{"5","0","0","0","0","0","0","0"});
		experimentos.add(new String[]{"5","0","0","1","0","0","0","0"});
		experimentos.add(new String[]{"5","0","1","0","0","0","0","0"});
		experimentos.add(new String[]{"5","0","1","1","0","0","0","0"});
		*/
		/*experimentos.add(new String[]{"5","1","0","0","0","0","0","0"});
		experimentos.add(new String[]{"5","1","0","1","0","0","0","0"});
		experimentos.add(new String[]{"5","1","1","0","0","0","0","0"});
		experimentos.add(new String[]{"5","1","1","1","0","0","0","0"});
		*/
		
		
		/* CODIGO ANTIGO:
		// base reuters 2
		//clusterGabarito
		experimentos.add(new String[]{"5","0","0","0","1","0","0","0"});
		experimentos.add(new String[]{"5","0","0","1","1","0","0","0"});
		
		//kmeans k=2
		experimentos.add(new String[]{"5","0","1","0","1","0","0","0"});
		experimentos.add(new String[]{"5","0","1","1","1","0","0","0"});
		
		//kmeans k=5
		experimentos.add(new String[]{"5","0","2","0","1","0","0","0"});
		experimentos.add(new String[]{"5","0","2","1","1","0","0","0"});
		
		//kmeans k=10
		experimentos.add(new String[]{"5","0","3","0","1","0","0","0"});
		experimentos.add(new String[]{"5","0","3","1","1","0","0","0"});
				
		//kmeans k=20
		experimentos.add(new String[]{"5","0","4","0","1","0","0","0"});
		experimentos.add(new String[]{"5","0","4","1","1","0","0","0"});
		*/
		
		/*
		// BASE news2
		//clusterGabarito
		experimentos.add(new String[]{"5","0","0","0","2","0","0","0"});
		experimentos.add(new String[]{"5","0","0","1","2","0","0","0"});
		
		//kmeans k=2
		experimentos.add(new String[]{"5","0","1","0","2","0","0","0"});
		experimentos.add(new String[]{"5","0","1","1","2","0","0","0"});
		
		//kmeans k=5
		experimentos.add(new String[]{"5","0","2","0","2","0","0","0"});
		experimentos.add(new String[]{"5","0","2","1","2","0","0","0"});
		
		//kmeans k=10
		experimentos.add(new String[]{"5","0","3","0","2","0","0","0"});
		experimentos.add(new String[]{"5","0","3","1","2","0","0","0"});
				
		//kmeans k=20
		experimentos.add(new String[]{"5","0","4","0","2","0","0","0"});
		experimentos.add(new String[]{"5","0","4","1","2","0","0","0"});
		*/
		
		/*
		// BASE new3
		//clusterGabarito
		experimentos.add(new String[]{"5","0","0","0","3","0","0","0"});
		experimentos.add(new String[]{"5","0","0","1","3","0","0","0"});
		
		//kmeans k=3
		experimentos.add(new String[]{"5","0","1","0","3","0","0","0"});
		experimentos.add(new String[]{"5","0","1","1","3","0","0","0"});
		
		//kmeans k=5
		experimentos.add(new String[]{"5","0","2","0","3","0","0","0"});
		experimentos.add(new String[]{"5","0","2","1","3","0","0","0"});
		
		//kmeans k=10
		experimentos.add(new String[]{"5","0","3","0","3","0","0","0"});
		experimentos.add(new String[]{"5","0","3","1","3","0","0","0"});
				
		//kmeans k=20
		experimentos.add(new String[]{"5","0","4","0","3","0","0","0"});
		experimentos.add(new String[]{"5","0","4","1","3","0","0","0"});
		*/
		
		
		//Substituido por:
		String opCode = "5";
		String [] balanceamentos = {"0"};//0-balanceado; 1-desbalanceado. na pratica com poucas categorias nao afeta resultado
		String [] clustersIds = {"0","1","2","3","4"};
		String [] entropias = {"0","1"}; //tipoOrdenacao: 0-Entropia 0, 1-Todos os termos
		String [] basesIDs = {"1","2","3"};
		String [] clusterCategoriaIds = {"0"};
		String [] tiposMapeamentoTermo = {"1"};//0-Frequencia Absoluta; 1-Frequencia Relativa
		String [] numsHeuristica = {"0"};
		
		for (String balanceamento : balanceamentos) {
			for (String clusterId : clustersIds) {
				for (String entropia : entropias) {
					for (String baseID : basesIDs) {
						for (String clusterCategoriaId : clusterCategoriaIds) {
							for (String tipoMapeamentoTermo : tiposMapeamentoTermo) {
								for (String numHeuristica : numsHeuristica) {
									experimentos.add(new String[]{opCode, balanceamento, clusterId, entropia, baseID, clusterCategoriaId,
											tipoMapeamentoTermo, numHeuristica});
								}
							}
						}
					}			
				}
			}
		} 
		
		
		String parametros="";		
		for (String[] experimento : experimentos) {
			parametros = "";
			for(int i=0; i< experimento.length ; i++){
				parametros = parametros + " " + experimento[i];
			}
			System.out.println("Comecando a rodar o experimento:" + parametros);
			exec(experimento);
			System.out.println("Resultados salvos em: "+ this.experimentoFolder);
		}
		
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
			
			//String nomePasta = args[1];
		
			
			//MONTA MATRIZ TERMO/ DOCUMENTO, COLOCANDO CADA DOCUMENTO NO ESPA‚O
			//String [][] tf_idf = Exec.montaTF_IDF(nomePasta, arquivoFrequenciaMin,dicionarioFrequenciaMin );
			
			//INICIA OS K CLUSTERS
			//RODA O K-MEANS
			
			
		}
		
		if(opCode == 2){
			
			
			InformationEntropy infoEntropy = new InformationEntropy(topWordsNum, termosEntropiaTXT, clusterOrderFileName, allTextBaseTxtFileName);
			
			infoEntropy.exec();
		}
		
		if(opCode == 3){
			
			int topN = 0;
			
			for (int i = 0; i < topNs.length; i++) {
				
				topN = topNs[i];
				
				/*Saida*/
				String termoCategoriaTXT = experimentoFolder+"termoCategoria/termoCategoria"+topN+".txt";
				String categoriasEncontradas = experimentoFolder+"classificationFiles/categoriasEncontradas"+topN+".txt";
				
					
				ClusterEntropyClassifier classificador = new ClusterEntropyClassifier(topN, balanceado, termoCategoriaTXT, 
						categoriasEncontradas, allTextBaseTxtFileName, termosOrdenadosTXT, termosCategoriaFull, categorias);
						
				classificador.montaDicionario();
				classificador.rodaHeuristica();
			}
		}
		
		if(opCode == 5){
			
			boolean balanceado = args[1].contentEquals("1");
			int clusterId = Integer.parseInt(args[2]);
			int tipoOrdenacao = Integer.parseInt(args[3]);
			int baseId = Integer.parseInt(args[4]);
			int clusterCategoriaId = Integer.parseInt(args[5]);
			int tipoMapeamentoTermo = Integer.parseInt(args[6]);
			int numHeuristica = Integer.parseInt(args[7]);
			
			getPorperties(balanceado, baseId, clusterId, clusterCategoriaId, tipoOrdenacao, tipoMapeamentoTermo, numHeuristica);
			
			Exec.rodaExperimento(this.clusterOrderFileName, this.allTextBaseTxtFileName, this.clusterCategoriaFileName, this.categoriasCorretasFileName, 
					this.tipoOrdenacao, this.tipoMapeamentoTermo, balanceado, this.topNs, this.termosCategoria, this.numHeuristica, this.resultados,
					this.categoriasFileName, this.experimentoFolder);
			
		}
		else if (opCode == 6){
			
			int idBase = Integer.parseInt(args[1]);
			Exec.formatBase(getBaseName(idBase),idBase);
		}
		
	}
	
	private String getBaseName(int idBase) {
		
		String pathCategorias = "";
		if (idBase == 0){
			//base da reuters25 ja foi processada
			pathCategorias = "";
		} else if (idBase == 1){
			//base reutersSS_2cat
			pathCategorias = "ArquivosEntrada/bases/reutersSS_2cat/dataset/";
		} else if (idBase == 2){
			//base newsgroup_2cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_2cat/dataset/";
		} else if (idBase == 3){
			//base newsgroup_3cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_3cat/dataset/";
		} else if (idBase == 4){
			//base newsgroup_3cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_4cat/dataset/";
		} else if (idBase == 5){
			//base newsgroup_3cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_5cat/dataset/";
		}
		
		return pathCategorias;
	}


	private void getPorperties(boolean balanceado, int baseId, int clusterId, int clusterCategoriaId, int tipoOrdenacao, 
			int tipoMapeamentoTermo, int numHeuristica) {
		
		this.experimentoFolder = "experimentos/";
		this.nomeExperimento = "";
		
		//qual base esta sendo usada
		if (baseId == 0 ){
			this.pathBase = "ArquivosEntrada/bases/reuters25/";
			this.nomeExperimento = this.nomeExperimento+ "R25";
			
		} else if (baseId == 1 ){
			this.pathBase = "ArquivosEntrada/bases/reutersSS_2cat/";
			this.nomeExperimento = this.nomeExperimento+ "R2/";
		
		}else if (baseId == 2 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_2cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG2/";
		
		} else if (baseId == 3 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_3cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG3/";
		
		} else if (baseId == 4 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_4cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG4/";
		
 		} else if (baseId == 5 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_5cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG5/";
		} 		
		
		this.allTextBaseTxtFileName = pathBase + baseId+ "_allTextBaseTxtFile.txt";
		this.categoriasCorretasFileName = pathBase + baseId+ "_categoriasCorretas.txt";
		this.categoriasFileName = pathBase + baseId + "_categorias.txt";
		this.clusterCategoriaFileName = pathBase + baseId+ "_clusterCategoria.txt";
		
		
		
		//tipo de clusterizacao a ser usada
		if (clusterId == 0 ){
			//0 - Cluster usando as categorias como Gabarito
			this.nomeExperimento = this.nomeExperimento+ "CG";
			this.clusterOrderFileName = pathBase + baseId+ "_clusterGabaritoOrdem.txt";
		} else if (clusterId == 1 ){
			//1- Cluster usando o k-means k=numcat
			this.nomeExperimento = this.nomeExperimento+ "CK";
			this.clusterOrderFileName = pathBase + baseId+ "_clusterKMeans.txt";
		} else if (clusterId == 2 ){
			//2- Cluster usando o k-means k=5
			this.nomeExperimento = this.nomeExperimento+ "CK5";
			this.clusterOrderFileName = pathBase + baseId+ "_clusterKMeans5.txt";
		} else if (clusterId == 3 ){
			//1- Cluster usando o k-means
			this.nomeExperimento = this.nomeExperimento+ "CK10";
			this.clusterOrderFileName = pathBase + baseId+ "_clusterKMeans10.txt";
		} else if (clusterId == 4 ){
			//1- Cluster usando o k-means
			this.nomeExperimento = this.nomeExperimento+ "CK20";
			this.clusterOrderFileName = pathBase + baseId+ "_clusterKMeans20.txt";
		}

		if (tipoOrdenacao == 0 ){
			//0 - termosByFrequenciaEntropiaZero
			this.nomeExperimento = this.nomeExperimento+ "E0";
		} else if (tipoOrdenacao == 1 ){
			//1 - termosByFrequenciaEntropia
			this.nomeExperimento = this.nomeExperimento+ "EF";
		}
		this.tipoOrdenacao = tipoOrdenacao;
		
		
		if (tipoMapeamentoTermo == 0 ){
			//0 - categoria escolhida pela frequencia absoluta
			this.nomeExperimento = this.nomeExperimento+ "FA";
		} else if (tipoMapeamentoTermo == 1 ){
			//1 - categoria escolhida pela frequencia relativa 
			this.nomeExperimento = this.nomeExperimento+ "FR";
		}
		this.tipoMapeamentoTermo = tipoMapeamentoTermo;
		
		
		this.numHeuristica = numHeuristica;
		//0 - heuristica que conta ponto por frequencia da palavra
		
		if (balanceado){
			this.nomeExperimento = this.nomeExperimento+ "BL";
		} else if (!balanceado){
			this.nomeExperimento = this.nomeExperimento+ "DB";
		}
		this.balanceado = balanceado;
		
		this.experimentoFolder = this.experimentoFolder + this.nomeExperimento+ "/";
		this.resultados = experimentoFolder+"resultados/resultado_"+this.nomeExperimento+".txt";
		
		this.termosCategoria = this.experimentoFolder + "termosCategorias/termosCategorias_"+this.nomeExperimento+".txt";
		
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
				((opCode == 4) && (args.length != 1))){
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
