package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.ContaPalavras;

public class RodaHeuristica {

	private List<String> documentos;
	private Map<String, String> topNTermosCategorias;
	
	public RodaHeuristica(List<String> documentos2, Map<String, String> topNTermosCategorias) {
		this.documentos = documentos2;
		this.topNTermosCategorias = topNTermosCategorias;
	}

	public List<String> exec(int numHeuristica) {
		
		List<String> categoriasEncontradas = new ArrayList<String>();
		
		if (numHeuristica == 0){
			categoriasEncontradas = rodaHeuristica0();
		}
		
		return categoriasEncontradas;
	}

	private List<String> rodaHeuristica0() {
		
		List<String> categoriasEncontradas = new ArrayList<String>();
		for (String documento : documentos) {
			categoriasEncontradas.add(verificaCategoria0(documento, topNTermosCategorias));
		}
		return categoriasEncontradas;
	}
	
	private String verificaCategoria0(String doc, Map<String, String> termosCategorias) {
		
		//separa lista de termos presentes na heuristica
		Set<String> termosHeuristica = termosCategorias.keySet();
		
		doc = doc.toLowerCase().replaceAll("[.,:;<>{}|_1234567890!@#$%&*()/?+=-]", " ");
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

	
	

}
