package classes;

import java.util.LinkedList;

public class Pattern 
{
	public LinkedList<Word> PatternWords = new LinkedList<Word>();
	private String structure;
	
	// structure contains the order of C and H words in this pattern
	public Pattern(String structure) throws Exception
	{
		if(structure.matches("[1-2]*")) this.structure = structure;
		else throw new Exception("Invalid Pattern Structure in Constructor!");
	}
	
	public String getStructure(){ return this.structure; }

}
