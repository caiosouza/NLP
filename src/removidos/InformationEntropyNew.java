package removidos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.CarregaDados;

import utils.ArquivoUtils;
import utils.ContaPalavras;

import entities.Cluster;
import entities.Termo;

public class InformationEntropyNew {
	
	/*arquivos saida*/
	private String termosEntropiaTXT;
	
	/*arquivos entrada*/
	private String clusterOrderFile;
    private String allTextBaseTxtFile;
    private String clusterCategoriaFileName;

	public InformationEntropyNew(String termosEntropiaTXT, String clusterOrderFile, String allTextBaseTxtFile) {
		this.termosEntropiaTXT = termosEntropiaTXT;
		this.clusterOrderFile = clusterOrderFile;
		this.allTextBaseTxtFile = allTextBaseTxtFile;
	}

	public void exec(){
		
		//pega o cluster e uma lista de documentos
		CarregaDados load = new CarregaDados(clusterOrderFile, allTextBaseTxtFile, clusterCategoriaFileName, allTextBaseTxtFile);
		Map<String, List<String>> clustersDocumentos = load.carregaClusters();
		
		
		Map<String, Termo> termos = montaTermos(clustersDocumentos);
		
		//calcula entropia de todos os termos
		Termo termoAtual;
		for(Entry<String, Termo> termo : termos.entrySet()){
			termoAtual = termo.getValue();
			termoAtual.calculaEntropiaF();
			termoAtual.defineCategoria();
		}
		
		/*
		 * Pega todos os arquivos usados que estão em um txt que junta todos os documentos
		 * Pega os clusteres geredos {pelo kmeans no MatLab, clusteres originais}
		 * Junta essas duas informações e consolida no arquivo termosEntropiaTXT
		 */
		//calcEntropias(categoriasTermosFrequencia, termosEntropiaTXT);
		
	}
	
	private Map<String, Termo> montaTermos(Map<String, List<String>> clustersDocumentos){
		
 		Map<String, Termo> termos = new HashMap<String, Termo>();
		Termo termoAtual;
		String nomeTermo;
		String clusterAtual;
		
		for(Entry<String, List<String>> clusterDocumento : clustersDocumentos.entrySet() ){
			clusterAtual = clusterDocumento.getKey();
			//pega a frequencia de todos os termos daquela categoria
			Map<String, Integer> atualTermosFrequencias = ContaPalavras.contaFrequencia(clusterDocumento.getValue(), 0, null);
			
			for(Entry<String, Integer> termoFrequencia: atualTermosFrequencias.entrySet()){
				
				nomeTermo = termoFrequencia.getKey();
				if(termos.containsKey(termoFrequencia.getKey())){
					termoAtual = termos.get(termoFrequencia.getKey());
				} else {
					termoAtual = new Termo(nomeTermo);
				}
				termoAtual.adicionaFrequencia(clusterAtual,termoFrequencia.getValue());
				termos.put(nomeTermo, termoAtual);
			}
		}
		
		return termos;
				
	}

	private Map<String, List<String>> getClustersDocumentos() {
		
		Map<String, List<String>> clusters = new HashMap<String, List<String>>();
		
		//pega a lista de clusters
		List<String> ordemClusters = ArquivoUtils.abreArquivo(clusterOrderFile);
		List<String> documentos = ArquivoUtils.abreArquivo(allTextBaseTxtFile);
		
		List<String> docsAtuais = new ArrayList<String>();
		String clusterAtual = "";
		
		for (int i = 0; i < ordemClusters.size(); i++) {
			clusterAtual = ordemClusters.get(i);
			if (clusters.containsKey(clusterAtual)){
				docsAtuais = clusters.get(clusterAtual);
			}	
			docsAtuais.add(documentos.get(i*2).toLowerCase().replaceAll("[.,:;<>{}|_1234567890!@#$%&*()/?+=-]", " "));
			clusters.put(ordemClusters.get(i), docsAtuais);
		}
		
		return clusters;
	}

}
