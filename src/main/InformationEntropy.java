package main;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.ArquivoUtils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;



public class InformationEntropy {

	private int topWordsNum;
	
	/*arquivos saida*/
	private String termosEntropiaTXT;
	
	/*arquivos entrada*/
	private String clusterOrderFile;
    private String allTextBaseTxtFile;

	public InformationEntropy(int topWordsNum, String termosEntropiaTXT, String clusterOrderFile, String allTextBaseTxtFile) {
		super();
		this.topWordsNum = topWordsNum;
		this.termosEntropiaTXT = termosEntropiaTXT;
		this.clusterOrderFile = clusterOrderFile;
		this.allTextBaseTxtFile = allTextBaseTxtFile;
	}

	public void exec(){
		
		HashMap<Integer, HashMap<String,Integer>> categoriasTermosFrequencia = getFrequencias(allTextBaseTxtFile, clusterOrderFile, topWordsNum);
		
		/*
		 * Pega todos os arquivos usados que estão em um txt que junta todos os documentos
		 * Pega os clusteres geredos {pelo kmeans no MatLab, clusteres originais}
		 * Junta essas duas informações e consolida no arquivo termosEntropiaTXT
		 */
		calcEntropias(categoriasTermosFrequencia, termosEntropiaTXT);
		
		
				
	}
	
	private void calcEntropias(HashMap<Integer, HashMap<String, Integer>> categoriasTermosFrequencia, String termosEntropiaTXT) {
		
		//Set<String> allTermos = new HashSet<String>();
		HashMap<String, Integer> termoFrequencia = new HashMap<String, Integer>();
		HashMap<String, HashMap<Integer,Integer>> termosCategoriasFrequencia = new HashMap<String,HashMap<Integer, Integer>>();
		
		//inverte o hashMap de categoriaTermoFrequencia para termoCategoriaFrequencia
		for(int i = 1; i <= 25 ; i++){
		
			termoFrequencia = categoriasTermosFrequencia.get(i);
			for (Map.Entry<String, Integer> entry : termoFrequencia.entrySet()) {
				String termo = entry.getKey() ;
		
				int frequencia = entry.getValue();
				
				HashMap<Integer, Integer> categoriasFrequencia = new HashMap<Integer, Integer>();
				
				//se ja contem o termo, pega as categoriasFrequencias e adiciona
				if (termosCategoriasFrequencia.containsKey(termo)){
					categoriasFrequencia = termosCategoriasFrequencia.get(termo);
					categoriasFrequencia.put(i, frequencia);
				}
				//se nao contem, adiciona o registro
				else {
					categoriasFrequencia.put(i, frequencia);
					termosCategoriasFrequencia.put(termo, categoriasFrequencia);
				}
			}
		}
		
		List<String> termosEntropia = new ArrayList<String>();
		
		//calcula a entropia de cada termo, quanto menor (mais proximo de 0 melhor)
		for (Entry<String, HashMap<Integer, Integer>> entryTermos : termosCategoriasFrequencia.entrySet()) {
			int frequenciaTotal = 0;
			String termo = entryTermos.getKey();
			String categoriaPrincipal = "";
			Double maiorFrequenciaRelativa = 0.0;
			
			HashMap<Integer, Integer> categoriasFrequencia = new HashMap<Integer, Integer>();
			
			categoriasFrequencia = entryTermos.getValue();
			for (Entry<Integer,Integer> entryCategoria : categoriasFrequencia.entrySet()) {
				frequenciaTotal = frequenciaTotal + entryCategoria.getValue();
			}
			
			Double entropia = 0.0;
			Double frequenciaRelativaAtual = 0.0;

			for (Entry<Integer,Integer> entryCategoria : categoriasFrequencia.entrySet()) {
				frequenciaRelativaAtual = 1.0 * entryCategoria.getValue()/frequenciaTotal;
				if (frequenciaRelativaAtual > maiorFrequenciaRelativa){
					maiorFrequenciaRelativa = frequenciaRelativaAtual;
					categoriaPrincipal = ""+ entryCategoria.getKey();
				}
				
				Double pxi = 1.0 * entryCategoria.getValue()/frequenciaTotal;
				entropia = entropia + pxi * Math.log(pxi);
			}
			
			if (entropia != 0 )
				entropia = entropia * -1;
			//concatena os campos separados por ; e substitui . por ,
			String linha = ""+ entropia;
			linha = linha.replace(".", ",");
			linha = termo+";"+linha+";"+frequenciaTotal+";"+categoriaPrincipal;
			termosEntropia.add(linha);
			
		}
		
		ArquivoUtils.salvaArquivo(termosEntropia, termosEntropiaTXT);
		System.out.println("Arquivo "+termosEntropiaTXT+" contendo o par, <termo,entropia> salvo com sucesso!");
	}

	public HashMap<Integer, HashMap<String,Integer>> getFrequencias(String allTextBaseTxtFile, String clusterOrderFile, int topWordsNum){
		
		List<String> docs = ArquivoUtils.abreArquivo(allTextBaseTxtFile);

		List<String> clusters = ArquivoUtils.abreArquivo(clusterOrderFile);
		/* docs = ArrayList de todos os documentos
		 * clusters = ArrayList de todos os clusters
		 */
		
		List<String> textByCluster = new ArrayList<String>();
		textByCluster.add("cluster 0, nao considerado.");
		
		for(Integer i = 1; i <= 25; i++){
			textByCluster.add(getClusterText(docs,clusters,i.toString()));
		}	
		
		/* RETORNA TEXTO POR CLUSTER 1 a 25 */	
		
		HashMap<Integer, HashMap<String,Integer>> categoriasTermosFrequencia = new HashMap<Integer,HashMap<String, Integer>>();
		for(Integer i = 1; i <= 25; i++){
			HashMap<String, Integer> topWordsFrequencia = new HashMap<String, Integer>();
			topWordsFrequencia = topWords(textByCluster.get(i), topWordsNum, i);
			categoriasTermosFrequencia.put(i, topWordsFrequencia);
		}
		
		
		/* Exemplo exibe textos do cluster 1 */
		// System.out.println(textByCluster.get(1));		
		
		return categoriasTermosFrequencia;
		
	}

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
	
	public static List<String> splitToArray(String file, String separador){
		String[] parts = file.split(separador);			
		List<String> arrayParts = new ArrayList<String>();
		for(String part : parts){
			arrayParts.add(part);
		}		
		return arrayParts;
	}
	
	public static HashMap<String, Integer> topWords(String text, int n, Integer numCluster){
		
		HashMap<String, Integer> topWordsFrequencia = new HashMap<String, Integer>();
		
		String[] vectorWords = text.split(" ");	
		List<String> arrayWords = new ArrayList<String>();
		
		List<String> wordArray = new ArrayList<String>();
		
		for(String w : vectorWords){
			if (w.trim().length()>1)
				arrayWords.add(w);
		}
		
		Multiset<String> words = HashMultiset.create(arrayWords);
		
	
		List<Multiset.Entry<String>> wordCounts = Lists.newArrayList(words.entrySet());
		Collections.sort(wordCounts, new Comparator<Multiset.Entry<String>>() {
		    public int compare(Multiset.Entry<String> left, Multiset.Entry<String> right) {
		        // Note reversal of 'right' and 'left' to get descending order
		    	Integer rInt = right.getCount();
		    	Integer lInt = left.getCount();	
		    	return rInt.compareTo(lInt);
		    }
		});
		// wordCounts now contains all the words, sorted by count descending

		// Take the first n entries (alternative: use a loop; this is simple because
		// it copes easily with < n elements)
		Iterable<Multiset.Entry<String>> firsts = Iterables.limit(wordCounts, n);
			
		
		// Guava-ey alternative: use a Function and Iterables.transform, but in this case
		// the 'manual' way is probably simpler:
		List<String> termoFrequencia = new ArrayList<String>();
		for (Multiset.Entry<String> entry : firsts) {
		    wordArray.add(entry.getElement());
		    
		    System.out.println(entry.getCount() + " ; " + entry.getElement());
		    termoFrequencia.add(entry.getCount()+ " ; " + entry.getElement());
		    topWordsFrequencia.put(entry.getElement(), entry.getCount());
		}
		//ArquivoUtils.salvaArquivo(termoFrequencia, "termoFrequenciaCluster"+numCluster+".txt");
		
		return topWordsFrequencia;
	}
	
	public static String getClusterText(List<String> docs , List<String> clusters, String c){
		int i;
		String clusterText = "";
		for(i = 0; i < docs.size(); i=i+2){			
			if(clusters.get(i/2).equals(c)){
				clusterText += docs.get(i).toLowerCase().replaceAll("[.,:;<>{}|_1234567890!@#$%&*()/?+=-]", " ");		
			}
		}
		return clusterText;
	}
	
	
}
