package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Termo {
	
	private Map<String, Integer> categoriasFrequencia;
	private Map<String, Integer> categoriasPresenca;
	private Double entropiaF;
	private Double entropiaP;
	private String categoriaPrincipal;
	private int totalFrequencia;
	private int totalPresenca;
	private Double formulaScoreF;
	private Double formulaScoreP;
	private String termo;
	
	
	public Termo(String termo){
	
		setCategoriasFrequencia(new HashMap<String, Integer>());
		setCategoriasPresenca(new HashMap<String, Integer>());
		setTermo(termo);
		setTotalFrequencia(0);
		setTotalPresenca(0);
	}
	
	public String getTermo() {
		return termo;
	}

	public void setTermo(String termo) {
		this.termo = termo;
	}

	public Termo(Map<String, Integer> categoriasFrequencia, Map<String, Integer> categoriasPresenca, 
			String categoriaPrincipal, int totalFrequencia, int totalPresenca, Double formulaScoreF, Double formulaScoreP) {
		setCategoriasFrequencia(categoriasFrequencia);
		setCategoriasPresenca(categoriasPresenca);
		setEntropiaF(categoriasFrequencia, totalFrequencia);
		setEntropiaP(categoriasPresenca, totalPresenca);
		setcategoriaPrincipal(categoriaPrincipal);
		setTotalFrequencia(totalFrequencia);
		setTotalPresenca(totalPresenca);
		setFormulaScoreF(formulaScoreF);
		setFormulaScoreP(formulaScoreP);
	}
	private void setEntropiaP(Map<String, Integer> categoriasPresenca2, int totalPresenca2) {
		// TODO Auto-generated method stub
		
	}
	private void setEntropiaF(Map<String, Integer> categoriasFrequencia2, int totalFrequencia2) {
		// TODO Auto-generated method stub
		
	}
	private void setcategoriaPrincipal(String categoriaPrincipal2) {
		// TODO Auto-generated method stub
		
	}
	public Map<String, Integer> getCategoriasFrequencia() {
		return categoriasFrequencia;
	}
	public void setCategoriasFrequencia(Map<String, Integer> categoriasFrequencia) {
		this.categoriasFrequencia = categoriasFrequencia;
	}
	public Map<String, Integer> getCategoriasPresenca() {
		return categoriasPresenca;
	}
	public void setCategoriasPresenca(Map<String, Integer> categoriasPresenca) {
		this.categoriasPresenca = categoriasPresenca;
	}
	public Double getEntropiaF() {
		return entropiaF;
	}
	public void setEntropiaF(Double entropiaF) {
		this.entropiaF = entropiaF;
	}
	public Double getEntropiaP() {
		return entropiaP;
	}
	public void setEntropiaP(Double entropiaP) {
		this.entropiaP = entropiaP;
	}
	public String getCategoriaPrincipal() {
		return categoriaPrincipal;
	}
	public void setCategoriaPrincipal(String categoriaPrincipal) {
		this.categoriaPrincipal = categoriaPrincipal;
	}
	public int getTotalFrequencia() {
		return totalFrequencia;
	}
	public void setTotalFrequencia(int totalFrequencia) {
		this.totalFrequencia = totalFrequencia;
	}
	public int getTotalPresenca() {
		return totalPresenca;
	}
	public void setTotalPresenca(int totalPresenca) {
		this.totalPresenca = totalPresenca;
	}
	public Double getFormulaScoreF() {
		return formulaScoreF;
	}
	public void setFormulaScoreF(Double formulaScoreF) {
		this.formulaScoreF = formulaScoreF;
	}
	public Double getFormulaScoreP() {
		return formulaScoreP;
	}
	public void setFormulaScoreP(Double formulaScoreP) {
		this.formulaScoreP = formulaScoreP;
	}

	public void adicionaFrequencia(String clusterAtual, Integer value) {
		
		int frequencia = 0;
		if(categoriasFrequencia.containsKey(clusterAtual)){
			frequencia = categoriasFrequencia.get(clusterAtual);
		}
		frequencia = frequencia + value;
		totalFrequencia = totalFrequencia + value; 

		categoriasFrequencia.put(clusterAtual, frequencia);
		
	}

	public void calculaEntropiaF() {
		
		Double entropia = 0.0;
		for (Entry<String, Integer> categoriaFrequencia : categoriasFrequencia.entrySet()) {
			Double pxi = 1.0 * categoriaFrequencia.getValue()/totalFrequencia;
			entropia = entropia + pxi * Math.log(pxi);
		}
		
		if (entropia != 0 )	entropia = entropia * -1;

		setEntropiaF(entropia);
		
	}

	public void defineCategoria() {
		
		Double frequenciaRelativa = 0.0;
		int maiorFrequencia = 0;
		for (Entry<String, Integer> categoriaFrequencia : categoriasFrequencia.entrySet()) {
			
			if (categoriaFrequencia.getValue() > maiorFrequencia){
				maiorFrequencia = categoriaFrequencia.getValue();
				categoriaPrincipal = categoriaFrequencia.getKey();
			}
			
		}
	}

	public void init() {
		
		//atualiza a frequencia total
		int totalFrequencia = 0;
		for (Entry<String, Integer> categoriaFrequencia : categoriasFrequencia.entrySet()) {
			totalFrequencia = totalFrequencia + categoriaFrequencia.getValue();
		}
		setTotalFrequencia(totalFrequencia);
		
		//atualiza a entropia
		Double entropiaF = 0.0;
		for (Entry<String, Integer> categoriaFrequencia : categoriasFrequencia.entrySet()) {
			Double pxi = 1.0 * categoriaFrequencia.getValue()/totalFrequencia;
			entropiaF = entropiaF + pxi * Math.log(pxi);
		}
		if (entropiaF != 0 )
			entropiaF = entropiaF * -1;
		setEntropiaF(entropiaF);
		
		//atualiza o valor da formula
		Double formulaScoreF = 1.0* Math.log10(totalFrequencia)/(entropiaF+1);
		setFormulaScoreF(formulaScoreF);
		
	}
	
}
