import java.util.ArrayList;

/**
 * Class MagicRoom - a room in an adventure game. 
 * MagicRoom is a Room that this room is a magical room. 
 *
 * @author Maryam Ghazagh
 * @version 2022.07.04
 */
public class MagicRoom extends Room
{
    private ArrayList<Room> rooms;
    private String description;
    
    public MagicRoom(String description , ArrayList<Room> rooms){
        super(description);
        this.rooms = rooms;
        this.description = description;
    }
    
    /**
     * This method return a random room.
     * 
     * @return a random room.
     */
    public Room getExit(){
        return new RandomRoom().getRandomRoom(rooms);
    }
    
    @Override
    public String getLongDescription()
    {
        return "You are " + description + "!\nExits : nothing";
    }
}
