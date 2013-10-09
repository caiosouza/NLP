package entities;

import java.util.Map;

public class DocFrequencia {

	private String documento;
	private String categoria;
	private Map<String,Integer> termoFrequencia;
	
	public DocFrequencia(String documento, String categoria,
			Map<String, Integer> termoFrequencia) {
		this.setDocumento(documento);
		this.setCategoria(categoria);
		this.setTermoFrequencia(termoFrequencia);
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Map<String, Integer> getTermoFrequencia() {
		return termoFrequencia;
	}

	public void setTermoFrequencia(Map<String, Integer> termoFrequencia) {
		this.termoFrequencia = termoFrequencia;
	}
	
}
