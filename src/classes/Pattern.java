package classes;

import java.util.LinkedList;

public class Pattern 
{

	public LinkedList<String> pattern;
	
	private int counter;
	
	private double M1_Value = 0;
	private double M2_Value = 0;
	private double M3_Value = 0;

	// Take the list of words that are a pattern and save it
	public Pattern(LinkedList<String> words)
	{
		pattern = new LinkedList<String>();
		copyWordsIntoPattern(words);
		counter = 1;
	}

	public boolean equals(Pattern otherPattern)
	{

		if(this.pattern.size() != otherPattern.pattern.size())	return false;

		for(int i = 0; i < pattern.size() ; i++)
		{
			if(this.pattern.get(i) != otherPattern.pattern.get(i)) return false;
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
	
	
	// Getters and Setters 
	
	public void increaseCounter() { counter++; } 
	
	public int getCounter() { return counter; }

	public double getM1_Value() { return M1_Value; }

	public void setM1_Value(double m1_Value) { M1_Value = m1_Value; }

	public double getM2_Value() { return M2_Value; }

	public void setM2_Value(double m2_Value) { M2_Value = m2_Value; }

	public double getM3_Value() { return M3_Value; }

	public void setM3_Value(double m3_Value) { M3_Value = m3_Value; }
	
}
