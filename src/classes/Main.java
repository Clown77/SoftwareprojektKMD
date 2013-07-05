package classes;

import java.io.*;

import classes.WordHashtable;

public class Main {
	
	public static void main(String[] args) throws IOException {

		// test if the user entered a filename
		/*
		if(args[0] == null)
		{
			System.out.println("\n\n\tUsage: \"CountWords.java\" <filename>");
			return;
		}
		*/
		
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
	 	
	 	
	 	Word[] test = WordHashtable.getHashWords(ourHash, 1, 50);
	 	
	 	System.out.println(test);
	 	
	 	
//	 	ourHash.printHashtable();
	    
	    br.close();
		fr.close();
		createGesamttext();
	}
	
	//Diese Funktion f�hrt dazu, dass Zeilenumbr�che nicht zu zwei getrennten W�rtern f�hren.
	public static String handleLineBreak (String tempZeile, BufferedReader br) throws IOException
	{
		do
		{
			if(!tempZeile.endsWith("-")) return tempZeile;
			tempZeile = tempZeile.concat(br.readLine());		
		}while(true);
	}
	
	// this will take all documents texts and put it into one textfile
	public static void createGesamttext() throws IOException
	{
		
		FileWriter gesamttextFileWriter = new FileWriter("kindleDocuments/Gesamttext/gesamttext.txt");
		
		// we need to open all 562 documents...
		for(int i = 1; i < 563; i++)
		{
			String filename;
			
			if(i < 10) filename = "kindleDocuments/document_00" +i +".txt";
			else	if(i < 100) filename = "kindleDocuments/document_0" +i +".txt";
					else filename = "kindleDocuments/document_" +i +".txt";
			
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			
			// copy data into another document, placed in the "Gesamttext" folder 
			/** Gehen im Moment von immer nur einer zeile aus!*/
			
			String zeile = br.readLine();
			
			while(zeile != null)
			{
				gesamttextFileWriter.append(zeile +"\n");
				zeile = br.readLine();
			}
			br.close();
			fr.close();
		}
		
		// do not forget to always close streams
		gesamttextFileWriter.close();
	}

}
