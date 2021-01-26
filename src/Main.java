import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * AUTHOR: David Anderson
 * FILE: Main.java
 *
 * PURPOSE: This is the main class for the Aisle Finder program. Aisle Finder allows
 * users to create an account, log in, create a grocery list, and the program can
 * automatically add aisle numbers to that grocery list (if aisles are found).
 * Users are able to update/remove/add to the list at any time. The program uses
 * grocerystoreinfo.db to store User login data (including hashed passwords) and
 * data about grocery items and their aisle numbers in associated stores.
 * Right now there are only a few local stores from Tucson, AZ because
 * I haven't determined how best to populate the database with accurate aisle data.
 *
 * USAGE:
 * java Main
 *
 *
 */

public class Main {
    //FIXME convert to local
    private static final Scanner scanner = new Scanner(System.in);
    //creates a dynamic list for storing items
    private static final List groceryList = new List();

    private static boolean quitApp = false; //master command to exit app
   // private static boolean acceptPW;

    public static void main(String[] args) {

        System.out.println("Welcome! Please select one: ");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        int selection = -1;
        try {
            selection = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e);
            main(args);
        }

        scanner.nextLine();
        //if user selects create account, we get details and send to UserProfile
        if (selection == 2) {
            createAccount();
        }
        boolean login;
        //created a do-while loop here so if login fails they can try again.
        do {
            System.out.println("Username: ");
            String user = scanner.nextLine();
            System.out.println("Password: ");
            String pass = scanner.nextLine();
            //UserProfile.validateLogin(user, pass);
//            if (!UserProfile.acceptPW()) {
//                System.out.println("Login failed. Try again.");
//                //FIXME: update once db is working
//                login = true;
//            } else {
//                login = true;
//            }
            login = true;
            welcomeMsg();
        } while (!login);
//        while (!quitApp && UserProfile.acceptPW()) {
//            welcomeMsg();
//        }

   }

   private static void createAccount() {
       UserProfile userProfile = new UserProfile();
       System.out.println("Please enter a username: ");
       userProfile.setName(scanner.nextLine());
       System.out.println("Enter a password: ");
       userProfile.setPassword(scanner.nextLine());
       System.out.println("Please enter an email: ");
       userProfile.setEmail(scanner.nextLine());
       userProfile.generateLogin
               (userProfile.getName(), userProfile.getPassword(), userProfile.getEmail());
       userProfile.write();
       System.out.println("Success! Now please log in: ");
   }

   //small helper method to provide initial instructions
   private static void welcomeMsg() {
       System.out.println("Welcome to Aisle finder!");
       Date date = new Date(); // This object contains the current date value
       SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
       System.out.println(formatter.format(date));
       System.out.println("-------------------------------");
       printListInstructions();
       createListOption();
   }

    /**
     * Purpose: This helper method uses an enhanced switch statement
     * with lambda expressions to call methods appropriate for the user's
     * choice.
     */
    private static void createListOption() {
        boolean quit = false;
        int choice;
        while (!quit && !quitApp) {
            System.out.println("Enter your choice (Press 0 to see options):");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 0 -> printListInstructions();
                case 1 -> groceryList.printGroceryList();
                case 2 -> addItems();
                case 3 -> modifyItem();
                case 4 -> removeItem();
                case 5 -> searchForItem();
                case 6 -> selectStoreAddAisles();
                case 7 -> {
                    System.out.println("Goodbye!");
                    quit = true;
                    quitApp = true;
                }
                case 8 -> addToDB();
            }
        }
    }

    /**
     * Another private helper method to select the store they'll be shopping at
     * and add the aisles to that store.
     */
    private static void selectStoreAddAisles() {
        System.out.println("Please select a store from the list:");
        String[] storeList = {"Fry's (1st Ave/Grant Rd)",
                "Safeway (Broadway/Campbell)", "Safeway (Saint Mary's/6th"};
        for (int i = 0; i < storeList.length; i++) {
            System.out.println((i + 1) + ". " + storeList[i] + ".");
        }
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1 -> {
                System.out.println("Aisles for " + storeList[choice - 1] + ": ");
                groceryList.addAisles("F01");
            }
            case 2 -> groceryList.addAisles("S01");
            case 3 -> groceryList.addAisles("S02");
            default -> System.out.println("Oops! Couldn't find that store. Try again.");
        }
    }
    //this method is hidden from users but allows the developer
    //to add items to the database
    private static void addToDB() {
        System.out.println("Store ID: ");
        String storeID = scanner.nextLine();
        System.out.println("Item name: ");
        String item = scanner.nextLine().toLowerCase();
        System.out.println("Aisle #: ");
        String aisle = scanner.nextLine();
        System.out.println("Category: ");
        String category = scanner.nextLine();
        groceryList.addToDB(storeID, item, aisle, category);
        System.out.println("Again? (Y/N) ");
        String again = scanner.nextLine();
        if (again.equals("Y") || again.equals("y")) {
            addToDB();
        } else {
            createListOption();
        }
    }

    //another helper method to print the grocery list instructions
    private static void printListInstructions() {

        System.out.println("Let's create a shopping list! Press ");
        System.out.println("\t 0 - To print choice options.");
        System.out.println("\t 1 - To print list of grocery items.");
        System.out.println("\t 2 - To add items to the list.");
        System.out.println("\t 3 - To modify an item in the list.");
        System.out.println("\t 4 - To remove an item from the list.");
        System.out.println("\t 5 - To search for an item in the list.");
        System.out.println("\t 6 - To select your store & add aisle numbers to the list.");
        System.out.println("\t 7 - To quit the application.");
    }
    //allows users to add an item to the grocery list
    private static void addItems() {
        System.out.print("Please enter a grocery item: (Type Q if done adding)\n");
        String item = scanner.nextLine();
        if (item.equals("Q") || item.equals("q")) {
            createListOption();
        } else {
            groceryList.addGroceryItem(item);
            System.out.println("Grocery item added!");
            addItems();
        }

    }
    //this let's user find an existing item in the list and update its name
    private static void modifyItem() {
        System.out.println("Enter the item's name you wish to update: ");
        String name = scanner.nextLine().toLowerCase();
        System.out.println("Enter the updated name: ");
        String newItem = scanner.nextLine().toLowerCase();
        groceryList.modifyGroceryItem(name, newItem);
        System.out.println("Item updated!");
    }
    //searches the grocery list and removes the selected item
    private static void removeItem() {
        System.out.println("Enter the item's name you wish to remove: ");
        String name = scanner.nextLine().toLowerCase();
        groceryList.removeItem(name);
        System.out.println("Would you like to remove another item? (Type Y or N)");
        String again = scanner.nextLine().toLowerCase();
        if (again.equals("y") || again.equals("yes")) {
            removeItem();
        } else {
            createListOption();
        }

    }
    //this allows users to check and see if they have already added an item
    private static void searchForItem() {
        System.out.println("Enter the name of the item you want to find: ");
        String name = scanner.nextLine().toLowerCase();
        if (groceryList.itemExists(name)) {
            groceryList.searchItem(name);
        } else {
            System.out.println("Item not found.");
            System.out.println("Would you like to add it? (Type Y or N)");
            String addChoice = scanner.nextLine().toLowerCase();
            if (addChoice.equals("y") || addChoice.equals("yes")) {
                groceryList.addGroceryItem(name);
                System.out.println("Item added!");
            } else {
                System.out.println("Oops. Couldn't understand that. Try again.");
            }
        }

    }
}