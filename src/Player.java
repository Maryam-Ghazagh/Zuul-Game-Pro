/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class is for player features.
 *
 * @author Maryam Ghazagh
 * @version 2022.07.05
 */
public class Player
{
    private Room currentRoom;
    private Room previousRoom;
    private Item item;
    
    public Player(){
       currentRoom = null;
       previousRoom = null;
       item = null;
    }
    
    /**
     * If the player is in a room, puts the room in currentRoom.
     * 
     * @param currentRoom The room where the player is in.
     */
    public void setCurrentRoom(Room currentRoom){
        if(currentRoom != null){
            this.currentRoom = currentRoom;
        }
    }
    
    /**
     * Return where the player is in.
     * 
     * @return currentRoom The room where the player is in.
     */
    public Room getCurrentRoom(){
        return currentRoom;
    }
    
    /**
     * If the player was in a room, puts the room in previousRoom.
     * 
     * @param previousRoom The room where the player was in.
     */
    public void setPreviousRoom(Room previousRoom){
        if(previousRoom != null){
            this.previousRoom = previousRoom;
        }
    }
    
    /**
     * Return where the player was in.
     * 
     * @return previousRoom The room where the player was in.
     */
    public Room getPreviousRoom(){
        return previousRoom;
    }
    
    /**
     * In the player item, enter the item that the player is going to carry.
     * @param item
     */
    public void setItem(Item item){
        this.item = item;
    }
    
    /**
     * return the item carried by the player.
     * @return item
     */
    public Item getItem(){
        return item;
    }
    
    /**
     * puts null in the player item.
     */
    public void removeItem(){
        item = null;
    }
}
