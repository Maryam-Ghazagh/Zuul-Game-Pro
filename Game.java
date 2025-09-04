import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Maryam Ghazagh
 * @version 2022.07.05
 */

public class Game 
{
    private Parser parser;
    private Player player;
    private ArrayList<Room> rooms ;
    private ArrayList<Item> items;
    private Item selectedDVD;
    private Item selectedFlash;
    private boolean lock;
    
    /** 
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        player = new Player();
        parser = new Parser();
        rooms = new ArrayList<>();
        items = new ArrayList<>();
        selectedDVD = null;
        selectedFlash = null;
        lock = true;
        createRooms();
        createItems();
        defaultMode();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
        Room library, readingRoom, gameCenter, gameCenterOffice;
        MagicRoom magicRoom , secondMagicRoom;
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        library = new Room("in the university library");
        readingRoom = new Room("in the reading room");
        gameCenter = new Room("in a game center");
        gameCenterOffice = new Room("in the game center admin office");
        rooms.add(outside);
        rooms.add(theater);
        rooms.add(pub);
        rooms.add(lab);
        rooms.add(office);
        rooms.add(library);
        rooms.add(readingRoom);
        rooms.add(gameCenter);
        rooms.add(gameCenterOffice);
        magicRoom = new MagicRoom("in the magic room" , rooms);
        secondMagicRoom = new MagicRoom("in the second magic room" , rooms);
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", gameCenter);

        theater.setExit("west", outside);
        theater.setExit("up", readingRoom);

        pub.setExit("east", outside);
        pub.setExit("south", lab);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("up", library);
        lab.setExit("north-west", pub);
        lab.setExit("south", magicRoom);

        office.setExit("west", lab);
        
        library.setExit("north", readingRoom);
        library.setExit("down", lab);
        
        readingRoom.setExit("south", library);
        readingRoom.setExit("down", theater);
        
        gameCenter.setExit("up" , gameCenterOffice);
        gameCenter.setExit("south" , outside);
        gameCenter.setExit("north" , secondMagicRoom);
        
        gameCenterOffice.setExit("down", gameCenter);

        player.setCurrentRoom(outside);  // start game outside
    }
    
    /**
     * create all the items and insert them in the random rooms.
     */
    private void createItems(){
        // create items
        items.add(new Item("blue DVD"));
        items.add(new Item("red DVD"));
        items.add(new Item("flash 2 GB"));
        items.add(new Item("flash 8 GB"));
        items.add(new Item("flash 16 GB"));
        
        // insert items in the random rooms
        HashSet<Room> randomRooms = new HashSet<>();
        while(randomRooms.size() != items.size()){
            int index = new Random().nextInt(rooms.size());
            randomRooms.add(rooms.get(index));
        }
        int i = 0;
        for(Room room : randomRooms){
            room.setItem(items.get(i));
            i++;
        }
    }
    
    private void defaultMode(){
        boolean found = false;
        boolean foundDVD = false;
        boolean foundFlash = false;
        while(!found){
            int index = new Random().nextInt(items.size());
            if(foundDVD && foundFlash){
                found = true;
            }
            else{
                if(!foundFlash && items.get(index).getName().contains("flash")){
                    selectedFlash = items.get(index);
                    foundFlash = true;
                }
                else if(!foundDVD && items.get(index).getName().contains("DVD")){
                    selectedDVD = items.get(index);
                    foundDVD = true;
                }
            }
        }
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
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case BACK:
                back(command);
                break;
            
            case TAKE:
                takeItem();
                break;
                
            case DROP:
                dropItem();
                break;
                
            case LIST:
                listItem();
                break;
                
            case LOOK:
                lookItem();
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit or player is in magic room, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        Room nextRoom;
        if(player.getCurrentRoom().getLongDescription().contains("magic")) {
            MagicRoom magicRoom = (MagicRoom) player.getCurrentRoom();
            nextRoom = magicRoom.getExit();
        }
        else {
            if(!command.hasSecondWord()) {
                // if there is no second word, we don't know where to go...
                System.out.println("Go where?");
                return;
            }
    
            String direction = command.getSecondWord();
    
            // Try to leave current room.
            nextRoom = player.getCurrentRoom().getExit(direction);
    
            if (nextRoom == null) {
                System.out.println("There is no door!");
                return;
            }
        }
    
        if((nextRoom.getLongDescription().contains("in a game center") && lock &&
        player.getCurrentRoom().getLongDescription().contains("outside"))||
        player.getCurrentRoom().getLongDescription().contains("in a game center")
        && !nextRoom.getLongDescription().contains("in the game center admin office") && lock){
            System.out.println("Sorry, the door is lock!");
            return;
        }
        player.setPreviousRoom(player.getCurrentRoom());
        player.setCurrentRoom(nextRoom);
        System.out.println(player.getCurrentRoom().getLongDescription());
        
        if(player.getCurrentRoom().getLongDescription().contains("in the game center admin office")){
            if(lock){
                if(player.getItem() == selectedDVD){
                    System.out.println("The security system has been disabled.");
                    lock = false;
                }
                else{
                    System.out.println("The security system was not disabled!");
                }
            }
            else{
                if(player.getItem() == selectedFlash){
                    System.out.println("Congratulation! You won the game!");
                }
            }
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
    
    /**
     * Try to back. If there is an Room for back, enter the previous
     * room, otherwise print an error message.
     */
    private void back(Command command){
        if(command.hasSecondWord()){
            System.out.println("Back what?");
        }
        else{
            if(player.getPreviousRoom() == null){
                System.out.println("back where?");
            }
            else{
                Room nextRoom = player.getPreviousRoom();
                player.setPreviousRoom(player.getCurrentRoom());
                player.setCurrentRoom(nextRoom);
                System.out.println(player.getCurrentRoom().getLongDescription());
            }
        }
    }
    
    /**
     * If there is an item where the player is in,
     * adds it to the player and remove it from the room.
     * Otherwise print an error message.
     */
    private void takeItem(){
        Item item = player.getCurrentRoom().getItem();
        if(item == null){
            System.out.println("There is no item here.");
        }
        else{
            player.setItem(item);
            player.getCurrentRoom().removeItem();
            System.out.println("done.");
        }
    }
    
    /**
     * If there is no other item in the room and there is an item in the player,
     * adds it to where the player is in and remove it from the player.
     * Otherwise print an error message.
     */
    private void dropItem(){
        if(player.getItem() != null){
            if(player.getCurrentRoom().getItem() != null){
                System.out.println("Sorry, there is another item here.");
            }
            else{
                player.getCurrentRoom().setItem(player.getItem());
                player.removeItem();
                System.out.println("done.");
            }
        }
        else{
            System.out.println("No item was for drop.");
        }
    }

    /**
     * Prints the item carried by the player.
     */
    private void listItem(){
        Item item = player.getItem();
        if(item == null){
            System.out.println("No item was taken.");
        }
        else{
            System.out.println(item.getName());
        }
    }
    
    /**
     * Prints the item in the room the player is in.
     */
    private void lookItem(){
        Item item = player.getCurrentRoom().getItem();
        if(item == null){
            System.out.println("There is no item here.");
        }
        else{
            System.out.println(item.getName());
        }
    }
}
