package classes;

import java.util.LinkedList;

public class Pattern 
{
	
	public LinkedList<String> pattern;

	// Take the list of words that are a pattern and save it
	public Pattern(LinkedList<String> words)
	{
		pattern = new LinkedList<String>();
		copyWordsIntoPattern(words);
	}
	
	private void copyWordsIntoPattern(LinkedList<String> words)
	{
		copyLists(words, pattern);
	}
	
	// just for visualization
	public String toString()
	{
		return pattern.toString();
	}
	
	// This methode makes a real copy and not just a reference
	public void copyLists(LinkedList<String> list1, LinkedList<String> list2)
	{
		int length = list1.size();
		
		for(int i = 0; i < length; i++)
		{
			list2.addLast(list1.get(i));
		}
	}
}
