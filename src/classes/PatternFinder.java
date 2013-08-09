package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class PatternFinder {
	
	public PatternFinder(){}
	
	/** TODO : Wir müssen noch diese Methode fertig schreiben. */ 
	// searches for all Patterns in the text
	public Pattern findAllPattern() throws IOException
	{
		FileReader fr = new FileReader("kindleDocuments/Gesamttext/gesamttext.txt");
		BufferedReader br = new BufferedReader(fr);
		
		String zeile = br.readLine();
		
		while(zeile != null)
		{
			zeile = handleLineBreakInPattern(zeile, br);
		}
		
		br.close();
		fr.close();
	}
	
	/** TODO : Wir müssen noch diese Methode fertig schreiben. */
	// it will happen, that a pattern is in two lines. This function will find a line break in a pattern and merge both lines. 
	public String handleLineBreakInPattern(String zeile, BufferedReader br)
	{
		String lastWord = getLastWord(zeile);
	}
	
	// returns the last word of a String in the same form, as it is in the hashTable
	// so we need to do every step, that we are also doing in the hashtable
	public String getLastWord(String zeile)
	{
		StringTokenizer st = new StringTokenizer(zeile);
		
		String word = "";
		
		// after this, result will be the last word of the String
		while(st.hasMoreTokens()) { word = st.nextToken();}
		
		word = normalize(word);
		
		return word;
	}
		
	// removes all characters that are neither digits nor letters
	public String normalize(String word)
	{
		word = word.replaceAll("[^a-zA-Z0-9_&ßöäü]", "");
		return word;
	}

}
