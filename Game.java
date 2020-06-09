import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around, interact with characters, pick up, give, and drop items.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling, David J. Barnes and Maxim Fishman
 * @version 2016.02.29
 */

public class Game
{
    private Room auditorium, kitchen, lobby, kclsu, vault, strand, waterloo, strandCampus, office, finalRoom;
    private Parser parser;
    private Room currentRoom;
    private ArrayList<Room> previousRooms;//used to track the player so the player can use the "back" command
    private HashMap<String,Item> possessions;//used to track the items that a player has in his possession
    private Room [] roomList = new Room [9];//used to store the rooms for the magic transportation scenario
    private static final int MAX_WEIGHT = 90;//maximum weight the player can hold
    private int totalWeight;//total weight the player is holding
    private boolean wantToQuit = false;//whether player wants to quit or not
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        possessions = new HashMap<>();
        previousRooms = new ArrayList<>();
        totalWeight = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        auditorium = new Room("Bush House Lecture Theatre", false);
        kitchen = new Room("King's Kitchen", false);
        lobby = new Room("the arcade caffe", false);
        kclsu = new Room("the student union", false);
        vault = new Room("school pub", false);
        strand = new Room("outside the main entrance of the university", false);
        waterloo = new Room("in Waterloo campus", false);
        strandCampus = new Room("in Strand's main campus", false);
        office = new Room("in Michael Kolling's Office!", true);

        //set exits, characters, and items for the auditorium
        auditorium.setExit("east",lobby);
        auditorium.setExit("west", kitchen);
        auditorium.setExit("north", office);
        auditorium.addCharacter("David", "Unfortunately, I don't know where Michael Kolling is. I would reccomend asking Josh Murphy");
        auditorium.addItem("Desk", "What Michael Kolling uses to present his slides", 120);

        //sets exits, characters, and items for the kitchen
        kitchen.setExit("west", vault);
        kitchen.setExit("east", auditorium);
        kitchen.addCharacter("Agi", "I will only answer your question if you find me a research paper about Pascal's Triangle");

        //sets exits, characters, and items for the lobby
        lobby.setExit("west", auditorium);
        lobby.setExit("north", kclsu);
        lobby.setExit("east", strand);
        lobby.setExit("south", strandCampus);
        lobby.addCharacter("Josh", "Michael Kolling told me that he is going out to Strand for a nice walk and to think about the origins of the universe.");
        lobby.addItem("Coffee", "Give you a boost of energy", 20);

        //sets exits, characters, and items for kclsu
        kclsu.setExit("south", lobby);
        kclsu.addCharacter("Matthew", "Sorry there. How can I help? I was just reading about latest math research ideas." +
        ".\n" + 
        "I mean...someone told me he was in a hurry and that no one knows where he is...but I don't know. Dont ask me");
        kclsu.addItem("MathResearch", "contains research about Pascal's Triangle", 30);
        kclsu.addItem("Table", "Where people eat", 100);

        //sets exits, characters, and items for vault
        vault.setExit("east", kitchen);
        vault.addCharacter("Odinaldo", "All I can say is one of the people who you might of asked didnt tell you the truth. Who do think it is?" + 
        ".\n" + 
        "I reccomend going through ALL the other rooms before answering this question. Because if you get this question wrong...you lose the game!" + 
        ".\n" + 
        "Your choices are Agi, Ernest, Matthew, Josh, Yani, and Sanjay.");
        vault.addItem("Beer", "A drink that calms you down", 25);

        //sets exits, characters, and items for strand
        strand.setExit("west", lobby);
        strand.setExit("north", waterloo);
        strand.setExit("south", strandCampus);
        strand.addCharacter("Ernest", "It seems fishy that Matthew Howard would dodge the question");
        strand.addItem("Kombatrin", "Medicine", 10);

        //sets exits, characters, and items for waterloo
        waterloo.setExit("south", strand);
        waterloo.addCharacter("Sanjay", "For someone to lie, he or she must believe some fact, uttering that he or she believes the opposite," +
        ".\n" + "with the intention of making you believe that the opposite is true");
        waterloo.addItem("Matrix movie disc", "Disc of the Movie 'Matrix'", 10);

        //sets exits, characters, and items for strand campus
        strandCampus.setExit("north", strand);
        strandCampus.addCharacter("Yani", "Bro I don't know anything about Michael Kolling's whereabouts. But I do have this key that might help you out.");
        strandCampus.addItem("key", "This key can be used to unlock a door", 10);

        //sets exits, characters, and items for the office
        office.setExit("south", auditorium);
        office.addCharacter("Andreas", "Odinaldo is the one has been lying to you all along. Go back to him and answer his question by typing 'Odinaldo'.");

        //fill the array will all the rooms that are in the game except for the locked room (office) (used for magic transport room)
        roomList[0] = auditorium;
        roomList[1] = kitchen;
        roomList[2] = lobby;
        roomList[3] = kclsu;
        roomList[4] = vault;
        roomList[5] = strand;
        roomList[6] = waterloo;
        roomList[7] = strandCampus;
        roomList[8] = office;//only included in character movement (not teleportation)

        currentRoom = auditorium;  // start game in the auditorium
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Find Michael Kolling adventure game!");
        System.out.println("In this game, you will need to guide yourself through London to find Michael Kolling");
        System.out.println("just to ask him that one question that you know will be difference between a pass and a fail on the final exam.");
        System.out.println("Throughout your journey you will encounter items and characters that might help you find your way.");
        System.out.println("Good luck! And type 'help' if you need help!");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println(".");
        System.out.println(showInventory());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If the user types in an unrecognized command, it is checked whether this invalid input is recognized by either answering a question or asking about an item.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {

        if(command.isUnknown()) {
            String text = parser.getInvalidInput();
            if(text.endsWith("?")){//if the player has a question about an item
                String itemName = text.substring(0,text.length()-1);
                if(currentRoom.itemInRoom(itemName)){//if the item in question exists in the room
                    System.out.println(currentRoom.getItem(itemName).getDescription());
                    return false;
                }
            }
            if(currentRoom.characterInRoom("Odinaldo")){//if the player in Odinaldo's room, the user can give a response to his question
                odinaldoResponse(text);
                return false;
            }

            System.out.println("I don't know what you mean...");//else there are no other recognized non-command user input
            return false;
        }
        //checks which command it is referring to and apply the correct method to service the command
        String commandWord = command.getCommandWord();
        if (commandWord.equalsIgnoreCase("help")) {
            printHelp();
        }
        else if (commandWord.equalsIgnoreCase("go")) {
            goRoom(command);
        }
        else if (commandWord.equalsIgnoreCase("take")){
            take(command);
        }
        else if(commandWord.equalsIgnoreCase("drop")){
            drop(command);
        }
        else if(commandWord.equalsIgnoreCase("ask")){
            ask(command);
        }
        else if(commandWord.equalsIgnoreCase("back")){
            back();
        }
        else if(commandWord.equalsIgnoreCase("give")){
            give(command);
        }
        else if (commandWord.equalsIgnoreCase("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     */
    private void printHelp() 
    {
        System.out.println("You just finished listening to a lecture by David at the Bush House Lecture theatre");
        System.out.println("You need to find Michael Kolling and ask him that legendary question.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println("Remember: If someone asks you a question, answer it by just typing in what you think the answer is (without any commands)");
        System.out.println("If you have a question about an item, type the item name with a question mark right after it (no spaces)");
        System.out.println("If you need to give something someone, type the command in the following format: give (name of item) (name of character)");
        System.out.println("If you need to answer a character's question, just type your answer in without any commands");
        System.out.println("Good luck!");
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * @param Command
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord().toLowerCase();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {//if there is no room, let the player know
            System.out.println("There is no door!");
        }

        else{ 
            if(nextRoom.isLocked()){
                if(possessions.containsKey("key")){//if a player tries to enter a locked door with a key
                    System.out.println("----------------------------------------------------------");
                    System.out.println("Congradulations! You have unlocked Michael Kollings Office!");
                    previousRooms.add(currentRoom);//add room to history so the player can go back if desired
                    currentRoom = nextRoom;
                    roomIntro(currentRoom);
                    return;
                }
                else{//if a player tries to enter a locked door without a key
                    System.out.println("You tried to enter a locked door with a key!");
                    System.out.println("You will now recieve the punishment of punishmenting to a random room in 5 seconds!");
                    punishment();
                    return;
                }
            }
            else if(finalRoom != null && nextRoom.equals(finalRoom))
            {//start checking only when final room is revealed
                if(possessions.containsKey("hovercraft"))
                {//if a player tries to enter the final room with a hovercraft
                    System.out.println("Congradulations! You have found Michael Kolling! Now you can finally ask him what an abstract class is and not fail the final exam in January.");
                    System.out.println("You have reached the end of the game with success.");
                    System.out.println("Congradulations again and have a nice day.");
                    wantToQuit = true;//end the game
                    return;
                }
                else
                {//if a player tries to enter the final room without a hovercraft
                    System.out.println("You need a hovercraft!");
                    return;
                }
            }

            else
            {//go to the next room
                previousRooms.add(currentRoom);//add room to history to the player can go back if desired
                currentRoom = nextRoom;
                roomIntro(currentRoom);
                if(currentRoom.characterInRoom("Odinaldo")){//if the next room is Odinaldo's room--automatically prints the character's phrase in order to make sure the user is aware of the question and the punishment for answering it wrong
                    System.out.println(".");
                    System.out.println("Odinaldo says: Hello there " + currentRoom.getCharacter("Odinaldo").getPhrase());
                }
            }
        }

    }

    /**
     * Takes the player back to the previous room
     * Keeps track of all the rooms the player has been in throughout the game
     */
    private void back(){
        if(previousRooms.isEmpty()){
            System.out.println("Can't go back!");//if there is no history
        }
        else{
            currentRoom = previousRooms.get(previousRooms.size()-1);//go to the last room in history
            previousRooms.remove(previousRooms.size()-1);//delete that room from history so that 'back' can be used again to access room further back
            roomIntro(currentRoom);//show the player the room
        }
    }

    /**
     * Takes an item from a room and places it in his/her inventory
     * @param Item
     */
    private void take(Command command){
        if(!command.hasSecondWord()){//if the player did not provide the necessary information
            System.out.println("Take what?");
            return;
        }
        String itemName = command.getSecondWord();

        if(Room.getAllItemNames().contains(itemName)){//checks if the item exists in the game
            if(currentRoom.itemInRoom(itemName)){//checks if the item the player is trying to pick up is in the same room as the player
                Item item = currentRoom.getItem(itemName);
                if(itemName.equalsIgnoreCase("coffee")){
                    System.out.println("There is no time for coffee!");
                }
                else if(itemName.equalsIgnoreCase("beer")){
                    System.out.println("That is Odinaldo's beer!");   
                }
                else if(itemName.equalsIgnoreCase("kombatrin")){
                    System.out.println("You are not sick!");   
                }
                else if(totalWeight + item.getWeight() > MAX_WEIGHT){
                    System.out.println("You're carrying too heavy of a load!");
                }
                else{//add the item by adding it to the player's possessions and taking it out of the room
                    possessions.put(itemName,item);
                    totalWeight+=item.getWeight();
                    currentRoom.removeItem(itemName);
                    System.out.println("You added a(n) " + itemName + "!");
                    System.out.println(showInventory());
                    System.out.println(currentRoom.getItemString());
                    return;
                }
            }
            else{//if the item is not in the room -- alert the player
                System.out.println("There is no such item in this room!");
            }
        }
        else{//if the does not exist -- alert the player
            System.out.println("This item does not exist!");
        }
    }

    /**
     * Drops an item from the player's possessions into the room he/she is currently in
     * @param Item
     */
    private void drop(Command command){
        if(!command.hasSecondWord()){//if the player did not provide the necessary information
            System.out.println("Drop what?");
            return;
        }
        String itemName = command.getSecondWord();
        if(possessions.containsKey(itemName)){//if player has item, add it to room, delete it from player, take away it's weight, and alert the player of the changes 
            Item item = possessions.get(itemName);
            totalWeight -= item.getWeight();
            possessions.remove(itemName);
            System.out.println("You dropped a(n) " + itemName + "!");
            System.out.println(showInventory());
            currentRoom.addItem(itemName,item);
            System.out.println(currentRoom.getItemString());
            return;
        }

        System.out.println("You don't have that item!");

    }

    /**
     * Ask a character in the room for a clue
     * @param Command
     */
    private void ask(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Ask who?");   
        }
        else{
            String characterName = command.getSecondWord();
            if(Room.getAllCharacterNames().contains(characterName)){//is the character the user mentioned an actual character?
                if(currentRoom.characterInRoom(characterName)){//is the player in the right room for that character?
                    System.out.println(characterName + " says: " + currentRoom.getCharacter(characterName).getPhrase());
                    return;
                }
                else{//if the character exists but in a different room
                    System.out.println("This character is in a different room!");
                    return;
                }
            }
            else{//if the character does not exist in the game at all
                System.out.println("This character does not exist!");
            }
        }

    }

    /**
     * Give a specific item to a specific character
     */
    private void give(Command command){
        if(!command.hasSecondWord()){//if the user did not provide what to give and to who
            System.out.println("Give what to who?");
        }
        else if(!command.hasThirdWord()){//if the user did not privde who to give that item to
            System.out.println("Give that to who?");
        }
        else{
            String itemName = command.getSecondWord();
            String characterName = command.getThirdWord();
            if(possessions.containsKey(itemName) && Room.getAllCharacterNames().contains(characterName))
            {//does the item and the character the user typed in actually exist in the game?
                if(itemName.equalsIgnoreCase("MathResearch") && characterName.equalsIgnoreCase("Agi"))
                {//is the item the math research and is the character Agi?
                    if(currentRoom.characterInRoom(characterName))
                    {//is the player in the correct room in order to give the math research to Agi?
                        System.out.println("Thank you for the math paper. All I can say is that I was in your position, I wouldn't trust what Ernest would have to say. Nevertheless, I think it is a bit strange that Matthew would dodge your question.");
                        totalWeight -= possessions.get(itemName).getWeight();
                        possessions.remove(itemName);
                        currentRoom.addItem("hovercraft", "useful when you need go up", 90);
                        System.out.println(currentRoom.getItemString());
                        System.out.println(showInventory());
                        return;
                    }
                    else
                    {//if player has the math research and gives it to Agi but the player is in the wrong room
                        System.out.println("Agi is not here!");
                        return;
                    }
                }
                else if(!itemName.equalsIgnoreCase("MathResearch") && characterName.equalsIgnoreCase("Agi"))
                {//If the player tries to give the wrong item to Agi
                    System.out.println("You cannot give that to Agi!");
                    return;
                }
                else if(itemName.equalsIgnoreCase("MathResearch") && !characterName.equalsIgnoreCase("Agi"))
                {//if the player tries to give the math research to the wrong character
                    System.out.println("You cannot give the math research to " + characterName + "!");
                    return;
                }
                else if(itemName.equalsIgnoreCase("Agi") && characterName.equalsIgnoreCase("MathResearch"))
                {//if the player inputs the correct item and character in the wrong order
                    System.out.println("You got the two mixed up!");
                }
                else if(itemName.equals(characterName))
                {
                    System.out.println("That is not possible!");
                }
                else
                {//If the player tries to give a wrong item to a wrong character
                    System.out.println("You are fobidden to do that!");
                    return;
                } 
            }
            else if(!possessions.containsKey(itemName) && currentRoom.getAllCharacterNames().contains(characterName))
            {//If a player tries to give an item he/she doesn't have
                System.out.println("You don't have that item!");
                return;
            }
            else if(possessions.containsKey(itemName) && !currentRoom.getAllCharacterNames().contains(characterName))
            {//If a player tries to give an item to a character that doesn't exist
                System.out.println("That character does not exist!");
                return;
            }
            else{//If a player tries to give an item he/she doesn't have to a character that does not exist
                System.out.println("That is not possible!");
            }

        }
    }

    /**
     * Print out all the possessions of the player
     */
    private String showInventory(){
        String result = "Items you have: ";
        if(possessions.size() == 0){
            result += "Nothing";
        }
        else{
            Set<String> keys = possessions.keySet();
            for(String itemName : keys){
                result += (" " + itemName);
            }
        }
        return result;
    }

    /**
     * Prints out all the necessary information about the room the player is currently in
     */
    private void roomIntro(Room room){
        System.out.println("----------------------------------------------------------");
        System.out.println(room.getLongDescription());
        System.out.println(".");
        System.out.println(showInventory());
    }

    /**
     * If the player tries to enter a locked door or answers a question wrong the player gets punished
     * protocol for a punishment: punishmenting a player to a random room after a 5 second delay 
     * deleting his/her travel history (can't use back function for rooms before the punishment)
     * and shuffling the characters to different rooms
     */
    private void punishment(){
        try//cause a 5 second delay
        {
            Thread.sleep(5000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        previousRooms.removeAll(previousRooms);//clear 'back' command history (part of punishment)
        
        currentRoom.scrambleCharacters();//shuffle the arraylist of all the characters

        for(int i = 0; i < 9; i++)//add the suffled characters to each room one by one after deleting the original characters 
        {
            roomList[i].removeAllCharactersInRoom();
            roomList[i].addCharacter(Room.getAllCharacters().get(i));
        }
        Random rand = new Random();
        int randIndex = rand.nextInt(8);
        currentRoom = roomList[randIndex];//transport the player to a random room in the list of rooms (not including office or final room)
        roomIntro(currentRoom);//show the player the new room he/she is in
        return;
    }

    /**
     * When the player is trying to answer Odinaldo's question
     * @param String The player's answer
     */
    private void odinaldoResponse(String input){
        if(input.equalsIgnoreCase("odinaldo"))
        {
            System.out.println("You are correct! I have been trying to decieve you all along! No one was trying to mislead you--except for me.");
            System.out.println("For showing out of the box thinking, I will tell you where Michael Kolling is.");
            System.out.println("There is a secret compartment in the ceiling of the Bush House lecture theatre. Go there and go up");
            finalRoom = new Room("the secret compartment of the Bush House lecture theatre", false);//create a new unlocked room
            auditorium.setExit("up", finalRoom);
            finalRoom.addCharacter("Michael", "Congragulations. You have found me and sucessfully completed the game.");//add a character in that room
            return;
        }
        else{
            System.out.println("I am very sorry but you answer is not correct.");
            System.out.println("Your punishment is that you get transported to a random room in 5 seconds!");
            punishment();
            return;
        }
    }

   
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

}