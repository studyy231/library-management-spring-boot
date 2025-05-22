package com.library.service;

import com.library.entity.Book;
import com.library.exception.BookNotFoundException;
import com.library.exception.DuplicateIsbnException;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        System.out.println("Retrieving all books");
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        System.out.println("Retrieving book with id: " + id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    public Book createBook(Book book) {
        System.out.println("Creating new book: " + book.getTitle());

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateIsbnException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        System.out.println("Updating book with id: " + id);

        Book existingBook = getBookById(id);

        if (!existingBook.getIsbn().equals(bookDetails.getIsbn()) &&
                bookRepository.existsByIsbn(bookDetails.getIsbn())) {
            throw new DuplicateIsbnException("Book with ISBN " + bookDetails.getIsbn() + " already exists");
        }

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setCategory(bookDetails.getCategory());
        existingBook.setAvailable(bookDetails.getAvailable());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        System.out.println("Deleting book with id: " + id);

        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooks(String searchTerm) {
        System.out.println("Searching books with term: " + searchTerm);
        return bookRepository.searchByTitleOrAuthor(searchTerm);
    }

    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks() {
        System.out.println("Retrieving available books");
        return bookRepository.findByAvailableTrue();
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByCategory(String category) {
        System.out.println("Retrieving books by category: " + category);
        return bookRepository.findByCategory(category);
    }
}