package library;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;
import java.sql.*;
import javax.naming.NameNotFoundException;
import javax.naming.SizeLimitExceededException;

public class Main {
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/library?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_USER = "root";
    static final String DB_PASS = "password";
    static Library library;

    static void tableGetBooks() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (tableExists(conn, "Books")) {
                String query = "Select * from Books";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    library.addBook(new Book(rs.getString("title"), rs.getString("author"), rs.getString("genre"),
                            rs.getString("contents"), rs.getInt("id"), rs.getDouble("price"),
                            rs.getBoolean("isAvailable")),
                            false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SizeLimitExceededException e) {
            System.out.println(e.getMessage());
        } catch (NameNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    static void tableRemoveBook(int bookID, boolean isCheckOut) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (tableExists(conn, "Books") && isCheckOut == false) {
                String query = "DELETE TOP(1) From Books where id=?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, bookID);
                preparedStatement.executeUpdate();
            } else if (tableExists(conn, "Books") && isCheckOut == true) {
                String query = "UPDATE Books SET isAvailable=? where id=? and isAvailable=? limit 1";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setBoolean(1, !isCheckOut);
                preparedStatement.setInt(2, bookID);
                preparedStatement.setBoolean(3, isCheckOut);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void tableAddLibraryCard(LibraryCard libraryCard) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (!tableExists(conn, "LibraryCards")) {
                String query = "CREATE TABLE LibraryCards(userID INTEGER not NULL, libraryCardID INTEGER not NULL, issueDate VARCHAR(255) not NULL)";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
            String query = "INSERT INTO LibraryCards VALUES(?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, libraryCard.userID);
            preparedStatement.setInt(2, libraryCard.libraryCardID);
            preparedStatement.setString(3, libraryCard.issueDate.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void tableAddBorrower(Borrower borrower) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (!tableExists(conn, "Borrowers")) {
                String query = "CREATE TABLE Borrowers(name varchar(255) not NULL, address varchar(255) not NULL, userID INTEGER not NULL, libraryCardID INTEGER not NULL)";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }

            String query = "INSERT INTO Borrowers Values(?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, borrower.name);
            preparedStatement.setString(2, borrower.address);
            preparedStatement.setInt(3, borrower.userID);
            preparedStatement.setInt(4, borrower.libraryCard.libraryCardID);
            tableAddLibraryCard(borrower.libraryCard);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void tableAddBook(Book book, boolean returnBook) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            if (tableExists(conn, "Books") && returnBook == false) {
                String query = "INSERT INTO Books VALUES(?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, book.id);
                preparedStatement.setString(2, book.title);
                preparedStatement.setString(3, book.author);
                preparedStatement.setString(4, book.contents);
                preparedStatement.setString(5, book.genre);
                preparedStatement.setDouble(6, Double.parseDouble(String.format("%.2f", book.price)));
                preparedStatement.setBoolean(7, book.isAvailable);
                preparedStatement.executeUpdate();
            } else if (tableExists(conn, "Books") && returnBook == true) {
                String query = "UPDATE Books SET isAvailable=? where id=? and isAvailable=? limit 1";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setBoolean(1, returnBook);
                preparedStatement.setInt(2, book.id);
                preparedStatement.setBoolean(3, !returnBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static boolean tableExists(Connection connection, String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ?"
                + "LIMIT 1;");
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }

    public static void initialiseLibrary() throws SQLException, SizeLimitExceededException, NameNotFoundException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        if (tableExists(conn, "Library")) {
            Statement stmt = conn.createStatement();
            String sql = "Select * from Library limit 1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                initialiseLibrary(rs.getInt("numSections"), rs.getInt("shelfBookCapacity"), rs.getDouble("penalty"));
            }

            tableGetBooks();
        }
    }

    public static void initialiseLibrary(int numSections, int shelfBookCapacity, double penalty)
            throws SQLException, SizeLimitExceededException, NameNotFoundException {
        library = new Library(numSections, shelfBookCapacity, penalty);

        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        if (!tableExists(conn, "Library")) {
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE Library (numSections INTEGER not NULL, shelfBookCapacity INTEGER not NULL, penalty double(5,2) not NULL)";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Library Values(" + numSections + " , " + shelfBookCapacity + " , " + penalty + ")";
            stmt.executeUpdate(sql);
            sql = "Create Table Books(id INTEGER not NULL, title varchar(255) not NULL, author varchar(255) not NULL, contents varchar(255) not NULL, genre varchar(255) not NULL, price double(5,2) not NULL, isAvailable BIT not NULL )";
            stmt.executeUpdate(sql);
        } else {

        }
    }

    public static void displayCard(int userID) {
        library.users.get(userID - 1).displayCard();
    }

    public static void addBook(Book book) {
        try {
            library.addBook(book, false);
            tableAddBook(book, false);
        } catch (SizeLimitExceededException e) {
            System.out.println(e.getMessage());
        } catch (NameNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addBorrower(String name, String address) {
        try {
            library.addBorrower(name, address);
            tableAddBorrower(library.users.get(library.users.size() - 1));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void returnBook(Book book, int userID) {
        try {
            library.addBook(book, true);
            for (Borrower borrower : library.users) {
                if (borrower.userID == userID) {
                    borrower.returnBook(book.id, book.title,
                            Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
            }
            tableAddBook(book, true);
        } catch (NameNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SizeLimitExceededException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeBook(Book book) {
        try {
            library.removeBook(-1, book.id,
                    book.title, false, null);

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void checkOut(int userID, int bookID, String bookTitle, Date dateTo) {
        try {
            library.removeBook(userID, bookID,
                    bookTitle, true, dateTo);
            tableRemoveBook(bookID, true);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException, SizeLimitExceededException, NameNotFoundException {
        initialiseLibrary();
        // initialiseLibrary(2, 4, 0.0);
        // addBook(new Book(
        // "Harry Potter And The Half-Blood Prince", "J.K. Rowling", "Fiction", "Chapter
        // 1", 1001, 100));
        addBorrower("Anand Verma", "4 Privet Drive");

        // checkOut(1, 1001, "Harry Potter And The Half-Blood Prince",
        // Date.from(LocalDate.of(2023, 10,
        // 01).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        // returnBook(new Book(
        // "Harry Potter And The Half-Blood Prince", "J.K. Rowling", "Fiction", "Chapter
        // 1", 1001, 100), 1);
        displayCard(1);
        // returnBook(new Book(
        // "Harry Potter And The Half-Blood Prince", "J.K. Rowling", "Fiction", "Chapter
        // 1", 1001, 100.00), 1);

        // displayCard(1);
        library.getDetails();
    }

}
