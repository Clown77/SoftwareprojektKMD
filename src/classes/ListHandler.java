package classes;

import java.util.Collection;
import java.util.LinkedList;

public class ListHandler {
	LinkedList<LinkedList<Pattern>> patternCandidates;
	// TODO: In Speicher erst inizialisieren wen es gebraucht wird;
	LinkedList<Pattern> M1Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M2Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M3Sorted = new LinkedList<Pattern>();

	public ListHandler(LinkedList<LinkedList<Pattern>> patternCandidates) {
		this.patternCandidates = patternCandidates;

	}

	// sort pattern and add them in M(mType) in Pattern
	public void sortPatternCandidatesM(int mType) {
		@SuppressWarnings("unchecked")
		LinkedList<LinkedList<Pattern>> copyPatternCandidates = (LinkedList<LinkedList<Pattern>>) patternCandidates.clone();
		while (!copyPatternCandidates.isEmpty()) {
			int smalestValue = 0;
			for (LinkedList<Pattern> linkedList : copyPatternCandidates) {
				if (linkedList.get(0).getM_Value(mType) < copyPatternCandidates.get(smalestValue).get(0).getM_Value(mType)) {
					smalestValue = copyPatternCandidates.indexOf(linkedList);
				}
			}
			switch (mType) {
			case 1:M1Sorted.addAll(copyPatternCandidates.get(smalestValue));
				break;
			case 2:M2Sorted.addAll(copyPatternCandidates.get(smalestValue));
				break;
			case 3:M3Sorted.addAll(copyPatternCandidates.get(smalestValue));
				break;

			default:
				throw new RuntimeException("wrong M Value");
			}
			copyPatternCandidates.remove(smalestValue);
		}
	}

	public void cleanSortedLists() {
		// clean M1
		if (M1Sorted.size() <= 100)
			;
		else
			M1Sorted = (LinkedList<Pattern>) M1Sorted.subList(
					M1Sorted.size() - 100, M1Sorted.size());
		// clean M2
		if (M2Sorted.size() <= 100)
			;
		else
			M2Sorted = (LinkedList<Pattern>) M2Sorted.subList(
					M2Sorted.size() - 100, M2Sorted.size());
		// clean M3
		if (M3Sorted.size() <= 100)
			;
		else
			M3Sorted = (LinkedList<Pattern>) M3Sorted.subList(
					M3Sorted.size() - 100, M3Sorted.size());
	}
}
