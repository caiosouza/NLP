package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtils {
	
	private static String codificacao = "UTF-8";
	
	public static String readFile(String filename)
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	public static List<String> abreArquivo(File arquivo){
		
		return abreArquivo(arquivo.getAbsolutePath());
		
	}
	public static List<String> abreArquivo(String nomeArq) {
		
		List<String> linhas = new ArrayList<String>();
		String linha = "";
		try{
			
			BufferedReader txtBuffer = new BufferedReader(new InputStreamReader(
					new FileInputStream(nomeArq), codificacao));
			
			linha = txtBuffer.readLine();
	        while (linha != null) {
	        	linhas.add(linha);
	            linha = txtBuffer.readLine();
	        }
	        txtBuffer.close();
		}
		catch (Exception e) {
			System.out.println("[ERROR] Erro ao abrir arquivo: "+ nomeArq);
			System.exit(-1);
		}
		return linhas;
		
	}
	
	public static int getNumLinhas(String nomeArquivo){

		return abreArquivo(nomeArquivo).size();
		
	}

	public static void salvaArquivo(String linha, String nomeArqSaida, Boolean append) {
		List<String> linhas = new ArrayList<String>();
		linhas.add(linha);
		salvaArquivo(linhas, nomeArqSaida, append);
	}
	
	public static void salvaArquivo(String linha, String nomeArqSaida) {
		List<String> linhas = new ArrayList<String>();
		linhas.add(linha);
		salvaArquivo(linhas, nomeArqSaida);
	}

	public static void salvaArquivo(List<String> linhas, String nomeArqSaida, Boolean append) {

		FileOutputStream arqSaida;
		PrintStream p;  
		
		try{  
			//cria o diretorio caso seja necessario
			criaDiretorioArquivo(nomeArqSaida);
			
			arqSaida = new FileOutputStream(nomeArqSaida, append);  
			p = new PrintStream(arqSaida, true, codificacao);
			 
			for (String linha : linhas) {
				p.println(linha);
			}	
			  
		    p.close();  
		} catch(Exception e) {  
			System.out.println("[ERROR] Erro ao salvar arquivo de saída.");
			System.exit(-1);
		}  
	}

	
	public static void salvaArquivo(List<String> linhas, String nomeArqSaida) {

		FileOutputStream arqSaida;
		PrintStream p;  
		
		try{  
			//cria o diretorio caso seja necessario
			criaDiretorioArquivo(nomeArqSaida);
			
			arqSaida = new FileOutputStream(nomeArqSaida);  
			p = new PrintStream(arqSaida, true, codificacao);
			 
			for (String linha : linhas) {
				p.println(linha);
			}	
			  
		    p.close();  
		} catch(Exception e) {  
			System.out.println("[ERROR] Erro ao salvar arquivo de saída.");
			System.exit(-1);
		}  
	}
	
	private static void criaDiretorioArquivo(String nomeArqSaida) {

		String dirPath = new File(nomeArqSaida).getParent();
		if (dirPath != null){
			File fDirPath = new File(dirPath);
			if (!fDirPath.exists()) {
				fDirPath.mkdirs();
			}
		}
		
	}

	private static void criaDiretorioArquivo(File arqSaida) {

		String dirPath = arqSaida.getParent();
		if (dirPath != null){
			File fDirPath = new File(dirPath);
			if (!fDirPath.exists()) {
				fDirPath.mkdirs();
			}
		}
		
	}

	public static List<File> getAllFilesRecursive(List<String> arquivos) {
		List<File> csvsCategorias = new ArrayList<File>();
		for (String arquivo : arquivos) {
			csvsCategorias.addAll(ArquivoUtils.getAllFilesRecursive(arquivo));
		}
		return csvsCategorias; 
	}
	
	public static List<File> getAllFilesRecursive(String aStartingDir) {
		File arquivo = new File(aStartingDir);
		return getAllFilesRecursive(arquivo); 
	}
	
	public static List<File> getAllFilesRecursive(File aStartingDir)  {
		
		List<File> result = new ArrayList<File>();
		try{
			if(aStartingDir.isFile()){
				result.add(aStartingDir); 
			}
			else {//must be a directory
					
				File[] filesAndDirs = aStartingDir.listFiles();
				for (File file : filesAndDirs) {
				
		    		if(file.isFile()){
						result.add(file); //add only files
					}
					else {//must be a directory
						List<File> deeperList = getAllFilesRecursive(file);//recursive call!
						result.addAll(deeperList);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao buscar arquivos em: "+aStartingDir.getAbsolutePath() );
			System.out.println("Verifique se esta pasta existe e se possui algum arquivo!");
			System.exit(-1);
		}
		return result;
		
	}

    public static void copia(File origem, File destino)  {
      
    	criaDiretorioArquivo(destino);
    	InputStream in;
		try {
			in = new FileInputStream(origem);
			OutputStream out = new FileOutputStream(destino);          
	        // Transferindo bytes de entrada para saída
	        byte[] buffer = new byte[1024];
	        int lenght;
	        while ((lenght= in.read(buffer)) > 0) {
	            out.write(buffer, 0, lenght);
	        }
	        in.close();
	        out.close();
	        
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao copiar arquivo: " + origem + " para o arquivo:" + destino);
			System.exit(-1);
		}
        
    }

	public static String pegaCabecalho(String nomeArq) {
			
		String cabecalho = "";
		try {
			
			BufferedReader txtBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArq), codificacao));
			cabecalho = txtBuffer.readLine();
			txtBuffer.close();	
		} catch (IOException e) {
			System.out.println("[ERROR] Erro ao buscar cabecalho do arquivo: "+ nomeArq);
			System.exit(-1);
		}
			
     	return cabecalho;
		
	}

	public static String pegaNome(String nomeArquivo) {
		File arquivo = new File(nomeArquivo);
		return arquivo.getName();
	}

	public static String contatenaLinhas(List<String> linhas) {
		
		String linhaFinal = "";
		for (String linha : linhas) {
			linhaFinal = linhaFinal +" "+ linha;
		}
		return linhaFinal;
	}

	public static boolean comparaArquivos(File oldResult, File newResult) {
		
		List<String> linhasNovo = abreArquivo(newResult);
		List<String> linhasAntigo = abreArquivo(oldResult);
		String parteAntigo = "";
		String parteNovo = "";
		boolean diferente = false ;
		if (linhasAntigo.size() == linhasNovo.size()){
			for (int i = 0; i < linhasAntigo.size(); i++) {
				if (! (linhasAntigo.get(i).equals(linhasNovo.get(i))) ){
					String [] tokensAntigo = linhasAntigo.get(i).replaceAll(" ",";").split(";");
					String [] tokensNovo = linhasNovo.get(i).replaceAll(" ",";").split(";");
					parteAntigo = "";
					parteNovo = "";
					for (int j = 0; j < tokensNovo.length; j++) {
						if (!(tokensAntigo[j].equals(tokensNovo[j])) && (tokensAntigo[j].length() > 2))
						{
							try{
								Double antigo = Double.valueOf(tokensAntigo[j]);
								Double novo = Double.valueOf(tokensNovo[j]);
								
								if (Math.abs(1 - (antigo/novo)) > 0.1)
								{
									parteAntigo = linhasAntigo.get(i);
									parteNovo = linhasNovo.get(i);
								}
							}catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					if(parteAntigo.trim().length() > 0){
						System.out.println("A "+ parteAntigo);
						System.out.println("N "+ parteNovo);
					}
					diferente = true;
					i = linhasAntigo.size();
				}
			}
		}
		return diferente;
	}
	
}
