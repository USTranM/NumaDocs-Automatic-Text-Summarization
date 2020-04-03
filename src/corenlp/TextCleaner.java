package corenlp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import org.wikiclean.WikiClean;

public class TextCleaner {
	public static void main (String [] args) {
		File f = new File("D:\\D - Downloads\\enwiki-20200201-pages-articles-multistream.xml\\enwiki-20200201-pages-articles-multistream.xml");

//		WikiClean cleaner = new WikiClean.Builder().build();
//		String content = cleaner.clean("D:\\D - Downloads\\enwiki-20200201-pages-articles-multistream.xml\\enwiki-20200201-pages-articles-multistream.xml");
//		System.out.println(content);
		
		String article = "hello";
		String xmlStartTag = "<text xml:space=\"preserve\">";
		String xmlEndTag = "</text>";
		String articleWithXml = xmlStartTag + article + xmlEndTag;
		WikiClean cleaner = new WikiClean.Builder().build();
		String plainWikiText = cleaner.clean(articleWithXml);
		System.out.println(plainWikiText);

//		try {
//		   FileInputStream inputStream = new FileInputStream(f);
//		   Scanner sc = new Scanner(inputStream, "UTF-8");
//		   while (sc.hasNextLine()) {
//		      String line = sc.nextLine();
//				WikiClean cleaner = new WikiClean.Builder().build();
//				String plainWikiText = cleaner.clean(line);
//				System.out.println(plainWikiText);
//		   } 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		

		
		
	}
}
