package main;

import java.util.List;

import utils.ArquivoUtils;

public class GeraDataSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void novoDataSet(String nomeDataSet, List<String> documentos, List<String> categoriasEncontradas) {
		
		for (int i = 0; i < documentos.size(); i++) {
			ArquivoUtils.salvaArquivo(documentos.get(i),nomeDataSet+categoriasEncontradas.get(i)+"/"+i+".txt");
		}
		
	}

}
