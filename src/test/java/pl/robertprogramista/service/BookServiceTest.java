package pl.robertprogramista.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.robertprogramista.model.author.dao.Author;
import pl.robertprogramista.model.author.dao.AuthorRepository;
import pl.robertprogramista.model.book.dao.Book;
import pl.robertprogramista.model.book.dao.BookRepository;
import pl.robertprogramista.model.book.dto.BookDto;
import pl.robertprogramista.model.category.dao.Category;
import pl.robertprogramista.model.category.dao.CategoryRepository;
import pl.robertprogramista.model.publishing.dao.Publishing;
import pl.robertprogramista.model.publishing.dao.PublishingRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookServiceTest {

    @Test
    @DisplayName("findAllBook result no books found")
    void findAllBook_no_books_found() {
        var bookRepository = mock(BookRepository.class);
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        var bookService = new BookService(bookRepository, null, null, null);

        ResponseEntity<List<BookDto>> result = bookService.findAllBook();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findAllBook result has books")
    void findAllBook_result_ok() {
        var bookRepository = mock(BookRepository.class);

        List<Book> books = getBooks();

        when(bookRepository.findAll()).thenReturn(books);

        var bookService = new BookService(bookRepository, null, null, null);

        ResponseEntity<List<BookDto>> result = bookService.findAllBook();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isEmpty()).isFalse();
        assertThat(result.getBody().size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("searchBooks result no books found")
    void findBooks_no_books_found() {

        var bookRepository = mock(BookRepository.class);
        when(bookRepository.searchBooks(anyString(), anyString(), anyString(), anyString())).thenReturn(new ArrayList<>());
        var bookService = new BookService(bookRepository, null, null, null);

        ResponseEntity<List<BookDto>> result = bookService.findBooks("","", "", "");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findBooks result has books")
    void findBooks_result_ok() {

        var bookRepository = mock(BookRepository.class);

        List<Book> books = getBooks();

        when(bookRepository.searchBooks(anyString(), anyString(), anyString(), anyString())).thenReturn(books);

        var bookService = new BookService(bookRepository, null, null, null);

        ResponseEntity<List<BookDto>> result = bookService.findBooks("","", "", "");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("findBookById result has book")
    void findBookById_result_ok() {

        var bookRepository = mock(BookRepository.class);

        List<Book> books = getBooks();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(books.get(0)));

        var bookService = new BookService(bookRepository, null, null, null);

        ResponseEntity<BookDto> result = bookService.findBookById(1);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findBookById result no book found")
    void findBookById_result_not_found() {

        var bookRepository = mock(BookRepository.class);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        var bookService = new BookService(bookRepository, null, null, null);

        ResponseEntity<BookDto> result = bookService.findBookById(1);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void saveBook() {
        var bookRepository = mock(BookRepository.class);
        var categoryRepository = mock(CategoryRepository.class);
        var publishingRepository = mock(PublishingRepository.class);
        var authorRepository = mock(AuthorRepository.class);

        when(categoryRepository.getCategoryIdByName(anyString())).thenReturn(1);

        when(publishingRepository.getPublishingIdByName(anyString())).thenReturn(1);

        when(authorRepository.getAuthorByFullName(anyString(), anyString())).thenReturn(1);

        when(bookRepository.save(any())).thenReturn(getBooks().get(0));

        var bookService = new BookService(
                bookRepository,
                categoryRepository,
                publishingRepository,
                authorRepository
        );

        ResponseEntity<Book> result = bookService.saveBook(getBooks().get(0));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("updateBook result not found")
    void updateBook_not_found() {
        var bookRepository = mock(BookRepository.class);
        when(bookRepository.existsById(anyInt())).thenReturn(false);

        var bookService = new BookService(bookRepository, null, null, null);

        var result = bookService.updateBook(1, getBooks().get(0));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("updateBook result no content")
    void updateBook_no_content() {

        var bookRepository = mock(BookRepository.class);
        var categoryRepository = mock(CategoryRepository.class);
        var publishingRepository = mock(PublishingRepository.class);
        var authorRepository = mock(AuthorRepository.class);

        when(categoryRepository.getCategoryIdByName(anyString())).thenReturn(1);

        when(publishingRepository.getPublishingIdByName(anyString())).thenReturn(1);

        when(authorRepository.getAuthorByFullName(anyString(), anyString())).thenReturn(1);

        when(bookRepository.existsById(anyInt())).thenReturn(true);
        when(bookRepository.save(any())).thenReturn(getBooks().get(0));

        var bookService = new BookService(
                bookRepository,
                categoryRepository,
                publishingRepository,
                authorRepository
        );

        var result = bookService.updateBook(1, getBooks().get(0));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteBook result not found")
    void deleteBook_not_found() {
        var bookRepository = mock(BookRepository.class);
        when(bookRepository.existsById(anyInt())).thenReturn(false);

        var bookService = new BookService(bookRepository, null, null, null);

        var result = bookService.deleteBook(1);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("deleteBook result no content")
    void deleteBook_no_content() {
        var bookRepository = mock(BookRepository.class);
        when(bookRepository.existsById(anyInt())).thenReturn(true);

        var bookService = new BookService(bookRepository, null, null, null);

        var result = bookService.deleteBook(1);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        Book testBook = new Book();

        Set<Author> authors = new HashSet<>();
        Author testAuthor = new Author();
        testAuthor.setName("Test");
        testAuthor.setSurname("Testowy");
        authors.add(testAuthor);
        testBook.setAuthors(authors);

        Category category = new Category();
        category.setName("testowa");
        testBook.setCategory(category);

        Publishing publishing = new Publishing();
        publishing.setName("Wydawnictwo testowe");
        testBook.setPublishing(publishing);

        books.add(testBook);
        return books;
    }
}