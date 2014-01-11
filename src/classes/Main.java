package classes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
public class Main {
 
    /** @DEBUG_MODE Set true to get information about the process in the terminal*/
    private static boolean DEBUG_MODE = true;
    
    /**Enter the Path of the text u want to use here */
    private static String PATH = "kindleDocuments/Gesamttext/Bibel.txt";
   
    private static int TABLESIZE = 0;
    private static int WORDNUMBER = 0;
    private static boolean WINDOWING = true;
    private static int WINDOWNUMBER = 4;
 
    public static void main(String[] args) throws Exception {
       
        checkArgumentVector(args);
       
        long programmStartTime = System.currentTimeMillis();
        		
        if(WINDOWING)
        {        	
        	String text = readInFile();
        	
        	LinkedList<String> parts = splitString(text, WINDOWNUMBER);
            
        	createWindows(parts, WINDOWNUMBER);
        	
        	LinkedList<LinkedList<Category>> categoriesCollector = collectCategories();
            
        	LinkedList<Category> finalSet =  mergeWindows(categoriesCollector);
        	
        	System.out.println("Final Set: " +finalSet.toString());
        }
        else
        {
        	LinkedList<Category> categories = discoverCategories();
        }
 
        if(DEBUG_MODE) System.out.println("Total time needed: " +((System.currentTimeMillis() - programmStartTime)/1000) +" seconds");
    }
    
    /** @description Does all the steps, described in the Paper "Efficient Unsupervised Discovery of Word Categories Using Symmetric Patterns and
     * 				 High Frequency Words."
     * 
     * @return A List of the found Categories, using the textfile in "PATH".
     * @throws Exception if the file was not found.
     */
    public static LinkedList<Category> discoverCategories() throws Exception
    {
    	WordHashtable ourHash = createTable();
   	 
        fillHashtable(ourHash);
 
        calculateBorders(ourHash);
       
        setWordTypes(ourHash);
 
        setWordNumber(ourHash);
 
        postprocessTypes(ourHash);
       
        LinkedList<LinkedList<Pattern>> foundPattern = getAllPattern(ourHash);
       
        executeMeasurements(foundPattern);
        
        ListHandler listHandler = generateFinalList(foundPattern);
        
        LinkedList<Category> categories = createCategories(listHandler);
        
		mergeCategories(categories);
    	
    	return categories;
    }
    
    /** @description Each Category has to exist in at least 2 Windows */
    public static LinkedList<Category> mergeWindows(LinkedList<LinkedList<Category>> categoriesCollector)
    {
    	LinkedList<Category> allCategories = new LinkedList<Category>();
    	LinkedList<Category> finalSet = new LinkedList<Category>();
    	
    	for (LinkedList<Category> subList : categoriesCollector)
		{
			allCategories.addAll(subList);
		}
    	
    	for (Category offeredCategory : allCategories)
		{
			for (Category currentCategory : allCategories)
			{
				// Don't compare a category with itself
				if(allCategories.indexOf(currentCategory) == allCategories.indexOf(offeredCategory)) continue;
				
				if(offeredCategory.equals(currentCategory))
				{
					finalSet.add(offeredCategory);
					
					// remove all occurrences of this Category
					boolean removed = true;
					
					do removed = allCategories.remove(offeredCategory);
					while(removed);
					
					break;
				}
			}
		}
    	
    	return finalSet;
    }
    
    /** Collects the Categories from all Window and returns them as a List of Lists. Each sublist belongs to one window.
     * @throws Exception if the File at "PATH" cannot be found.
     */
    public static LinkedList<LinkedList<Category>> collectCategories() throws Exception
    {
    	 LinkedList<LinkedList<Category>> categoriesCollector = new LinkedList<LinkedList<Category>>();
    	
    	for (int i = 0; i < WINDOWNUMBER; i++)
		{
			PATH = "Part" +i +".txt";
			if(DEBUG_MODE) System.out.println("#### WINDOW " +(i+1) +" ####");
			categoriesCollector.add(discoverCategories());
		}
    	
    	return categoriesCollector;
    }
    
    /** @description Returns a String that contains the whole text of the textfile at 'PATH'
     * @throws Exception if the file at 'PATH' doesn't exist.
     */
    public static String readInFile() throws Exception
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
       
        br.close();
        fr.close();
       
        return text;
    }
    
    /** @description Returns a List of parts-1 Strings of the same size. The last part contains the rest of the string which can be slightly different in size
     * 				 to the other parts. 
     * 
     * @param text is the String to be split
     * @param parts is the number of parts that will be returned
     */
    public static LinkedList<String> splitString(String text, int parts)
	{
		int chars = text.length();
		int partsize = chars/parts;
		
		LinkedList<String> result = new LinkedList<String>();
		
		int beginIndex = 0, endIndex = 0;
		
		for(int i = 0; i < parts-1 ; i++)
		{
			beginIndex = i*partsize;
			endIndex = (i+1)*partsize;
			result.add(text.substring(beginIndex, endIndex));
		}
		
		// letzter Rest fehlt noch
		result.add(text.substring(endIndex));
		
		return result;
	}
   
    /** Creates the windows as different textfiles and returns a list of the files.
     * 
     * @param parts : Each part will be the text of a single window
     * @param windowNumber is the number of windows that will be created
     * @throws IOException if the FileWrite cannot open the file, e.g. because it doesn't exist.
     */
    public static void createWindows(LinkedList<String> parts, int windowNumber) throws IOException
    {
        for(int i = 0; i < windowNumber; i++)
        {
        	File currentTextfile = new File("Part" +i +".txt");
        	
        	FileWriter writer = new FileWriter(currentTextfile);
        	
        	// this fills a window with text
        	writer.write(parts.get(i));
        	
        	writer.flush();
        	writer.close();
        }
        
        return;
    }


	/**Line separator won't cut words.*/
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
   
    /** @description Fills the Hashtable with all words of the textfile */
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
 
            // This checks if the line ends with a separator (-). If it does, the lines will be concatenated
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
                "\n|X| < " +ourHash.getCWBorder() +" --> X = Content Word" +
                "\n|X| > " +ourHash.getHFWBorder() +" --> X = High Frequency Word.");
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
        //example: ourHash.changeWordType("heiliger", 'C');
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
     * @param args Possible arguments: 'DEBUG_ENABLE' - Will activate all console outputs for debugs
     */
    public static void checkArgumentVector(String[] args)
    {
        if(args.length > 0)
        {
            if(args[0].equals("DEBUG_ENABLED")) DEBUG_MODE = true;
        }
 
        return;
    }
    
    /**@description Does the Measurements M1, M2 and M3 for each Meta Pattern
     * 
     * @param foundPattern
     */
    public static void executeMeasurements(LinkedList<LinkedList<Pattern>> foundPattern)
    {
    	for(int i = foundPattern.size()-1; i >= 0; i--) 
    	{
    		if(foundPattern.get(i).isEmpty())
    		{
    			foundPattern.remove(i);
    			continue;
    		}
    		SinglePatternGraph spg = new SinglePatternGraph(foundPattern.get(i));
    	} 
    }
    
    /**@description Creates 3 Lists, sorted by M1, M2 and M3. Then builds a list of final Patterns, for our final Graph using zB and zT.
     * 				It also creates a BidirectionalList, which contains all Pattern, that are bidirectional connected.
     */
    private static ListHandler generateFinalList(LinkedList<LinkedList<Pattern>> foundPattern) throws Exception 
    {
    	ListHandler listHandler = new ListHandler(foundPattern);
    	
        listHandler.sortPatternCandidatesM(1);
        listHandler.sortPatternCandidatesM(2);
        listHandler.sortPatternCandidatesM(3);
        
        /** Removes all Patterns that appear in the Bottom ZB of any List */
        listHandler.removePatternOfZB();
        
        listHandler.createFinalList();
        
        listHandler.collectBidirectionalPattern();
		return listHandler;
	}
    
    // Because we used SinglePatternGraphs, there should be no double words anymore in the final SinglePatternGraph! But we need to test it!
    /** @description Returns a List that contains all the words, that would be in the final SinglePatternGraph. The Words are representated as Strings, not Pattern!

     */
    private static LinkedList<String> finalListToStrings(LinkedList<Pattern> finalList)
    {
    	LinkedList<String> allWords = new LinkedList<String>();
    	
    	for (Pattern pattern : finalList)
		{
    		// without double words
			if(!allWords.contains(pattern.pattern.getFirst())) allWords.add(pattern.pattern.getFirst());
			if(!allWords.contains(pattern.pattern.getLast()))  allWords.add(pattern.pattern.getLast());
		}

    	if(DEBUG_MODE) System.out.println("All Words: " +allWords.toString());
    	
    	return allWords;
    }
    
    public static LinkedList<Category> createCategories(ListHandler listHandler)
    {
    	LinkedList<Pattern> finalList = listHandler.getFinalList();
        LinkedList<String>	allWords = finalListToStrings(finalList);
        LinkedList<Pattern> biDirectionalPattern = listHandler.getBidirectionalPattern();
        
        // Each Bidirectional Pattern is a clique
        LinkedList<Category> categories = new LinkedList<Category>();
        
        for (Pattern clique : biDirectionalPattern)
		{
			categories.add(new Category(clique));
		}

        // fill each category using all the final words
        for (Category category : categories)
		{
			category.fillCategory(finalList, allWords);
		}
        
		if(DEBUG_MODE) System.out.println("Kategorien: " +categories.toString());
        
        return categories;
    }
    
    public static void mergeCategories(LinkedList<Category> categories)
    {
    	// Collects all Categories that are merged into other categories and deletes the old ones
		LinkedList<Category> collector = new LinkedList<Category>();
		
	    // Offer each Category to each category (n² -.-)
	    for (Category category : categories)
		{
			for (Category offeredCategory : categories)
			{
				// upper Line: Else the category will always merge with itself and delete itself after that
				if(categories.indexOf(offeredCategory) == categories.indexOf(category)) break; 
				if(category.offerCategory(offeredCategory.category)) collector.add(offeredCategory);
			}
		}
	    
	    // Delete all categories that were merged into other categories
	    for (Category category : collector)
		{
			categories.remove(category);
		}
	    
		if(DEBUG_MODE) System.out.println("Merged Categories: " +categories.toString());
    }
}