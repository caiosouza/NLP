package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.ContaPalavras;

public class ProcessaClusters {
	
	private Map<String, List<String>> clusters;
	private Map<String, String> clusterCategoria;
	
	public ProcessaClusters(Map<String, List<String>> clusters, Map<String, String> clusterCategoria) {
		super();
		this.clusters = clusters;
		this.clusterCategoria = clusterCategoria;
	}
	
	public HashMap<String, Map<String,Integer>> getTermosCategoriasFrequencias(){
		
		HashMap<String, Map<String,Integer>> mapTermosCategoriasFrequencia = new HashMap<String, Map<String, Integer>>();
		
		HashMap<String, Map<String,Integer>> mapCategoriasTermosFrequencia = getCategoriasTermosFrequencias();
		
		//inverte o hashMap de categoriaTermoFrequencia para termoCategoriaFrequencia
		Map<String, Integer> mapTermosFrequencias;
		Map<String, Integer> mapCategoriasFrequencias = new HashMap<String, Integer>();
		
		String termoAtual;
		String categoriaAtual;
		int frequenciaAtual;
		
		for ( Entry<String, Map<String,Integer>>  mapCategoriaTermoFrequencia : mapCategoriasTermosFrequencia.entrySet()){
			
			categoriaAtual = mapCategoriaTermoFrequencia.getKey();
			mapTermosFrequencias = mapCategoriaTermoFrequencia.getValue();
			for (Map.Entry<String, Integer> mapTermoFrequencia : mapTermosFrequencias.entrySet()) {
				
				termoAtual = mapTermoFrequencia.getKey();
				frequenciaAtual = mapTermoFrequencia.getValue();
				
				if(mapTermosCategoriasFrequencia.containsKey(termoAtual)){
					mapCategoriasFrequencias = mapTermosCategoriasFrequencia.get(termoAtual);
					mapCategoriasFrequencias.put(categoriaAtual, frequenciaAtual);
				} else {
					mapCategoriasFrequencias = new HashMap<String, Integer>();
					mapCategoriasFrequencias.put(categoriaAtual, frequenciaAtual);
					mapTermosCategoriasFrequencia.put(termoAtual, mapCategoriasFrequencias);
				}
			}
				
		}
		
		return mapTermosCategoriasFrequencia;
	}
	
	public HashMap<String, Map<String,Integer>> getCategoriasTermosFrequencias(){
				
		HashMap<String, Map<String,Integer>> mapCategoriasTermosFrequencia = new HashMap<String, Map<String, Integer>>();
		
		Map<String,Integer> termosFrequencias;
		String categoria;
		for (Entry<String, List<String>> cluster : clusters.entrySet()){
			
			categoria = clusterCategoria.get(cluster.getKey());
			termosFrequencias = ContaPalavras.contaFrequencia(cluster.getValue(), 0, null);
			mapCategoriasTermosFrequencia.put(categoria, termosFrequencias);
		}
		return mapCategoriasTermosFrequencia;
		
	}

	public Map<String, Integer> getCategoriasNumDocs() {
		
		Map<String, Integer> categoriaNumDocs = new HashMap<String, Integer>();
		for (Entry<String, List<String>> cluster : clusters.entrySet()){
			categoriaNumDocs.put(cluster.getKey(), cluster.getValue().size());
		}
		return categoriaNumDocs;
	}

}
