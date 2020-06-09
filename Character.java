
/**
 * This class is part of the "find Michael Kolling" application. 
 * "Find Michael Kolling" is a very simple, text based adventure game.
 *  
 *  This class stores a character that has a phrase. The player can interact
 *  with any character in order to gain clues about what to do next.
 *
 * @author Maxim Fishman
 * @version (2018.11.30)
 */
public class Character
{
    private String phrase;

    /**
     * Constructor for objects of class Character
     * @param phrase (what the character says)
     */
    public Character(String phrase)
    {
     this.phrase = phrase;
    }
    
    /**
     * Returns the phrase of the character
     * @return phrase
     */
    public String getPhrase()
    {
       return phrase;
    }
}
