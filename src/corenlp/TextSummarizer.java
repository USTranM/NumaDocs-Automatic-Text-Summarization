package corenlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TextSummarizer {
	
	protected static HashMap<String, Term> dictionary = new HashMap<String, Term>(50000, (float)0.33);
	protected static int totalNumWords = 0;
	
	public static void main (String [] args) throws IOException {
		
		ArrayList<Sentence> list = new ArrayList<Sentence>();
		
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		File[] allFiles = new File(args[0]).listFiles();
		
		for (int file = 0; file < allFiles.length; file++) {
		
			BufferedReader br = new BufferedReader(new FileReader(allFiles[file]));
	
			String line;
	
			while ((line = br.readLine()) != null) {
				
				Annotation annotation = new Annotation(line);
				pipeline.annotate(annotation);
				
				List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
				
				for (CoreMap sent : sentences) {
					String sentence = sent.toString();
					sentence = TextSummarizer.cleanTextContent(sentence);
					list.add(new Sentence(sentence));
					
				    //removes all punctuation and symbols
				    sentence = sentence.replaceAll("\\p{P}", "");

					edu.stanford.nlp.simple.Sentence NLPSentence = new edu.stanford.nlp.simple.Sentence(sentence.toLowerCase());
					List<String> allLemmas = NLPSentence.lemmas();
					
					for (int i = 0; i < allLemmas.size(); i++) {
						
						
						if (dictionary.get(allLemmas.get(i)) == null) {
							dictionary.put(allLemmas.get(i), new Term(allLemmas.get(i)));
						} else {
							dictionary.put(allLemmas.get(i), dictionary.get(allLemmas.get(i)).incrementCount());
						}
						
						if (!dictionary.get(allLemmas.get(i)).getAllDocs().contains(allFiles[file].getName())) {
							dictionary.get(allLemmas.get(i)).getAllDocs().add(allFiles[file].getName());
						}
						totalNumWords++;
					}
					
				}

			}
			
			br.close();

		}
		
		//end of allFiles
		Iterator<Sentence> iter = list.iterator();
		PriorityQueue<Sentence> pq = new PriorityQueue<Sentence>(new TFIDFComparator());
		
		while (iter.hasNext()) {
			
			Sentence s = iter.next();
			s.tfidf(s.getSentence(), allFiles.length);
			pq.add(s);
			
		}
		
		for (int i = 0; i < Integer.parseInt(args[1]); i++) {
			
			System.out.println(pq.poll() + " ");
			
		}		
		
		
	}
	
	private static String cleanTextContent(String text) 
	{
	    // strips off all non-ASCII characters
	    text = text.replaceAll("[^\\x00-\\x7F]", "");
	 
	    // erases all the ASCII control characters
	    text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
	     
	    // removes non-printable characters from Unicode
	    text = text.replaceAll("\\p{C}", "");
	    
	    text = text.replaceAll("[^a-zA-Z ]", "");
	    
	    return text.trim();
	}
	
	public static class TFIDFComparator implements Comparator<Sentence> {

		@Override
		public int compare(Sentence o1, Sentence o2) {
			if (o1.tfidf > o2.tfidf) {
				return 1;
			} else if (o1.tfidf == o2.tfidf) {
				return 0;
			} else {
				return -1;
			}
		}
		
		
		
	}
	
	public static class Sentence {
		
		private String sentence;
		private double tfidf;
		
		public Sentence (String sentence) {
			
			this.sentence = sentence;
			this.tfidf = 0;
			
		}
		
		public String getSentence() {
			return sentence;
		}

		public void setSentence(String sentence) {
			this.sentence = sentence;
		}

		@Override
		public int hashCode() {
			return sentence.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			Sentence s = (Sentence)o;
			return this.tfidf == s.tfidf;
		}
		
		public void tfidf(String sentence, int totalNumFiles) {
			double total = 0;
			edu.stanford.nlp.simple.Sentence NLPSentence = new edu.stanford.nlp.simple.Sentence(sentence.replaceAll("[^a-zA-Z ]", "").toLowerCase());
			List<String> allLemmas = NLPSentence.lemmas();
			
			for (int i = 0; i < allLemmas.size(); i++) {
				Term term  = dictionary.get(allLemmas.get(i));

				double idf = Math.log((double)totalNumFiles / term.getCountOfWord());
				double rtf  = (float)term.getCountOfWord() / totalNumWords;
				total = total + (rtf * idf);
			}
			
			this.tfidf = total / totalNumFiles;
		}
		
		public String toString() {
			return this.sentence;
		}
		
	}
	
	public static class Term {
		
		private String term;
		private int countOfWord;
		private ArrayList<String> allDocs;
		
		public Term(String term) {
			this.term       	 = term;
			this.countOfWord	 = 1;
			this.allDocs		 = new ArrayList<String>();
		}

		public String getTerm() {
			return term;
		}

		public int getNumDocsWithWord() {
			return allDocs.size();
		}
		
		public int getCountOfWord() {
			return countOfWord;
		}
		
		public Term incrementCount() {
			++countOfWord;
			return this;
		}

		public ArrayList<String> getAllDocs() {
			return allDocs;
		}

		
	}
	
	
}
