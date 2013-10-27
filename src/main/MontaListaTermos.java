package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Termo;

public class MontaListaTermos {

	private List<Termo> termos;
	private Map<String, String> mapTermosCategorias;
	private String[] categorias;

	
	public MontaListaTermos(List<Termo> termos, Map<String, String> mapTermosCategorias, String[] categorias) {
		this.termos = termos;
		this.mapTermosCategorias = mapTermosCategorias;
		this.categorias = categorias;
	}

	public Map<String, String> getTopNTermos(int topN, boolean balanceado) {
		
		Map<String, Integer> mapCategoriaAchadas = loadCategoriaAchadas(topN);
		Map<String,List<String>> mapCategoriaTermos = new HashMap<String, List<String>>();
		List<String> termosCategoriaLida = new ArrayList<String>();
		
		String termo = "";
		Termo termoAtual;
		String categoria = "";
		int numTermoAtual = 0;
		int faltam = topN;
		
		//le os termos atŽ que todas as categorias tenham no minimo topN termos ou ate que todos os termos tenham sido lidos
		while((mapCategoriaAchadas.size()>0) && (numTermoAtual < termos.size())){
				
			termoAtual = termos.get(numTermoAtual);
			termo = termoAtual.getTermo();
			
			if (mapTermosCategorias.containsKey(termo)){
				categoria = mapTermosCategorias.get(termo);
				
				if (mapCategoriaAchadas.containsKey(categoria)){
					faltam = mapCategoriaAchadas.get(categoria);
					if(faltam == 1 ){
						mapCategoriaAchadas.remove(categoria);
					} else {
						mapCategoriaAchadas.put(categoria, faltam-1);
					}
				}
			}

			if (mapCategoriaTermos.containsKey(categoria)){
				termosCategoriaLida = mapCategoriaTermos.get(categoria);
			} else {
				termosCategoriaLida = new ArrayList<String>();
			}
			termosCategoriaLida.add(termo);
			mapCategoriaTermos.put(categoria, termosCategoriaLida);
			
			numTermoAtual = numTermoAtual + 1;
		}
		
		int minEncontradas = topN;
		Map<String,String> topNTermoCategoria = new HashMap<String,String>();

		if(balanceado){
			
			//se percorreu o arquivo inteiro, eh porque algum categoria nao alcancou as topN palavras, nesse caso trunca pela menor
			if (numTermoAtual == termos.size()){
				for(Map.Entry<String, Integer> entry: mapCategoriaAchadas.entrySet()){
					if(entry.getValue() < minEncontradas){
						minEncontradas = topN - entry.getValue();
					}
				}
			}
		}
		
		//pega uma palavra de cada categoria ate que sejam adicionadas minEncontradas
		for (int i = 0; i < minEncontradas; i++) {
			for ( Map.Entry<String, List<String>> entry : mapCategoriaTermos.entrySet()) {
				if(entry.getValue().size() > i){
					topNTermoCategoria.put(entry.getValue().get(i),entry.getKey());
				}
			}
		}
		
		return topNTermoCategoria;
	}
	
	private Map<String, Integer> loadCategoriaAchadas(int topN) {
		
		Map<String, Integer> categoriaAchadas = new HashMap<String, Integer>();
		for (int i = 0; i < categorias.length; i++) {
			categoriaAchadas.put(categorias[i], topN);
		}
		return categoriaAchadas;
		
	}
	

}
