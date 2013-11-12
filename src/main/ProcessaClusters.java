package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.ContaPalavras;

public class ProcessaClusters {
	
	private Map<String, List<String>> clustersDocumentos;
	private Map<String, List<String>> categoriasDocumentos;
	private Map<String, String> clusterCategoria;
	
	public ProcessaClusters(Map<String, List<String>> clustersDocumentos, Map<String, List<String>> categoriasDocumentos,
			Map<String, String> clusterCategoria) {
		super();
		this.clustersDocumentos = clustersDocumentos;
		this.categoriasDocumentos = categoriasDocumentos;
		this.clusterCategoria = clusterCategoria;
	}
	
	public HashMap<String, Map<String,Integer>> getTermosClustersFrequencias(){
		
		HashMap<String, Map<String,Integer>> mapTermosClustersFrequencia = new HashMap<String, Map<String, Integer>>();
		
		HashMap<String, Map<String,Integer>> mapClustersTermosFrequencia = getClustersTermosFrequencias();
		
		//inverte o hashMap de categoriaTermoFrequencia para termoCategoriaFrequencia
		Map<String, Integer> mapTermosFrequencias;
		Map<String, Integer> mapClustersFrequencias = new HashMap<String, Integer>();
		
		String termoAtual;
		String clusterAtual;
		int frequenciaAtual;
		
		for ( Entry<String, Map<String,Integer>>  mapClusterTermoFrequencia : mapClustersTermosFrequencia.entrySet()){
			
			clusterAtual = mapClusterTermoFrequencia.getKey();
			mapTermosFrequencias = mapClusterTermoFrequencia.getValue();
			for (Map.Entry<String, Integer> mapTermoFrequencia : mapTermosFrequencias.entrySet()) {
				
				termoAtual = mapTermoFrequencia.getKey();
				frequenciaAtual = mapTermoFrequencia.getValue();
				
				if(mapTermosClustersFrequencia.containsKey(termoAtual)){
					mapClustersFrequencias = mapTermosClustersFrequencia.get(termoAtual);
					mapClustersFrequencias.put(clusterAtual, frequenciaAtual);
				} else {
					mapClustersFrequencias = new HashMap<String, Integer>();
					mapClustersFrequencias.put(clusterAtual, frequenciaAtual);
					mapTermosClustersFrequencia.put(termoAtual, mapClustersFrequencias);
				}
			}
				
		}
		
		return mapTermosClustersFrequencia;
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
	
	public HashMap<String, Map<String,Integer>> getTermosEstruturaPresencas(Map<String, List<String>> estruturaDocumentos){
		
		HashMap<String, Map<String,Integer>> mapTermosEstruturaPresenca = new HashMap<String, Map<String, Integer>>();
		
		HashMap<String, Map<String,Integer>> mapEstruturaTermosPresenca = getEstruturaTermosPresencas(estruturaDocumentos);
		
		//inverte o hashMap de categoriaTermoFrequencia para termoCategoriaFrequencia
		Map<String, Integer> mapTermosPresenca;
		Map<String, Integer> mapEstruturaPresencas = new HashMap<String, Integer>();
		
		String termoAtual;
		String estruturaAtual;
		int presencaAtual;
		
		for ( Entry<String, Map<String,Integer>>  mapEstruturaTermoPresenca : mapEstruturaTermosPresenca.entrySet()){
			
			estruturaAtual = mapEstruturaTermoPresenca.getKey();
			mapTermosPresenca = mapEstruturaTermoPresenca.getValue();
			for (Map.Entry<String, Integer> mapTermoPresenca : mapTermosPresenca.entrySet()) {
				
				termoAtual = mapTermoPresenca.getKey();
				presencaAtual = mapTermoPresenca.getValue();
				
				if(mapTermosEstruturaPresenca.containsKey(termoAtual)){
					mapEstruturaPresencas = mapTermosEstruturaPresenca.get(termoAtual);
					mapEstruturaPresencas.put(estruturaAtual, presencaAtual);
				} else {
					mapEstruturaPresencas = new HashMap<String, Integer>();
					mapEstruturaPresencas.put(estruturaAtual, presencaAtual);
					mapTermosEstruturaPresenca.put(termoAtual, mapEstruturaPresencas);
				}
			}
				
		}
		
		return mapTermosEstruturaPresenca;
	}
	
	
	
	public HashMap<String, Map<String,Integer>> getClustersTermosFrequencias(){
				
		HashMap<String, Map<String,Integer>> mapClustersTermosFrequencia = new HashMap<String, Map<String, Integer>>();
		
		Map<String,Integer> termosFrequencias;
		String clusterId;
		for (Entry<String, List<String>> cluster : clustersDocumentos.entrySet()){
			
			clusterId = cluster.getKey();
			termosFrequencias = ContaPalavras.contaFrequencia(cluster.getValue(), 0, null);
			mapClustersTermosFrequencia.put(clusterId, termosFrequencias);
		}
		return mapClustersTermosFrequencia;
		
	}

	public HashMap<String, Map<String,Integer>> getCategoriasTermosFrequencias(){
		
		HashMap<String, Map<String,Integer>> mapCategoriasTermosFrequencia = new HashMap<String, Map<String, Integer>>();
		
		Map<String,Integer> termosFrequencias;
		String categoriaId;
		for (Entry<String, List<String>> categoria : categoriasDocumentos.entrySet()){
			
			categoriaId = categoria.getKey();
			termosFrequencias = ContaPalavras.contaFrequencia(categoria.getValue(), 0, null);
			mapCategoriasTermosFrequencia.put(categoriaId, termosFrequencias);
		}
		return mapCategoriasTermosFrequencia;
		
	}
	
	private Map<String, Map<String, Integer>> getTermosEstruturaFrequencias(Map<String, List<String>> estruturaDocumentos) {
		
		HashMap<String, Map<String,Integer>> mapEstruturaTermosFrequencia = new HashMap<String, Map<String, Integer>>();
		
		Map<String,Integer> termosPresencas;
		String estruturaId;
		for (Entry<String, List<String>> estrutura : estruturaDocumentos.entrySet()){
			estruturaId = estrutura.getKey();
			termosPresencas = ContaPalavras.contaFrequencia(estrutura.getValue(), 0, null);
			mapEstruturaTermosFrequencia.put(estruturaId, termosPresencas);
		}
		return mapEstruturaTermosFrequencia;
	}
	
	public Map<String, Map<String, Integer>> getTermosClustersPresencas() {
		return getTermosEstruturaPresencas(clustersDocumentos);
	}
	
	public Map<String, Map<String, Integer>> getTermosCategoriasPresencas() {
		return getTermosEstruturaPresencas(categoriasDocumentos);
	}

	private  HashMap<String, Map<String, Integer>> getEstruturaTermosPresencas(Map<String, List<String>> estruturaDocumentos) {
		
		HashMap<String, Map<String,Integer>> mapEstruturaTermosPresenca = new HashMap<String, Map<String, Integer>>();
		
		Map<String,Integer> termosPresencas;
		String estruturaId;
		for (Entry<String, List<String>> estrutura : estruturaDocumentos.entrySet()){
			estruturaId = estrutura.getKey();
			termosPresencas = ContaPalavras.contaPresenca(estrutura.getValue(), 0, null);
			mapEstruturaTermosPresenca.put(estruturaId, termosPresencas);
		}
		return mapEstruturaTermosPresenca;
	}

	
	
	public Map<String, Integer> getClustersNumDocs() {
		
		Map<String, Integer> clusterNumDocs = new HashMap<String, Integer>();
		for (Entry<String, List<String>> cluster : clustersDocumentos.entrySet()){
			clusterNumDocs.put(cluster.getKey(), cluster.getValue().size());
		}
		return clusterNumDocs;
	}

	public Map<String, Integer> getCategoriasNumDocs() {
		
		Map<String, Integer> categoriaNumDocs = new HashMap<String, Integer>();
		for (Entry<String, List<String>> categoria : categoriasDocumentos.entrySet()){
			categoriaNumDocs.put(categoria.getKey(), categoria.getValue().size());
		}
		return categoriaNumDocs;
	}
	
}
