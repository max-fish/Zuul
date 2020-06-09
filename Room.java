import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * A "Room" also stores all the items and character associated with it
 * 
 * @author  Michael Kölling, David J. Barnes, and Maxim Fishman
 * @version 2016.02.29
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;//stores exits of this room
    private HashMap<String, Item> items;//stores the items of this room
    private HashMap<String, Character> characters;//stores the characters of this room
    private static ArrayList<String> allItemNames;//stores all the items in the game
    private static ArrayList<String> allCharacterNames;//stores all the character names in the game
    private static ArrayList<HashMap<String, Character>> allCharacters;
    //all the characters in the game stored in an ArrayList of Hashmaps format (for character movement)
    private boolean locked;//whether this room is locked or not

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, boolean locked) 
    {
        //initialize instance and static varaibles
        this.description = description;
        this.locked = locked;
        exits = new HashMap<>();
        items = new HashMap<>();
        characters = new HashMap<>();
        allItemNames = new ArrayList<>();
        allCharacterNames = new ArrayList<>();
        allCharacters = new ArrayList<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);//associate a direction with a room
    }
    
    /**
     * Add a brand new item to this room
     * @param name 
     * @param description 
     * @param weight
     */
    public void addItem(String itemName, String description, int weight)
    {
        Item item = new Item(description, weight);//create new item
        items.put(itemName, item);//add an association between the name of the item and the item object (add to hashmap)
        allItemNames.add(itemName);//add name to the general list of item names
    }
    
    /**
     * Add an item to this room by reference (an item that already exists in the game)
     * @param name of item
     * @param item itself
     */
    public void addItem(String itemName, Item item)
    {
        items.put(itemName, item);//add an association between the name of the item and the item object (add to hashmap)
    }
    
    /**
     * Add a character to this room with a specified name and phrase (use for creating brand new characters)
     * @param name of character
     * @param phrase of character
     */
    public void addCharacter(String characterName, String phrase)
    {
        Character character = new Character(phrase);//create new character
        characters.put(characterName, character);//add an association between the name of the character and the character itself (add to hashmap)
        allCharacterNames.add(characterName);//add name of character to the general list of character names
        allCharacters.add(characters);//add the character to the general list of character hashmaps
    }

    /**
     * Add a character with an already defined HashMap of this character (use for assigning existing characters)
     * @param HashMap<String,Character>
     */
    public void addCharacter(HashMap<String, Character> newCharacters)
    {
     characters.putAll(newCharacters);
    }
    
    /**
     * Return an item given it's name
     * @param name of item
     * @return item
     */
    public Item getItem(String itemName)
    {
        return items.get(itemName);
    }
    
    /**
     * Remove an item from this room given it's name
     * @param name of item
     */
    public void removeItem(String itemName)
    {
        items.remove(itemName);
    }
    
    /**
     * Check if an item is in this room
     * @param name of item
     * @return true/false
     */
    public boolean itemInRoom(String itemName)
    {
        return items.keySet().contains(itemName);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     Items in room: axe hammer
     *     Characters in room: Bob Joe
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are at " + description + ".\n" + ".\n" + getExitString() + ".\n" + ".\n" + getItemString() + ".\n" + ".\n" + getCharacterString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return a string describing the items that are in the room, for example
     * "Items in room: axe hammer"
     * @return Names of the room's items
     */
    public String getItemString(){
        String itemString = "Items in room:";
        if(items.size() == 0){
            itemString += " None";
        }
        else{
            Set<String> itemNames = items.keySet();
            for(String itemName : itemNames){
                itemString += (" " + itemName);
            }
        }
        return itemString;
    }

    /**
     * Retrn a string describing the characters that are in the room, for example
     * "Characters in room: Bob Joe"
     * @return Names of the room's characters
     */
    public String getCharacterString(){
        String characterString = "";
        if(characters.size() == 0){
            characterString += "There is nobody here who can help you!";
        }
        else{
            characterString += "Character(s) in room:";
            Set<String> characterNames = characters.keySet();
            for(String characterName : characterNames){
                characterString += (" " + characterName);
            }
        }
        return characterString;
    }

    /**
     * Returns a character given it's name
     * @param name of the character
     * @return character instance object (value) associated with the character name (key) in the hashmap of characters in the room
     */
    public Character getCharacter(String characterName){
        return characters.get(characterName);
    }
    
    /**
     * Return the arraylist of all characters of type HashMap<String,Character>
     * @return HashMap<String,Character>
     */
    public static ArrayList<HashMap<String, Character>> getAllCharacters(){
        return allCharacters;
    }
    
    /**
     * Return the arraylist of all the character names
     * @return ArrayList<String>
     */
    public static ArrayList<String> getAllCharacterNames(){
     return allCharacterNames;   
    }
    
    /**
     * Return the arraylist of all the item names
     * @return ArrayList<String>
     */
    public static ArrayList<String> getAllItemNames(){
     return allItemNames;
    }
    
    /**
     * Remove all the characters in this room (used when moving characters)
     */
    public void removeAllCharactersInRoom(){
        characters = new HashMap<>();
    }

    /**
     * Checks if a given character is in the room
     * @param Name of character
     */
    public boolean characterInRoom(String characterName){
        return characters.containsKey(characterName);
    }

    /**
     * Checks if the room is locked
     * @return true/false
     */
    public boolean isLocked(){
        return locked;   
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * return all the neighbors of this room
     * @return Collection<Room>
     */
    private Collection<Room> getNeighbors(){
        return exits.values();
    }
    
    /**
     * Checks if this room is equivalent to a given room by determining
     * whether it has the same neighbors
     * @param Room
     * @return true/false
     */
    public boolean equals(Room room){
        return exits.values().equals(room.getNeighbors());
    }
    
    /**
     * shuffles the arrayList of all the characters of type HashMap<String, Character>
     */
    public void scrambleCharacters(){//used in game class in order to move the characters to different locations
        Collections.shuffle(allCharacters);
    }
}