import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    public static final String DB_NAME = "grocerystoreinfo.db";
    public static final String CONNECTION_STRING =
            "jdbc:sqlite:" + DB_NAME;
    private static boolean acceptPW;

    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    public UserProfile() {

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void generateLogin(String username, String password, String email) {
        String hashedPass = hashPassword(password);
        try {
            Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = conn.createStatement();
            String query = "INSERT INTO users (username, pass, email) " +
                    "VALUES('" + username + "', '" + hashedPass + "', '" + email + "')";

            statement.execute(query);

            statement.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }

    public static void validateLogin(String username, String password) {
        //check user login info against database
        try {
            Connection conn = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = conn.createStatement();

            String passQuery = "SELECT pass FROM users WHERE username='" + username + "'";
            //get the password for the provided username

            ResultSet results = statement.executeQuery(passQuery);
            //pull the hashed password from that row and send to checkPass
            String hashedpass = results.getString("pass");
            boolean passwordConfirm = checkPass(password, hashedpass);
            if (passwordConfirm) {
                System.out.println("Login success!");
                acceptPW = true;
            } else {
                acceptPW = false;
            }
            results.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }

    public static boolean acceptPW() {
        return acceptPW;
    }

    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private static boolean checkPass(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", address='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }


    public List<String> write() {
        List<String> values = new ArrayList<>();
        values.add(0, this.name);
        values.add(1, "" + this.email);
        values.add(2, "" + this.phoneNumber);

        return null;
    }

    public void read(List<String> savedValues) {
        if (savedValues != null && savedValues.size() > 0) {
            this.name = savedValues.get(0);
            this.email = savedValues.get(1);
            this.phoneNumber = savedValues.get(2);

        }
    }
}
