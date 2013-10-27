package main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculaAcertoNew {

	private List<String> categoriasCorretas;
	private List<String> categoriasEncontradas;
	private String[] categorias;
	private DecimalFormat df = new DecimalFormat("#,###.00");  
	
	

	public CalculaAcertoNew(List<String> categoriasCorretas, List<String> categoriasEncontradas, String[] categorias) {
		this.categoriasCorretas = categoriasCorretas;
		this.categoriasEncontradas = categoriasEncontradas;
		this.categorias = categorias;
	}
	
	public List<String> exec() {
		
		List<String> outPut = new ArrayList<String>();
		
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
		
		
		outPut = exibeResultado(matrizConfusao, categoriasCorretas.size(), naoEncontrado);
		//System.out.println("Resultados Processados!");
		
		return outPut;
		
	}
	

	private List<String> exibeResultado(int[][] matrizConfusao, int size, int naoEncontrado) {
	
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
		outPut.add(size +";"+ naoEncontrado +";"+ df.format(1.0 *naoEncontrado/size) +";"+ acertos +";"+ (size-naoEncontrado)
				+";"+ df.format(1.0*acertos/(size-naoEncontrado)) +";"+ df.format(1.0*acertos/size));
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
			outPut.add(categorias[i]+ ": "+ df.format(1.0*matrizConfusao[i][i]/somaLinha[i] ));
		}
		
		outPut.add(" ");
		outPut.add("recall");
		for (int i = 0; i < categorias.length; i++) {
			outPut.add(categorias[i]+ ": "+ df.format(1.0*matrizConfusao[i][i]/somaColuna[i] ));
		}
		
		return outPut;
		
	}
	
	private int getIdCategoria(String categoria) {
		
		int id = -1;
		for (int i = 0; i < 25; i++) {
			if(categorias[i].compareTo(categoria) == 0) id = i;
		}
		return id;
	}
	

}
