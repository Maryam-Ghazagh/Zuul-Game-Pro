
/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class holds an item that the player carries,
 * or a room has it.
 *
 * @author Maryam Ghazagh
 * @version 2022.07.05
 */
public class Item
{
   private String name;
   
   public Item(String name){
       this.name = name;
   }
   
   /**
    * @return the item name.
    */
   public String getName(){
       return name;
   }
}
