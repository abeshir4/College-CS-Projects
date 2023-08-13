import java.io.*;
import java.sql.*;
import java.util.*;
import oracle.jdbc.driver.*;
import org.apache.ibatis.jdbc.ScriptRunner;

public class Student{
    static Connection con;
    static Statement stmt;

    public static void main(String argv[])
    {
        try {
            connectToDatabase();
            displayMenu();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void connectToDatabase()
    {
        String driverPrefixURL="jdbc:oracle:thin:@";
        String jdbc_url="artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";

        // IMPORTANT: DO NOT PUT YOUR LOGIN INFORMATION HERE. INSTEAD, PROMPT USER FOR HIS/HER LOGIN/PASSWD
        Scanner sc= new Scanner(System.in);
        System.out.print("Enter user-name: ");
        String username=sc.nextLine();
        System.out.print("Enter password: ");
        String password=sc.nextLine();

        try{
            //Register Oracle driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (Exception e) {
            System.out.println("Failed to load JDBC/ODBC driver.");
            return;
        }

        try{
            System.out.println(driverPrefixURL+jdbc_url);
            con=DriverManager.getConnection(driverPrefixURL+jdbc_url, username, password);
            DatabaseMetaData dbmd=con.getMetaData();
            stmt=con.createStatement();

            System.out.println("Connected.");

            if(dbmd==null){
                System.out.println("No database meta data");
            }
            else {
                System.out.println("Database Product Name: "+dbmd.getDatabaseProductName());
                System.out.println("Database Product Version: "+dbmd.getDatabaseProductVersion());
                System.out.println("Database Driver Name: "+dbmd.getDriverName());
                System.out.println("Database Driver Version: "+dbmd.getDriverVersion());
            }
            //System.out.print("Enter the location of the 'CustomerTransactions.sql' script file: ");
            //String scriptFilePath = sc.nextLine();
            // Initialize the script runner
            ScriptRunner sr = new ScriptRunner(con);
            // Creating a reader object
            try {                                         //stringfilepath
                Reader reader = new BufferedReader(new FileReader("CustomerTransactions.sql"));
                // Running the script
                sr.runScript(reader);
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// End of connectToDatabase()

    public static void displayMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. View table contents");
            System.out.println("2. Search by customer_ID");
            System.out.println("3. Search by one or more attributes");
            System.out.println("4. Exit");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewTableContents();
                    break;
                case 2:
                    searchByCustomerID();
                    break;
                case 3:
                    searchByAttributes();
                    break;
                case 4:
                    System.out.println("Exiting the program.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    public static void viewTableContents() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Product (Yes/No): ");
        String choice1 = sc.nextLine();
        System.out.print("Customer (Yes/No): ");
        String choice2 = sc.nextLine();
        System.out.print("Transactions (Yes/No): ");
        String choice3 = sc.nextLine();
        System.out.print("Transaction_Contains (Yes/No): ");
        String choice4 = sc.nextLine();
        if(choice1.equals("Yes")){
            String sqlQuery = "SELECT * FROM Product";
            PreparedStatement pstmt2 = con.prepareStatement(sqlQuery);
            ResultSet rset = pstmt2.executeQuery();
            System.out.println("\nPRODUCT TABLE:" );
            System.out.println("UPC, brand, product_name, product_description, category, marked_price, quantity_instock");
            while (rset.next ()){
                System.out.print(rset.getString(1)+", ");
                System.out.print(rset.getString(2)+", ");
                System.out.print(rset.getString(3)+", ");
                System.out.print(rset.getString(4)+", ");
                System.out.print(rset.getString(5)+", ");
                System.out.print(rset.getFloat(6)+", ");
                System.out.println(rset.getInt(7)+" ");
            }
        }
        else if(choice1.equals("No")){
            System.out.println("Product table display declined");
        }
        else{
            System.out.println("Invalid input");
        }
        System.out.print("\n");
        if(choice2.equals("Yes")){
            String sqlQuery = "SELECT * FROM Customer";
            PreparedStatement pstmt2 = con.prepareStatement(sqlQuery);
            ResultSet rset = pstmt2.executeQuery();
            System.out.println("\nCUSTOMER TABLE:" );
            System.out.println("customer_ID, first_name, last_name, age, gender, zip code");
            while (rset.next ()){
                System.out.print(rset.getInt(1)+", ");
                System.out.print(rset.getString(2)+", ");
                System.out.print(rset.getString(3)+", ");
                System.out.print(rset.getInt(4)+", ");
                System.out.print(rset.getString(5)+", ");
                System.out.println(rset.getInt(6)+" ");
            }
        }
        else if(choice2.equals("No")){
            System.out.println("Customer table display declined");
        }
        else{
            System.out.println("Invalid input");
        }
        System.out.print("\n");
        if(choice3.equals("Yes")){
            String sqlQuery = "SELECT * FROM Transactions";
            PreparedStatement pstmt2 = con.prepareStatement(sqlQuery);
            ResultSet rset = pstmt2.executeQuery();
            System.out.println("\nTRANSACTIONS TABLE:" );
            System.out.println("transaction_ID, customer_ID, transaction_date, payment_method, total");
            while (rset.next ()){
                System.out.print(rset.getInt(1)+", ");
                System.out.print(rset.getInt(2)+", ");
                System.out.print(rset.getDate(3)+", ");
                System.out.print(rset.getString(4)+", ");
                System.out.println(rset.getInt(5)+" ");
            }
        }
        else if(choice3.equals("No")){
            System.out.println("Transactions table display declined");
        }
        else{
            System.out.println("Invalid input");
        }
        System.out.print("\n");
        if(choice4.equals("Yes")){
            String sqlQuery = "SELECT * FROM Transaction_Contains";
            PreparedStatement pstmt2 = con.prepareStatement(sqlQuery);
            ResultSet rset = pstmt2.executeQuery();
            System.out.println("\nTRANSACTION_CONTAINS TABLE:" );
            System.out.println("transaction_ID, UPC, quantity");
            while (rset.next ()){
                System.out.print(rset.getInt(1)+", ");
                System.out.print(rset.getString(2)+", ");
                System.out.println(rset.getInt(3)+" ");
            }
        }
        else if(choice4.equals("No")){
            System.out.println("Transactions_contains table display declined");
        }
        else{
            System.out.println("Invalid input");
        }
    }

    public static void searchByCustomerID() throws SQLException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer id: ");
        int custid = sc.nextInt();
        System.out.print("\n");
        String sqlQuery = "SELECT * FROM Customer WHERE customer_ID=" + custid;
        PreparedStatement pstmt2 = con.prepareStatement (sqlQuery);
        System.out.println("ATTRIBUTES OF CUSTOMER WITH CUSTOMER ID " + custid + ":");
        System.out.println("customer_ID, first_name, last_name, age, gender, zip code");
        ResultSet rset = pstmt2.executeQuery();
        while (rset.next ()){
            System.out.print(rset.getInt(1)+", ");
            System.out.print(rset.getString(2)+", ");
            System.out.print(rset.getString(3)+", ");
            System.out.print(rset.getInt(4)+", ");
            System.out.print(rset.getString(5)+", ");
            System.out.println(rset.getInt(6)+" ");
        }
        String sqlQuery2 = "SELECT COUNT(transaction_ID) FROM Transactions WHERE customer_ID=" + custid;
        PreparedStatement pstmt3 = con.prepareStatement (sqlQuery2);
        ResultSet rset2 = pstmt3.executeQuery();
        int numtrans = 0;
        if (rset2.next()) {
            numtrans = rset2.getInt(1);
            System.out.println("Total number of transactions: " + numtrans);
        }
    }

    public static void searchByAttributes() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input fields:");
        System.out.print("UPC: ");
        String inputUPC = sc.nextLine();
        System.out.print("product_name: ");
        String inputProductName = sc.nextLine();
        System.out.print("customer_ID: ");
        String inputCustomerID = sc.nextLine();
        System.out.print("transaction_ID: ");
        String inputTransactionID = sc.nextLine();

        System.out.println("\nOutput fields:");
        System.out.print("UPC (Yes/No): ");
        boolean upc = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("brand (Yes/No): ");
        boolean brand = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("product_name (Yes/No): ");
        boolean productName = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("product_description (Yes/No): ");
        boolean productDescription = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("category (Yes/No): ");
        boolean category = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("marked_price (Yes/No): ");
        boolean markedPrice = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("quantity_instock (Yes/No): ");
        boolean quantityInStock = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("transaction_ID (Yes/No): ");
        boolean transactionID = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("customer_ID (Yes/No): ");
        boolean customerID = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("transaction_date (Yes/No): ");
        boolean transactionDate = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("payment_method (Yes/No): ");
        boolean paymentMethod = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("total (Yes/No): ");
        boolean total = sc.nextLine().equalsIgnoreCase("Yes");
        System.out.print("quantity (Yes/No): ");
        boolean quantity = sc.nextLine().equalsIgnoreCase("Yes");

        System.out.print("\nDistinct (Yes/No): ");
        boolean distinct = sc.nextLine().equalsIgnoreCase("Yes");

        String selectClause = "SELECT ";
        if (distinct) {
            selectClause += "DISTINCT ";
        }
        if (upc) selectClause += "P.UPC, ";
        if (brand) selectClause += "P.brand, ";
        if (productName) selectClause += "P.product_name, ";
        if (productDescription) selectClause += "P.product_description, ";
        if (category) selectClause += "P.category, ";
        if (markedPrice) selectClause += "P.marked_price, ";
        if (quantityInStock) selectClause += "P.quantity_instock, ";
        if (transactionID) selectClause += "T.transaction_ID, ";
        if (customerID) selectClause += "T.customer_ID, ";
        if (transactionDate) selectClause += "T.transaction_date, ";
        if (paymentMethod) selectClause += "T.payment_method, ";
        if (total) selectClause += "T.total, ";
        if (quantity) selectClause += "TC.quantity, ";

        selectClause = selectClause.substring(0, selectClause.length() - 2);
        String fromClause = " FROM Product P, Transactions T, Transaction_Contains TC";
        String whereClause = " WHERE P.UPC = TC.UPC AND T.transaction_ID = TC.transaction_ID";

        if (!inputUPC.isEmpty()) {
            whereClause += " AND P.UPC = '" + inputUPC + "'";
        }
        if (!inputProductName.isEmpty()) {
            whereClause += " AND P.product_name LIKE '%" + inputProductName + "%'";
        }
        if (!inputCustomerID.isEmpty()) {
            whereClause += " AND T.customer_ID = " + inputCustomerID;
        }
        if (!inputTransactionID.isEmpty()) {
            whereClause += " AND T.transaction_ID = " + inputTransactionID;
        }

        String sqlQuery = selectClause + fromClause + whereClause;
        PreparedStatement pstmt = con.prepareStatement(sqlQuery);
        if (upc) {
            System.out.print("UPC, ");
        }
        if (brand) {
            System.out.print("brand, ");
        }
        if (productName) {
            System.out.print("product_name, ");
        }
        if (productDescription) {
            System.out.print("product_description, ");
        }
        if (category) {
            System.out.print("category, ");
        }
        if (markedPrice) {
            System.out.print("marked_price, ");
        }
        if (quantityInStock) {
            System.out.print("quantity_in_stock, ");
        }
        if (transactionID) {
            System.out.print("transaction_ID, ");
        }
        if (customerID) {
            System.out.print("customer_ID, ");
        }
        if (transactionDate) {
            System.out.print("transaction_date, ");
        }
        if (paymentMethod) {
            System.out.print("payment_method, ");
        }
        if (total) {
            System.out.print("total, ");
        }
        if (quantity) {
            System.out.print("quantity, ");
        }
        System.out.println();
        ResultSet rset = pstmt.executeQuery();

        while (rset.next()) {
            int columnIndex = 1;
            if (upc) {
                System.out.print(rset.getString(columnIndex++) + ", ");
            }
            if (brand) {
                System.out.print(rset.getString(columnIndex++) + ", ");
            }
            if (productName) {
                System.out.print(rset.getString(columnIndex++) + ", ");
            }
            if (productDescription) {
                System.out.print(rset.getString(columnIndex++) + ", ");
            }
            if (category) {
                System.out.print(rset.getString(columnIndex++) + ", ");
            }
            if (markedPrice) {
                System.out.print(rset.getDouble(columnIndex++) + ", ");
            }
            if (quantityInStock) {
                System.out.print(rset.getInt(columnIndex++) + ", ");
            }
            if (transactionID) {
                System.out.print(rset.getInt(columnIndex++) + ", ");
            }
            if (customerID) {
                System.out.print(rset.getInt(columnIndex++) + ", ");
            }
            if (transactionDate) {
                System.out.print(rset.getDate(columnIndex++) + ", ");
            }
            if (paymentMethod) {
                System.out.print(rset.getString(columnIndex++) + ", ");
            }
            if (total) {
                System.out.print(rset.getDouble(columnIndex++) + ", ");
            }
            if (quantity) {
                System.out.print(rset.getInt(columnIndex++) + ", ");
            }
            System.out.println();
        }
    }
}// End of class
