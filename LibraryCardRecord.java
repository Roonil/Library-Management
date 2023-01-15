package library;

import java.util.NoSuchElementException;

class LibraryCardRecord {
    final String dateFrom, dateTo;
    final int bookID, libraryCardID;
    final String bookTitle;
    String returnDate;
    boolean hasReturned = false;

    LibraryCardRecord(String dateFrom, String dateTo, int bookID, int libraryCardID, String bookTitle) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.bookID = bookID;
        this.libraryCardID = libraryCardID;
        this.bookTitle = bookTitle;

    }

    LibraryCardRecord(String dateFrom, String dateTo, int bookID, int libraryCardID, String bookTitle,
            boolean hasReturned, String returnDate) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.bookID = bookID;
        this.libraryCardID = libraryCardID;
        this.bookTitle = bookTitle;
        this.hasReturned = hasReturned;
        this.returnDate = returnDate;
    }

    LibraryCardRecord returnBook(int bookID, String bookTitle, String returnDate) throws NoSuchElementException {
        if (this.bookID == bookID && this.bookTitle.equals(bookTitle)) {
            hasReturned = true;
            this.returnDate = returnDate;
            return this;
        } else
            throw new NoSuchElementException("The specified book does not exist in these Records!");
    }
}