package library;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class LibraryCard {
    final int userID, libraryCardID;
    final String issueDate;
    final ArrayList<LibraryCardRecord> records = new ArrayList<LibraryCardRecord>();

    LibraryCard(int userID, String issueDate) {
        this.userID = userID;
        this.libraryCardID = userID + 1000;
        this.issueDate = issueDate;
    }

    LibraryCard(int userID, int libraryCardID, String issueDate) {
        this.userID = userID;
        this.libraryCardID = libraryCardID;
        this.issueDate = issueDate;

    }

    void rentBook(int bookID, String bookTitle, String dateFrom, String dateTo) {
        // records.add(new LibraryCardRecord);
        records.add(new LibraryCardRecord(dateFrom, dateTo, bookID, libraryCardID, bookTitle));

    }

    LibraryCardRecord returnBook(int bookId, String bookTitle, String returnDate) throws NoSuchElementException {
        boolean returned = false;
        LibraryCardRecord rec = null;
        for (LibraryCardRecord record : records) {
            if (record.bookID == bookId && record.hasReturned == false) {
                rec = record.returnBook(bookId, bookTitle, returnDate);
                returned = true;
            }
        }
        if (returned) {
            System.out.println("Book returned!");
            return rec;
        } else {
            throw new NoSuchElementException("The specified Book does not exist in these records");
        }
    }
}
