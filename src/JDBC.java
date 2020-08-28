//import java.sql.*;
//
//public class JDBC {
//
//    private String store;
//    private String item;
//
//    public JDBC(String store, String item) {
//        this.store = store;
//        this.item = item;
//    }
//
//    public String getStore() {
//        return store;
//    }
//
//    public void setStore(String store) {
//        this.store = store;
//    }
//
//    public String getItem() {
//        return item;
//    }
//
//    public void setItem(String item) {
//        this.item = item;
//    }
//
//    public void databaseConn() {
//        try {
//            Connection conn = DriverManager.getConnection
//                    ("jdbc:sqlite:C:\\Users\\Main\\Desktop\\databases\\grocerystoreiteminfo.db");
//            // conn.setAutoCommit(false);
//            Statement statement = conn.createStatement();
//           // statement.execute("CREATE TABLE IF NOT EXISTS contacts " +
//                //    "(name TEXT, phone INTEGER, email TEXT)");
//
////            statement.execute("INSERT INTO contacts (name, phone, email) " +
////                    "VALUES('Joe', 453243, 'joe@gmail.com')");
////
////            statement.execute("INSERT INTO contacts (name, phone, email) " +
////                    "VALUES('Jessica', 123123, 'jane@gmail.com')");
////
////            statement.execute("INSERT INTO contacts (name, phone, email) " +
////                    "VALUES('Hector', 9038, 'dog@gmail.com')");
//
////            statement.execute("UPDATE contacts SET phone=524234 WHERE name='Jessica'");
////            statement.execute("DELETE FROM contacts WHERE name='Joe'");
//
//            statement.execute("SELECT * FROM aisles");
//            ResultSet results = statement.getResultSet();
//            while (results.next()) {
//                System.out.println(results.getString("Store_ID") + " " +
//                        results.getInt("ItemName") + " " +
//                        results.getString("Aisle"));
//            }
//
//            results.close();
//
//            statement.close();
//            conn.close();
//
//
//            // Connection conn = DriverManager.getConnection
//            //         ("jdbc:sqlite:C:\\Users\\Main\\Desktop\\databases\\testjava.db");
//            // Statement statement = conn.createStatement();
//
//
//        } catch (SQLException e) {
//            System.out.println("Something went wrong: " + e.getMessage());
//        }
//    }
//
//
//
//}
