package library;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.naming.NameNotFoundException;
import javax.naming.SizeLimitExceededException;

public class Library {
    final ArrayList<Section> sections;
    // TODO: Implement penalty
    final double penalty;
    final int numSections, shelfBookCapacity;
    final ArrayList<Borrower> users = new ArrayList<Borrower>();

    Library(int numSections, int shelfBookCapacity, double penalty) {
        this.penalty = penalty;
        sections = new ArrayList<Section>(numSections);
        char alphabet = 97;
        this.numSections = numSections;
        this.shelfBookCapacity = shelfBookCapacity;
        for (int i = 0; i < numSections; i++) {
            sections.add(new Section(shelfBookCapacity, alphabet, (char) (alphabet - 1 + (26 / numSections))));
            alphabet = (char) (alphabet + 26 / numSections);
        }
    }

    void displayCard(int userID) {
        users.get(userID - 1).displayCard();
    }

    void addBook(Book book, boolean returnBook) throws NameNotFoundException, SizeLimitExceededException {
        char startAlphabet = Character.toLowerCase(book.title.charAt(0));
        int idx = (startAlphabet - 97 + 1) * numSections / 26;
        sections.get(idx).addBook(book, returnBook);
    }

    void removeBook(int userID, int bookID, String bookTitle, boolean checkOut, String dateTo)
            throws NoSuchElementException {
        char startAlphabet = Character.toLowerCase(bookTitle.charAt(0));
        int idx = (startAlphabet - 97 + 1) * numSections / 26;
        try {
            sections.get(idx).removeBook(bookID, bookTitle, checkOut);
            if (checkOut)
                users.get(userID - 1).libraryCard.rentBook(bookID, bookTitle,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()), dateTo);

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }

    }

    void addBorrower(String name, String address) {
        users.add(new Borrower(name, address, users.size() + 1));
    }

    void addBorrower(String name, String address, int userID, LibraryCard libraryCard) {
        users.add(new Borrower(name, address, userID, libraryCard));
    }

    void removeBorrower(String name, int userID) throws NoSuchElementException {
        for (Borrower user : users) {
            if (user.userID == userID && user.name == name) {
                users.remove(user);
                return;
            }
        }

        throw new NoSuchElementException("The specified user does not exist!");
    }

    void getDetails() {
        int sectionNum = 1;
        for (Section section : this.sections) {
            System.out.println("Section: " + sectionNum);
            int shelfNum = 1;
            for (Shelf shelf : section.shelves) {
                System.out.println("Shelf # " + shelfNum);
                System.out.println(shelf.alphabet);
                int bookNum = 0;

                for (Book book : shelf.books) {
                    bookNum++;
                    System.out.println("Book #" + bookNum);
                    System.out.println(book.title);
                }
                shelfNum++;
            }
            sectionNum++;
        }

        for (Borrower borrower : this.users) {
            System.out.print(borrower.userID + " ");
            System.out.println(borrower.name);
        }
    }
}
