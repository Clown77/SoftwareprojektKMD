package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class PatternFinder 
{
	// Means the pattern has to occure TP times in 1 million words
	private final int TP = 10;
	
	String path;
	
	// Contains all possible patterns
	private LinkedList<String> legalPattern;

	// Contains all pattern that were found in the text
	private LinkedList<Pattern> foundPattern;

	/** Here you can add more patterns for experiments
	 */
	public PatternFinder(String path)
	{
		this.path = path;
		foundPattern = new LinkedList<Pattern>();
		legalPattern = new LinkedList<String>();
		legalPattern.addLast("CHCH");	// CHCH 
		legalPattern.addLast("CHC");	// CHC 
		legalPattern.addLast("CHHC");	// CHHC
		legalPattern.addLast("HCHC");	// HCHC 
	}

	// we will now search in the text for all patterns and save them in foundPattern
	public void findAllPattern(WordHashtable ourHash) throws Exception
	{
		// tests if there are pattern doubled or with more then two Content words (and removes them)
		removeIllegalPattern();

		String text = readInFile();

		while(!legalPattern.isEmpty())
		{
			String currentPatternStructure = legalPattern.getFirst();
			legalPattern.removeFirst();

			// number of words we will be looking for
			int patternLength = currentPatternStructure.length();

			// this list will contain the words we are currently looking at
			LinkedList<String> currentWords = new LinkedList<String>();

			// search for words
			StringTokenizer st = new StringTokenizer(text);

			// for i times, give me the next word
			for(int i = 0; i < patternLength; i++)
			{
				if(st.hasMoreTokens())
				{
					currentWords.add(st.nextToken());
				}
			}

			// if a pattern is found, add it to the list
			if(isPattern(currentWords, currentPatternStructure, ourHash))
			{
				foundPattern.add(new Pattern(currentWords));
			}


			// take the next i words
			while(st.hasMoreTokens() && !currentWords.isEmpty())
			{
				currentWords.removeFirst();
				currentWords.add(st.nextToken());

				// if a pattern is found, add it to the list
				if(isPattern(currentWords, currentPatternStructure, ourHash))
				{
					// patternInList increases Pattern counter if true
					if (!patternInList(currentWords)) 
					{
						foundPattern.add(new Pattern(currentWords));
					}

				}
			}
		}
	}
	
	// patternInList increases Pattern counter if true
	private boolean patternInList(LinkedList<String> test) 
	{
		for (Pattern testingPattern : foundPattern) 
		{
			if (testingPattern.equals(new Pattern(test))) 
			{
				testingPattern.increaseCounter();
				return true;
			}
		}
		return false;
	}


	// delete all symbols etc. that we don't need
	public  String normalize(String word)
	{
		// SPACE NEEDED
		word = word.replaceAll("[^a-zA-Z\t ]", "");
		return word;
	}

	public String readInFile() throws Exception
	{
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);

		// we fill the whole text into 'text' --> maybe change it later for better performance
		String text = "";
		String zeile = br.readLine();

		while(zeile != null)
		{
			text = text.concat(zeile +" ");
			zeile = br.readLine();
		}

		// we have to edit the text like we did with the words for the hashtable
		text = text.toLowerCase();
		text = normalize(text);

		br.close();
		fr.close();

		return text;
	}

	// returns if it is a pattern or not
	public boolean isPattern(LinkedList<String> currentWords, String currentPatternStructure, WordHashtable ourHash)
	{

		int i = 0;

		LinkedList<String> currentWordsCopy = new LinkedList<String>();
		copyLists(currentWords, currentWordsCopy);

		// end if the list is empty
		while(!currentWordsCopy.isEmpty())
		{
			// take the first word of the list and remove it 
			String temp = currentWordsCopy.getFirst();
			currentWordsCopy.removeFirst();

			// get the type of the word and compare it to the pattern we are looking for
			String type = "" +ourHash.getKindOfWord(temp);
			if(type.charAt(0) != currentPatternStructure.charAt(i)) return false;

			// increase the word counter
			i++;
		}

		// there was no mismatch found
		return true;
	}

	// we don't want to work on references, so we copy the list
	public void copyLists(LinkedList<String> list1, LinkedList<String> list2)
	{
		int length = list1.size();

		for(int i = 0; i < length; i++)
		{
			list2.addLast(list1.get(i));
		}
	}


	// We don't need the High Frequency words anymore, so we return a copy of our pattern that just contains the Content words
	public void removeHighFrequencyWords(WordHashtable ourHash)
	{
		for (Pattern currentPattern : foundPattern) {

			int length = currentPattern.pattern.size();

			for(int i = (length-1); i >= 0; i--)
			{
				if(ourHash.getKindOfWord(currentPattern.pattern.get(i)) == 'H')
				{
					currentPattern.pattern.remove(i);
				}
			}
		}
	}

	/**@return Returns null, if the methode 'findAllPattern' has not been used before.*/
	public LinkedList<Pattern> getFoundPattern()
	{
		return foundPattern;
	}

	// Patterns with more then 2 content words cannot be used for M1. So we remove them
	public void removeIllegalPattern()
	{
		int length = legalPattern.size();
		for(int i = length-1; i >= 0; i--)
		{
			if(legalPattern.get(i).replaceAll("H", "").length() > 2)
			{
				System.out.println("Illegal Pattern has been removed: " +legalPattern.get(i));
				legalPattern.remove(i);
			}
		}
	}
	
	// Can be used only, if the method 'findAllPattern' has been calles before
	public void removeLowAppearencePattern(final int WORDNUMBER)
	{
		// Change TP for tests
		double ratio = TP/1000000.0;
		double patternRatio = 0;
		
		for (int i = (foundPattern.size()-1); i >= 0; i--) 
		{
			patternRatio = (double)(foundPattern.get(i).getCounter())/WORDNUMBER;
			if(patternRatio < ratio) foundPattern.remove(i);
		}
	}
}
