package test;

import utils.AmostraArquivos;

public class AmostraArquivosTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		amostraReuters2Cat();
	//	amostraNewsGroup2Cat();
		amostraNewsGroup3Cat();
		amostraNewsGroup4Cat();
		amostraNewsGroup5Cat();
		
	}
	
	private static void amostraReuters2Cat(){
		
		String base = "ArquivosEntrada/bases/reutersSS_2cat/dataset/";
		Double percentualTreino = 0.7;
		AmostraArquivos.amostraArquivos(base, percentualTreino);
		
	}

	private static void amostraNewsGroup2Cat(){
		
		String base = "ArquivosEntrada/bases/newsgroup_2cat/dataset/";
		Double percentualTreino = 0.7;
		AmostraArquivos.amostraArquivos(base, percentualTreino);
		
	}
	
	private static void amostraNewsGroup3Cat(){
	
		String base = "ArquivosEntrada/bases/newsgroup_3cat/dataset/";
		Double percentualTreino = 0.7;
		AmostraArquivos.amostraArquivos(base, percentualTreino);
		
	}
	private static void amostraNewsGroup4Cat(){
	
		String base = "ArquivosEntrada/bases/newsgroup_4cat/dataset/";
		Double percentualTreino = 0.7;
		AmostraArquivos.amostraArquivos(base, percentualTreino);
		
	}
	private static void amostraNewsGroup5Cat(){
		
		String base = "ArquivosEntrada/bases/newsgroup_5cat/dataset/";
		Double percentualTreino = 0.7;
		AmostraArquivos.amostraArquivos(base, percentualTreino);
		
	}
	
}
