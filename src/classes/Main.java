package classes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
public class Main {
 
    /** @DEBUG_MODE Set true to get information about the process in the terminal*/
    private static boolean DEBUG_MODE = false;
   
    private static int TABLESIZE = 0;
    private static int WORDNUMBER = 0;
    private static String PATH = "kindleDocuments/Gesamttext/Bibel.txt";
 
    public static void main(String[] args) throws Exception {
       
        checkArgumentVector(args);
       
        long programmStartTime = System.currentTimeMillis();
 
        WordHashtable ourHash = createTable();
 
        fillHashtable(ourHash);
 
        calculateBorders(ourHash);
       
        setWordTypes(ourHash);
 
        setWordNumber(ourHash);
 
        postprocessTypes(ourHash);
       
        LinkedList<LinkedList<Pattern>> foundPattern = getAllPattern(ourHash);
       
        for(int i = 0; i < foundPattern.size(); i++) { SinglePatternGraph spg = new SinglePatternGraph(foundPattern.get(i)); }
       
        // The listHandler will sort the patterns by M1, M2 and M3 and then create a final list of pattern candidates
        ListHandler listHandler = new ListHandler(foundPattern);
        listHandler.sortPatternCandidatesM(1);
        listHandler.sortPatternCandidatesM(2);
        listHandler.sortPatternCandidatesM(3);
 
        if(DEBUG_MODE) System.out.println("Total time needed: " +((System.currentTimeMillis() - programmStartTime)/1000) +" seconds");
    }
 
    //Diese Funktion führt dazu, dass Zeilenumbr�che nicht zu zwei getrennten Wörtern führen.
    public static String handleLineBreak (String tempZeile, BufferedReader br) throws IOException
    {
        do
        {
            if(!tempZeile.endsWith("-")) return tempZeile;
            tempZeile = tempZeile.concat(br.readLine());       
        }while(true);
    }
 
    /** @description Uses the size of the textfile to make a guess of how many words are in the text.
     *  @return Returns the calculated number of words.
     */
    public static int calculateTableSize()
    {
        // Returns the size of the textfile in Bytes
        File file = new File(PATH);
        long length = file.length();
 
        return (int)length/3;
    }
   
    /** @desciption Fills the Hashtable with all words of the textfile */
    public static void fillHashtable(WordHashtable wHash) throws IOException
    {
        FileReader fr = new FileReader(PATH);
        BufferedReader br = new BufferedReader(fr);
       
        String zeile = br.readLine();
 
        // read the file line for line until its at the end
        while(zeile != null)
        {
            // We are not interested in case sensitivity
            zeile = zeile.toLowerCase();
 
            // This checks if the line ends with a seperator (-). If it does, the lines will be concatenated
            zeile = handleLineBreak(zeile, br);
 
            // filters the words out of the string and adds them into the hashtable
            wHash.handleString(zeile);
 
            zeile = br.readLine();
        }
 
        br.close();
        fr.close();
       
        if(DEBUG_MODE) System.out.println("Table filled.");
    }
   
    /** @description Uses the total number of words to calculate how often a word has to appear to be a HFW or CW. */
    public static void calculateBorders(WordHashtable ourHash)
    {
        ourHash.setHFWBorder();
        ourHash.setCWBorder();
        if(DEBUG_MODE) System.out.println("Word Borders have been set. " +
                "\n|X| <" +ourHash.getCWBorder() +" --> X = Content Word" +
                "\n|X| >" +ourHash.getHFWBorder() +" --> X = High Frequency Word.");
    }
   
    /** @description Creates a Hashtable that fits well to our textsize */
    public static WordHashtable createTable()
    {
        TABLESIZE = calculateTableSize();
        WordHashtable wht = new WordHashtable(TABLESIZE);
       
        if(DEBUG_MODE) System.out.println(TABLESIZE +" entries created in the Hashtable.");
        return wht;
    }
   
    /** @description When the Word Borders are calculated, this function sets the type of each word
     *  @attention The Function 'calculateBorders' has to be used before! Else the result will be meaningless. */
    public static void setWordTypes(WordHashtable ourHash)
    {
        ourHash.setWordTypes();
        if(DEBUG_MODE) System.out.println("Word categories set.");
    }
   
    /** @description Sets the value for how many words are in the text. This number will be needed to set the filter (TP) later. */
    public static void setWordNumber(WordHashtable ourHash)
    {
        WORDNUMBER = ourHash.textsize;
        if(DEBUG_MODE) System.out.println("The textfile contains " +WORDNUMBER +" words.");
    }
   
    /** @description Use this method to optimize the result for your text, by changing the type of any word. Use C for Content Word, H for High Frequency Words and N to give it no meaning */
    public static void postprocessTypes(WordHashtable ourHash)
    {
        //example: ourHash.changeWordType("Gott", 'C');
        if(DEBUG_MODE) System.out.println("Postprocessing word types has been successfull.");
    }
   
    /** @description Collects all Pattern candidates in a List of Lists. One List for each meta Pattern defined in the PatternFinder.
     * @return Returns List of Lists that contains meta pattern.
     * @throws Exception if the textfile was not found at 'PATH' */
    public static LinkedList<LinkedList<Pattern>> getAllPattern(WordHashtable ourHash) throws Exception
    {
        //The patternfinder can work on the text and return us a list with all patterns we want to search for
        PatternFinder patternfinder = new PatternFinder(PATH, WORDNUMBER);
 
        // in this step the patternfinder collects all patterns. Including High frequency words
        patternfinder.findAllPattern(ourHash);
       
        LinkedList<LinkedList<Pattern>> foundPattern = patternfinder.getFoundPattern();
       
        if(DEBUG_MODE) System.out.println("Without High-Frequency Words: " +foundPattern.toString());
       
        return foundPattern;
    }
   
    /**@description Parses the arguments vector and sets flags
     *
     * @param args Possible argmuents: 'DEBUG_ENABLE' - Will activate all console outputs for debugs
     */
    public static void checkArgumentVector(String[] args)
    {
        if(args.length > 0)
        {
            if(args[0].equals("DEBUG_ENABLED")) DEBUG_MODE = true;
        }
 
        return;
    }
}