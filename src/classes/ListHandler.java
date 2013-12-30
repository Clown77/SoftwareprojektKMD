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

	public void clearAllM() throws Exception 
	{	
		// removes pattern that appear in the ZB of any list
		removeDoubleM();
		
		for (int i = 1; i <= 3; i++) 
		{
			
			// TODO
			clearZT_M(i);
		}
		
	}

	/**
	 * @param mType Type of MSorted to operate
	 * @return MSorted
	 * @throws Exception gets an invalid mType
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
	 * @param mType Type of MSorted to opperate
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
	
	public void generateCliques() 
	{
		// Throw all MSorted into one completed list
		inizializeCompleteList();

		// Uses the Pattern to find bidirectional arcs
		findBydirectionalList();
		
		
		searchCompleteClique();
	}
	
	/**
	 * {@code}inizialise completeList
	 * @param completeList = M1Sorted + M2Sorted + M3Sorted
	 */
	private void inizializeCompleteList() 
	{
		
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
	private void findBydirectionalList() 
	{
		
		LinkedList<Pattern> bydirectionalList = new LinkedList<Pattern>();
		
		for (int i = 0; i < completeList.size(); i++) 
		{
			for (int j = i; j < completeList.size(); j++) 
			{
				if (completeList.get(i).pattern.getFirst().equals(completeList.get(j).pattern.getLast()) 
					&&completeList.get(i).pattern.getLast().equals(completeList.get(j).pattern.getFirst()))
				{
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
	 * {@code}search for last ZB Elements in MSortedX and delete in other MSortedY 
	 * @param mType is the ID of the List
	 * @throws Exception if getSortedM gets an invalid mType
	 */
	
	//TODO: Alle die gelöscht werden sollen in eine Liste Packen und erst dann aus den jeweiligen Listen entfernen
	private void removeDoubleM() throws Exception 
	{
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
			System.out.println("i hat den Wert: " +i);
		}
		
		for (Pattern currentPattern : ZBElements) 
		{
			for(int i = 1; i <= 3; i++)
			{
				getSortedM(i).remove(currentPattern);
				System.out.println("Lösche aus M" +i +": " +currentPattern);
			}
			
		}
		
	}

	/**
	 * {@code}short MSorted to zT
	 * @param mType Type of MSorted to operate
	 * @throws Exception if getSortedM gets an invalid mType
	 */
	private void clearZT_M(int mType) throws Exception 
	{
		if (getSortedM(mType).size()>=zT) 
		{
			int sizeM = getSortedM(mType).size();
			for (int i = 0; i < sizeM-zT; i++) 
			{
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
		
		for (int i = 0; i < completeList.size(); i++) 
		{
			for (int j = i+1; j < completeList.size(); j++) 
			{
				if (completeList.get(i).equals(completeList.get(j))) 
				{
					toDelete.add(completeList.get(j));
				}
			}
		}
		
		for (Pattern pattern : toDelete) 
		{
			completeList.remove(pattern);
		}
	}

	

	

}
