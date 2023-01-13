package library;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.naming.NameNotFoundException;
import javax.naming.SizeLimitExceededException;

public class Section {
    final ArrayList<Shelf> shelves;
    final int numShelves;
    final char startAlphabet, endAlphabet;

    Section(int numBooks, char startAlphabet, char endAlphabet) {
        int numShelves = endAlphabet - startAlphabet + 1;
        shelves = new ArrayList<Shelf>(numShelves);
        this.numShelves = numShelves;
        this.endAlphabet = endAlphabet;
        this.startAlphabet = startAlphabet;
        char alphabet = startAlphabet;
        for (int i = 0; i < numShelves; i++) {
            shelves.add(new Shelf(numBooks, alphabet));
            alphabet++;
        }
    }

    void addBook(Book book, boolean returnBook) throws NameNotFoundException, SizeLimitExceededException {
        boolean inserted = false;
        if (Character.toLowerCase(book.title.charAt(0)) >= Character.toLowerCase(startAlphabet)
                && Character.toLowerCase(book.title.charAt(0)) <= Character.toLowerCase(endAlphabet)) {
            for (Shelf shelf : shelves) {
                if (shelf.alphabet == Character.toLowerCase(book.title.charAt(0))) {
                    if (returnBook == false) {
                        shelf.placeBook(book);
                        inserted = true;
                        break;
                    } else {
                        for (Book shelfBook : shelf.books) {
                            if (shelfBook.id == book.id && shelfBook.isAvailable == false) {
                                shelfBook.isAvailable = true;
                                inserted = true;
                            }
                        }
                    }
                }
            }
        }
        if (inserted) {
            return;
        }
        throw new NameNotFoundException("No such Book exists!");

    }

    void removeBook(int bookId, String bookTitle, boolean checkOut) throws NoSuchElementException {
        char startAlphabet = Character.toLowerCase(bookTitle.charAt(0));
        if (startAlphabet < this.startAlphabet || startAlphabet > this.endAlphabet) {
            throw new NoSuchElementException("Cannot remove book from this Section!");
        }
        int idx = startAlphabet - this.startAlphabet;
        shelves.get(idx).removeBook(bookId, checkOut);
    }
}
