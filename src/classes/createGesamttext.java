package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// THIS CLASS IS NOT IMPORTANT FOR OUR PROJECT, IGNORE IT!
public class createGesamttext {
	
	// this will take all documents texts and put it into one textfile
	public static void doIt() throws IOException
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
