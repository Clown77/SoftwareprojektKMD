package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class PatternFinder 
{
	// Contains all possible patterns
	LinkedList<String> legalPattern;
	
	// Contains all pattern that were found in the text
	LinkedList<Pattern> foundPattern;
	
	/** Here you can add more patterns for experiments
	 */
	public PatternFinder()
	{
		foundPattern = new LinkedList<Pattern>();
		legalPattern = new LinkedList<String>();
		legalPattern.addLast("1212");	// CHCH
		legalPattern.addLast("121");	// CHC
		legalPattern.addLast("1221");	// CHHC
		legalPattern.addLast("2121");	// HCHC
	}
	
	// we will now search in the text for all patterns and save them in foundPattern
	public void findPattern(WordHashtable ourHash) throws Exception
	{
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
				System.out.println(currentWords); 
				
				currentWords.removeFirst();
				currentWords.add(st.nextToken());
				
				// if a pattern is found, add it to the list
				if(isPattern(currentWords, currentPatternStructure, ourHash))
				{
					foundPattern.add(new Pattern(currentWords));
				}
			}
		}
		
		 System.out.println("PATTERN: " +foundPattern.toString());
			
	}
	
	
	// delete all symbols etc. that we don't need
	public  String normalize(String word)
	{
		// SPACE NEEDED
		word = word.replaceAll("[^a-zA-Z0-9_&ßöäü ]", "");
		return word;
	}
	
	public String readInFile() throws Exception
	{
		FileReader fr = new FileReader("kindleDocuments/Gesamttext/gesamttext.txt");
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
	 	
	 	System.out.println(text);
	 	
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
			System.out.print(type);
			if(type.charAt(0) != currentPatternStructure.charAt(i)) return false;
			
			// increase the word counter
			i++;
		}
		
		// there was no mismatch found
		return true;
	}
	
	
	public void copyLists(LinkedList<String> list1, LinkedList<String> list2)
	{
		int length = list1.size();
		
		for(int i = 0; i < length; i++)
		{
			list2.addLast(list1.get(i));
		}
	}
}
