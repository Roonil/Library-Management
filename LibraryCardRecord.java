package library;

import java.util.Date;
import java.util.NoSuchElementException;

class LibraryCardRecord {
    final Date dateFrom, dateTo;
    final int bookId;
    final String bookTitle;
    Date returnDate;
    boolean hasReturned = false;

    LibraryCardRecord(Date dateFrom, Date dateTo, int bookId, String bookTitle) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
    }

    void returnBook(int bookId, String bookTitle, Date returnDate) throws NoSuchElementException {
        if (this.bookId == bookId && this.bookTitle == bookTitle) {
            hasReturned = true;
            this.returnDate = returnDate;
        } else
            throw new NoSuchElementException("The specified book does not exist in these Records!");
    }
}