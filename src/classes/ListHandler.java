package classes;

import java.util.Collection;
import java.util.LinkedList;

public class ListHandler {
	LinkedList<LinkedList<Pattern>> patternCandidates;
	// TODO: In Speicher erst inizialisieren wen es gebraucht wird;
	LinkedList<Pattern> M1Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M2Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M3Sorted = new LinkedList<Pattern>();

	/**Top x of each list remain*/
	private int zT = 100;
	/**Last x of each list to delete*/
	private int zB = 20;
	
	public ListHandler(LinkedList<LinkedList<Pattern>> patternCandidates) {
		this.patternCandidates = patternCandidates;

	}

	/**
	 * {@code}search for last MSorted zB and delete in other MSorted 
	 * @param mType Type of MSorted to opperate
	 * @throws Exception if getSortedM gets an invalid mType
	 */
	public void removeDoubleM(int mType) throws Exception {
		for (int i = getSortedM(mType).size(); i > zB; i--) {
			for (int j = 0; j < getSortedM((mType%3)+1).size(); j++) {
				if (getSortedM(mType).getLast().equals(getSortedM((mType%3)+1).get(j))) {
					getSortedM((mType%3)+1).remove(j);
					break;
				}
			}
			for (int k = 0; k < getSortedM((mType%3)+2).size(); k++) {
				if (getSortedM(mType).getLast().equals(getSortedM((mType%3)+2).get(k))) {
					getSortedM((mType%3)+2).remove(k);
					break;
				}
			}
			getSortedM(mType).removeLast();
		}
	}

	/**
	 * {@code}short MSorted to zT
	 * @param mType Type of MSorted to opperate
	 * @throws Exception if getSortedM gets an invalid mType
	 */
	public void clearZT_M(int mType) throws Exception {
		if (getSortedM(mType).size()>=zT) {
			int sizeM = getSortedM(mType).size();
			for (int i = 0; i < sizeM-zT; i++) {
				getSortedM(mType).removeLast();
			}
		}
	}

	/**
	 * @param mType Type of MSorted to opperate
	 * @return MSorted
	 * @throws Exception gets an invalid mType
	 */
	public LinkedList<Pattern> getSortedM(int mType) throws Exception {
		switch (mType) {
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


}
