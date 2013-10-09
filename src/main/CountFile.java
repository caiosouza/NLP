package main;

import java.io.File;
import utils.ArquivoUtils;

public class CountFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		java.util.List<File> arquivos = ArquivoUtils.getAllFilesRecursive("corpusTxt/StemmeStop");
		System.out.println(arquivos.size());

		arquivos = ArquivoUtils.getAllFilesRecursive("corpusTxt/Steme");
		System.out.println(arquivos.size());

		arquivos = ArquivoUtils.getAllFilesRecursive("corpusTxt/Stop");
		System.out.println(arquivos.size());

		arquivos = ArquivoUtils.getAllFilesRecursive("corpusTxt/Original");
		System.out.println(arquivos.size());

	
	}

}
