package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AmostraArquivos {

	public static List<File> amostraArquivos(String root, Double spliPercentage){
		return amostraArquivos(new File(root), spliPercentage);
	}
	
	public static List<File> amostraArquivos(File root, Double percentualTreino){
		
		List<File> arquivosOriginais = ArquivoUtils.getAllFilesRecursive(root);
		List<String> linhas = new ArrayList<String>();
		String fileName;
		
		for (File file : arquivosOriginais) {
			
			linhas = ArquivoUtils.abreArquivo(file);
			fileName = file.getAbsolutePath();
			
			if( Math.random() < percentualTreino){				
				fileName = fileName.replace(root.getName(), root.getName()+ "_Treino");
			} else{
				fileName = fileName.replace(root.getName(), root.getName()+ "_Teste");
			}
			
			ArquivoUtils.salvaArquivo(linhas, fileName);
		}
		
		return arquivosOriginais;
	}
	
}
