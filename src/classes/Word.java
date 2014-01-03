package classes;

public class Word implements Comparable<Word>{

	private String word;
	private int counter = 0;
	private char kindOfWord = 'C';

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
	
	//	set kind of the current word
	public void setKindOfWord(char kindOfWord){this.kindOfWord = kindOfWord;}
	
	//	boolean functions for getting the occurrence of the word
	public boolean isContentWord(){ return this.kindOfWord == 'C'; }
	
	public boolean isEgalWord(){ return this.kindOfWord == 'N'; }
	
	public boolean isHighFrequencyWord(){ return this.kindOfWord == 'H'; }
	
	public char getKindOfWord(){ return this.kindOfWord; }

	public String toString(){
		return "" +this.kindOfWord;
	}

}

