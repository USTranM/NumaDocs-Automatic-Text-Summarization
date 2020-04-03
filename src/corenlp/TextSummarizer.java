package corenlp;
import java.io.*;
import java.util.*;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;

/** app for testing if Maven distribution is working properly */

public class TextSummarizer {
	
	private HashMap<String, Term> dictionary;
	private int totalNumDocs;
	
	public TextSummarizer() {
		this.dictionary   = new HashMap<String, Term>(50000);
		this.totalNumDocs = 0;
	}
	
//	public double tfidf(String term) {
//		Term word = dictionary.get(term);
//		double idf = Math.log((float)word.getNumDocsWithWord() / word.getCount());
//	}
	
	public static void main(String[] args) throws IOException
    {
       
    }
	
	
	public class Term {
		
		private String term;
		private int numDocsWithWord, count;
		
		public Term(String term) {
			this.term       	 = term;
			this.numDocsWithWord = 1;
			this.count			 = 1;
		}

		public String getTerm() {
			return term;
		}

		public int getNumDocsWithWord() {
			return numDocsWithWord;
		}
		
		public void incrementNumDocs() {
			numDocsWithWord++;
		}

		public int getCount() {
			return count;
		}
		
		public void incrementCount() {
			count++;
		}
		
		
		
	}
	

}