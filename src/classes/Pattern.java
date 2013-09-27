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

	/** TODO NEEDS TO BE TESTED */
	public boolean equals(Pattern otherPattern)
	{

		if(this.pattern.size() != otherPattern.pattern.size())	return false;

		for(int i = 0; i < pattern.size() ; i++)
		{
			if(!this.pattern.get(i).equals(otherPattern.pattern.get(i)))
			{
				return false;
			}

		}
		return true;
	}
	
	// Creates a real copy, not just references
	private void copyWordsIntoPattern(LinkedList<String> words)
	{
		int length = words.size();

		for(int i = 0; i < length; i++)
		{
			pattern.addLast(words.get(i));
		}
	}

	// just for visualization
	public String toString()
	{
		return pattern.toString();
	}
}
