import java.util.ArrayList;
import java.util.HashMap;
public class Treasure {
    private HashMap<String, Integer> treasure;
    private String item1;
    private String item2;
    private String item3;

    public Treasure(String item1, String item2, String item3){
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    public String getListOfTreasures(){
        String listOfItems = "";
        for (String key: treasure.keySet()){
            listOfItems += key + "\n";
        }
        return listOfItems;
    }
    public String dig(){
        int number = ((int) (Math.random() * 15) + 1);
        if (number <= 2){
            return item1;
        }
        else if (number <= 4){
            return item2;
        }
        else if (number <= 6){
            return item3;
        }
        else {
            return "";
        }
    }




}
