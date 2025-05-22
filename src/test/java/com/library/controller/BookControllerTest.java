package com.library.controller;

import com.library.entity.Book;
import com.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
@DisplayName("BookController Tests")
class BookControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public BookService bookService() {
            return mock(BookService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("GET /api/books should return all books")
    void getAllBooks_ShouldReturnAllBooks() throws Exception {
        // Given
        List<Book> books = Arrays.asList(testBook);
        when(bookService.getAllBooks()).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Book"))
                .andExpect(jsonPath("$[0].author").value("Test Author"));
    }

    @Test
    @DisplayName("GET /api/books/{id} should return book by id")
    void getBookById_ShouldReturnBook() throws Exception {
        // Given
        when(bookService.getBookById(1L)).thenReturn(testBook);

        // When & Then
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.isbn").value("978-0123456789"));
    }

    @Test
    @DisplayName("POST /api/books should create new book")
    void createBook_ShouldCreateBook() throws Exception {
        // Given
        when(bookService.createBook(any(Book.class))).thenReturn(testBook);

        // When & Then
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"));
    }

    @Test
    @DisplayName("PUT /api/books/{id} should update book")
    void updateBook_ShouldUpdateBook() throws Exception {
        // Given
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("978-0987654321");
        updatedBook.setCategory("Non-Fiction");
        updatedBook.setAvailable(false);

        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(updatedBook);

        // When & Then
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Updated Author"));
    }

    @Test
    @DisplayName("DELETE /api/books/{id} should delete book")
    void deleteBook_ShouldDeleteBook() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}