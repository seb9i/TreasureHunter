import java.util.ArrayList;
import java.util.HashMap;
public class Treasure {
    private HashMap<String, Integer> treasure;

    public Treasure(int weight1, int weight2, int weight3){
        treasure = new HashMap<String, Integer>();
        treasure.put("Necklace", weight1);
        treasure.put("Bottle", weight2);
        treasure.put("Stone", weight3);
    }

    public String getListOfTreasures(){
        String listOfItems = "";
        for (String key: treasure.keySet()){
            listOfItems += key + "\n";
        }
        return listOfItems;
    }
    public int dig(){
        int number = ((int) (Math.random() * 15) + 1);
        if (number <= 2){
            return treasure.get("Necklace");
        }
        else if (number <= 4){
            return treasure.get("Bottle");
        }
        else if (number <= 6){
            return treasure.get("Stone");
        }
        else {
            return 0;
        }
    }




}
