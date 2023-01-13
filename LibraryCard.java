package library;

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

public class LibraryCard {
    final int userID, libraryCardID;
    final Date issueDate;
    final ArrayList<LibraryCardRecord> records = new ArrayList<LibraryCardRecord>();

    LibraryCard(int userID, Date issueDate) {
        this.userID = userID;
        this.libraryCardID = userID + 1000;
        this.issueDate = issueDate;
    }

    void rentBook(int bookId, String bookTitle, Date dateFrom, Date dateTo) {
        // records.add(new LibraryCardRecord);
        records.add(new LibraryCardRecord(dateFrom, dateTo, bookId, bookTitle));

    }

    void returnBook(int bookId, String bookTitle, Date returnDate) throws NoSuchElementException {
        boolean returned = false;
        for (LibraryCardRecord record : records) {
            if (record.bookId == bookId && record.hasReturned == false)
                record.returnBook(bookId, bookTitle, returnDate);
            returned = true;
        }
        if (returned)
            System.out.println("Book returned!");
        else
            throw new NoSuchElementException("The specified Book does not exist in these records");
    }
}
