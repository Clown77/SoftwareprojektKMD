package classes;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Pattern;


public class TextGenerator {

	public static LinkedList<String> highFrequencyWords = new LinkedList<String>();
	public static LinkedList<String> contentWords = new LinkedList<String>();

	static Random randGen = new Random();

	static LinkedList<String> legalPattern = new LinkedList<String>();



	public static void main(String[] args) { 

		highFrequencyWords.add("hallo ");
		highFrequencyWords.add("Nein ");
		highFrequencyWords.add("Essen ");

		contentWords.add("Pelikan ");
		contentWords.add("Hasse ");
		contentWords.add("Trinken ");

		legalPattern.addLast("CHCH");
		legalPattern.addLast("CHC");
		legalPattern.addLast("CHHC");
		legalPattern.addLast("HCHC");

		PrintWriter textFileWriter = null;
		PrintWriter loesungWriter = null;
		try { 
			textFileWriter = new PrintWriter(new FileWriter("GenerierterText.txt"));
			loesungWriter = new PrintWriter(new FileWriter("Lösung.txt"));
			generateText(textFileWriter, loesungWriter);
			loesungWriter.println("highfrequent Wörter " +highFrequencyWords);
			loesungWriter.println("content Wörter " +contentWords);

		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (textFileWriter != null) 
				textFileWriter.flush();
			if (loesungWriter != null) 
				loesungWriter.flush(); 
		} 
	}

	private static void generateText(PrintWriter textFileWriter, PrintWriter loesungWriter) {

		//TODO: willkürlicher machen
		makeHighFrequent(textFileWriter, loesungWriter);

		makePattern(textFileWriter, loesungWriter);

	}

	private static void makePattern(PrintWriter textFileWriter, PrintWriter loesungWriter)
	{
		for (String patterncandidate : legalPattern)
		{
			loesungWriter.println("Pattern "+ patterncandidate);
			for (int i = 0; i < patterncandidate.length(); i++)
			{
				switch (patterncandidate.charAt(i))
				{
				case 'C':
					String c = (contentWords.get(randGen.nextInt(contentWords.size())));
					textFileWriter.print(c);
					loesungWriter.print(c);
					break;
				case 'H':
					String h = (highFrequencyWords.get(randGen.nextInt(highFrequencyWords.size())));
					textFileWriter.print(h);
					loesungWriter.print(h);
					break;
				default:
					break;
				}
			}
			loesungWriter.println();
		}
	}

	private static void makeHighFrequent(PrintWriter textFileWriter, PrintWriter loesungWriter)
	{
		for (String string : highFrequencyWords)
		{
			times(WordHashtable.getTH(), string, textFileWriter);
		}
		textFileWriter.println();
	}

	private static void times(int i, String string, PrintWriter pWriter) {

		for ( int j = 0 ; j < i ; j++) {
			pWriter.print(string);
		}

	} 

}
