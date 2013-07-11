package classes;

import java.util.LinkedList;

public class PatternCollector 
{
	private LinkedList<String> legalPatternStructures = new LinkedList<String>();
	
	// we can use this hardcoded to add other pattern structures.
	public PatternCollector()
	{
		legalPatternStructures.add("121"); 	//CHC
		legalPatternStructures.add("1212");	//CHCH
		legalPatternStructures.add("1221");	//CHHC
		legalPatternStructures.add("2121"); //HCHC
	}
	
	// test, if a finished pattern is a legal one
	public boolean isLegalPattern(String patternStructure)
	{
		return legalPatternStructures.contains(patternStructure);
	}
	
	// test, if a part of a structure we already found is a prefix of any possible structure
	// if yes, it means we can go on searching
	public boolean isPrefixOfLegalPattern(String prefix)
	{
		// use for each loop for the LinkedList
		for(String structure: legalPatternStructures)
		{
			if(structure.startsWith(prefix)) return true;
		}
		return false;
	}
}
