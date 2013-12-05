package main;

import java.util.ArrayList;
import java.util.List;

public class NLP {

	public static final int arquivoFrequenciaMin = 0;
	public static final int dicionarioFrequenciaMin = 20;
	
	private String experimentoFolder;
	private String nomeExperimento;
	private String clusterOrderFileName;
	private String allTextBaseTxtFileName;
	private String clusterCategoriaFileName;
	private String categoriasFileName;
	
	private String categoriasCorretasFileName;
	private String resultados;
	private boolean balanceado;
	private int tipoOrdenacao;
	private int numHeuristica;
	
	private Integer[] topNs = {1,2,3,4,5,10,15,20,25,30,35,40,45,50,60,70,80,90,100,200};//,300,400,500,600,700,800,900,1000};
	private int tipoMapeamentoTermo;
	private String termosCategoria;
	private String pathBase;
	private int ngramas;
	
	
	
	public static void main(String[] args) {
		
		NLP nlp = new NLP();
		nlp.teste();
		
	}

	
	public void teste(){
		
		//formataBases();
		rodaExperimentos();
		//verificaExperimentos();
		consolidaExperimentos();
		analisaClusters();
	}
	
	private void analisaClusters() {
		
		String opCode = "9";
//		String [] bases = {"1","2","3"};
//		String [] numCategorias = {"2", "2", "3"};
		String [] bases = {"4","5"};
		String [] numCategorias = {"4", "5"};
		String [] clusters = {"", "5", "10", "20"};
		String [] numClusters = {"0", "5", "10", "20"};
		for (int i = 0; i < bases.length; i++) {
			String base = bases[i];
			String numCategoria = numCategorias[i];
			String pathBase = getBaseName(base);
			for (int j = 0; j < clusters.length; j++) {
				String cluster = clusters[j];
				String numCluster = numClusters[j];
				exec(new String []{opCode, pathBase, pathBase + base+ "_clusterGabaritoOrdem.txt", 
						pathBase + base+ "_clusterKMeans" + cluster+ ".txt", numCategoria, numCluster});
			}
			System.out.println("Base "+ base + " analizada.");
		}
		
	}

	@SuppressWarnings("unused")
	private void consolidaExperimentos() {
		
		String opCode = "8";
		//String [] basesIds = {"1", "2","3"};
		String [] basesIds = {"4", "5"};
		String [] ngramas = {"1", "2", "3"};
		for (String basesId : basesIds) {
			for (String ngrama : ngramas) {
				exec(new String []{opCode, "experimentos/NG" + basesId + "NGR" + ngrama+"/"});
			}
		}
		
		
	}


	@SuppressWarnings("unused")
	private void verificaExperimentos() {
		
		String opCode = "7";
		String oulExperimentos = "experimentos1";
		String newExperimentos = "experimentos";
		exec(new String []{opCode, oulExperimentos, newExperimentos});
				
	}


	@SuppressWarnings("unused")
	private void formataBases(){
		
		List<String[]> experimentos = new ArrayList<String[]>();
		
		String opCode = "6";
		
		//String [] bases = {"1","2","3"};
		String [] bases = {"4","5"};
		
		for (String base : bases) {
			experimentos.add(new String[]{opCode,base});
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
	
	@SuppressWarnings("unused")
	private void rodaExperimentos(){
		
		List<String[]> experimentos = new ArrayList<String[]>();
		
		//codigo para rodar os experimentos
		String opCode = "5";
		String [] balanceamentos = {"0","1"};//0-desbalanceado, 1-balanceado; na pratica com poucas categorias nao afeta resultado
		String [] clustersIds = {"0","1","2","3","4"};
		String [] entropias = {"0","1"}; //tipoOrdenacao: 0-Entropia=0, 1-Todos os termos
		//String [] basesIDs = {"1","2","3"};
		String [] basesIDs = {"4","5"};
		//String [] clusterCategoriaIds = {"0"};
		String [] tiposMapeamentoTermo = {"0","1"};//0-Frequencia Absoluta; 1-Frequencia Relativa
		String [] numsHeuristica = {"0"};
		String [] ngramas = {"1","2","3"};
		
		for (String balanceamento : balanceamentos) {
			for (String clusterId : clustersIds) {
				for (String entropia : entropias) {
					for (String baseID : basesIDs) {
			//			for (String clusterCategoriaId : clusterCategoriaIds) {
							for (String tipoMapeamentoTermo : tiposMapeamentoTermo) {
								for (String numHeuristica : numsHeuristica) {
									for (String ngrama : ngramas) {
										experimentos.add(new String[]{opCode, balanceamento, clusterId, entropia, baseID, //clusterCategoriaId,
											tipoMapeamentoTermo, numHeuristica, ngrama});
									}
								}
							}
						}
					}			
				//}
			}
		} 
		
		
		String parametros="";
		int i = 0;
		for (String[] experimento : experimentos) {
			i = i + 1;
			parametros = "";
			for(int j=0; j< experimento.length ; j++){
				parametros = parametros + " " + experimento[j];
			}
			System.out.println("Experimento "+ i + " de "+ experimentos.size());
			System.out.println("Comecando a rodar o experimento:" + parametros);
			exec(experimento);
			System.out.println("Resultados salvos em: "+ this.experimentoFolder);
		}
		
	}

	private void exec(String[] args){

		int opCode = validaEntrada(args);
		
		if(opCode == 5){
			
			boolean balanceado = args[1].contentEquals("1");
			int clusterId = Integer.parseInt(args[2]);
			int tipoOrdenacao = Integer.parseInt(args[3]);
			int baseId = Integer.parseInt(args[4]);
			//int clusterCategoriaId = Integer.parseInt(args[5]);
			int tipoMapeamentoTermo = Integer.parseInt(args[5]);
			int numHeuristica = Integer.parseInt(args[6]);
			int ngramas =  Integer.parseInt(args[7]);
			
			getPorperties(balanceado, baseId, clusterId, tipoOrdenacao, tipoMapeamentoTermo, numHeuristica, ngramas);
			
			Exec.rodaExperimento(this.clusterOrderFileName, this.allTextBaseTxtFileName, this.clusterCategoriaFileName, this.categoriasCorretasFileName, 
					this.tipoOrdenacao, this.tipoMapeamentoTermo, this.balanceado, this.topNs, this.termosCategoria, this.numHeuristica, this.ngramas, this.resultados,
					this.categoriasFileName, this.experimentoFolder);
			
		}
		else if (opCode == 6){
			
			int idBase = Integer.parseInt(args[1]);
			Exec.formatBase(getBaseName(idBase)+"dataset/",idBase);
		}
		else if (opCode == 7){
			
			Double percentDiff = Exec.comparaExperimentos(args[1], args[2]);
			System.out.println("Percentual de arquivos diferente: "+ percentDiff);
		}
		else if (opCode == 8){
			String ngramaBasePath = args[1];
			Exec.consolidaResultados(ngramaBasePath);
		}

		else if (opCode == 9){
			String pathBase = args[1];
			String clusterGabarito = args[2];
			String clusterEncontrado = args[3];
			int numCategoria = Integer.parseInt(args[4]);
			int numCluster = Math.max(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
			Exec.analisaClusters(pathBase, clusterGabarito, clusterEncontrado, numCategoria, numCluster);
		}
	}
	
	private String getBaseName(String base) {
	
		int idBase = Integer.parseInt(base);
		return getBaseName(idBase);
	}
	
	private String getBaseName(int idBase) {
		
		String pathCategorias = "";
		if (idBase == 0){
			//base da reuters25 ja foi processada
			pathCategorias = "ArquivosEntrada/bases/reuters25/";
		} else if (idBase == 1){
			//base reutersSS_2cat
			pathCategorias = "ArquivosEntrada/bases/reutersSS_2cat/";
		} else if (idBase == 2){
			//base newsgroup_2cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_2cat/";
		} else if (idBase == 3){
			//base newsgroup_3cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_3cat/";
		} else if (idBase == 4){
			//base newsgroup_3cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_4cat/";
		} else if (idBase == 5){
			//base newsgroup_3cat
			pathCategorias = "ArquivosEntrada/bases/newsgroup_5cat/";
		}
		
		return pathCategorias;
	}


	private void getPorperties(boolean balanceado, int baseId, int clusterId, int tipoOrdenacao, 
			int tipoMapeamentoTermo, int numHeuristica, int ngramas) {
		
		this.experimentoFolder = "experimentos/";
		this.nomeExperimento = "";
		
		//qual base esta sendo usada
		if (baseId == 0 ){
			this.pathBase = "ArquivosEntrada/bases/reuters25/";
			this.nomeExperimento = this.nomeExperimento+ "R25";
			
		} else if (baseId == 1 ){
			this.pathBase = "ArquivosEntrada/bases/reutersSS_2cat/";
			this.nomeExperimento = this.nomeExperimento+ "R2";
		
		}else if (baseId == 2 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_2cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG2";
		
		} else if (baseId == 3 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_3cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG3";
		
		} else if (baseId == 4 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_4cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG4";
		
 		} else if (baseId == 5 ){
			this.pathBase = "ArquivosEntrada/bases/newsgroup_5cat/";
			this.nomeExperimento = this.nomeExperimento+ "NG5";
		} 
		
		this.ngramas = ngramas;
		this.nomeExperimento = this.nomeExperimento+ "NGR"+ this.ngramas+ "/";
		
		
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
		} else {
			this.nomeExperimento = this.nomeExperimento+ "DB";
		}
		this.balanceado = balanceado;
		
		this.experimentoFolder = this.experimentoFolder + this.nomeExperimento + "/";
		this.resultados = this.experimentoFolder+"resultados/"+this.nomeExperimento.replace("/", "") + ".txt";
		this.termosCategoria = this.experimentoFolder + "termosCategorias/"+this.nomeExperimento.replace("/", "") + ".txt";
		
	}

	private int validaEntrada(String[] args) {
		
		// 0: preprocessa csv para gerar arquivos do corpus como txt
		int opCode = -1;
		if (args.length == 0){
			imprimeUsage();
		}
		else {
			opCode = Integer.parseInt(args[0]);
			//TODO
			if (((opCode == 6) && (args.length != 2))){
				imprimeUsage();
			}
		}
		
		return opCode;
	}
	
	private void imprimeUsage() {
		
		System.out.println("Opcoes de Uso:");
		// 0: preprocessa csv para gerar arquivos do corpus como txt
		//TODO
		System.exit(-1);
	}
		
		
		
	
}
