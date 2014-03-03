package classes;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;


public class TextGenerator {

	public static LinkedList<String> highFrequencyWords = new LinkedList<String>();
	public static LinkedList<String> contentWords = new LinkedList<String>();

	public static void main(String[] args) { 

		highFrequencyWords.add("hallo");
		highFrequencyWords.add("Nein");

		contentWords.add("Pelikan");
		contentWords.add("Hasse");

		PrintWriter pWriter = null; 
		try { 
			pWriter = new PrintWriter(new FileWriter("GenerierterText.txt")); 
			generateText(pWriter);
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (pWriter != null) 
				pWriter.flush(); 
		} 
	}

	private static void generateText(PrintWriter pWriter) {
		
		makeHighFrequent(pWriter);
		
		

	}

	private static void makeHighFrequent(PrintWriter pWriter)
	{
		for (String string : highFrequencyWords)
		{
			times(1000000, string, pWriter);
		}
	}

	private static void times(int i, String string, PrintWriter pWriter) {

		for ( int j = 0 ; j < i ; j++) {
			pWriter.println(string);
		}

	} 

}
