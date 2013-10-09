package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import enstemmer.implementations.PorterStemmer;
import utils.ArquivoUtils;

public class PreProcessaCorpus {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		PreProcessaCorpus preProcessaCorpus = new PreProcessaCorpus();
		preProcessaCorpus.exec(args);
		
	}
	
	public void exec(String[] args){
		
		String nomeDiretorio;
		if (args.length > 0){
			nomeDiretorio = args[0];
		}
		else {
			System.out.println("Entre com o nome do Diretorio a ser preprocessado");
			Scanner scanner = new Scanner(System.in);
			nomeDiretorio = scanner.next();
		}
		
		File diretorio = new File(nomeDiretorio);
		Boolean removeStopWords = true;
		Boolean reduzStemme = true;
		String stopListNameFile = "StopList.txt";
		preProcessaDiretorio(diretorio, removeStopWords, reduzStemme, stopListNameFile);
		
	}
	
	private void preProcessaDiretorio(File diretorio, Boolean removeStopWords, Boolean reduzStemme, String stopListNameFile) {

		try{
			File[] files = diretorio.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isDirectory()) {
					preProcessaDiretorio(f, removeStopWords, reduzStemme, stopListNameFile);
				}
				else {
					List<String> linhasPreProcessadas = preProcessaArquivo(f, removeStopWords, reduzStemme, stopListNameFile);
					//salva o documento preprocessado na nova pasta
					String nomeDiretorioSaida = f.getAbsolutePath().replace("Original", "preProssed");
					ArquivoUtils.salvaArquivo(linhasPreProcessadas, nomeDiretorioSaida);
				}
			}
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao preprocessar diretorio: " + diretorio.getAbsoluteFile());
			System.exit(-1);
		}
	}

	private List<String> preProcessaArquivo(File arquivo, Boolean removeStopWords, Boolean reduzStemme, String stopListNameFile) {
		
		List<String> linhasOriginais = new ArrayList<String>();
		List<String> linhasPreProcessadas = new ArrayList<String>();
		
		try {
			linhasOriginais = ArquivoUtils.abreArquivo(arquivo.getAbsolutePath());
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao ler arquivo: " + arquivo);
			System.exit(-1);
		}
		
		for (String linha : linhasOriginais) {
			linhasPreProcessadas.add(preProcessaLinha(linha, removeStopWords, reduzStemme, stopListNameFile));	
		}
		 
		return linhasPreProcessadas;
		
	}

	public static String preProcessaLinha(String linha, Boolean removeStopWords, Boolean reduzStemme, String stopListNameFile) {

		String outPutText = linha;
		
		if (removeStopWords){
			//remove stopwords
			StopWords stopwords = new StopWords();
			linha = stopwords.removeStopWords(linha, stopListNameFile);
			outPutText = linha;
		}
		
		if (reduzStemme){
			//executa Stemming
			outPutText = "";
			PorterStemmer stemmer = new PorterStemmer();
			String [] tokens = linha.split(" ");
			for (int i = 0; i < tokens.length; i++) {
				outPutText = outPutText +" "+ stemmer.stem(tokens[i]);
			}
		}
		
		return outPutText;
	}

}
