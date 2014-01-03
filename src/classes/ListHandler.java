package classes;

import java.util.LinkedList;

public class ListHandler 
{
	LinkedList<LinkedList<Pattern>> patternCandidates;
	
	LinkedList<Pattern> M1Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M2Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M3Sorted = new LinkedList<Pattern>();
	
	LinkedList<Pattern> finalList = new LinkedList<Pattern>();
	LinkedList<Pattern> biDirectionalList = new LinkedList<Pattern>();

	/** @zT The top zT Elements of the MxSorted Lists will remain */
	private int zT = 100;
	/** @zB The bottom zB ELements of the MxSorted Lists will be deleted */
	private int zB = 20;
	
	
	// Constructor
	public ListHandler(LinkedList<LinkedList<Pattern>> patternCandidates) 
	{
		this.patternCandidates = patternCandidates;
	}

	public LinkedList<Pattern> getBidirectionalPattern()
	{
		return biDirectionalList;
	}
	
	public LinkedList<Pattern> getFinalList()
	{
		return finalList;
	}

	/** @description Returns the List M<mType>Sorted
	 */
	public LinkedList<Pattern> getSortedM(int mType) throws Exception 
	{
		switch (mType) 
		{
		case 1:
			return M1Sorted;
		case 2: 
			return M2Sorted;
		case 3:
			return M3Sorted;
		default:
			throw new Exception("invalid mType");
		}
	}

	/** 
	 * sort pattern and add them in M(mType) in Pattern
	 * @param mType Type of MSorted to operate
	 */
	public void sortPatternCandidatesM(int mType) 
	{
		@SuppressWarnings("unchecked")
		LinkedList<LinkedList<Pattern>> patternCandidatesCopy = (LinkedList<LinkedList<Pattern>>) patternCandidates.clone();
		
		while (!patternCandidatesCopy.isEmpty()) 
		{
			int smallestValue = 0;
			
			for (LinkedList<Pattern> linkedList : patternCandidatesCopy) 
			{
				if (linkedList.get(0).getM_Value(mType) < patternCandidatesCopy.get(smallestValue).get(0).getM_Value(mType)) 
				{
					smallestValue = patternCandidatesCopy.indexOf(linkedList);
				}
			}
			
			switch (mType) 
			{
			case 1:	M1Sorted.addAll(patternCandidatesCopy.get(smallestValue));
					break;
			case 2:	M2Sorted.addAll(patternCandidatesCopy.get(smallestValue));
					break;
			case 3:	M3Sorted.addAll(patternCandidatesCopy.get(smallestValue));
					break;
			default:
				throw new RuntimeException("wrong M Value");
			}
			
			patternCandidatesCopy.remove(smallestValue);
		}
		
		switch(mType)
		{
			case 1: System.out.println("Sortierte Liste M" +mType +": " +M1Sorted); break;
			case 2: System.out.println("Sortierte Liste M" +mType +": " +M2Sorted); break;
			case 3: System.out.println("Sortierte Liste M" +mType +": " +M3Sorted); break;
			default: ;
		}
	}

	/**@description Builds the final List using the filtered Lists M1, M2 and M3 and zT. 
	 */
	public void createFinalList() throws Exception 
	{
		for(int i = 1; i <= 3; i++)
		{
			if(getSortedM(i).size() > zT)
			{
				for(int j = 0; j < zT; j++)
				{
					finalList.add(getSortedM(i).get(j));
				}
			}
			else
			{
				finalList.addAll(getSortedM(i));
			}
		}
		
		deleteDoublePattern();
		deletePhantomPattern();
		
		System.out.println("FinalList: " +finalList.toString());
		
		return;
	}

	/** @description Collects all Bidirectional Pattern from the finalList.
	 */
	public void collectBidirectionalPattern() 
	{
		for (int i = 0; i < finalList.size(); i++) 
		{
			for (int j = i; j < finalList.size(); j++) 
			{
				if (finalList.get(i).pattern.getFirst().equals(finalList.get(j).pattern.getLast()) 
					&&finalList.get(i).pattern.getLast().equals(finalList.get(j).pattern.getFirst()))
				{
					biDirectionalList.add(finalList.get(i));
					// Use this if you want to visualize this as a graph: 
					// biDirectionalList.add(finalList.get(j));
				}
			}
		}
	}

	/** @description Applies the Filter zB on the M1, M2 and M3 Lists
	 */
	public void removePatternOfZB() throws Exception 
	{
		// We cannot apply the zB filter if there are not even zB elements in the lists
		if(M1Sorted.size() < zB)
		{
			System.out.println("The zB-Filter was ignored, because there are not enough pattern found.");
			return;
		}
		
		LinkedList<Pattern> ZBElements = new LinkedList<Pattern>();
		
		for(int i = 0 ; i < zB; i++)
		{
			for(int j = 1; j <= 3; j++)
			{
				
				boolean contains = false;
				
				for (Pattern currentPattern : ZBElements) 
				{
					if(currentPattern.equals(getSortedM(j).getLast()))
					{
						contains = true;
						break;
					}
				}
				
				if(!contains) ZBElements.add(getSortedM(j).getLast());
				getSortedM(j).removeLast();
			}
		}
		
		for (Pattern currentPattern : ZBElements) 
		{
			for(int i = 1; i <= 3; i++) getSortedM(i).remove(currentPattern);
		}
		
	}

	/**
	 * @description Deletes double pattern in finalList.
	 */
	private void deleteDoublePattern() 
	{	
		LinkedList<Pattern> toDelete = new LinkedList<Pattern>();
		
		for (int i = 0; i < finalList.size(); i++) 
		{
			for (int j = i+1; j < finalList.size(); j++) 
			{
				if (finalList.get(i).equals(finalList.get(j))) 
				{
					toDelete.add(finalList.get(j));
					
					// if it found itself once, break! Else it will delete all occurrences of itself
					break;
				}
			}
		}
		
		for (Pattern pattern : toDelete) 
		{
			finalList.remove(pattern);
		}
	}
	
	/** @description PhantomPattern are Pattern, that contain all twice the same content words. The will form categories with exactly the same words, we don't want that.
	 * 				 So we remove them.
	 */
	private void deletePhantomPattern()
	{
		LinkedList<Pattern> phantomPattern = new LinkedList<Pattern>();
		
		for (Pattern currentPattern : finalList)
		{
			if(currentPattern.pattern.getFirst().equals(currentPattern.pattern.getLast())) phantomPattern.add(currentPattern);
		}
		
		for (Pattern pattern : phantomPattern)
		{
			finalList.remove(pattern);
		}
		
		return;
	}
}
