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

		System.out.println("Postprocessing word categories has been successfull.");

		//ourHash.tableToString();
		// The patternfinder can work on the text and return us a list with all patterns we want to search for
		PatternFinder patternfinder = new PatternFinder(path, WORDNUMBER);

		// in this step the patternfinder collects all patterns. Including High frequency words
		patternfinder.findAllPattern(ourHash);
		LinkedList<LinkedList<Pattern>> foundPattern = patternfinder.getFoundPattern();
		System.out.println("Without High-Frequency Words: " +foundPattern.toString());

		for(int i = 0; i < foundPattern.size(); i++)
		{
			SinglePatternGraph spg = new SinglePatternGraph(foundPattern.get(i));
		}

		System.out.println("Alle Pattern haben den Wert M1 = : " +foundPattern.get(0).get(0).getM1_Value());
		System.out.println("Alle Pattern haben den Wert M2 = : " +foundPattern.get(0).get(0).getM2_Value());
		System.out.println("Alle Pattern haben den Wert M3 = : " +foundPattern.get(0).get(0).getM3_Value());

		ListHandler listHandler = new ListHandler(foundPattern);
		listHandler.sortPatternCandidates();

		System.out.println(listHandler.M1Sorted);
		System.out.println(listHandler.M2Sorted);
		System.out.println(listHandler.M3Sorted);

		System.out.println("M1 of CHCH"+foundPattern.get(0).get(0).getM1_Value());
		System.out.println("M2 of CHCH"+foundPattern.get(0).get(0).getM2_Value());
		System.out.println("M3 of CHCH"+foundPattern.get(0).get(0).getM3_Value());

		System.out.println("M1 of CHC"+foundPattern.get(1).get(0).getM1_Value());
		System.out.println("M2 of CHC"+foundPattern.get(1).get(0).getM2_Value());
		System.out.println("M3 of CHC"+foundPattern.get(1).get(0).getM3_Value());

		System.out.println("M1 of CHHC"+foundPattern.get(2).get(0).getM1_Value());
		System.out.println("M2 of CHHC"+foundPattern.get(2).get(0).getM2_Value());
		System.out.println("M3 of CHHC"+foundPattern.get(2).get(0).getM3_Value());

		System.out.println("M1 of HCHC"+foundPattern.get(3).get(0).getM1_Value());
		System.out.println("M2 of HCHC"+foundPattern.get(3).get(0).getM2_Value());
		System.out.println("M3 of HCHC"+foundPattern.get(3).get(0).getM3_Value());


		long programTimeNeeded = (System.currentTimeMillis() - programmStartTime)/1000;

		System.out.println("Total time needed: " +programTimeNeeded +" seconds");
	}

	//Diese Funktion führt dazu, dass Zeilenumbr�che nicht zu zwei getrennten Wörtern führen.
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