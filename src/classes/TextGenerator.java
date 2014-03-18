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
		
//		randomStrukture(textFileWriter);

	}

	private static void randomStrukture(PrintWriter textFileWriter)
	{
		for (int i = 0; i < 100; i++)
		{
			int g = randGen.nextInt(1);
			
			switch (i)
			{
			case 0:
				makeRandomPattern(textFileWriter);
				break;
			case 1:
				int r = randGen.nextInt(60)+1;
				int v = randGen.nextInt(highFrequencyWords.size())+1;

				times(r, highFrequencyWords.get(v), textFileWriter);
				
			default:
				break;
			}
			
		}
	}

	private static void makeRandomPattern(PrintWriter textFileWriter)
	{
		int l = randGen.nextInt(legalPattern.size())+1;
		
		for (int i = 0; i < legalPattern.get(l).length(); i++)
		{
			switch (legalPattern.get(l).charAt(i))
			{
			case 'C':
				String c = (contentWords.get(randGen.nextInt(contentWords.size())));
				textFileWriter.print(c);
				break;
			case 'H':
				String h = (highFrequencyWords.get(randGen.nextInt(highFrequencyWords.size())));
				textFileWriter.print(h);
				break;
			default:
				break;
			}
		}
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
			times(6, highFrequencyWords.getFirst(), textFileWriter);
			loesungWriter.println();
		}
	}

	private static void makeHighFrequent(PrintWriter textFileWriter, PrintWriter loesungWriter)
	{
		for (String string : highFrequencyWords)
		{
//			TODO: von times die zahl automatisch bestimmen
			times(50000, string, textFileWriter);
		}
		textFileWriter.println();
	}

	private static void times(int i, String string, PrintWriter pWriter) {

		for ( int j = 0 ; j < i ; j++) {
			pWriter.print(string);
		}

	} 

}
