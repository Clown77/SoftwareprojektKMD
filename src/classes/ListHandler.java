package classes;
import java.util.LinkedList;
public class ListHandler {
	LinkedList<LinkedList<Pattern>> patternCandidates;

	LinkedList<Pattern> M1Sorted;
	LinkedList<Pattern> M2Sorted;
	LinkedList<Pattern> M3Sorted;

	public ListHandler(LinkedList<LinkedList<Pattern>> patternCandidates)
	{
		this.patternCandidates = patternCandidates;

	}

	
//	sort pattern and add them in Mi in Pattern
	public void sortPatternCandidates() {
//		sort for m1
		for (int i = patternCandidates.size(); i>1; i--) {
			for (int j = 0; j < i-1; j++) {
				if (patternCandidates.get(j).get(0).getM1_Value()> patternCandidates.get(j+1).get(0).getM1_Value()) {
					swapPatternCandidates(j, j+1);
				}
			}
		}
//		add sorted to M1
		for (int i = 0; i < patternCandidates.size()-1; i++) {
			for (int j = 0; j < patternCandidates.get(i).size()-1; j++) {
				M1Sorted.add(patternCandidates.get(i).get(j));
			}
		}
//		sort for m2
		for (int i = patternCandidates.size(); i>1; i--) {
			for (int j = 0; j < i-1; j++) {
				if (patternCandidates.get(j).get(0).getM2_Value()> patternCandidates.get(j+1).get(0).getM2_Value()) {
					swapPatternCandidates(j, j+1);
				}
			}
		}
//		add sorted to M2
		for (int i = 0; i < patternCandidates.size()-1; i++) {
			for (int j = 0; j < patternCandidates.get(i).size()-1; j++) {
				M2Sorted.add(patternCandidates.get(i).get(j));
			}
		}
//		sort for m3
		for (int i = patternCandidates.size(); i>1; i--) {
			for (int j = 0; j < i-1; j++) {
				if (patternCandidates.get(j).get(0).getM3_Value()> patternCandidates.get(j+1).get(0).getM3_Value()) {
					swapPatternCandidates(j, j+1);
				}
			}
		}
//		add sorted to M3
		for (int i = 0; i < patternCandidates.size()-1; i++) {
			for (int j = 0; j < patternCandidates.get(i).size()-1; j++) {
				M3Sorted.add(patternCandidates.get(i).get(j));
			}
		}
	}
	
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

	public void swapPatternCandidates(int candidates1index, int candidates2index ) {
		LinkedList<Pattern> candidate1 = new LinkedList<Pattern>(patternCandidates.get(candidates1index));
		LinkedList<Pattern> candidate2 = new LinkedList<Pattern>(patternCandidates.get(candidates2index));
		patternCandidates.remove(patternCandidates.get(patternCandidates.indexOf(candidate1)));
		patternCandidates.remove(patternCandidates.get(patternCandidates.indexOf(candidate2)));
		patternCandidates.add(candidates1index, candidate2);
		patternCandidates.add(candidates2index, candidate1 );
	}





}
