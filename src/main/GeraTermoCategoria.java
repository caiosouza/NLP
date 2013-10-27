package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GeraTermoCategoria {
	
	private Map<String,Map<String,Integer>> mapTermosCategoriasFrequencias;
	private Map<String,Integer> mapCategoriaNumDocs;

	public GeraTermoCategoria(Map<String, Map<String, Integer>> mapTermosCategoriasFrequencias, Map<String, Integer> mapCategoriaNumDocs) {
		this.mapTermosCategoriasFrequencias = mapTermosCategoriasFrequencias;
		this.mapCategoriaNumDocs = mapCategoriaNumDocs;
	}
	
	public Map<String,String> getTermosCategorias(int code){
		
		Map<String,String> termosCategorias = new HashMap<String, String>();
		
		//retorna a categoria referente ao cluster de maior frequencia
		if(code == 0){
			
			termosCategorias = getCategoriaFrequenciaAbsoluta();
		} 
		//retorna a categoria referente ao cluster de maior frequencia relativa
		else if (code == 1){
			termosCategorias = getCategoriaFrequenciaRelativa();
		}
		
		return termosCategorias;
		
	}

	private Map<String, String> getCategoriaFrequenciaRelativa() {
		
		Map<String,String> termosCategorias = new HashMap<String, String>();
		Map<String,Integer> atualCategoriasFrequencias;
		String categoriaPrincipal = "";
		Double maiorFrequencia = 0.0;
		int numDocsCategoriaAtual = 0;
		Double frequenciaRelativa = 0.0;
		
		for ( Entry<String,Map<String,Integer>> termoCategoriasFrequencias : mapTermosCategoriasFrequencias.entrySet()) {
			
			atualCategoriasFrequencias = termoCategoriasFrequencias.getValue();
			
			for (Entry<String, Integer> categoriaFrequencia : atualCategoriasFrequencias.entrySet()) {
			
				numDocsCategoriaAtual = mapCategoriaNumDocs.get(categoriaFrequencia.getKey());
				frequenciaRelativa = 1.0* (categoriaFrequencia.getValue()/numDocsCategoriaAtual);
				
				if (frequenciaRelativa > maiorFrequencia){
					maiorFrequencia = frequenciaRelativa;
					categoriaPrincipal = categoriaFrequencia.getKey();
				}
			
			}
			termosCategorias.put(termoCategoriasFrequencias.getKey(), categoriaPrincipal);
		}
		
		return termosCategorias;
	}

	private Map<String, String> getCategoriaFrequenciaAbsoluta() {
		
		Map<String,String> termosCategorias = new HashMap<String, String>();
		Map<String,Integer> atualCategoriasFrequencias;
		String categoriaPrincipal;
		int maiorFrequencia;
		
		for ( Entry<String,Map<String,Integer>> termoCategoriasFrequencias : mapTermosCategoriasFrequencias.entrySet()) {
			
			atualCategoriasFrequencias = termoCategoriasFrequencias.getValue();
			categoriaPrincipal = "";
			maiorFrequencia = 0;
			
			for (Entry<String, Integer> categoriaFrequencia : atualCategoriasFrequencias.entrySet()) {
			
				if (categoriaFrequencia.getValue() > maiorFrequencia){
					maiorFrequencia = categoriaFrequencia.getValue();
					categoriaPrincipal = categoriaFrequencia.getKey();
				}
			
			}
			termosCategorias.put(termoCategoriasFrequencias.getKey(), categoriaPrincipal);
		}
		
		return termosCategorias;
	}

	
}
