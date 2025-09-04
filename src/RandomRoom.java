import java.util.ArrayList;
import java.util.Random;

/**
 * This class has a method that return a random room.
 * 
 * @author Maryam Ghazagh
 * @version 2022.07.04
 */
public class RandomRoom
{
    /**
     * This method get the game rooms and
     * return a random room between them.
     * 
     * @param the game rooms.
     * @return a random room.
     */
    public Room getRandomRoom(ArrayList<Room> rooms){
        Random rand = new Random();
        int index = rand.nextInt(rooms.size());
        return rooms.get(index);
    }
}
