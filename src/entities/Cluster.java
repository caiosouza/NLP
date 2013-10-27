package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cluster {

	private String nome;
	private List<String> documentos;
	private Map<Termo,Integer> termosFrequencia;
	
	
	public Cluster(String nome, String documento, Map<Termo, Integer> termosFrequencia) {
		
		setNome(nome);
		List<String> documentos = new ArrayList<String>();
		documentos.add(documento);
		setDocumentos(documentos);
		setTermosFrequencia(termosFrequencia);
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<String> getDocumentos() {
		return documentos;
	}
	public void setDocumentos(List<String> documentos) {
		this.documentos = documentos;
	}
	public Map<Termo, Integer> getTermosFrequencia() {
		return termosFrequencia;
	}
	public void setTermosFrequencia(Map<Termo, Integer> termosFrequencia) {
		this.termosFrequencia = termosFrequencia;
	}
	public void addDocumentos(String string) {
		this.documentos.add(string);
	}
	
}
