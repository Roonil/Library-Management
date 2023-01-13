package library;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;

public class Borrower {
    final String name, address;
    final int userID;
    final LibraryCard libraryCard;

    Borrower(String name, String address, int userID) {
        this.name = name;
        this.address = address;
        this.userID = userID;
        this.libraryCard = new LibraryCard(userID,
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    void returnBook(int bookId, String bookTitle, Date returnDate) throws NoSuchElementException {
        libraryCard.returnBook(bookId, bookTitle, returnDate);
    }

    void rentBook(int bookId, String bookTitle, Date dateFrom, Date dateTo) {
        libraryCard.rentBook(bookId, bookTitle, dateFrom, dateTo);
    }

    void displayCard() {
        System.out.println("Card ID: " + libraryCard.libraryCardID + " User ID: " + libraryCard.userID + " Issue Date: "
                + libraryCard.issueDate);

        for (LibraryCardRecord record : libraryCard.records) {
            System.out.println(record.bookId + " " + record.bookTitle + " " + record.dateFrom + " " + record.dateTo
                    + " " + record.hasReturned + " " + record.returnDate);
        }
    }
}
