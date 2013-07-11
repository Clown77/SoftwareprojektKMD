package classes;

public class Word implements Comparable<Word>{

	private String word;
	private int counter = 0;
	private int kindOfWord = 1;
	
	// Constructor overload
	public Word(){};
	public Word(String word){ this.word = word; }
	
	public void setWord(String word){ this.word = word; }
	
	public void increaseCounter(){ counter++; }
	
	public String getWord(){ return word; }
	
	public int getCounter(){ return counter; }
	
	// overload
	public boolean sameWord(String word){return this.word.compareTo(word) == 0; }
	public boolean sameWord(Word word){return this.word.compareTo(word.word) == 0; }
	
	public boolean isEmpty(){ return word == ""; }

	// implement Comparable interface, so we can use java sort algorithms
	public int compareTo(Word word) {
		
		// swap to change from asc to desc
		if(this.counter > word.counter) return -1;
		
		if(this.counter < word.counter) return 1;
		
		return 0;
	}
	
	
	/**
	 * @param kindOfWord 	0 = mark as nothing
	 * 						1 = mark as content word
	 * 						2 = mark as high frequency word				
	 */
	public void setKindOfWord(int kindOfWord){this.kindOfWord = kindOfWord;}
	
	public int getKindOfWord(){return this.kindOfWord;}
	
}
