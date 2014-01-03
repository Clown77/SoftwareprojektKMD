package classes;
 
import java.util.Arrays;
import java.util.StringTokenizer;
 
public class WordHashtable {
   
    public Word[] table;
   
    int size;       
    int regularWordsCount = 0;
    int textsize = 0;
   
    // Describes how often a word has to appear in 1 000 000 words
    private int TH = 200;
    private int TC = 100;
   
    // hold the values for word categories, calculated for our amount of words
    private double CONTENT_WORD_BORDER;
    private double HIGHFREQUENCY_WORD_BORDER;
   
    // for better understanding of the code
    private final char HIGHFREQUENCY_WORD = 'H';
    private final char CONTENT_WORD = 'C';
    private final char NO_MEANING = 'N';
 
   
    public WordHashtable(int size)
    {
        if(size < 1)
        {
            System.out.println("\n\tConstructor can be used with positive argument only! Will use default value 100.");
            size = 100;
        }
        table = new Word[size];
       
        // initialize the whole array with the empty word, or there will be problems using predefined sort algorithms
        Arrays.fill(table, new Word(""));
        this.size = size;
    }
   
    public void handleString(String zeile)
    {
        String word;
       
        // uses delimeters in default, perfect for our task
        StringTokenizer st = new StringTokenizer(zeile);
       
        while(st.hasMoreTokens())
        {
            word = st.nextToken();
            word = normalize(word);
           
            // this way, we filter a character sequence like ""
            if(word.length() > 0)
            {
                hash(word);
                textsize++;
            }
        }
    }
   
    // removes all characters that are neither digits nor letters
    public  String normalize(String word)
    {
        word = word.replaceAll("[^a-z\t'\u00e4''\u00f4''\u00f6''\u00df''\u00fc']", "");
        return word;
    }
   
    public void hash(String word)
    {
        int hashValue = Math.abs(word.hashCode() % size);
       
        while(!isfull())
        {
           
            /* After hashing, if this index is not used yet, we will
             * just put the word here and set the counter to 1
             */
            if(table[hashValue].isEmpty())
            {
                table[hashValue] = new Word(word);
                table[hashValue].increaseCounter();
               
                // if a new word was found, increade the class-counter
                regularWordsCount++;
                return;
            }
           
            /* if there is a collision while hashing, that means the words are the same
             * and the counter should be increased
             */
            if(table[hashValue].sameWord(word))
            {
                table[hashValue].increaseCounter();
                return;
            }
           
            /* if this index is already taken and the both words are not the same
             * we will have to look at the next place
             */
            hashValue = (hashValue+1) % size;
        }
        System.out.println("\n\t.::Cannot hash anymore because the table is full!::.\n");
    }
   
    // Returns true, if there is no empty slot in our hashtable anymore
    public boolean isfull()
    {
        for(int i = 0; i < size; i++)
        {
            if(table[i].isEmpty()) return false;
        }
       
        return true;
    }
   
    // simple output to console including the word with its appearence
    public void printHashtable()
    {
        for(int i = 0; i < size; i++)
        {
            if(!(table[i].isEmpty())) System.out.println(table[i].getWord()
                    +"\t: " +table[i].getCounter()
                    +"\tkind of word:" +table[i].getKindOfWord());
        }
    }
 
    /* We don't need this method anymore. In our new Version, we calculate high frequency words and content
     * while we are hashing. This saves much time and increases our performance.
     */
    public void sortHashtable()
    {
        Arrays.sort(table);
    }
   
    // remove empty slots
    public void removeEmptySlots()
    {
        Word[] newArray = new Word[regularWordsCount];
       
        for(int i = 0; i < regularWordsCount; i++)
        {
            newArray[i] = table[i];
        }
        table = newArray;
    }
   
    // Will return the kind of a word, given as param.
    // For this, we need to search in our table for exactly the same word
    public char getKindOfWord(String word)
    {
        int indexOfWordInTable = getIndexOfWordInTable(word);
        return table[indexOfWordInTable].getKindOfWord();
    }
   
    // returns the index value of a word in the table. we have to sondier
    // in the same way, as the hash does
    public int getIndexOfWordInTable(String word)
    {
        int hashValue = Math.abs(word.hashCode() % size);
       
        // this seems like and endless slope - but for the fact, that our Word HAS TO exist in our table, it will stop when the word is found
        while(true)
        {
            // if our code works correctly, this will never happen!
            if(table[hashValue].isEmpty())
            {
                throw new RuntimeException(".:: An error occured. The programm has been searching for a word, that does not exist. Normaly this should NEVER happen. Please inform the programmers about it." +word +"<-");
            }
           
            // return the hashValue as index of the word in our table
            if(table[hashValue].sameWord(word))
            {
                return hashValue;
            }
           
            // if the word is not the same, we need to sondier the same way, as the hashtable does
            hashValue = (hashValue+1) % size;
        }   
    }
   
    // Marks every word in our table, if it is a high frequency word or a word that doesn't matter at all.
    public void setWordTypes()
    {
        for(int i = 0; i < size; i++)
        {
            //If the Word appears more than 50 times, its not a content word any more
            if(table[i].getCounter() > CONTENT_WORD_BORDER)    table[i].setKindOfWord(NO_MEANING);
           
            //If the word appears more than 100 times, its a high frequency word
            if(table[i].getCounter() > HIGHFREQUENCY_WORD_BORDER) table[i].setKindOfWord(HIGHFREQUENCY_WORD);
        }
    }
   
    // Use this methodes only, when you are finished with hashing
    public void setHFWBorder()
    {
        HIGHFREQUENCY_WORD_BORDER = ((double)(textsize * TH))/1000000.0;
    }
   
    public void setCWBorder()
    {
        CONTENT_WORD_BORDER =  ((double)(textsize * TC))/1000000.0;
    }
   
    // use this method for debug
    public void tableToString()
    {
        System.out.println(textsize);
        for(int i = 0; i < size; i++)
        {
            if(table[i].getWord() != "")
            System.out.println(table[i].getWord() +" " +table[i].getCounter() +" " +table[i].getKindOfWord());
        }
    }
   
    // I wanted to add this methode for future work. Allows to modify word categories afterwards
    public boolean changeWordType(String word, char kindOfWord)
    {
        if(kindOfWord != CONTENT_WORD && kindOfWord != HIGHFREQUENCY_WORD && kindOfWord != NO_MEANING)
        {
            System.out.println("Word category \"" +kindOfWord +"\" doesn't excist.");
            return false;
        }
       
        int index = 0;
       
        try
        {
            index = getIndexOfWordInTable(word);
            table[index].setKindOfWord(kindOfWord);
            return true;
        }
        catch (RuntimeException notFound)
        {
            System.out.println("The specified word doesn't exist in the text.");
            return false;
        }
    }
   
    public int getCWBorder()
    {
        return (int)CONTENT_WORD_BORDER+1;
    }
   
    public int getHFWBorder()
    {
        return (int)HIGHFREQUENCY_WORD_BORDER+1;
    }
   
}