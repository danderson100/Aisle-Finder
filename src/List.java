import java.sql.*;
import java.util.ArrayList;

/*
 * AUTHOR: David Anderson
 * FILE: List.java
 *
 * PURPOSE: This class stores all of the user's grocery list information. It allows users to
 * update items, remove items, add, and print their existing list. The List class also has private
 * methods to call information from the database, but the path will have to be changed to match
 * the user's path to their grocerystoreinfo.db file.
 *
 * USAGE INSTRUCTIONS:
 * List groceryList = new List();
 *
 * EXAMPLES:
 * groceryList.addItem(item);
 *
 * groceryList.printGroceryList();
 *
 *
 *
 *
 */
public class List {
    //these are for SQL queries
    public static final String DB_NAME = "grocerystoreinfo.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/home/dayvihd/IdeaProjects/Aisle-Finder/"
            + DB_NAME;
    //stores
    private final ArrayList<String> groceryList = new ArrayList<>();

    /**
     * Purpose: This functions similarly to a toString() method, printing
     * the existing grocery list.
     */
    public void printGroceryList() {
        System.out.println("You have " + groceryList.size() + " items in your grocery list.");

        for (int i = 0; i < groceryList.size(); i++) {
            String firstLetter = groceryList.get(i).substring(0, 1);

            System.out.println((i + 1) + ". " + firstLetter.toUpperCase() +
                    groceryList.get(i).substring(1));
        }
    }

    /**
     * Purpose: call this method to add an item to the list
     * @param item, is the item to be added.
     */
    public void addGroceryItem(String item) {
        groceryList.add(item.toLowerCase());
    }

    /**
     * Purpose: this method calls the findItem helper method, if
     * findItem returns index >= 0 then the item is found and
     * can be modified.
     * @param currentName, current item name
     * @param newItem, name they wish to change it to
     */
    public void modifyGroceryItem(String currentName, String newItem) {
        int position = findItem(currentName);
        if (position >= 0) {
            modifyItem(position, newItem);
        }
    }
    //this is an unlisted option to add items to the database.
    //I created different code to fill the database.
    public void addToDB(String store, String item, String aisle, String category) {
        try {
            Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = conn.createStatement();
            String query = "INSERT INTO aisles (storeID, itemName, aisle, category) " +
                    "VALUES('" + store + "', '" + item + "', '" + aisle + "', '" + category + "')";

            statement.execute(query);

            statement.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }

    /**
     * Purpose: This method retrieves the aisle data from the database
     * and, if the item exists in the database, it adds the aisle number
     * to their grocery list.
     *
     * @param storeSelection, is the store they wish to get aisle # for.
     */
    public void addAisles(String storeSelection) {

        try {
            Connection conn = DriverManager.getConnection
                    (CONNECTION_STRING);

            String[] aisleNum = new String[groceryList.size()];
            for (int i = 0; i < groceryList.size(); i++) {
                String item = groceryList.get(i);
                Statement statement = conn.createStatement();

                String query = "SELECT aisle FROM aisles  WHERE storeID='"
                        + storeSelection + "' AND itemName LIKE '%" + item + "%'";
                ResultSet results = statement.executeQuery(query);
                String firstLetter = item.substring(0, 1);
                //checks to see if the item is in the database
                if (results.next()) {
                    aisleNum[i] = results.getString("aisle");
                    System.out.println((i + 1) + ". " + firstLetter.toUpperCase() + item.substring(1)
                            + " - Aisle " + aisleNum[i]);
                } else {
                    System.out.println((i + 1) + ". " + firstLetter.toUpperCase() + item.substring(1)
                            + " - Aisle not found.");
                }

                results.close();
                statement.close();
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Purpose: Private helper method that changes the item
     * name at the given index (position)
     * @param position, index to modify
     * @param newName, new name.
     */
    private void modifyItem(int position, String newName) {
        groceryList.set(position, newName);

    }

    private void removeItem(int position, String name) {
        groceryList.remove(position);
        String capitalizeItemName = name.substring(0, 1).toUpperCase() + name.substring(1);
        System.out.println(capitalizeItemName + " has been removed from your shopping list.");
    }

    /**
     * Purpose: This is the user-facing method which finds the item's
     * index and then calls another method to remove the item.
     *
     * @param name, is the name of the item to be removed.
     */
    public void removeItem(String name) {
        int position = findItem(name);
        if (itemExists(name) && position >= 0) {
            removeItem(position, name);
        } else {
            System.out.println("Item not found.");
        }
    }

    public void searchItem(String item) {
        int position = findItem(item);
        if (position >= 0) {
            searchItems(position);

        }
    }

    public boolean itemExists(String name) {
        return groceryList.contains(name);
    }

    private void searchItems(int position) {
        System.out.println(groceryList.get(position) + " is in your shopping list!");

    }

    private int findItem(String searchItem) {
        return groceryList.indexOf(searchItem);
    }

}