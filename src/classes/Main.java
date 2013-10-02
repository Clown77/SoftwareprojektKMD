package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
	
	private final static int TABLESIZE = 30000;
	private static int WORDNUMBER = 0;
	
	public static void main(String[] args) throws Exception {
		
		FileReader fr = new FileReader("kindleDocuments/Gesamttext/gesamttext.txt");
	 	BufferedReader br = new BufferedReader(fr);

	 	WordHashtable ourHash = new WordHashtable(TABLESIZE);
	 	
	 	String zeile = br.readLine();
	 	
	 	// read the file line for line until its at the end
	 	while(zeile != null)
	 	{
	 		// We are not interested in case sensitivity
	 		zeile = zeile.toLowerCase();
	 		
	 		// This checks if the line ends with a seperator (-). If it does, the lines will be concatenated
	 		zeile = handleLineBreak(zeile, br);
	 		
	 		// filters the words out of the string and adds them into the hashtable
	 		ourHash.handleString(zeile);
	 		
	 		zeile = br.readLine();
	 	}
	 	
	 	ourHash.setHFWBorder();
	 	ourHash.setCWBorder();
	 	ourHash.setWordTypes();
	 	
	 	ourHash.tableToString();
	 	
	 	WORDNUMBER = ourHash.regularWordsCount;
	 	
	 	br.close();
		fr.close();
	 	
		// The patternfinder can work on the text and return us a list with all patterns we want to search for
	 	PatternFinder patternfinder = new PatternFinder();
	 	
	 	// in this step the patternfinder collects all patterns. Including High frequency words
	 	patternfinder.findAllPattern(ourHash);
	 	
	 	// High Frequency words are just used to find patterns, but they are not needed for future work anymore
	 	patternfinder.removeHighFrequencyWords(ourHash);
	 	
	 	// Patterns, that don't appear for Tp times, will be removed
	 	patternfinder.removeLowAppearencePattern(WORDNUMBER);

	 	
	 	LinkedList<Pattern> foundPattern = patternfinder.getFoundPattern();
	 	
	 	System.out.println("Ohne High Frequency Words: " +foundPattern.toString());

	}
	
	//Diese Funktion führt dazu, dass Zeilenumbrüche nicht zu zwei getrennten Wörtern führen.
	public static String handleLineBreak (String tempZeile, BufferedReader br) throws IOException
	{
		do
		{
			if(!tempZeile.endsWith("-")) return tempZeile;
			tempZeile = tempZeile.concat(br.readLine());		
		}while(true);
	}
}
