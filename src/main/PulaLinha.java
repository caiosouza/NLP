package main;

import java.util.ArrayList;
import java.util.List;

import utils.ArquivoUtils;
import utils.ContaPalavras;
import utils.PreProcessaCorpus;

public class PulaLinha {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//String nomeArq = "ReutersTrainTop100.txt";
		String nomeArq = "experimentos/Top25_1/allTextBaseTxtFile/ReutersTest.txt";
		List<String> linhas = ArquivoUtils.abreArquivo(nomeArq);
		List<String> novalinhas = new ArrayList<String>();
		for (String linha : linhas) {
			linha = linha.toLowerCase();
			linha = ContaPalavras.limpacaracteres(linha);
			linha = ContaPalavras.removeNumeros(linha);
		    
			linha = PreProcessaCorpus.preProcessaLinha(linha, true, true, "enStopList.txt");
			novalinhas.add(linha);
			novalinhas.add(" ");
		}
		
		//ArquivoUtils.salvaArquivo(novalinhas, "ReutersTrainTop100SS.txt");
		ArquivoUtils.salvaArquivo(novalinhas, "experimentos/Top25_1/allTextBaseTxtFile/ReutersTestSS.txt");

	}

}