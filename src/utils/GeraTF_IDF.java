package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import entities.DocFrequencia;
import entities.ModeloBayesiano;
import entities.TF_IDF;

public class GeraTF_IDF {
	
//	private static final String MLBases = "/var/dsp/MLBases/";
	//private static DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); 
	
	public static void main (String args[]){
		
		GeraTF_IDF geraTF_IDF = new GeraTF_IDF();
		List<File> arquivos = geraTF_IDF.pegaArquivos(args);
		geraTF_IDF.run(arquivos, "matrizTFIDF.csv", 0);
		System.out.println("Fim");
		
	} 

	
	private void run(List<File> arquivos, String string, int i) {
		exec(arquivos, "matrizTFIDF.csv", 0, null);
	}

	private List<File> pegaArquivos(String[] args) {
			
		String nomeDiretorio;
		if (args.length > 0){
			nomeDiretorio = args[0];
		}
		else {
			System.out.println("Entre com o nome do Diretorio a ser treinado");
			Scanner scanner = new Scanner(System.in);
			nomeDiretorio = scanner.next();
		}

		List<File> arquivos = ArquivoUtils.getAllFilesRecursive(nomeDiretorio);
		return arquivos;
		
	}
			
	public static void exec(List<File> arquivos, String outputArqNome, int minArquivoFrequencia, Map<String, Integer> dicionarioFrequencia){
		
		System.out.println("Preprocessando Arquivos");
		ModeloBayesiano modelo = preparaTF_IDF(arquivos,minArquivoFrequencia, dicionarioFrequencia);
		System.out.println("Gerando Matriz TF_IDF");
		TF_IDF matriz = montaTF_IDF(modelo);
		System.out.println("Salvando Matriz TF_IDF");
		List<String> linhasMatriz = matrizToLinhas(matriz, modelo);
		//else salvaMatrizMatLab(matriz, modelo, outputArqNome);
		ArquivoUtils.salvaArquivo(linhasMatriz, outputArqNome);
		System.out.println("Matriz salva com sucesso.");
		System.out.println("Salvando Matriz Matlab.");
		outputArqNome = outputArqNome.replace(".txt", "ML.txt");
		ArquivoUtils.salvaArquivo(matriz.getMatriz(), outputArqNome);
		System.out.println("Matriz no formato do MatLab salva com sucesso.");
		
	}
	
	public static List<String> matrizToLinhas(TF_IDF tF_IDF, ModeloBayesiano modelo) {

		List<String> linhas = new ArrayList<String>();
		List<String> matriz = tF_IDF.getMatriz();
		
		//coloca o cabecalho no arquivo
		String[] cabecalho = tF_IDF.getCabecalho();
		String[] documento = tF_IDF.getDocumentos();
		String csv = org.apache.commons.lang3.StringUtils.join(cabecalho, ",");  
		csv = "documentos,"+csv + ",categorias1";
		
		linhas.add(csv);

		String[] categorias = tF_IDF.getCategorias();
		String linha;
		System.out.println("Preparando para salvar "+matriz.size()+" linhas da matriz nos arquivos de saida.");
		for (int i = 0; i < matriz.size(); i++) {
			linha = matriz.get(i);
			csv = documento[i]+","+linha + formatoCategoriaWeka(categorias[i]);
			linhas.add(csv);
			
			if ((i%1000==0) && (i > 0)) System.out.println(i +" linhas da matriz prontas para salvar no arquivo de saida.");
		}
		
		return linhas;
	}

	private static String formatoCategoriaWeka(String categoria) {
		return categoria;//.substring(categoria.indexOf("_"));
	}

	public static TF_IDF montaTF_IDF(ModeloBayesiano modelo) {
		
		List<String> listTermos = modelo.getListTermos();
		
		List<DocFrequencia> docsFrequencia = modelo.getDocsFrequencias();
		int numDocs = docsFrequencia.size();
		int numTermos = listTermos.size();
		List<String> matriz = new ArrayList<String>();
		String[] categorias = new String [numDocs];
		String[] documentos = new String[numDocs];
		String[] cabecalho = new String [numTermos];
		DocFrequencia docAtual;
		/*
		String linhaBase = "";
		for (int i = 0; i < numTermos; i++) {
			linhaBase = linhaBase + "0,"; 
		}
		*/
		//coloca o cabecalho, na verdade vai ser rodapé...
		for (int j = 0; j < numTermos; j++) {
			cabecalho[j] = listTermos.get(j);
		}

		System.out.println(numDocs +" documentos a serem inseridos na matrix TF_IDF.");
		for (int i = 0; i < numDocs; i++) {
			
			docAtual = docsFrequencia.get(i);
			String linha = "";
			//int acumulado = 0;
			for (int j = 0; j < numTermos; j++) {
				
				//coloca a frequencia
				int frequencia;
				String termo = listTermos.get(j);
				if (docAtual.getTermoFrequencia().containsKey(termo)){
					frequencia = docAtual.getTermoFrequencia().get(termo);
				//	linha = linha + getAcumulado(linhaBase, acumulado) + frequencia + ",";
					//acumulado = 0;
				}
				else{ //acumulado = acumulado +1;}
					frequencia = 0;
				}
				linha = linha + frequencia + ",";
			}
			//linha = linha + getAcumulado(linhaBase,acumulado);
			matriz.add(linha);
			//coloca a categoria
			categorias[i] = docsFrequencia.get(i).getCategoria();
			//coloca o documento
			documentos[i] = docsFrequencia.get(i).getDocumento(); 
			
			if ((i%1000==0) && (i > 0)) System.out.println(i +" documentos inseridos na matriz TF_IDF.");
		}
		
		TF_IDF tfIdf = new TF_IDF(matriz, cabecalho, categorias, documentos); 
		return tfIdf;
		
	}

	public static ModeloBayesiano preparaTF_IDF(List<File> arquivos, int minArquivoFrequencia, Map<String, Integer> dicionarioFrequencia) {
		
		ModeloBayesiano modelo = new ModeloBayesiano();
		//ContaPalavras contaPalavras = new ContaPalavras();
		
		//Set<String> termos = new HashSet<String>();
		List<DocFrequencia> docsFrequencias = new ArrayList<DocFrequencia>();
		docsFrequencias.add(new DocFrequencia("dicionario","dicionario", dicionarioFrequencia));
		
		int i = 0;
		System.out.println(arquivos.size()+ " documentos para analizar a frequencia.");
		
		for (File arquivo : arquivos) {
			i++;
			List<String> linhas;
			try {

				linhas = ArquivoUtils.abreArquivo(arquivo.getAbsolutePath());
				String categoria = pegaCategoria(arquivo.getParent());
				Map<String, Integer> termoFrequenciaDocumento = ContaPalavras.contaFrequenciaNgrama(linhas, minArquivoFrequencia, dicionarioFrequencia.keySet(),1);
				//prepara lista para montar matriz TF_IDF
				docsFrequencias.add(new DocFrequencia(categoria+"_"+arquivo.getName(),categoria, termoFrequenciaDocumento));
				//atualiza a lista de categorias e a lista de termos do dicionario
				//termos.addAll(termoFrequenciaDocumento.keySet());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (i%1000==0) System.out.println(i +" arquivos com a frequencia de tokens ja contabilizada.");
			
		}
		System.out.println("Total de "+ i +" arquivos com a frequencia de tokens contabilizada.");
		List<String> listTermos = new ArrayList<String>();
		listTermos.addAll(dicionarioFrequencia.keySet());
		modelo.setListTermos(listTermos);
		modelo.setNumDocs(arquivos.size());
		modelo.setNumTermos(dicionarioFrequencia.keySet().size());
		modelo.setTermos(dicionarioFrequencia.keySet());
		modelo.setDocsFrequencias(docsFrequencias);
		
		return modelo;
	}

	private static String pegaCategoria(String fileParent) {
		return new File(fileParent).getName();
	}

	/*
	public static List<String> separaTreinoTeste(List<String> linhasMatriz, float percentTreino, 
			List<String> primeirasLinhas, String pastaCsv) {
			
		List<String> arquivosSalvos = new ArrayList<String>();
		List<String> treino = new ArrayList<String>();
		List<String> teste = new ArrayList<String>();
		
		
		if (primeirasLinhas.size()> 0 ){
			teste.addAll(primeirasLinhas);
		} else {
			teste.add(linhasMatriz.get(0));
		}
		treino.add(linhasMatriz.get(0));
		
		for (int i = 1; i < linhasMatriz.size(); i++) {
				
			float prandom = (float) (Math.random()*100);
			if (prandom < percentTreino){
				treino.add(linhasMatriz.get(i));
			} else teste.add(linhasMatriz.get(i));
		}
	
		Date data = new java.util.Date();
		String dataS = dateFormat.format(data);
		if (treino.size() > 1){
			ArquivoUtils.salvaArquivo(treino, pastaCsv+ "weka/treino/"+dataS+"_treino_weka.csv");
			arquivosSalvos.add(pastaCsv+ "weka/treino/"+dataS+"_treino_weka.csv");
			ArquivoUtils.salvaArquivo(preparaMatlab(treino), pastaCsv+ "matlab/treino/"+dataS+"_treino_matlab.csv");
			arquivosSalvos.add(pastaCsv+ "matlab/treino/"+dataS+"_treino_matlab.csv");
		}
		if (teste.size() > 1 ){
			ArquivoUtils.salvaArquivo(teste, pastaCsv+ "weka/teste/"+dataS+"_teste_weka.csv");
			arquivosSalvos.add(pastaCsv+ "weka/teste/"+dataS+"_teste_weka.csv");
			ArquivoUtils.salvaArquivo(preparaMatlab(teste), pastaCsv+ "matlab/teste/"+dataS+"_teste_matlab.csv");
			arquivosSalvos.add(pastaCsv+ "matlab/teste/"+dataS+"_teste_matlab.csv");
		}
		return arquivosSalvos;
		
	}
*/
/*	
	private static List<String> preparaMatlab(List<String> linhasIn) {
		
		List<String> linhasOut = new ArrayList<String>();
		
		for (int i = 1; i < linhasIn.size(); i++) {
			linhasOut.add(linhasIn.get(i).replace(",_", ","));
		}	
		
		return linhasOut;
	}
*/
	/*

	public static ModeloBayesiano preparaTeste(List<File> arquivos, String cabecalho, minArquivoFrequencia) {

		ModeloBayesiano modelo = new ModeloBayesiano();
		ContaPalavras contaPalavras = new ContaPalavras();
		
		Set<String> termos = new HashSet<String>();
		List<String> listTermos = new ArrayList<String>();
		
		String[] tokens = cabecalho.split(",");
		for (int i = 0; i < tokens.length-1; i++) {
			termos.add(tokens[i]);
			listTermos.add(tokens[i]);
		}
		
		System.out.println(arquivos.size()+ " documentos para analizar a frequencia.");
		List<DocFrequencia> docsFrequencias = new ArrayList<DocFrequencia>();
		int i = 0;
		for (File arquivo : arquivos) {
			i++;
			List<String> linhas;
			try {

				linhas = ArquivoUtils.abreArquivo(arquivo.getAbsolutePath());
				String categoria = pegaCategoria(arquivo.getParent());
				Map<String, Integer> termoFrequenciaDocumento = contaPalavras.contaFrequencia(linhas, minArquivoFrequencia, termos);
				
				//prepara lista para montar matriz TF_IDF
				docsFrequencias.add(new DocFrequencia(arquivo.getPath(),categoria, termoFrequenciaDocumento));
				//atualiza a lista de categorias e a lista de termos do dicionario
				//termos.addAll(termoFrequenciaDocumento.keySet());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (i%1000==0) System.out.println(i +" arquivos com a frequencia de tokens ja contabilizada.");
			
		}
		System.out.println("Total de "+ i +" arquivos com a frequencia de tokens contabilizada.");
		modelo.setListTermos(listTermos);
		modelo.setNumDocs(arquivos.size());
		modelo.setNumTermos(termos.size());
		modelo.setTermos(termos);
		modelo.setDocsFrequencias(docsFrequencias);
		
		return modelo;
	}
*/
}
