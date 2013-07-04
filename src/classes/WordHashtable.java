package classes;

import java.util.StringTokenizer;
import java.util.Arrays;

public class WordHashtable {
	
	Word[] table;
	int size;		// size is prefered to be a prime number
	int regularWords = 0; 
	
	public WordHashtable(int size)
	{ 
		if(size < 1)
		{
			System.out.println("\n\tConstructor can be used with positive argument only! Will use default value 100.");
			size = 100;
		}
		table = new Word[size];
		
		// initialize the whole array with the empty word, or there will be problems using predefined sort algorithms
		Arrays.fill(table, new Word(""));
		this.size = size; 
	}
	
	public void handleString(String zeile)
	{
		String word;
		// uses delimeters in default, perfect for our task
		StringTokenizer st = new StringTokenizer(zeile);
	    while(st.hasMoreTokens()) {
	    	word = st.nextToken();
	    	word = normalize(word);
	    	
	    	// this way, we filter a character sequence like ""
	    	if(word.length() > 0) hash(word);
	    }
	}
	
	// removes all characters that are neither digits nor letters
	public  String normalize(String word)
	{
		word = word.replaceAll("[^a-zA-Z0-9_&ßöäü]", "");
		return word;
	}
	
	/**MÜSSEN EVENTUELL AUF LONG UMSTEIGEN!*/
	public void hash(String word)
	{
		int hashValue = Math.abs(word.hashCode() % size);
		
		while(!isfull())
		{
			
			/* After hashing, if this index is not used yet, we will 
			 * just put the word here and set the counter to 1
			 */
			if(table[hashValue].isEmpty())
			{
				table[hashValue] = new Word(word);
				table[hashValue].increaseCounter();
				
				// if a new word was found, increade the class-counter
				regularWords++;
				return;
			}
			
			/* if there is a collision while hashing, that means the words are the same
			 * and the counter should be increased
			 */
			if(table[hashValue].sameWord(word))
			{
				table[hashValue].increaseCounter();
				return;
			}
			
			/* if this index is already taken and the both words are not the same
			 * we will have to look at the next place
			 */
			hashValue = (hashValue+1) % size;
		}
		System.out.println("\n\t.::Cannot hash anymore because the table is full!::.\n");
	}
	
	public boolean isfull()
	{
		boolean full = true;
		
		for(int i = 0; i < size; i++)
		{
			if(table[i].isEmpty()) full = false;
		}
		
		return full;
	}
	
	// simple output to console including the word with its appearence
	public void printHashtable()
	{
		for(int i = 0; i < size; i++)
		{
			if(!(table[i].isEmpty())) System.out.println(table[i].getWord() +"\t: " +table[i].getCounter());
		}
	}

	// modified mergesort. assert O(log n)
	public void sortHashtable()
	{
		Arrays.sort(table);
	}
	
	// remove empty slots
	public void removeEmptySlots()
	{
		Word[] newArray = new Word[regularWords];
		
		for(int i = 0; i < regularWords; i++)
		{
			newArray[i] = table[i];
		}
		table = newArray;
	}
}
