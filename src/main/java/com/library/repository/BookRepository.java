package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);

    // Find books by author
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Find books by title
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books by category
    List<Book> findByCategory(String category);

    // Find available books
    List<Book> findByAvailableTrue();

    // Custom query to search books by title or author
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("searchTerm") String searchTerm);

    // Check if ISBN already exists
    boolean existsByIsbn(String isbn);
}