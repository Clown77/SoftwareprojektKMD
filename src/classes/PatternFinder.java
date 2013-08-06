package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class PatternFinder {
	
	public PatternFinder(){}
	
	// searches for all Patterns in the text
	public Pattern findAllPattern() throws IOException // TODO: soll es ein Pattern zur�ckgeben oder void
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
	
	// it will happen, that a pattern is in two lines. This function will find a line break in a pattern and merge both lines. 
	public static String handleLineBreakInPattern(String zeile, BufferedReader br)
	{
		String lastWord = getLastWord(br);
	}
	
	//returns the last word of a String
	public static String getLastWord(String zeile)
	{
		StringTokenizer st = new StringTokenizer(zeile);
		while(st.hasMoreTokens()) 
		{
		    	String word = st.nextToken();
		    	word = normalize(word);
		    	
		    	// this way, we filter a character sequence like ""
		    	if(word.length() > 0) hash(word); //TODO: hash schreiben  -> was soll es genau machen
	    }
	}

	private static String normalize(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i)<= 'Z' && word.charAt(i)>= 'A' ) {
				word.replace(word.charAt(i), (char) ('z'- 'Z' - word.charAt(i)));
			}
		}
		return word;
	}

}
