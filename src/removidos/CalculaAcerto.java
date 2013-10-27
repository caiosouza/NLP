package removidos;

import java.util.ArrayList;
import java.util.List;

import utils.ArquivoUtils;

public class CalculaAcerto {

	/**
	 * @param args
	 */
	//private static final String categoriasEncontradasTXT = experimentoFolder+ "classificationFiles/categoriasEncontradas"+topN+".txt";
	//private static final String categoriasEncontradasTXT = experimentoFolder+ "classificationFiles/categoriasEncontradasTest"+topN+".txt";
	private String categoriasEncontradasTXT;
	
	private String categoriasOriginaisTXT;
	//private static final String categoriasOriginaisTXT = experimentoFolder+ "classificationFiles/ordemCategoriasTestSS.txt";
	
	//private static final String resultados = experimentoFolder+"resultados/"+topN+"resultado.txt";
	private String resultados;
	private String[] categorias;
	
	public CalculaAcerto(String categoriasEncontradasTXT, String categoriasOriginaisTXT,
			String resultados, String[] categorias) {
		
		this.categoriasEncontradasTXT = categoriasEncontradasTXT;
		this.categoriasOriginaisTXT = categoriasOriginaisTXT;
		this.resultados = resultados;
		this.categorias = categorias;
	}

	public void exec() {
		
		List<String> categoriasCorretas = ArquivoUtils.abreArquivo(categoriasOriginaisTXT);
		List<String> categoriasEncontradas = ArquivoUtils.abreArquivo(categoriasEncontradasTXT);
		
		//int acertos = 0;
		String categoriaCorreta;
		String categoriaEncontrada;
		int idCategoriaCorreta;
		int idCategoriaEncontrada;
		int naoEncontrado = 0;
		int[][] matrizConfusao = new int[categoriasCorretas.size()][categoriasCorretas.size()];
		for (int i = 0; i < categoriasCorretas.size(); i++) {
			categoriaCorreta = categoriasCorretas.get(i);
			categoriaEncontrada = categoriasEncontradas.get(i);
			idCategoriaCorreta = getIdCategoria(categoriaCorreta);
			idCategoriaEncontrada = getIdCategoria(categoriaEncontrada);
			if (idCategoriaEncontrada == -1 ){
				naoEncontrado = naoEncontrado + 1;
			} else {
			matrizConfusao[idCategoriaCorreta][idCategoriaEncontrada] = matrizConfusao[idCategoriaCorreta][idCategoriaEncontrada] +1;
			}
			
		}
		
		
		exibeResultado(matrizConfusao, categoriasCorretas.size(), naoEncontrado);
		System.out.println("Resultados Processados!");
		
	}
	

	private void exibeResultado(int[][] matrizConfusao, int size, int naoEncontrado) {
	
		List<String> outPut = new ArrayList<String>();
		
		int[] somaLinha = new int[categorias.length];
		int[] somaColuna = new int[categorias.length];
		String linha = "";
		int numCategorias = categorias.length;
		
		int acertos = 0;
		int total = 0;

		
		//outPut.add("numero arquivos:" + size);
		//outPut.add("Nao Encontrados: " + naoEncontrado + " "+ 1.0 *naoEncontrado/size);
		
		for (int i = 0; i < numCategorias; i++) {
			for (int j = 0; j < numCategorias; j++) {
				somaLinha[i] = somaLinha[i] + matrizConfusao[i][j];
				somaColuna[j] = somaColuna[j] + matrizConfusao[i][j];
			}
			 
		}
		
		for (int i = 0; i < categorias.length; i++) {
			acertos = acertos + matrizConfusao[i][i];
			total = total + somaColuna[i];
		}
		
		//outPut.add(" ");
		//outPut.add("acertos em documentos definidos: "+ acertos);
		//outPut.add("total definido: "+ (size-naoEncontrado));
		//outPut.add("Acuracia: "+ 1.0*acertos/(size-naoEncontrado));
		
		//outPut.add(" ");
		//outPut.add("Acuracia em todos os documentos: "+ 1.0*acertos/size);
		
		outPut.add("numero arquivos;N‹o Encontrados;% N‹o Encontrados;Acertos em documentos definidos;Total Definido;" +
				"Acur‡cia em Definidos;Acur‡cia Global");
		outPut.add(size+";"+naoEncontrado+";"+1.0 *naoEncontrado/size+";"+acertos+";"+(size-naoEncontrado)
				+";"+1.0*acertos/(size-naoEncontrado)+";"+1.0*acertos/size);
		outPut.add("");
		outPut.add("matriz de confusao");
		
		
		for (int i = 0; i < numCategorias; i++) {
			linha = linha + " " + categorias[i];  
		}
		outPut.add(linha);
		for (int i = 0; i < numCategorias; i++) {
			//linha = matrizConfusao[i];
			linha = categorias[i];
			for (int j = 0; j < numCategorias; j++) {
				linha = linha + " " + matrizConfusao[i][j];
			}
			outPut.add(linha);
			 
		}
		
		
		outPut.add(" ");
		outPut.add("precision");
		for (int i = 0; i < categorias.length; i++) {
			outPut.add(categorias[i]+ ": "+ 1.0*matrizConfusao[i][i]/somaLinha[i] );
		}
		
		outPut.add(" ");
		outPut.add("recall");
		for (int i = 0; i < categorias.length; i++) {
			outPut.add(categorias[i]+ ": "+ 1.0*matrizConfusao[i][i]/somaColuna[i] );
		}
		
		ArquivoUtils.salvaArquivo(outPut, resultados);
		
	}

	private int getIdCategoria(String categoria) {
		
		int id = -1;
		for (int i = 0; i < 25; i++) {
			if(categorias[i].compareTo(categoria) == 0) id = i;
		}
		return id;
	}

}
