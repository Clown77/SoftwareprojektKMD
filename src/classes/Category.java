package classes;

import java.util.LinkedList;

public class Category
{
	// clique will contain the bidirectional Pattern
	Pattern clique;

	// We have 2-Cliques. This Words are the two words of the clique
	String cliqueFirst;
	String cliqueSecond;

	// The category contains the clique and all Words that fit to that category
	LinkedList<String> category;

	// constructor
	public Category(Pattern biDirectionalPattern)
	{
		clique = biDirectionalPattern;

		cliqueFirst = clique.pattern.getFirst();
		cliqueSecond = clique.pattern.getLast();

		category = new LinkedList<String>();
		category.addAll(clique.pattern);
	}

	/**
	 * @description will search through the whole List of all Patterns and add
	 *              all words to the category that fulfill the conditions to be
	 *              part of the category. These conditions are: (1) At least
	 *              unidirectional connected to all words from the clique and
	 *              (2) bidirectional connected to at least one word of the
	 *              clique
	 * 
	 * @param finalPatternList
	 *            is the List of all Pattern that were collected doing the M1,
	 *            M2 and M3 measurements
	 * @param allWords
	 *            contains all the words from the finalPatternList as Strings
	 */
	public void fillCategory(LinkedList<Pattern> finalPatternList, LinkedList<String> allWords)
	{
		for (String word : allWords)
		{
			// don't add words to categories, that are already part of the category
			if(category.contains(word)) continue;
			
			// test if the word is unidirectional connected to all words of the clique	
			if (!uniDirectionalToAll(word, finalPatternList)) continue;

			// test if the word is bidirectional connected to all words of the clique
			if (!biDirectionalToOne(word, finalPatternList)) continue;
			
			// if the word passed all tests, add it to the category
			category.add(word);
		}
	}

	/** @description Tests if the given word is unidirectional connected to all
	 *              words in the clique
	 */
	private boolean uniDirectionalToAll(String word, LinkedList<Pattern> finalPatternList)
	{
		// If the word is unidirectional connected, there has to be any connection to the first word of the clique...
		if (!(hasArc(word, cliqueFirst, finalPatternList) || hasArc(cliqueFirst, word, finalPatternList)))
			return false;

		// ...and any connection to the second word of the clique.
		if (!(hasArc(word, cliqueSecond, finalPatternList) || hasArc(cliqueSecond, word, finalPatternList)))
			return false;

		return true;
	}

	/** @description Tests if the given word is bidirectional connected to at
	 *              least one word in the clique
	 */
	private boolean biDirectionalToOne(String word, LinkedList<Pattern> finalPatternList)
	{
		// there has to be a bidirectional connection to the first...
		if (hasArc(word, cliqueFirst, finalPatternList) && hasArc(cliqueFirst, word, finalPatternList))
			return true;

		// ... or the second word of the clique.
		if (hasArc(word, cliqueSecond, finalPatternList) && hasArc(cliqueSecond, word, finalPatternList))
			return true;

		return false;
	}

	/** @description Tests if there is a connection between the Strings "from" to "to".
	 */
	private boolean hasArc(String from, String to, LinkedList<Pattern> finalPatternList)
	{
		// First we create a FakePattern...
		LinkedList<String> ghostPattern = new LinkedList<String>();
		ghostPattern.addFirst(from);
		ghostPattern.addLast(to);

		Pattern fakePattern = new Pattern(ghostPattern);

		// for some reason, contains doesn't use my own equals method, so we need this for-each loop
		for (Pattern currentPattern : finalPatternList)
		{
			if(currentPattern.equals(fakePattern)) return true;
		}
		
		return false;
	}

	/** @description Tests if the offered Category contains >= 50 % of this
	 *              category AND this category contains >= 50 % of the offered category. 
	 *              If yes, it will merge both categories and return true, else returns false.
	 */
	public boolean offerCategory(LinkedList<String> offeredCategory)
	{
		LinkedList<String> differentWords = new LinkedList<String>();
		double identicalWordsOffered = 0;
		double identicalWordsThis = 0;
		
		// counts how many words of the offered category are contained in our category 
		for (String word : offeredCategory)	
		{
			if(containsWord(word)) identicalWordsOffered++;
			else differentWords.add(word);
		}
		
		// calculate the relation for the offeredCategory
		double ratioOffered = identicalWordsOffered/offeredCategory.size();
		
		// counts how many words of this category are contained in the offered category
		for (String word : category)
		{
			if(offeredCategory.contains(word)) identicalWordsThis++;
			else differentWords.add(word);
		}
		
		// calculate the relation for this category
		double ratioThis = identicalWordsThis/category.size();
		
		if((ratioThis >= 0.5) && (ratioOffered >= 0.5))
		{
			category.addAll(differentWords);
			return true;
		}
		
		return false;
	}

	/** @description Returns whether this category contains the given word or not.
	 */
	public boolean containsWord(String word)
	{
		for (String categoryWord : category)
		{
			if (categoryWord.equals(word))
				return true;
		}

		return false;
	}

	/** @description returns a String-representation of the Category
	 */
	public String toString()
	{
		return category.toString();
	}
	
	/** @description returns true, if both categories contain exact the same words as category */
	public boolean equals(Category otherCategory)
	{
		if(category.size() != otherCategory.category.size()) return false;
		
		for (String word : category)
		{
			if(!otherCategory.containsWord(word)) return false;
		}
		
		// Same size and all words contained in each other --> true
		return true;
	}

}
