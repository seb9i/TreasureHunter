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

        // set hunter instance variable
        hunter = new Hunter(name, 10);

        System.out.print("Hard mode? (y/n): ");
        String hard = scanner.nextLine();
        if (hard.equalsIgnoreCase("y"))
        {
            hardMode = true;
        }
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

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

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
            if (hunter.getGold() <= 0) {
                System.exit(1);
            }
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            String leftAlignFormat = "|| %-17s || %-30s || %-30s || %n";
            System.out.format("//=================[]========================================[]========================\\\\%n");
            System.out.format("|| Inventory         || Shop                           || Town                           ||%n");
            System.out.format("\\\\=================[]=======================================[]=========================//%n");
            for (int i = 0; i < options.size() / 2; i++){
                System.out.format(leftAlignFormat,null,  options.get(i), options.get(options.size() - i - 1));
            }
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
        }
        else
        {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}