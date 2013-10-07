package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
	
	private static int TABLESIZE = 0;
	private static int WORDNUMBER = 0;
	private static String path = "kindleDocuments/Gesamttext/Bibel.txt";

	public static void main(String[] args) throws Exception {
		
		long programmStartTime = System.currentTimeMillis();
		
		FileReader fr = new FileReader(path);
	 	BufferedReader br = new BufferedReader(fr);
	 	
	 	TABLESIZE = calculateTableSize();

	 	WordHashtable ourHash = new WordHashtable(TABLESIZE);
	 	
	 	System.out.println(TABLESIZE +" entries created in the Hashtable.");
	 	
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
	 	
	 	br.close(); 
	 	fr.close();
	 	
	 	System.out.println("Table filled.");
	 	
	 	// will first calculate the High Frequency und Content Word Border
	 	ourHash.setHFWBorder();
	 	ourHash.setCWBorder();
	 	
	 	// Depending on the Word borders we can now sort the words into theire specific categories
	 	ourHash.setWordTypes();
	 	
	 	WORDNUMBER = ourHash.textsize;
	 	
	 	System.out.println("Word categories set.");
	 	
	 	ourHash.changeWordType("jesus", 'C');
	 	ourHash.changeWordType("gott", 'C');
	 	ourHash.changeWordType("herr", 'C');
	 	
	 	System.out.println("Postprocessing word categories has been successfull.");
	 	
	 	//ourHash.tableToString();

		// The patternfinder can work on the text and return us a list with all patterns we want to search for
	 	PatternFinder patternfinder = new PatternFinder(path);
	 	
	 	// in this step the patternfinder collects all patterns. Including High frequency words
	 	patternfinder.findAllPattern(ourHash);
	 	
	 	// Patterns, that don't appear for Tp times, will be removed
	 	patternfinder.removeLowAppearencePattern(WORDNUMBER);
	 	
	 	// High Frequency words are just used to find patterns, but they are not needed for future work anymore
	 	// patternfinder.removeHighFrequencyWords(ourHash);

		LinkedList<Pattern> foundPattern = patternfinder.getFoundPattern();

		System.out.println("Without High-Frequency Words: " +foundPattern.toString());
		
		long programTimeNeeded = (System.currentTimeMillis() - programmStartTime)/1000;
		
		System.out.println("Total time needed: " +programTimeNeeded +" seconds");
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
	
	
	public static int calculateTableSize()
	{
		// Returns the size of the textfile in Bytes
		File file = new File(path);
		long length = file.length();
		
		return (int)length/3;
	}
}
