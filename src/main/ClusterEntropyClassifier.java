package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multiset.Entry;

import utils.ArquivoUtils;
import utils.ContaPalavras;
import utils.Utils;


public class ClusterEntropyClassifier {
	
	public static final String experimentoFolder = "experimentos/Top25_2/";
	private static final int topN = 1;
	private static final String termoCategoriaTXT = experimentoFolder+"termoCategoria/termoCategoriaDominio"+topN+".txt";
	
	//private static final String categoriasEncontradas = experimentoFolder+"classificationFiles/categoriasEncontradas"+topN+".txt";
	private static final String categoriasEncontradas = experimentoFolder+"classificationFiles/categoriasEncontradasDominio"+topN+".txt";
	
	private static final String baseDocumentos = experimentoFolder+"allTextBaseTxtFile/ReutersTrainTop100SS.txt";
	//private static final String baseDocumentos = experimentoFolder+"allTextBaseTxtFile/ReutersTestSS.txt";
	
	private static final String gabaritoTermoCategoria = experimentoFolder+"DicionarioTermoCategoria.csv";
	//private static final String termosEntropiaArquivo = experimentoFolder+"termosEntropia/termosEntropiaTrainTop100SS.txt";
	private static final String termosEntropiaCategoria = experimentoFolder+"termoCategoria/termosCategoriaDominio.txt"; 
	//+		"termosEntropia/termosEntropiaCategoriaTrainTop100SS.txt";
	//private static final String termosNaoMapeadosTxt = experimentoFolder+ "termosNaoMapeados.txt";
	private static final String termosEntropiaArquivoOrdenados = experimentoFolder + "termoEntropia/termosEntropiaTrainTop100SSOrdered.csv";
	private static final String[] categorias = {"acq","bop","cocoa","coffee","corn","cpi","crude","dlr","earn","gnp","gold","grain","interest",
		"livestock","money-fx","money-supply","nat-gas","oilseed","reserves","ship","soybean","sugar","trade","veg-oil","wheat"};
	
	public static void main(String[] args) {
		
		ClusterEntropyClassifier classificador = new ClusterEntropyClassifier();
		//classificador.filtraDicionario();
		//classificador.consolidaTermoEntropiaCategoria();
		classificador.montaDicionario();
		classificador.rodaHeuristica();
		
		
	}

	private void consolidaTermoEntropiaCategoria() {
		
		List<String> termosEntropias = ArquivoUtils.abreArquivo(termosEntropiaArquivoOrdenados);
		Map<String,String> termoCategoria = new HashMap<String, String>();
		List<String> linhasDicionario = ArquivoUtils.abreArquivo(gabaritoTermoCategoria);
		
		for (String linha : linhasDicionario) {
			String[] tokens = linha.split(";");
			termoCategoria.put(tokens[0].toLowerCase().replaceAll("[.,1234567890#$@!&();/?+=]", ""), tokens[1]);
		}
		
		String[] tokens;
		List<String> termosEntropiaCategorias = new ArrayList<String>();
		for (String termoEntropia : termosEntropias) {
			tokens = termoEntropia.split(";");
			if(termoCategoria.containsKey(tokens[0])){
				termoEntropia = termoEntropia + ";"+ termoCategoria.get(tokens[0]);
			}
			else termoEntropia = termoEntropia + ";" + "termoNaoMapeado";
			termosEntropiaCategorias.add(termoEntropia);
		}
		ArquivoUtils.salvaArquivo(termosEntropiaCategorias, termosEntropiaCategoria);
	}

	private void montaDicionario() {
		
		List<String> termosEntropiaLinha = ArquivoUtils.abreArquivo(termosEntropiaCategoria);
		Map<String, Integer> categoriaAchadas = loadCategoriaAchadas();
		
		Map<String, String> termoCategoria = loadTermoCategoria();
		List<String> termoCategoriaTopN = new ArrayList<String>();
		
		List<String> prioridade = new ArrayList<String>();
		int numLinhaAtual = 0;
		String linhaAtual = "";
		String termo = "";
		String[] tokens;
		String categoria = "";
		int faltam = topN;
		while((categoriaAchadas.size()>0) && (numLinhaAtual < termosEntropiaLinha.size())){
			linhaAtual = termosEntropiaLinha.get(numLinhaAtual);
			tokens = linhaAtual.split(";");
			termo = tokens[0];
			if (termoCategoria.containsKey(termo)){
				categoria = termoCategoria.get(termo);
				if (categoriaAchadas.containsKey(categoria)){
					termoCategoriaTopN.add(termo+ ";"+ categoria);
					faltam = categoriaAchadas.get(categoria);
					if(faltam == 1 ){
						categoriaAchadas.remove(categoria);
					} else {
						categoriaAchadas.put(categoria, faltam-1);
					}
				}
			}
			else{
				//System.out.println(termo);
				prioridade.add(termo);
				//for (Map.Entry<String, Integer> entry : categoriaAchadas.entrySet()) {
				//	System.out.println(entry.getKey()+";"+ entry.getValue());
				//}
				//System.exit(-1);
				
			}
			numLinhaAtual = numLinhaAtual + 1;
			
		}
		System.out.println(prioridade.size());
		for (String string : prioridade) {
			System.out.println(string);
		}
		ArquivoUtils.salvaArquivo(termoCategoriaTopN, experimentoFolder+ "termoCategoria/termoCategoriaDominio"+topN+ ".txt");
		
		
	}

	private Map<String, String> loadTermoCategoria() {
		
		Map<String, String> termoCategoria = new HashMap<String, String>();
		
		List<String> dicionarioTermoCategoriaLinhas = ArquivoUtils.abreArquivo(termosEntropiaCategoria);
		String[] tokens;
		for (String linha : dicionarioTermoCategoriaLinhas) {
			tokens = linha.split(";");
			termoCategoria.put(tokens[0], tokens[5]);
		}
		return termoCategoria;
		
		
	}

	private Map<String, Integer> loadCategoriaAchadas() {
		
		Map<String, Integer> categoriaAchadas = new HashMap<String, Integer>();
		for (int i = 0; i < categorias.length; i++) {
			categoriaAchadas.put(categorias[i], topN);
		}
		return categoriaAchadas;
		
	}
/*
	private void filtraDicionario() {
		
		Map<String,String> termoCategoria = new HashMap<String, String>();
		List<String> linhasDicionario = ArquivoUtils.abreArquivo(gabaritoTermoCategoria);
		
		for (String linha : linhasDicionario) {
			String[] tokens = linha.split(";");
			termoCategoria.put(tokens[0].toLowerCase().replaceAll("[.,1234567890#$@!&();/?+=]", ""), tokens[1]);
		}
		
		List<String> termosFrequencias = ArquivoUtils.abreArquivo(termosEntropiaArquivo);
		List<String> novoTermosFrequencias = new ArrayList<String>();
		List<String> termosNaoMapeados = new ArrayList<String>();
		for (String linha : termosFrequencias) {
			String[] tokens = linha.split(";");
			if(termoCategoria.containsKey(tokens[0])){
				novoTermosFrequencias.add(linha+";"+termoCategoria.get(tokens[0]));
			} else {
				termosNaoMapeados.add(tokens[0]);
			}
			
		}
		
		ArquivoUtils.salvaArquivo(novoTermosFrequencias, experimentoFolder+ "termosCategorias/EntropiaTrainTop100SS.txt");
		ArquivoUtils.salvaArquivo(termosNaoMapeados, termosNaoMapeadosTxt);
		
	}
*/
	private void rodaHeuristica() {
		
		HashMap<String, String> termosCategorias = getTermoCategoria();
		
		List<String> docs = ArquivoUtils.abreArquivo(baseDocumentos);
		
		List<String> categorias = new ArrayList<String>();
		
		for (String doc : docs) {
			if (doc.trim().length() > 0){
				String categoria = verificaCategoria(doc.toLowerCase().replaceAll("[.,1234567890#$@!&();/?+=]", ""), termosCategorias);
				categorias.add(categoria);
			}
			
		}
				
		ArquivoUtils.salvaArquivo(categorias, categoriasEncontradas);
		System.out.println("Arquivo categoriasEncontradas.txt salvo com sucesso.");
	}

	private String verificaCategoria(String doc, HashMap<String, String> termosCategorias) {
		
		//separa lista de termos presentes na heuristica
		Set<String> termosHeuristica = termosCategorias.keySet();
		
		//pega do documento frequencia referente apenas aos termos da heuristica
		Map<String, Integer> termosFrequenciaDocumento = ContaPalavras.contaFrequencia(doc, 0, termosHeuristica);
		
		//cria um mapa para guardar os pontos de cada categoria, e vai atualizando a cada termo do documento
		int maxPontos = 0;
		String categoriaEscolhida = "naoIdentificado";
		Map<String, Integer> categoriaPonto = new HashMap<String, Integer>(); 
		
		Set<String> termosDocumentos = termosFrequenciaDocumento.keySet();
		String categoria;
		int pontosCategoria;
		for (String termo : termosDocumentos) {
			categoria = "";
			pontosCategoria = 0;
			
			if(termosCategorias.containsKey(termo)){
				categoria = termosCategorias.get(termo);
			}
			
			if(categoriaPonto.containsKey(categoria)){
				pontosCategoria = categoriaPonto.get(categoria);
			}
			
			pontosCategoria = pontosCategoria + termosFrequenciaDocumento.get(termo);
			categoriaPonto.put(categoria, pontosCategoria);
			
			if (pontosCategoria > maxPontos){
				maxPontos = pontosCategoria;
				categoriaEscolhida = categoria;
			}
			
		} 
		
		return categoriaEscolhida;
	}

	private HashMap<String, String> getTermoCategoria() {
		
		HashMap<String, String> termosCategorias = new HashMap<String, String>();
		List<String> linhas = ArquivoUtils.abreArquivo(termoCategoriaTXT);
		for (String linha : linhas) {
			String[] termoCategoria = linha.split(";");
			termosCategorias.put(termoCategoria[0],termoCategoria[1]);
						
		}
	
		return termosCategorias;
	}
	
	
	
	
}
