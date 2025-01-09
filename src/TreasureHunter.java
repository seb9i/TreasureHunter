/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all of the display based on the messages it receives from the Town object.
 *
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class TreasureHunter
{
    //Instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean cheatMode;

    //Constructor
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter()
    {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
    }

    // starts the game; this is the only public method
    public void play ()
    {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();



        System.out.print("Hard mode or easy mode? (h/e/n): ");
        String hard = scanner.nextLine();

        // cheat input "xyz"
        if (hard.equalsIgnoreCase("y"))
        {
            hardMode = true;
        }
        if (hard.equalsIgnoreCase("e")){
            easyMode = true;
        }
        if (hard.equalsIgnoreCase("xyz")){
            cheatMode = true;
            System.out.println("Cheat mode enabled.");
        }

        // set hunter instance variable
        hunter = new Hunter(name, ((easyMode)? 15: 1000));
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown()
    {
        double markdown = 0.25;
        double toughness = 0.4;
        if (hardMode)
        {
            // in hard mode, you get less money back when you sell items
            markdown = 0.5;

            // and the town is "tougher"
            toughness = 0.75;
        }
        if (easyMode){
            // in easy mode, you get more money back when you sell items
            markdown = 0.99;

            // and the town is "weaker"
            toughness = 0.3;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, cheatMode);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, cheatMode);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String choice = "";
        ArrayList<String> options = new ArrayList<String>();
        options.add("(B)uy something at the shop.");
        options.add("(S)ell something at the shop.");
        options.add("(L)ook for trouble");
        options.add("(M)ove on to a different town.");
        options.add("(H)unt for treasure");


        while (!(choice.equalsIgnoreCase("e")))
        {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println(hunter);
            System.out.println(currentTown);
            // Menu
            System.out.println("//===========[]==================================[]================================\\\\");
            System.out.println("|| Inventory ||               Shop               ||              Town              ||");
            System.out.println("|]===========[]==================================[]================================[|");
            System.out.println("|| 1-" + currentTown.getLuck() + "%      || (B)uy something at the shop.     || (H)unt for treasure.           ||");
            System.out.println("|| 2-XXXXX   || (S)ell something at the shop.    || (M)ove on to a different town. ||");
            System.out.println("|| 3-XXXXX   ||                                  || (L)ook for trouble             ||");
            System.out.println("|| 4-XXXXX   ||                                  || (C)asino                       ||");
            System.out.println("|| 5-XXXXX   ||                                  ||                                ||");
            System.out.println("|| 6-XXXXX   ||                                  ||                                ||");
            System.out.println("\\\\===========[]==================================[]================================//");
            System.out.println();


            System.out.print("What's your next move? ");
            choice = scanner.nextLine();
            choice = choice.toUpperCase();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice)
    {
        if (choice.equalsIgnoreCase("s") || choice.equalsIgnoreCase("b"))
        {
            currentTown.enterShop(choice);
        }
        else if (choice.equalsIgnoreCase("m"))
        {
            if (currentTown.leaveTown())
            {
                //This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        }
        else if (choice.equalsIgnoreCase("l"))
        {
            currentTown.lookForTrouble();

        }
        else if (choice.equalsIgnoreCase("e"))
        {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        }
        else if (choice.equalsIgnoreCase("h")) {
            currentTown.lookForTreasure();
            System.out.println(currentTown.getLatestNews());
        }
        else if (choice.equalsIgnoreCase("c")) {
            if (hunter.getGold() != 0) {
                System.out.println("\nWelcome to Lucky Dice!");
                Scanner s = new Scanner(System.in);
                System.out.print("How much gold are you willing to bet?: ");
                int gold = Integer.parseInt(s.nextLine());
                if (gold > hunter.getGold()) {
                    System.out.println("I know your tricks! Try again.");
                }
                else {
                    if (gold < hunter.getGold()) {
                        System.out.println(gold + " gold. Don't be a cheapskate.");
                    }
                    else {
                        System.out.println("Now this is a good bet!");
                    }
                    Scanner x = new Scanner(System.in);
                    System.out.print("Pick a number, any number: ");
                    int num = Integer.parseInt(x.nextLine());
                    currentTown.gamble(gold, num);
                }
            }
            else {
                System.out.println("Looks like you don't have enough gold. Look for some more.");
            }
        }
        else
        {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}