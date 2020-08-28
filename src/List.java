import java.sql.*;
import java.util.ArrayList;

public class List {
    public static final String DB_NAME = "grocerystoreinfo.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Main\\Desktop\\databases\\"
            + DB_NAME;

    private ArrayList<String> groceryList = new ArrayList<>();

    public void printGroceryList() {
        System.out.println("You have " + groceryList.size() + " items in your grocery list.");

        for (int i = 0; i < groceryList.size(); i++) {
            String firstLetter = groceryList.get(i).substring(0, 1);

            System.out.println((i + 1) + ". " + firstLetter.toUpperCase() +
                    groceryList.get(i).substring(1));
        }
    }

    public void addGroceryItem(String item) {
        groceryList.add(item.toLowerCase());
    }

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

    private void modifyItem(int position, String newName) {
        groceryList.set(position, newName);

    }

    private void removeItem(int position, String name) {
        groceryList.remove(position);
        String capitalizeItemName = name.substring(0, 1).toUpperCase() + name.substring(1);
        System.out.println(capitalizeItemName + " has been removed from your shopping list.");
    }

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