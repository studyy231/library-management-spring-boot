package com.library.service;

import com.library.entity.Book;
import com.library.exception.BookNotFoundException;
import com.library.exception.DuplicateIsbnException;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("978-0123456789");
        testBook.setCategory("Fiction");
        testBook.setAvailable(true);
    }

    @Test
    @DisplayName("Should return all books when getAllBooks is called")
    void getAllBooks_ShouldReturnAllBooks() {
        // Given
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // When
        List<Book> actualBooks = bookService.getAllBooks();

        // Then
        assertEquals(expectedBooks.size(), actualBooks.size());
        assertEquals(expectedBooks.get(0).getTitle(), actualBooks.get(0).getTitle());
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("Should return book when valid ID is provided")
    void getBookById_WithValidId_ShouldReturnBook() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // When
        Book actualBook = bookService.getBookById(1L);

        // Then
        assertEquals(testBook.getTitle(), actualBook.getTitle());
        assertEquals(testBook.getAuthor(), actualBook.getAuthor());
        assertEquals(testBook.getIsbn(), actualBook.getIsbn());
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when invalid ID is provided")
    void getBookById_WithInvalidId_ShouldThrowException() {
        // Given
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookService.getBookById(999L)
        );
        assertEquals("Book not found with id: 999", exception.getMessage());
        verify(bookRepository).findById(999L);
    }

    @Test
    @DisplayName("Should create book when ISBN doesn't exist")
    void createBook_WithUniqueIsbn_ShouldCreateBook() {
        // Given
        when(bookRepository.existsByIsbn(testBook.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        Book createdBook = bookService.createBook(testBook);

        // Then
        assertEquals(testBook.getTitle(), createdBook.getTitle());
        assertEquals(testBook.getAuthor(), createdBook.getAuthor());
        verify(bookRepository).existsByIsbn(testBook.getIsbn());
        verify(bookRepository).save(testBook);
    }

    @Test
    @DisplayName("Should throw DuplicateIsbnException when ISBN already exists")
    void createBook_WithDuplicateIsbn_ShouldThrowException() {
        // Given
        when(bookRepository.existsByIsbn(testBook.getIsbn())).thenReturn(true);

        // When & Then
        DuplicateIsbnException exception = assertThrows(
                DuplicateIsbnException.class,
                () -> bookService.createBook(testBook)
        );
        assertEquals("Book with ISBN " + testBook.getIsbn() + " already exists", exception.getMessage());
        verify(bookRepository).existsByIsbn(testBook.getIsbn());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should delete book when valid ID is provided")
    void deleteBook_WithValidId_ShouldDeleteBook() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(true);

        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when deleting non-existent book")
    void deleteBook_WithInvalidId_ShouldThrowException() {
        // Given
        when(bookRepository.existsById(999L)).thenReturn(false);

        // When & Then
        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(999L)
        );
        assertEquals("Book not found with id: 999", exception.getMessage());
        verify(bookRepository).existsById(999L);
        verify(bookRepository, never()).deleteById(anyLong());
    }
}