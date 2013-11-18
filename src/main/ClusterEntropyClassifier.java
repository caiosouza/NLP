package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.ArquivoUtils;
import utils.ContaPalavras;


public class ClusterEntropyClassifier {
	
	private int topN;
	private boolean balanceado;
	
	/*SAIDA*/
	private String termoCategoriaTXT;
	private String categoriasEncontradas;
	
	/*Entrada*/
	private String baseDocumentos;
	private String termosOrdenadosTXT;
	private String termosCategoriaFull;
	
	private String[] categorias;
	
	public ClusterEntropyClassifier(int topN, boolean balanceado, String termoCategoriaTXT, String categoriasEncontradas,
			String baseDocumentos, String termosOrdenadosTXT, String termosCategoriaFull, String[] categorias) {
		
		this.topN = topN;
		this.termoCategoriaTXT = termoCategoriaTXT;
		this.categoriasEncontradas = categoriasEncontradas;
		this.baseDocumentos = baseDocumentos;
		this.termosOrdenadosTXT = termosOrdenadosTXT;
		this.termosCategoriaFull = termosCategoriaFull;
		this.categorias = categorias;
		this.balanceado = balanceado;
	}

	public void montaDicionario() {
		
		//abre o arquivo com os termos ordenados e o com o mapeamento entre <termo;categoria>
		List<String> termosEntropiaLinha = ArquivoUtils.abreArquivo(termosOrdenadosTXT);
		Map<String, String> termoCategoria = loadTermoCategoria();
		
		
		Map<String, Integer> categoriaAchadas = loadCategoriaAchadas();
		Map<String,List<String>> categoriaTermos = new HashMap<String, List<String>>();
		List<String> termosCategoriaLida = new ArrayList<String>();
		
		String termo = "";
		String[] tokens;
		String linhaAtual = "";
		String categoria = "";
		int numLinhaAtual = 0;
		int faltam = topN;
		
		//le os termos atŽ que todas as categorias tenham no minimo topN termos ou ate que todos os termos tenham sido lidos
		while((categoriaAchadas.size()>0) && (numLinhaAtual < termosEntropiaLinha.size())){
				
			linhaAtual = termosEntropiaLinha.get(numLinhaAtual);
			tokens = linhaAtual.split(";");
			termo = tokens[0];
			
			if (termoCategoria.containsKey(termo)){
				categoria = termoCategoria.get(termo);
				
				if (categoriaAchadas.containsKey(categoria)){
					faltam = categoriaAchadas.get(categoria);
					if(faltam == 1 ){
						categoriaAchadas.remove(categoria);
					} else {
						categoriaAchadas.put(categoria, faltam-1);
					}
				}
			}

			if (categoriaTermos.containsKey(categoria)){
				termosCategoriaLida = categoriaTermos.get(categoria);
			} else {
				termosCategoriaLida = new ArrayList<String>();
			}
			termosCategoriaLida.add(termo);
			categoriaTermos.put(categoria, termosCategoriaLida);
			
			numLinhaAtual = numLinhaAtual + 1;
		}
		
		int minEncontradas = topN;
		List<String> termoCategoriaTopN = new ArrayList<String>();
		
		if(balanceado){
			
			//se percorreu o arquivo inteiro, eh porque algum categoria nao alcancou as topN palavras, nesse caso trunca pela menor
			if (numLinhaAtual == termosEntropiaLinha.size()){
				for(Map.Entry<String, Integer> entry: categoriaAchadas.entrySet()){
					if(entry.getValue() < minEncontradas){
						minEncontradas = topN - entry.getValue();
					}
				}
			}
		}
		
		//pega uma palavra de cada categoria ate que sejam adicionadas minEncontradas
		for (int i = 0; i < minEncontradas; i++) {
			for ( Map.Entry<String, List<String>> entry : categoriaTermos.entrySet()) {
				if(entry.getValue().size() > i){
					termoCategoriaTopN.add(entry.getValue().get(i)+";"+entry.getKey());
				}
			}
		}
		
		ArquivoUtils.salvaArquivo(termoCategoriaTopN, termoCategoriaTXT);
		System.out.println("Foram encontradas "+minEncontradas+" palavras para cada categoria.");
		
		
	}

	private Map<String, String> loadTermoCategoria() {
		
		Map<String, String> termoCategoria = new HashMap<String, String>();
		
		List<String> dicionarioTermoCategoriaLinhas = ArquivoUtils.abreArquivo(termosCategoriaFull);
		String[] tokens;
		for (String linha : dicionarioTermoCategoriaLinhas) {
			tokens = linha.split(";");
			termoCategoria.put(tokens[0], tokens[1]);
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

	public void rodaHeuristica() {
		
		HashMap<String, String> termosCategorias = getTermoCategoriaTopN();
		
		List<String> docs = ArquivoUtils.abreArquivo(baseDocumentos);
		
		List<String> categorias = new ArrayList<String>();
		
		for (String doc : docs) {
			if (doc.trim().length() > 0){
				String categoria = verificaCategoria(doc, termosCategorias);
				categorias.add(categoria);
			}
			
		}
				
		ArquivoUtils.salvaArquivo(categorias, categoriasEncontradas);
		System.out.println("Arquivo "+categoriasEncontradas+" salvo com sucesso.");
	}

	private String verificaCategoria(String doc, HashMap<String, String> termosCategorias) {
		
		//separa lista de termos presentes na heuristica
		Set<String> termosHeuristica = termosCategorias.keySet();
		
		doc = doc.toLowerCase().replaceAll("[.,:;<>{}|_1234567890!@#$%&*()/?+=-]", " ");
		//pega do documento frequencia referente apenas aos termos da heuristica
		Map<String, Integer> termosFrequenciaDocumento = ContaPalavras.contaFrequenciaNgrama(doc, 0, termosHeuristica, 1);
		
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

	private HashMap<String, String> getTermoCategoriaTopN() {
		
		HashMap<String, String> termosCategorias = new HashMap<String, String>();
		List<String> linhas = ArquivoUtils.abreArquivo(termoCategoriaTXT);
		for (String linha : linhas) {
			String[] termoCategoria = linha.split(";");
			termosCategorias.put(termoCategoria[0],termoCategoria[1]);
						
		}
	
		return termosCategorias;
	}
	
	
	
	
}
