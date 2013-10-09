package entities;

import java.util.List;

public class TF_IDF {
	
	//private int[][] matriz;
	private List<String> matriz;
	private String[] cabecalho;
	private String[] categorias;
	private String[] documentos;
	
	public TF_IDF(List<String> matriz, String[] cabecalho, String[] categorias,
			String[] documentos) {
		this.matriz = matriz;
		this.cabecalho = cabecalho;
		this.categorias = categorias;
		this.documentos = documentos;
	}

	public List<String> getMatriz() {
		return matriz;
	}

	public void setMatriz(List<String> matriz) {
		this.matriz = matriz;
	}

	public String[] getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String[] cabecalho) {
		this.cabecalho = cabecalho;
	}

	public String[] getCategorias() {
		return categorias;
	}

	public void setCategorias(String[] categorias) {
		this.categorias = categorias;
	}

	public String[] getDocumentos() {
		return documentos;
	}

	public void setDocumentos(String[] documentos) {
		this.documentos = documentos;
	}
	
	

}
