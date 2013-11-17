package classes;
import java.util.Collection;
import java.util.LinkedList;
public class ListHandler {
	LinkedList<LinkedList<Pattern>> patternCandidates;
	LinkedList<Pattern> sortetList;

	LinkedList<Pattern> M1Sorted;
	LinkedList<Pattern> M2Sorted;
	LinkedList<Pattern> M3Sorted;
	LinkedList<Pattern> patternM1 = new LinkedList<Pattern>();

	public ListHandler(LinkedList<LinkedList<Pattern>> patternCandidates)
	{
		this.patternCandidates = patternCandidates;

	}
	
	//		sort pattern and add them in Mi in Pattern
	public void sortPatternCandidatesM(int mType) {
		//		sort for m1
		sort(mType);
		//		add sortetList to MiSorted
		for (Pattern currentList : sortetList) 
		{
			switch (mType) {
			case 1:M1Sorted.add(currentList);
				break;
			case 2:M2Sorted.add(currentList);
				break;
			case 3:M3Sorted.add(currentList);
				break;

			default:
				throw new RuntimeException("wrong M Value");
			}
		}
		sortetList.clear();
	}

	private void sort(int mType) {
//		TODO: adding pattern from patternCandidates to Linkedlist<Pattern> is not possible
		for (LinkedList<Pattern> PatternLink : patternCandidates) {
			sortetList.addAll(PatternLink); //throws Nullpointer Exeption becouse foundpattern semms empty
		}
//		TODO: sort sortedList
	}

//	A Version of Felix (not working, same Problem withe NullpointerExeption)
	//	public void sortPatternCandidatesM(int mType) {
	//
	//		for (LinkedList<Pattern> currentLinked : patternCandidates) {
	//			for (Pattern pattern : currentLinked) {
	//				patternM1.add(pattern);
	//			}
	//		}
	//
	//		M1Sorted.add(patternM1.getFirst());
	//
	//		for(int i = 1; i < patternM1.size(); i++){
	//
	//			for(int j = 0; j < M1Sorted.size(); j++){
	//
	//				if(patternM1.get(i).getM_Value(mType) < M1Sorted.get(j).getM_Value(mType)) 
	//					M1Sorted.add(j, patternM1.get(i));
	//				else M1Sorted.addLast(patternM1.get(i));
	//			}
	//		}
	//
	//	}

	public void cleanSortedLists()
	{
		//		clean M1
		if(M1Sorted.size()<=100)
			;
		else
			M1Sorted =  (LinkedList<Pattern>) M1Sorted.subList(M1Sorted.size()-100, M1Sorted.size());
		//		clean M2
		if(M2Sorted.size()<=100)
			;
		else
			M2Sorted = (LinkedList<Pattern>) M2Sorted.subList(M2Sorted.size()-100, M2Sorted.size());
		//		clean M3
		if(M3Sorted.size()<=100)
			;
		else
			M3Sorted = (LinkedList<Pattern>) M3Sorted.subList(M3Sorted.size()-100, M3Sorted.size());
	}
}
