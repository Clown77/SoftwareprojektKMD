package classes;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.StringTokenizer;
 
public class PatternFinder
{
    /** @DEBUG_MODE Set true to get information about the process in the terminal*/
    private final static boolean DEBUG_MODE = true;
   
    // Means the pattern has to occur TP times in 1 million words
    private final int TP = 0;
   
    String PATH;
    int WORDNUMBER;
   
    // Contains all possible patterns
    public static LinkedList<String> legalPattern;
   
    // Contains all pattern that were found in the text
    private LinkedList<LinkedList<Pattern>> foundPattern;
   
    /** @Info Here you can add more meta patterns for experiments */
    public PatternFinder(String path, int WORDNUMBER)
    {
        this.PATH = path;
        this.WORDNUMBER = WORDNUMBER;
       
        foundPattern = new LinkedList<LinkedList<Pattern>>();
        legalPattern = new LinkedList<String>();
       
        legalPattern.addLast("CHCH");
        legalPattern.addLast("CHC");
        legalPattern.addLast("CHHC");
        legalPattern.addLast("HCHC");
    }
    
    // we will now search in the text for all patterns and save them in foundPattern
    public void findAllPattern(WordHashtable ourHash) throws Exception
    {
        long starttime = System.currentTimeMillis();
       
        String text = readInFile();
       
        if(DEBUG_MODE) System.out.println("Time needed to read in the file: " +(System.currentTimeMillis()-starttime)/1000 +" seconds");
 
        while(!legalPattern.isEmpty())
        {
            LinkedList<Pattern> tempList = new LinkedList<Pattern>();
           
            String currentPatternStructure = legalPattern.getFirst();
            legalPattern.removeFirst();
           
            // number of words we will be looking for
            int patternLength = currentPatternStructure.length();
          
            // this list will contain the words we are currently looking at
            LinkedList<String> currentWords = new LinkedList<String>();
          
            // search for words
            StringTokenizer stringToken = new StringTokenizer(text);
          
            inizialiseCurrentWords(patternLength, currentWords, stringToken);
          
            addFoundPattern(ourHash, currentPatternStructure, currentWords, tempList);
          
            long timestart = System.currentTimeMillis();
           
            checkNextWords(ourHash, currentPatternStructure, currentWords, stringToken, tempList);
           
            long timeend = System.currentTimeMillis();
           
            // in this list we don't need the HFW's any more
            removeHighFrequencyWords(ourHash, tempList);
           
            if(DEBUG_MODE) System.out.println("High frequency words were removed from the pattern candidates.");
           
            // Pattern that don't appear often enough are removed
            removeLowAppearencePattern(WORDNUMBER, tempList);
            if(DEBUG_MODE) System.out.println("Low appearence Pattern have been removed (TP Filter). TP is set to: " +TP);
           
            foundPattern.add(tempList);
           
            if(DEBUG_MODE) System.out.println("Finished searching for " +currentPatternStructure +"-Pattern. (" +((timeend - timestart)/1000) +" seconds needed)");
        }
    }
   
   
    private void checkNextWords(WordHashtable ourHash, String currentPatternStructure, LinkedList<String> currentWords, StringTokenizer stringToken, LinkedList<Pattern> tempList)
    {
        // take the next i words
        while(stringToken.hasMoreTokens() && !currentWords.isEmpty())
        {
            currentWords.removeFirst();
           
            currentWords.add(stringToken.nextToken());
           
            // if a pattern is found, add it to the list
            if(isPattern(currentWords, currentPatternStructure, ourHash))
            {
                // patternInList increases Pattern counter if true
                if (!patternInList(currentWords, tempList))
                {
                    tempList.add(new Pattern(currentWords));
                }
            }
        }
    }
   
   
    private void addFoundPattern(WordHashtable ourHash, String currentPatternStructure, LinkedList<String> currentWords, LinkedList<Pattern> temp)
    {
        // if a pattern is found, add it to the list
        if(isPattern(currentWords, currentPatternStructure, ourHash))
        {
            temp.add(new Pattern(currentWords));
        }
    }
   
   
    private void inizialiseCurrentWords(int patternLength, LinkedList<String> currentWords, StringTokenizer stringToken) {
       
        // for i times, give me the next word
        for(int i = 0; i < patternLength; i++)
        {
            if(stringToken.hasMoreTokens())
            {
                currentWords.add(stringToken.nextToken());
            }
        }
    }
   
    // patternInList increases Pattern counter if true
    private boolean patternInList(LinkedList<String> test, LinkedList<Pattern> temp)
    {
        for (Pattern testingPattern : temp)
        {
            if (testingPattern.equals(new Pattern(test)))
            {
                testingPattern.increaseCounter();
                return true;
            }
        }
        return false;
    }
 
   
    // delete all symbols etc. that we don't need
    public  String normalize(String word)
    {
        // SPACE NEEDED
        word = word.replaceAll("[^a-z\t'\u00e4''\u00f4''\u00f6''\u00df''\u00fc' ]", "");
        return word;
    }
   
   
    public String readInFile() throws Exception
    {
        FileReader fr = new FileReader(PATH);
        BufferedReader br = new BufferedReader(fr);
       
        // we fill the whole text into 'text' --> maybe change it later for better performance
        StringBuffer input = new StringBuffer("");
       
        String zeile = br.readLine();
       
        while(zeile != null)
        {
            input.append(zeile +" ");
            zeile = br.readLine();
        }
       
        // we have to edit the text like we did with the words for the hashtable
        String text = input.toString();
       
        text = text.toLowerCase();
        text = normalize(text);
       
        br.close();
        fr.close();
       
        return text;
    }
   
   
    // returns if it is a pattern or not
    public boolean isPattern(LinkedList<String> currentWords, String currentPatternStructure, WordHashtable ourHash)
    {
        int i = 0;
       
        LinkedList<String> currentWordsCopy = new LinkedList<String>();
       
        copyLists(currentWords, currentWordsCopy);
       
        // end if the list is empty
        while(!currentWordsCopy.isEmpty())
        {
            // take the first word of the list and remove it
            String temp = currentWordsCopy.getFirst();
           
            currentWordsCopy.removeFirst();
           
            // get the type of the word and compare it to the pattern we are looking for
            String type = "" +ourHash.getKindOfWord(temp);
           
            if(type.charAt(0) != currentPatternStructure.charAt(i)) return false;
           
            // increase the word counter
            i++;
        }
        // there was no mismatch found
        return true;
    }
   
   
    // we don't want to work on references, so we copy the list
    public void copyLists(LinkedList<String> list1, LinkedList<String> list2)
    {
        int length = list1.size();
       
        for(int i = 0; i < length; i++)
        {
            list2.addLast(list1.get(i));
        }
    }
 
   
    // We don't need the High Frequency words anymore, so we return a copy of our pattern that just contains the Content words
    public void removeHighFrequencyWords(WordHashtable ourHash, LinkedList<Pattern> tempList)
    {
        for (Pattern currentPattern : tempList)
        {
            int length = currentPattern.pattern.size();
           
            for(int i = (length-1); i >= 0; i--)
            {
                if(ourHash.getKindOfWord(currentPattern.pattern.get(i)) == 'H')
                {
                    currentPattern.pattern.remove(i);
                }
            }
        }
    }
   
   
    /**@return Returns null, if the method 'findAllPattern' has not been used before.*/
    public LinkedList<LinkedList<Pattern>> getFoundPattern()
    {
        return foundPattern;
    }
   
   
    // Can be used only, if the method 'findAllPattern' has been calls before
    public void removeLowAppearencePattern(final int WORDNUMBER, LinkedList<Pattern> tempList)
    {
        // Change TP for tests
        double ratio = TP/1000000.0;
        double patternRatio = 0;
       
        for (int i = (tempList.size()-1); i >= 0; i--)
        {
            patternRatio = (double)(tempList.get(i).getCounter())/WORDNUMBER;
            if(patternRatio < ratio) tempList.remove(i);
        }
    }
}