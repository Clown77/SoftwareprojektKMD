package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
	
	private final static int WORDNUMBER = 30000;
	
	public static void main(String[] args) throws Exception {
		
		FileReader fr = new FileReader("kindleDocuments/Gesamttext/gesamttext.txt");
	 	BufferedReader br = new BufferedReader(fr);

	 	WordHashtable ourHash = new WordHashtable(WORDNUMBER);
	 	
	 	String zeile = br.readLine();
	 	
	 	// read the file line for line until its at the end
	 	while(zeile != null)
	 	{
	 		// We are not interested in case sensitivity
	 		zeile = zeile.toLowerCase();
	 		zeile = handleLineBreak(zeile, br);
	 		ourHash.handleString(zeile);
	 		zeile = br.readLine();
	 	}
	 	
	 	br.close();
		fr.close();
	 	
	 	PatternFinder patternfinder = new PatternFinder();
	 	patternfinder.findAllPattern(ourHash);
	 	LinkedList<Pattern> foundPattern = patternfinder.getFoundPattern();
	 	System.out.println("Der Maulwurf ist endlich auf dem Mond angekommen: " +foundPattern.toString());

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
