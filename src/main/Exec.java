package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entities.Termo;

import utils.ArquivoUtils;
import utils.ContaPalavras;
import utils.GeraTF_IDF;
import utils.PreProcessaCorpus;
import utils.ProcessaCSVtoTextFiles;

public class Exec {
	
	 private static final String stopListNameFile = "ArquivosEntrada/enStopList.txt";
	 public static final int frequenciaMin = 0;

	public static String csvToTxt(String nomeArquivo, Boolean removeStopWords, Boolean reduzStemme, String corpusTxtPath) {
		
		ProcessaCSVtoTextFiles.processCSVFile(removeStopWords, reduzStemme, nomeArquivo, stopListNameFile, corpusTxtPath);
		return null;
	}

	public static Map<String, Integer> montaAllTokensFrequencia(String nomePath, int minFrequenciaArquivo, int minFrequenciaGlobal, int ngrama){
		
		List<File> arquivos = ArquivoUtils.getAllFilesRecursive(nomePath);
		Map<String, Integer> dicionarioFrequencia = new HashMap<String, Integer>();
		Map<String, Integer> arquivoFrequencia = new HashMap<String, Integer>();
		
		for (File arquivo : arquivos) {
			
			List<String> linhas = ArquivoUtils.abreArquivo(arquivo);
			arquivoFrequencia = ContaPalavras.contaFrequenciaNgrama(linhas, minFrequenciaArquivo, null, ngrama);
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

	public static String[][] montaTF_IDF(String nomePasta, int minArquivoFrequencia, int minDicionarioFrequencia, int ngrama) {
		
		//PROCESSA TODOS OS TEXTOS DE UMA PASTA
		//MONTA O DICIONARIO - TOKEN FREQUENCIA
		Map<String, Integer> dicionarioFrequencia = Exec.montaAllTokensFrequencia(nomePasta,minArquivoFrequencia, minDicionarioFrequencia, ngrama);
		
		//SALVA O DICIONARIO
		String dicName = nomePasta.replace("corpusTxt", "dicFrequencia") + ".txt";
		ArquivoUtils.salvaArquivo((ContaPalavras.imprimeFrequencias(dicionarioFrequencia)),dicName);
		
		//pega a lista de tokens
		//String [] dicHead = Exec.getDicHead(dicionarioFrequencia);
		
		List<File> arquivos = ArquivoUtils.getAllFilesRecursive(nomePasta);
		String nomeTF_IDF = nomePasta.replace("corpusTxt", "TfIdf") + ".txt";
		//nomeTF_IDF = nomeTF_IDF.replace("/", "_");
		GeraTF_IDF.exec(arquivos, nomeTF_IDF, minArquivoFrequencia, dicionarioFrequencia);
		
		return null;
	}

	public static void rodaExperimento(String clusterOrderFileName, String allTextBaseTxtFileName, String clusterCategoriaFileName, 
			String categoriasCorretasFileName, int tipoOrdenacao, int tipoMapeamentoTermo, boolean balanceado, 
			Integer[] topNs, String termosCategoria, int numHeuristica, int ngrama, String resultados, String categoriasFileName,
			String experimentoPath) {
		
		System.out.println(new Date()+ " carregando a base...");
		//carrega a base e os clusters gerados para a memoria
		CarregaDados load = new CarregaDados(clusterOrderFileName, allTextBaseTxtFileName, clusterCategoriaFileName, 
				categoriasCorretasFileName,categoriasFileName);
		Map<String, List<String>> clustersDocumentos = load.carregaClustersDocumentos();
		Map<String, List<String>> categoriasDocumentos = load.carregaCategoriasDocumentos();
		//Map<String, String> mapClusterCategoria = load.carregaClusterCategoria();
		
		System.out.println(new Date()+ " montanto a estrutura com termpoclusterfrequencia...");
		//processa o cluster montanto a estrutura com termpoclusterfrequencia
		ProcessaEstrutura clustersProcessados = new ProcessaEstrutura(clustersDocumentos);
		ProcessaEstrutura categoriasProcessados = new ProcessaEstrutura(categoriasDocumentos);
		Map<String, Map<String, Integer>> mapTermosClustersFrequencias = clustersProcessados.getTermosEstruturasFrequencias(ngrama);
		Map<String, Map<String, Integer>> mapTermosCategoriasFrequencias = categoriasProcessados.getTermosEstruturasFrequencias(ngrama);
		Map<String, Map<String, Integer>> mapTermosClustersPresencas = clustersProcessados.getTermosEstruturaPresencas(ngrama);
		Map<String, Map<String, Integer>> mapTermosCategoriasPresencas = categoriasProcessados.getTermosEstruturaPresencas(ngrama);
		
		imprimeTermoEstrutura(mapTermosClustersFrequencias, experimentoPath+ "TermoClusterFrequencias.txt");
		imprimeTermoEstrutura(mapTermosCategoriasFrequencias, experimentoPath+ "TermosCategoriasFrequencias.txt");
		imprimeTermoEstrutura(mapTermosClustersPresencas, experimentoPath+ "TermosClustersPresencas.txt");
		imprimeTermoEstrutura(mapTermosCategoriasPresencas, experimentoPath+ "TermosCategoriasPresencas.txt");
		
		System.out.println(new Date()+ " processando as categorias...");
		Map<String, Integer> mapCategoriaNumDocs = categoriasProcessados.getEstruturasNumDocs();
		
		System.out.println(new Date()+ " ordenando os termos...");
		//dado os termos e suas frequencias em cada categoria, ordena os termos a serem usados
		OrdenaTermos ordenaTermosCluster = new OrdenaTermos(mapTermosClustersFrequencias);
		List<Termo> termosCluster = ordenaTermosCluster.getTermosOrdenados(tipoOrdenacao);
		
		System.out.println(new Date()+ " mapeando suas categorias...");
		//descobre de maneira automatica a categoria de cada um dos termpos
		GeraTermoCategoria geraTermoCategoria = new GeraTermoCategoria(mapTermosCategoriasFrequencias, mapCategoriaNumDocs);
		Map<String, String> mapTermosCategorias = geraTermoCategoria.getTermosCategorias(tipoMapeamentoTermo);
		imprime(mapTermosCategorias,experimentoPath+ "termoCategoria.txt");
		
		System.out.println(new Date()+ " carregando as categorias corretas...");
		MontaListaTermos montaListaTermos = new MontaListaTermos(termosCluster, mapTermosCategorias, load.getCategorias());
		List<String> documentos = load.getDocumentos();
		List<String> categoriasCorretas = load.carregaCategoriasCorretas();
		
		List<String> resultadosGerais = new ArrayList<String>();
		resultadosGerais.add("N;numero arquivos;N‹o Encontrados;% N‹o Encontrados;Acertos em documentos definidos;Total Definido;" +
				"Acur‡cia em Definidos;Acur‡cia Global");
		for (int topN : topNs) {
		
			System.out.println(new Date()+ " montando a lista de topN termos para topN = "+ topN);
			//monta a lista de termos a ser usada nesse experimento
			Map<String, String> topNTermosCategorias = montaListaTermos.getTopNTermos(topN, balanceado);
			
			List<String> topNTC = new ArrayList<String>();
			for (Entry<String, String> topNTermoCategoria : topNTermosCategorias.entrySet()) {
				topNTC.add(topNTermoCategoria.getKey()+";"+ topNTermoCategoria.getValue());
			}
			
			String arquivoTermosCategorias = termosCategoria.replace(".txt", topN+".txt");
			ArquivoUtils.salvaArquivo(topNTC, arquivoTermosCategorias);
			
			System.out.println(new Date()+ " rodando a heuristica...");
			//pega a base, a lista de termos e gera uma lista das categorias encontradas
			RodaHeuristica rodaHeuristica = new RodaHeuristica(documentos, topNTermosCategorias, ngrama);
			List<String> categoriasEncontradas = rodaHeuristica.exec(numHeuristica);
			
			System.out.println(new Date()+ " calculando taxa de acerto...");
			//calcula acerto
			CalculaAcertoNew calculaAcerto = new CalculaAcertoNew(categoriasCorretas, categoriasEncontradas, load.getCategorias(), topN);
			List<String> resultado = calculaAcerto.exec();
			
			String arquivoResultados = resultados.replace(".txt", topN+".txt");
			ArquivoUtils.salvaArquivo(resultado, arquivoResultados);
			
			resultadosGerais.add(resultado.get(1));
		}
		ArquivoUtils.salvaArquivo(resultadosGerais, resultados);
	
		
	}

	private static void imprimeTermoEstrutura(Map<String, Map<String, Integer>> mapTermosEstruturaOcorrencias, String arqTermoEstrutura) {
		
		List<String> termoEstrutura = new ArrayList<String>();
		
		for (Entry<String, Map<String, Integer>> mapTermoEstruturaOcorrencia: mapTermosEstruturaOcorrencias.entrySet()){
			
			String termo = mapTermoEstruturaOcorrencia.getKey();
			Map<String, Integer> mapEstruturasOcorrencias = mapTermoEstruturaOcorrencia.getValue();
			String estruturaOcorrencias = "";
			for (Entry<String, Integer> mapEstruturaOcorrencias : mapEstruturasOcorrencias.entrySet()) {
				estruturaOcorrencias = mapEstruturaOcorrencias.getKey() +":"+ mapEstruturaOcorrencias.getValue() +";"+ estruturaOcorrencias;
			}
			termoEstrutura.add(termo +":"+ estruturaOcorrencias);
		}
		ArquivoUtils.salvaArquivo(termoEstrutura, arqTermoEstrutura);
	}

	private static void imprime(Map<String, String> mapTermosCategorias, String arqTermoCategoria) {
		List<String> termoCategoria = new ArrayList<String>();
		for (Entry<String, String> mapTermoCategoria : mapTermosCategorias.entrySet()) {
			termoCategoria.add(mapTermoCategoria.getKey()+ ";"+ mapTermoCategoria.getValue());
		}
		ArquivoUtils.salvaArquivo(termoCategoria, arqTermoCategoria);
	}

	public static void formatBase(String baseName, int idBase) {
		
		List<String> allTexts = new ArrayList<String>();
		List<String> categoriasCorretas = new ArrayList<String>();
		List<String> clusterGabaritoOrdem = new ArrayList<String>();
		List<String> clusterCategoria = new ArrayList<String>();
		List<String> categorias = new ArrayList<String>();
		
		
		File root = new File(baseName);
		File[] pathsCategorias = root.listFiles();
		
		int clusterId = 0;
		for (File pathCategoria : pathsCategorias) {
			
			if (pathCategoria.isDirectory()){
				//monta o relacionamento entre cluster e categoria
				clusterId = clusterId + 1;
				clusterCategoria.add(clusterId + ";"+ pathCategoria.getName());
				
				//gera a lista das categorias
				categorias.add(pathCategoria.getName());
				
				File[] arquivosCategoriaAtual = pathCategoria.listFiles();
				
				for (File arquivoCategoriaAtual : arquivosCategoriaAtual) {
				
					//monta a ordem dos documentos no cluster usando a categoria como gabarito
					clusterGabaritoOrdem.add(""+ clusterId);
					
					//monta a ordem das categorias
					categoriasCorretas.add(pathCategoria.getName());
					
					
					//monta o arquivo contendo todos os documentos separados por uma linha em branco
					//remove stopwords, reduz stemme
					List<String> linhasPreProcessadas = PreProcessaCorpus.preProcessaArquivo(arquivoCategoriaAtual, true, true, stopListNameFile);
					//coloca uma linha por arquivo
					allTexts.add(ArquivoUtils.contatenaLinhas(linhasPreProcessadas));
					allTexts.add("");
					
				}
			}
			
		}
		
		ArquivoUtils.salvaArquivo(allTexts, baseName + idBase+"_allTextBaseTxtFile.txt");
		ArquivoUtils.salvaArquivo(categoriasCorretas,baseName + idBase+"_categoriasCorretas.txt");
		ArquivoUtils.salvaArquivo(clusterGabaritoOrdem, baseName + idBase+"_clusterGabaritoOrdem.txt");
		ArquivoUtils.salvaArquivo(clusterCategoria, baseName + idBase+"_clusterCategoria.txt");
		ArquivoUtils.salvaArquivo(categorias, baseName + idBase+"_categorias.txt");
		
	}

	public static Double comparaExperimentos(String oldExperimentos, String newExperimentos){
		
		int numDiff = 0;
		List<File> newResults = ArquivoUtils.getAllFilesRecursive(newExperimentos);
		boolean diferente = false;
		int lidos = 0;
		int numEguais = 0;
		for (File newResult : newResults) {
			lidos = lidos + 1;
			if (newResult.getName().contains("1GR")){
				File oldResult = new File(newResult.getAbsolutePath().replace(newExperimentos, oldExperimentos));
				diferente = ArquivoUtils.comparaArquivos(oldResult, newResult);
				if (diferente){
					numDiff = numDiff +  1;
//					System.out.println("Arquivos Diferentes: " );
//					System.out.println(newResult.getAbsoluteFile());
//					System.out.println(oldResult.getAbsolutePath());
//					System.out.println("Arquivos Diferentes encontrados: "+ numDiff);
				} else numEguais = numEguais + 1;
				
				if(lidos%1000==0){
					System.out.println("Arquivo "+ lidos + " de "+ newResults.size());
				}
//				System.out.println("Arquivo "+ lidos + " de "+ newResults.size());
				
			}
			
		}
		System.out.println("Eguais: "+ numEguais);
		System.out.println("Diferentes: " + numDiff);
		
		
		return 1.0 * (numDiff/newResults.size());
	}

	public static void consolidaResultados(String ngramaBasePathName ) {
		
		String [] clusters = {"CK20","CK10","CK5","CK","CG"};
		String [] entropias = {"EF", "E0"};
		String [] frequencias = {"FR","FA"};
		String [] balanceados = {"BL", "DB"};
		
		String folderAtual;
		File ngramaBase = new File(ngramaBasePathName);
		//File[] ngramaBaseDirs = new File("experimentos").listFiles();
		//for (File ngramaBase : ngramaBaseDirs) {
			folderAtual= "";
			for (String entropia : entropias) {
				for (String frequencia : frequencias) {
					for (String balanceado : balanceados) {
						folderAtual = entropia + frequencia + balanceado;
						List<String> resultadosConsolidados = new ArrayList<String>();
						for (String cluster : clusters) {
							//folderAtual = ngramaBase.getAbsolutePath()+"/" + cluster+ folderAtual;
							resultadosConsolidados.add(getAcuracia(ngramaBase.getAbsolutePath()+"/" + cluster+ folderAtual));
						}
						ArquivoUtils.salvaArquivo(resultadosConsolidados, ngramaBase+ "/"+ folderAtual+ ".txt");
					}
				}
			}
		//}
		 
	}

	private static String getAcuracia(String dirPath) {
		

		File pathResultados = new File(dirPath + "/resultados/"); 
		File [] resultados = pathResultados.listFiles();
		int menorPath = 1000;
		File resultadoConsolidado = null;
		for (File resultado : resultados) {
			if (resultado.getName().length() < menorPath){
				menorPath = resultado.getName().length();
				resultadoConsolidado = resultado;
			}
		}
		
		List<String> linhas = ArquivoUtils.abreArquivo(resultadoConsolidado);
		String acuracias = "";
		for (int i = 1; i < linhas.size(); i++) {
			String [] tokens = linhas.get(i).split(";");
			acuracias = acuracias + tokens[tokens.length-1]+ ";";
		}
		
		return acuracias;
	}

	public static void analisaClusters(String pathbase, String clusterGabaritoFile, String clusterEncontradoFile, int numCategoria, int numCluster) {
		
		
		int [][] matrizConfusao = new int [numCategoria][numCluster];
		
		List<String> linhasClustersGabarito = ArquivoUtils.abreArquivo(clusterGabaritoFile);
		List<String> linhasClustersEncontrados = ArquivoUtils.abreArquivo(clusterEncontradoFile);
		
		int clusterGabaritoAtual = 0;
		int clusterEncontradoAtual = 0;
		
		for (int i = 0; i < linhasClustersGabarito.size(); i++) {
			
			clusterGabaritoAtual = Integer.parseInt(linhasClustersGabarito.get(i)) -1;
			clusterEncontradoAtual = Integer.parseInt(linhasClustersEncontrados.get(i)) -1;
			
			matrizConfusao[clusterGabaritoAtual][clusterEncontradoAtual] = matrizConfusao[clusterGabaritoAtual][clusterEncontradoAtual] + 1;
		}	
			
		List<String> linhasSaida = new ArrayList<String>();
		String linha;
		
		//imprime cabecalho
		linha = "idCat";
		for (int j = 0; j < numCluster; j++) {
			linha = linha + ";cluster" + (j+1);
		}
		linhasSaida.add(linha);
	
		for (int i = 0; i < numCategoria; i++) {
			linha = "idCat"+ (i+1);
			for (int j = 0; j < numCluster; j++) {
				linha = linha + ";" + matrizConfusao[i][j];
			}
			linhasSaida.add(linha);
		}
		File arquivoSaidaNomePath = new File(pathbase);
		String arquivoSaida = pathbase+ arquivoSaidaNomePath.getName()+ "_analiseCluster"+numCluster+".txt";
		ArquivoUtils.salvaArquivo(linhasSaida, arquivoSaida);
		
	}
	
	
}
