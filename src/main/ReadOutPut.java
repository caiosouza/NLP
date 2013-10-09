package main;

import java.util.ArrayList;
import java.util.List;

import utils.ArquivoUtils;

public class ReadOutPut {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ReadOutPut readOutPut = new ReadOutPut();
		readOutPut.exec(args);

	}

	private void exec(String[] args) {
		String fileName = args[0];
		readO(fileName);
				
		
	}

	private void readO(String fileName) {
		
		List<String> lines = ArquivoUtils.abreArquivo(fileName);
		List<String> clusterInformation = new ArrayList<String>();
		for (String line : lines) {
			if(line.indexOf("score:") > 0){
				clusterInformation.add(line);
			}
		}
		
		String outFileName = fileName.replace("output", "clusterInformation");
		System.out.println(outFileName);
		ArquivoUtils.salvaArquivo(clusterInformation, outFileName);
		
		
	}

}
