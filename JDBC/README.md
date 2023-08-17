Project Description:

The goal of this assignment is to familiarize you with JDBC (Java Database Connectivity). We
would like to create a command-line interface to access customers’ transaction information.
Write a Java program using JDBC to connect to your Oracle account, execute
CustomerTransactions.sql script file provided with this project to create and insert data into your
database, and implement a command-line interface to search the database.

Your database consists of the following tables:

Product (UPC, brand, product_name, product_description, category, marked_price,
quantity_instock)

Customer (customer_ID, first_name, last_name, age, gender, zip_code)

Transactions (transaction_ID, customer_ID, transaction_date, payment_method, total)

Transaction_Contains (transaction_ID, UPC, quantity)

Foreign keys:

 Transactions (customer_ID) references Customer (customer_ID)

 Transaction_Contains (transaction_ID) references Transactions (transaction_ID)

 Transaction_Contains (UPC) references Product (UPC)

Your command-line interface should include the following functionalities.

1. The user can view the contents of each table. The user can select more than one table to view.

2. The user can search by customer_ID and return all attributes from Customer table and a total
number of transactions field for each customer.

3. The user can search Product, Transactions, and Transaction_Contains tables by specifying one
or more input attributes from {UPC, product_name, customer_ID, transaction_ID} and specify
one or more output attributes from Product, Transactions, and Transaction_Contains tables. The
user can add DISTINCT keyword to return distinct tuples in the result. Your search should
consider pattern matching for product_name.

4. Your program should exit only when the user chooses to.
