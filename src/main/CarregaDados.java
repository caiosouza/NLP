package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import utils.ArquivoUtils;



public class CarregaDados {

	/*arquivos saida*/
	//private String termosEntropiaTXT;
	
	/*arquivos entrada*/
	private String clusterOrderFileName;
    private String allTextBaseTxtFileName;
    private String clusterCategoriaFileName;
    private String categoriasCorretasFileName;
    private List<String> documentos;
	
	public CarregaDados(String clusterOrderFileName, String allTextBaseTxtFileName, String clusterCategoriaFileName, String categoriasCorretasFileName) {
		this.clusterOrderFileName = clusterOrderFileName;
		this.allTextBaseTxtFileName = allTextBaseTxtFileName;
		this.clusterCategoriaFileName = clusterCategoriaFileName;
		this.categoriasCorretasFileName = categoriasCorretasFileName;
		this.documentos = new ArrayList<String>();
	}



	public Map<String, List<String>> carregaClusters() {
		
		Map<String, List<String>> clusters = new HashMap<String, List<String>>();
		
		//pega a lista de clusters
		List<String> ordemClusters = ArquivoUtils.abreArquivo(clusterOrderFileName);
		List<String> documentos = ArquivoUtils.abreArquivo(allTextBaseTxtFileName);
		
		documentos = loadDocumentos(documentos);
		setDocumentos(documentos);
		List<String> docsAtuais = new ArrayList<String>();
		String clusterAtual = "";
		
		for (int i = 0; i < ordemClusters.size(); i++) {
			clusterAtual = ordemClusters.get(i);
			if (clusters.containsKey(clusterAtual)){
				docsAtuais = clusters.get(clusterAtual);
			} else {
				docsAtuais = new ArrayList<String>();
			}
			
			docsAtuais.add(documentos.get(i).toLowerCase().replaceAll("[.,:;<>{}|_1234567890!@#$%&*()/?+=-]", " "));
			clusters.put(ordemClusters.get(i), docsAtuais);
		}
		
		return clusters;
	}



	private List<String> loadDocumentos(List<String> documentos) {
		
		List<String> docs = new ArrayList<String>();
		for (int i = 0; i < documentos.size(); i= i+2) {
			docs.add(documentos.get(i));
		}
		return docs;
	}



	public Map<String, String> carregaClusterCategoria() {
		
		Map<String, String> clusterCategoriaMap = new HashMap<String, String>();
		List<String> linhasArquivo = ArquivoUtils.abreArquivo(clusterCategoriaFileName);
		
		String[] tokens;
		for (String linha : linhasArquivo) {
			tokens = linha.split(";");
			clusterCategoriaMap.put(tokens[0],tokens[1]);
		}

		return clusterCategoriaMap;
	}

	public List<String> getDocumentos() {
		return this.documentos;
	}
	
	public void setDocumentos(List<String> documentos) {
		this.documentos = documentos;
	}

	public List<String> carregaCategoriasCorretas() {
		
		List<String> categoriasCorretas = ArquivoUtils.abreArquivo(categoriasCorretasFileName);
		return categoriasCorretas;
	}
}
