package library;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

public class Borrower {
    final String name, address;
    final int userID;
    final LibraryCard libraryCard;

    Borrower(String name, String address, int userID) {
        this.name = name;
        this.address = address;
        this.userID = userID;
        this.libraryCard = new LibraryCard(userID, DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
    }

    Borrower(String name, String address, int userID, LibraryCard libraryCard) {
        this.name = name;
        this.address = address;
        this.userID = userID;
        this.libraryCard = libraryCard;
    }

    LibraryCardRecord returnBook(int bookId, String bookTitle, String returnDate) throws NoSuchElementException {
        return libraryCard.returnBook(bookId, bookTitle, returnDate);
    }

    void rentBook(int bookId, String bookTitle, String dateFrom, String dateTo) {
        libraryCard.rentBook(bookId, bookTitle, dateFrom, dateTo);
    }

    void displayCard() {
        System.out.println("Card ID: " + libraryCard.libraryCardID + " User ID: " + libraryCard.userID + " Issue Date: "
                + libraryCard.issueDate);

        for (LibraryCardRecord record : libraryCard.records) {
            System.out.println(record.bookID + " " + record.bookTitle + " " + record.dateFrom + " " + record.dateTo
                    + " " + record.hasReturned + " " + record.returnDate);
        }
    }
}
