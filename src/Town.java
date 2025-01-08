
/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private Treasure treasure;
    private static int amountTreasure;
    private final boolean CHEAT_MODE;

    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean cheatMode)
    {
        this.shop = shop;
        CHEAT_MODE = cheatMode;
        this.terrain = getNewTerrain();
        this.treasure = new Treasure("Necklace", "Heirloom", "Saddle");

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);

        amountTreasure = 0;
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }
        printMessage += "\n╚══════════════════════════════════════════════════════════════════════════╝";

        if (Math.random() > noTroubleChance)
        {
            printMessage = "╔══════════════════════════════════════════════════════════════════════════╗\nYou couldn't find any trouble";
        }
        else
        {
            double winValue = Math.random();
            int goldDiff = (int)(Math.random() * 10) + 1;
            if (CHEAT_MODE){
                winValue = 1;
                goldDiff = 100;

            }
            printMessage = "╔══════════════════════════════════════════════════════════════════════════╗\nYou want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            if (winValue > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
                if (hunter.getGold() <= 0) {
                    System.exit(1);
                }
            }
        }
        if (hunter.getGold() <= 0) {
            printMessage += "\nYou ran out of gold, you lost!";
        }
    }

    public void lookForTreasure() {
        if (treasure != null) {
            String value = treasure.dig();
            if (!value.isEmpty()) {
                if (hunter.hasItemInKit(value)){
                    printMessage = String.format("╔══════════════════════════════════════════════════════════════════════════╗\nYou have found a(n) %s, but you already have it inside of your kit.", value);
                    treasure = null;
                }
                else {
                    printMessage = "╔══════════════════════════════════════════════════════════════════════════╗\nYou have found treasure! You have found " + value + "!";
                    amountTreasure++;
                    hunter.addItem(value);
                    treasure = null;
                }
            } else {
                printMessage = "╔══════════════════════════════════════════════════════════════════════════╗\nYou haven't found any treasure";
            }
        } else {
            printMessage = "╔══════════════════════════════════════════════════════════════════════════╗\nThere is no treasure here, maybe in another town?";
        }
        if (amountTreasure == 3) {
            printMessage = "╔══════════════════════════════════════════════════════════════════════════╗\nYou have found all the treasure!";
            System.exit(1);
        }
        printMessage += "\n╚══════════════════════════════════════════════════════════════════════════╝";

    }


    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }

    // Plays the Lucky Dice game
    public void gamble(int gold, int num) {
        hunter.changeGold(-1 * gold);
        int dice1 = (int)((Math.random() * 6) + 1);
        int dice2 = (int)((Math.random() * 6) + 1);
        int sum = dice1 + dice2;
        if (sum == num) {
            printMessage += "Wow! You've hit the jackpot! You have won " + gold * 2 + " gold!";
            hunter.changeGold(gold * 2);
        }
        else if (Math.abs(sum - num) == 2) {
            printMessage += "You were close! You have won " + gold + " gold!";
            hunter.changeGold(gold);
        }
        else {
            printMessage += "What a bummer. You have lost all your gold. Try again";
        }
    }
}
