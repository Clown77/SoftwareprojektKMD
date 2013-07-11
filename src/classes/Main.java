package classes;

import java.io.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		FileReader fr = new FileReader("kindleDocuments/Gesamttext/gesamttext.txt");
	 	BufferedReader br = new BufferedReader(fr);

	 	int size = 30000;
	 	WordHashtable ourHash = new WordHashtable(size);
	 	
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
	 	
	 	ourHash.sortHashtable();
	 	ourHash.printHashtable();
	    
	    br.close();
		fr.close();
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
