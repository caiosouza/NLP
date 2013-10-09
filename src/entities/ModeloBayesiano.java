package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ModeloBayesiano {

	private Integer numDocs;
	private Integer numCats;
	private Integer numTermos;
	private Set<String> categorias;
	private Set<String> termos;
	private List<String> listTermos;
	private Hashtable<String,Integer> categDFreq;//<categoria,numdocs>
	private Hashtable<String,Integer> categTFreq;//<categoria,numtermos>
	private Hashtable<String,Map<String,Integer>> categTermFreq; //<categ,<termo,freq>>
	private Hashtable<String,Map<String,Integer>> termCategFreq;//<termo,<categ,freq>>
	private Hashtable<String,Map<String,Integer>> termDocFreq;//TF_IDF <termo,<categ,freq>>
	private List<DocFrequencia> docsFrequencias; //para montar matriz TF_IDF
	
	
	public Integer getNumDocs() {
		return numDocs;
	}
	public void setNumDocs(Integer numDocs) {
		this.numDocs = numDocs;
	}
	public Set<String> getCategorias() {
		return categorias;
	}
	public void setCategorias(Set<String> categorias) {
		this.categorias = categorias;
	}
	public Set<String> getTermos() {
		return termos;
	}
	public void setTermos(Set<String> termos) {
		this.termos = termos;
	}
	public Hashtable<String, Map<String, Integer>> getCategTermFreq() {
		return categTermFreq;
	}
	public void setCategTermFreq(
			Hashtable<String, Map<String, Integer>> categTermFreq) {
		this.categTermFreq = categTermFreq;
	}
	public Hashtable<String, Map<String, Integer>> getTermCategFreq() {
		return termCategFreq;
	}
	public void setTermCategFreq(
			Hashtable<String, Map<String, Integer>> termCategFreq) {
		this.termCategFreq = termCategFreq;
	}
	public Hashtable<String, Integer> getCategDFreq() {
		return categDFreq;
	}
	public void setCategDFreq(Hashtable<String, Integer> categDFreq) {
		this.categDFreq = categDFreq;
	}
	public Hashtable<String, Integer> getCategTFreq() {
		return categTFreq;
	}
	
	public void setCategTFreq(Hashtable<String, Map<String, Integer>> categTermFreq) {
		
		Hashtable<String, Integer> categTFreq = new Hashtable<String,Integer>();
		
		String categoria;
		int nTermos;
		Collection<Integer> frequencias = new ArrayList<Integer>();
		
		for (Entry<String, Map<String, Integer>> entry : categTermFreq.entrySet()) {
			categoria = entry.getKey();
			Map<String, Integer> termoFreq = entry.getValue();
			frequencias = termoFreq.values();
			nTermos = 0;
			for (Integer frequencia : frequencias) {
				nTermos = nTermos + frequencia;	
			}
			categTFreq.put(categoria, nTermos);
		}
			
		this.categTFreq = categTFreq;
	}
	
	public Integer getNumCats() {
		return numCats;
	}
	public void setNumCats(Integer numCats) {
		this.numCats = numCats;
	}
	public Integer getNumTermos() {
		return numTermos;
	}
	public void setNumTermos(Integer numTermos) {
		this.numTermos = numTermos;
	}
	public Hashtable<String, Map<String, Integer>> getTermDocFreq() {
		return termDocFreq;
	}
	public void setTermDocFreq(Hashtable<String, Map<String, Integer>> termDocFreq) {
		this.termDocFreq = termDocFreq;
	}
	/*	
  	public void setCategTFreq(Hashtable<String, Integer> categTFreq) {
		this.categTFreq = categTFreq;
	}
	*/
	public List<DocFrequencia> getDocsFrequencias() {
		return docsFrequencias;
	}
	public void setDocsFrequencias(List<DocFrequencia> docsFrequencias) {
		this.docsFrequencias = docsFrequencias;
	}
	public List<String> getListTermos() {
		return this.listTermos;
	}
	public void setListTermos(List<String> listTermos){
		this.listTermos = listTermos;
	}
	
	
}
