package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.ContaPalavras;

public class ProcessaEstrutura {
	
	private Map<String, List<String>> estruturaDocumentos;
//	private Map<String, List<String>> categoriasDocumentos;
	
	public ProcessaEstrutura(Map<String, List<String>> estruturaDocumentos) {
		super();
		this.estruturaDocumentos = estruturaDocumentos;
		//this.categoriasDocumentos = categoriasDocumentos;
	}
	
//	public HashMap<String, Map<String,Integer>> getTermosClustersFrequencias(int ngrama){
//		return getTermosEstruturasFrequencias(estruturaDocumentos, ngrama);
//	}

//	public HashMap<String, Map<String,Integer>> getTermosCategoriasFrequencias(int ngrama){
//		return getTermosEstruturasFrequencias(categoriasDocumentos, ngrama);
//	}
	
//	public Map<String, Map<String, Integer>> getTermosClustersPresencas(int ngrama) {
//		return getTermosEstruturaPresencas(estruturaDocumentos, ngrama);
//	}
	
//	public Map<String, Map<String, Integer>> getTermosCategoriasPresencas(int ngrama) {
//		return getTermosEstruturaPresencas(categoriasDocumentos, ngrama);
//	}

	public HashMap<String, Map<String,Integer>> getTermosEstruturasFrequencias(int ngrama){
		
		Map<String, Map<String, Integer>> mapCategoriasTermosFrequencia = getEstruturasTermosFrequencias(ngrama);	
		return inverteEstruturaTermosToTermosEstrutura(mapCategoriasTermosFrequencia);
	}
	
	public HashMap<String, Map<String,Integer>> getTermosEstruturaPresencas(int ngrama){
		
		Map<String, Map<String,Integer>> mapEstruturaTermosPresenca = getEstruturaTermosPresencas(ngrama);
		return inverteEstruturaTermosToTermosEstrutura(mapEstruturaTermosPresenca);
	}
	
//	public Map<String, Integer> getClustersNumDocs() {
//		return getEstruturasNumDocs(estruturaDocumentos);
//	}

//	public Map<String, Integer> getCategoriasNumDocs() {
//		return getEstruturasNumDocs(categoriasDocumentos);
//	}
	
	private HashMap<String, Map<String,Integer>> inverteEstruturaTermosToTermosEstrutura(Map<String, Map<String, Integer>> mapEstruturaTermosOcorrencia) {
		
		HashMap<String, Map<String,Integer>> mapTermosEstruturaOcorrencia = new HashMap<String, Map<String, Integer>>();
		
		//inverte o hashMap de categoriaTermoFrequencia para termoCategoriaFrequencia
		Map<String, Integer> mapTermosOcorrencia;
		Map<String, Integer> mapEstruturaOcorrencia = new HashMap<String, Integer>();
		
		String termoAtual;
		String estruturaAtual;
		int ocorrenciaAtual;
		
		for ( Entry<String, Map<String,Integer>>  mapEstruturaTermoOcorrencia : mapEstruturaTermosOcorrencia.entrySet()){
			
			estruturaAtual = mapEstruturaTermoOcorrencia.getKey();
			mapTermosOcorrencia = mapEstruturaTermoOcorrencia.getValue();
			for (Map.Entry<String, Integer> mapTermoOcorrencia : mapTermosOcorrencia.entrySet()) {
				
				termoAtual = mapTermoOcorrencia.getKey();
				ocorrenciaAtual = mapTermoOcorrencia.getValue();
				
				if(mapTermosEstruturaOcorrencia.containsKey(termoAtual)){
					mapEstruturaOcorrencia = mapTermosEstruturaOcorrencia.get(termoAtual);
					mapEstruturaOcorrencia.put(estruturaAtual, ocorrenciaAtual);
				} else {
					mapEstruturaOcorrencia = new HashMap<String, Integer>();
					mapEstruturaOcorrencia.put(estruturaAtual, ocorrenciaAtual);
					mapTermosEstruturaOcorrencia.put(termoAtual, mapEstruturaOcorrencia);
				}
			}
				
		}
		
		return mapTermosEstruturaOcorrencia;
	}
	
	private Map<String, Map<String, Integer>> getEstruturasTermosFrequencias(int ngrama) {
		
		HashMap<String, Map<String,Integer>> mapEstruturaTermosFrequencia = new HashMap<String, Map<String, Integer>>();
		
		Map<String,Integer> termosPresencas;
		String estruturaId;
		for (Entry<String, List<String>> estrutura : this.estruturaDocumentos.entrySet()){
			estruturaId = estrutura.getKey();
			termosPresencas = ContaPalavras.contaFrequenciaNgrama(estrutura.getValue(), 0, null, ngrama);
			mapEstruturaTermosFrequencia.put(estruturaId, termosPresencas);
		}
		return mapEstruturaTermosFrequencia;
	}
	
	private  Map<String, Map<String, Integer>> getEstruturaTermosPresencas(int ngrama) {
		
		HashMap<String, Map<String,Integer>> mapEstruturaTermosPresenca = new HashMap<String, Map<String, Integer>>();
		
		Map<String,Integer> termosPresencas;
		String estruturaId;
		for (Entry<String, List<String>> estrutura : this.estruturaDocumentos.entrySet()){
			estruturaId = estrutura.getKey();
			termosPresencas = ContaPalavras.contaPresencaNgrama(estrutura.getValue(), 0, null, ngrama);
			mapEstruturaTermosPresenca.put(estruturaId, termosPresencas);
		}
		return mapEstruturaTermosPresenca;
	}
	
	public Map<String, Integer> getEstruturasNumDocs() {
	
		Map<String, Integer> categoriaNumDocs = new HashMap<String, Integer>();
		for (Entry<String, List<String>> categoria : this.estruturaDocumentos.entrySet()){
			categoriaNumDocs.put(categoria.getKey(), categoria.getValue().size());
		}
		return categoriaNumDocs;
	}
	
}
