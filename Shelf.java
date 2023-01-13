package library;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import javax.naming.SizeLimitExceededException;

public class Shelf {
    final ArrayList<Book> books = new ArrayList<Book>();
    final int numBooks;
    final char alphabet;

    Shelf(int numBooks, char alphabet) {
        this.numBooks = numBooks;
        this.alphabet = alphabet;
    }

    void placeBook(Book book) throws SizeLimitExceededException {
        if (Character.toLowerCase(book.title.charAt(0)) == Character.toLowerCase(alphabet)
                && books.size() - 1 < numBooks - 1) {
            books.add(book);
        } else {
            throw new SizeLimitExceededException("Cannot add book in this shelf!");
        }
    }

    void removeBook(int bookID, boolean checkOut) throws NoSuchElementException {
        boolean found = false;
        for (Book book : books) {
            if (book.id == bookID) {
                if (checkOut && book.isAvailable) {
                    book.isAvailable = false;
                    found = true;
                } else if (checkOut == false && book.isAvailable) {
                    books.remove(book);
                    found = true;
                }
                if (found)
                    break;
            }
        }
        if (found) {
            if (checkOut)
                System.out.println("Book Checked Out!");
            else
                System.out.println("Book removed from Library!");
            return;
        } else {
            throw new NoSuchElementException("The specified Book doesn't exist!");
        }
    }

}
