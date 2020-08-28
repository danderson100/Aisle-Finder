import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    //creates a dynamic list for storing items
    private static final List groceryList = new List();

    private static boolean quitApp = false; //master command to exit app
   // private static boolean acceptPW;

    public static void main(String[] args) {

        System.out.println("Welcome! Please select one: ");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        int selection = scanner.nextInt();
        scanner.nextLine();
        //if user selects create account, we get details and send to UserProfile
        if (selection == 2) {
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
        boolean login;
        //created a do-while loop here so if login fails they can try again.
        do {
            System.out.println("Username: ");
            String user = scanner.nextLine();
            System.out.println("Password: ");
            String pass = scanner.nextLine();

            UserProfile.validateLogin(user, pass);
            if (!UserProfile.acceptPW()) {
                System.out.println("Login failed. Try again.");
                login = false;
            } else {
                login = true;
            }
        } while (!login);
        while (!quitApp && UserProfile.acceptPW()) {
            System.out.println("Welcome to Aisle finder!");
            Date date = new Date(); // This object contains the current date value
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            System.out.println(formatter.format(date));
            System.out.println("-------------------------------");
            printListInstructions();
            createListOption();
        }

    }

    private static void createListOption() {
        boolean quit = false;
        int choice;
        while (!quit && !quitApp) {
            System.out.println("Enter your choice (Press 0 to see options):");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 0:
                    printListInstructions();
                    break;
                case 1:
                    groceryList.printGroceryList();
                    break;
                case 2:
                    addItems();
                    break;
                case 3:
                    modifyItem();
                    break;
                case 4:
                    removeItem();
                    break;
                case 5:
                    searchForItem();
                    break;
                case 6:
                    selectStoreAddAisles();
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    quit = true;
                    quitApp = true;
                    break;
                case 8:
                    addToDB();
                    break;

            }
        }
    }

    private static void selectStoreAddAisles() {
        System.out.println("Please select a store from the list:");
        String[] storeList = {"Fry's (1st Ave/Grant Rd)",
                "Safeway (Broadway/Campbell)", "Safeway (Saint Mary's/6th"};
        for (int i = 0; i < storeList.length; i++) {
            System.out.println((i + 1) + ". " + storeList[i] + ".");
        }
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                System.out.println("Aisles for " + storeList[choice -1] + ": ");
                groceryList.addAisles("F01");
                break;
            case 2:
                groceryList.addAisles("S01");
                break;
            case 3:
                groceryList.addAisles("S02");
                break;
            default:
                System.out.println("Oops! Couldn't find that store. Try again.");
                break;
        }
    }

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

    private static void modifyItem() {
        System.out.println("Enter the item's name you wish to update: ");
        String name = scanner.nextLine().toLowerCase();
        System.out.println("Enter the updated name: ");
        String newItem = scanner.nextLine().toLowerCase();
        groceryList.modifyGroceryItem(name, newItem);
        System.out.println("Item updated!");
    }

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