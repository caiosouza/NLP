package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import utils.ContaPalavras;

public class TestaContaPalavras {

	public static void main(String [] args){
		
		TestaContaPalavras testes = new TestaContaPalavras();
		testes.testaContaFrequenciaNgrama();
		
	}

	private void testaContaFrequenciaNgrama() {
		
		List<String> linhas = new ArrayList<String>();
		linhas.add("eu vi o rato do rabo do rei       de roma cair no chao");
		linhas.add("eu vi o rato com rabo do rei da romenia        cair no chao");
		linhas.add("eu vi       o rei da romenia cair no chao");
		
		int ngrama = 4;
		int minFrequencia = 0;
		Set<String> filtroTermos = null;
		
		Map<String, Integer> termosFrequencias = ContaPalavras.contaFrequenciaNgrama(linhas, minFrequencia, filtroTermos , ngrama);
	
		for (Entry<String, Integer> termoFrequencia : termosFrequencias.entrySet()) {
			System.out.println(termoFrequencia.getKey() + " " + termoFrequencia.getValue());
		}
		
	}
}
