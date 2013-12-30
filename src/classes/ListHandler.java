package classes;

import java.util.Collection;
import java.util.LinkedList;

import javax.naming.BinaryRefAddr;
import javax.swing.UIDefaults.LazyInputMap;

public class ListHandler {
	LinkedList<LinkedList<Pattern>> patternCandidates;
	// TODO: In Speicher erst inizialisieren wen es gebraucht wird;
	LinkedList<Pattern> M1Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M2Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> M3Sorted = new LinkedList<Pattern>();
	LinkedList<Pattern> completeList = new LinkedList<Pattern>();
	LinkedList<Pattern> bydirectionalList;
	LinkedList<LinkedList<Pattern>> completeClique = new LinkedList<LinkedList<Pattern>>();

	/**
	 * Top x of each list remain
	 */
	private int zT = 100;
	/**
	 * Last x of each list to delete
	 */
	private int zB = 20;
	
	public ListHandler(LinkedList<LinkedList<Pattern>> patternCandidates) {
		this.patternCandidates = patternCandidates;

	}

	public void clearAllM() throws Exception {
		
		for (int i = 1; i < 3; i++) {
			removeDoubleM(i);
			clearZT_M(i);
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

	/** 
	 * sort pattern and add them in M(mType) in Pattern
	 * @param mType Type of MSorted to opperate
	 */
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
	
	public void generateCliques() {
		inizializeCompleteList();
		findBydirectionalList();
		searchCompleteClique();
	}
	
	/**
	 * {@code}inizialise completeList
	 * @param completeList = M1Sorted + M2Sorted + M3Sorted
	 */
	private void inizializeCompleteList() {
		
		completeList.addAll(M1Sorted);
		completeList.addAll(M2Sorted);
		completeList.addAll(M3Sorted);
		deleteDoublePattern();
	}

	/**
	 * searches for binary pairs in the completeList
	 * @param completeList is the complete List of found Pattern after m1,m2,m3
	 * @return LinkedList<Pattern> twoBinaryList is a list of pairs of Pattern which are binary conected
	 */
	private void findBydirectionalList() {
		
		LinkedList<Pattern> bydirectionalList = new LinkedList<Pattern>();
		
		for (int i = 0; i < completeList.size(); i++) {
			for (int j = i; j < completeList.size(); j++) {
				if (completeList.get(i).pattern.getFirst().equals(completeList.get(j).pattern.getLast()) 
					&&completeList.get(i).pattern.getLast().equals(completeList.get(j).pattern.getFirst())){
					bydirectionalList.add(completeList.get(i));
				}
			}
		}
	}
	
	/**
	 * creates completeCluster and fills it whith all connections of Words from twoBinaryList 
	 * and cleans then the ones whithout at least one binary Conection and conections to all Words
	 */
	private void searchCompleteClique() {
		
		fillCompleteClique();
		
		cleanCompleteClique();
		
	}

	/**
	 * creates completeCluster and fills it whith all connections of Words from twoBinaryList 
	 */
	private void fillCompleteClique() {
		
		for (int i = 0; i < completeList.size(); i++) {
			// add first element for checking
			completeClique.add(new LinkedList<Pattern>());
			completeClique.getLast().add(completeList.get(i));
			for (int j = i+1; j < completeList.size(); j++) {
				if (completeList.get(i).patternHasSameString(completeList.get(j))) {
					completeClique.getLast().add(completeList.get(j));
				}
			}
			if (completeClique.getLast().size() == 1) {
				completeClique.removeLast();
			}
		}
		
	}

	/**
	 * cleans then the ones whithout at least one binary Conection and conections to all Words
	 */
	private void cleanCompleteClique() {
		
		deleteDoubleConections();
		
		for (LinkedList<Pattern> Clique : completeClique) {
			for (Pattern pattern : Clique) {
				if(!isInBydirectionalList(pattern)){
					Clique.remove(pattern);
					
				}
				if(!hasConectionToAllPattern(pattern, Clique)){
					Clique.remove(pattern);
				}
			}
			if (Clique.size()<=1) {
				completeClique.remove(Clique);
			}
			
		}
		
	}

	/**
	 * deletes one of the conections which are bydirectional becouse we need just one conection
	 */
	private void deleteDoubleConections() {
		for (LinkedList<Pattern> Clique : completeClique) {
			for (Pattern pattern : Clique) {
				for (Pattern pattern2 : bydirectionalList) {
					if (pattern.equals(pattern2)) {
						Clique.remove(pattern);
					}
				}
			}
		}
	}

	private boolean hasConectionToAllPattern(Pattern pattern, LinkedList<Pattern> Clique) {
		int conectionCOunter = 0;
		for (Pattern curentPattern : Clique) {
			if (pattern.patternHasSameString(curentPattern)) {
				conectionCOunter += 1;
			}
		}
		if (conectionCOunter < completeClique.size()) {
			return false;
		}
		return true;
	}

	private boolean isInBydirectionalList(Pattern pattern) {
		
		for (Pattern curentPattern : bydirectionalList) {
			if (pattern.patternHasSameString(curentPattern)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@code}search for last MSorted zB and delete in other MSorted 
	 * @param mType Type of MSorted to opperate
	 * @throws Exception if getSortedM gets an invalid mType
	 */
	private void removeDoubleM(int mType) throws Exception {
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
	private void clearZT_M(int mType) throws Exception {
		if (getSortedM(mType).size()>=zT) {
			int sizeM = getSortedM(mType).size();
			for (int i = 0; i < sizeM-zT; i++) {
				getSortedM(mType).removeLast();
			}
		}
	}

	/**
	 * deletes double pattern in the List in completeList
	 * @param completeList
	 */
	private void deleteDoublePattern() {
		
		LinkedList<Pattern> toDelete = new LinkedList<Pattern>();
		
		for (int i = 0; i < completeList.size(); i++) {
			for (int j = i+1; j < completeList.size(); j++) {
				if (completeList.get(i).equals(completeList.get(j))) {
					toDelete.add(completeList.get(j));
				}
			}
		}
		for (Pattern pattern : toDelete) {
			completeList.remove(pattern);
		}
	}

	

	//TODO: doppelte Cliquen löschen


}
