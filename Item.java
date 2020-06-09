/**

 * This class is part of the "find Michael Kolling" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * The Item class stores an Item that has a decriptiona and weight.
 * This class is used by the game class to create specific items and assign them to specific rooms. 
 * This class is also used by the room class in order to store these specific items in an hashmap of type <String, Item> in these specific rooms.
 *
 * @author Maxim Fishman
 * @version (2018.11.19)
 */
public class Item
{

    private int weight;
    private String description;

    /**
     * Create an Item with a specified description and weight
     */
    public Item(String description, int weight)
    {//initialize the description and weight
        this.description = description;
        this.weight = weight;
    }

    /**
     * returns the weight of this item
     * @return weight of this tiem
     */
    public int getWeight(){
        return weight;
    }

    /**
     * returns the description of this item
     * @return description of this item
     */
    public String getDescription(){
        return description;
    }

}
