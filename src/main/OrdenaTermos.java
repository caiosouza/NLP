package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entities.Termo;

public class OrdenaTermos {

	Map<String, Map<String, Integer>> mapTermosClustersFrequencias;
	
	public OrdenaTermos(Map<String, Map<String, Integer>> mapTermosClustersFrequencias) {
		this.mapTermosClustersFrequencias = mapTermosClustersFrequencias;
	}
	
	public List<Termo> getTermosOrdenados(int tipoOrdenacao){
		
		List<Termo> termosOrdenados = new ArrayList<Termo>();
		
		if(tipoOrdenacao == 0){
			termosOrdenados = getTermosByFrequenciaEntropiaZero();
		}
		else if (tipoOrdenacao == 1){
			termosOrdenados = getTermosByLogFrequenciaEntropia();
		}
		
		return termosOrdenados;
		
	}

	private List<Termo> getTermosByLogFrequenciaEntropia() {
		
		List<Termo> termosOrdenados = new ArrayList<Termo>();
		Termo termoAtual;
		for(Entry<String, Map<String, Integer>> mapTermoClusterFrequencia: mapTermosClustersFrequencias.entrySet()){
			
			termoAtual = new Termo(mapTermoClusterFrequencia.getKey());
			termoAtual.setClustersFrequencia(mapTermoClusterFrequencia.getValue());
			termoAtual.init();
			termosOrdenados.add(termoAtual);
		}
	
		Collections.sort(termosOrdenados, 
				new Comparator<Termo>(){
					public int compare(Termo t1,Termo t2){return t2.getFormulaScoreF().compareTo(t1.getFormulaScoreF());}
				}
		);
		
		return termosOrdenados;
	}

	private List<Termo> getTermosByFrequenciaEntropiaZero() {
	
		List<Termo> termosOrdenados = new ArrayList<Termo>();
		Termo termoAtual;
		for(Entry<String, Map<String, Integer>> mapTermoClusterFrequencia: mapTermosClustersFrequencias.entrySet()){
			
			termoAtual = new Termo(mapTermoClusterFrequencia.getKey());
			termoAtual.setClustersFrequencia(mapTermoClusterFrequencia.getValue());
			termoAtual.init();
			if(termoAtual.getEntropiaF().equals(0.0)){
				termosOrdenados.add(termoAtual);
			}
		}
	
		Collections.sort(termosOrdenados, 
				new Comparator<Termo>(){
					public int compare(Termo t1,Termo t2){return t2.getFormulaScoreF().compareTo(t1.getFormulaScoreF());}
				}
		);
	
		return termosOrdenados;	
	}

}
