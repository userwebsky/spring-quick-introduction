package pl.robertprogramista.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.robertprogramista.model.author.dao.Author;
import pl.robertprogramista.model.book.dao.Book;
import pl.robertprogramista.model.book.dao.BookRepository;
import pl.robertprogramista.model.book.dto.BookDto;
import pl.robertprogramista.model.category.dao.Category;
import pl.robertprogramista.model.publishing.dao.Publishing;
import pl.robertprogramista.service.BookService;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BookRepository repository;

    @Autowired
    private BookService bookService;

    @Test
    void findAllBook() {
        int initial = repository.findAll().size();
        bookService.saveBook(getBook());

        ResponseEntity<BookDto[]> response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books",
                BookDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(Objects.requireNonNull(response.getBody())).count()).isEqualTo(initial + 1);
    }

    @Test
    void findBooks_empty_request() {
        var response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/search",
                BookDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(Objects.requireNonNull(response.getBody())).count()).isEqualTo(0);
    }

    @Test
    void findBooks_byName() {
        Book book = getBook();
        book.setTitle("ABC");
        bookService.saveBook(book);

        var response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/search?name=ABC",
                BookDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(Objects.requireNonNull(response.getBody())).count()).isGreaterThan(0);
    }

    @Test
    void findBooks_byAuthor() {
        bookService.saveBook(getBook());

        var response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/search?author=Testowy",
                BookDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(Objects.requireNonNull(response.getBody())).count()).isGreaterThan(0);
    }

    @Test
    void findBooks_byCategory() {
        bookService.saveBook(getBook());

        var response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/search?category=testowa",
                BookDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(Objects.requireNonNull(response.getBody())).count()).isGreaterThan(0);
    }

    @Test
    void findBooks_byPublishing() {
        bookService.saveBook(getBook());

        var response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/search?publishing=Wydawnictwo testowe",
                BookDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(Objects.requireNonNull(response.getBody())).count()).isGreaterThan(0);
    }

    @Test
    void findBooks_byAll() {
        bookService.saveBook(getBook());

        var response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/search?" +
                        "publishing=Wydawnictwo testowe&name=Wielki test&author=Test Testowy&category=testowa",
                BookDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(Objects.requireNonNull(response.getBody())).count()).isGreaterThan(0);
    }

    @Test
    void findBookById() {
        var result = bookService.saveBook(getBook());

        ResponseEntity<BookDto> response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/" + Objects.requireNonNull(result.getBody()).getId(),
                BookDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getTitle()).isEqualTo("Wielki test");
    }

    @Test
    void saveBook() {
        ResponseEntity<Book> response = testRestTemplate.postForEntity("http://localhost:" + port + "/books",
                getBook(),
                Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getTitle()).isEqualTo("Wielki test");
    }

    @Test
    void updateBook() {
        var result = bookService.saveBook(getBook());
        var id = Objects.requireNonNull(result.getBody()).getId();
        var beforeTitle = result.getBody().getTitle();

        Book toUpdate = getBook();
        toUpdate.setTitle("Nowy tytuł");

        HttpEntity<Book> requestEntity = new HttpEntity<>(toUpdate);

        var response = testRestTemplate.exchange("http://localhost:" + port + "/books/" + id,
                HttpMethod.PUT, requestEntity, Book.class);

        HttpStatus status = response.getStatusCode();

        assertThat(status).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<BookDto> responseById = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/books/" + id,
                BookDto.class
        );

        assertThat(Objects.requireNonNull(responseById.getBody()).getTitle()).isNotEqualTo(beforeTitle);
        assertThat(responseById.getBody().getTitle()).isEqualTo("Nowy tytuł");
    }

    @Test
    void deleteBook() {
        int initial = repository.findAll().size();
        var result = bookService.saveBook(getBook());
        var id = Objects.requireNonNull(result.getBody()).getId();
        int beforeDeleteBookCount = repository.findAll().size()-initial;

        var response = testRestTemplate.exchange("http://localhost:" + port + "/books/" + id,
                HttpMethod.DELETE, null, Book.class);

        int afterDeleteBookCount = repository.findAll().size()-initial;

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(beforeDeleteBookCount).isEqualTo(1);
        assertThat(afterDeleteBookCount).isEqualTo(0);
    }

    @Test
    void handleExceptions() {
        Book book = getBook();
        book.setTitle(null);
        var response = testRestTemplate.postForEntity("http://localhost:" + port + "/books",
                book,
                LinkedHashMap.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).get("BAD_REQUEST")).isEqualTo("Validation error!");
        assertThat(response.getBody().get("title")).isEqualTo("Title is mandatory");
    }

    private Book getBook() {
        Book book = new Book();

        book.setTitle("Wielki test");
        Set<Author> authors = new HashSet<>();
        Author testAuthor = new Author();
        testAuthor.setName("Test");
        testAuthor.setSurname("Testowy");
        authors.add(testAuthor);
        book.setAuthors(authors);

        Category category = new Category();
        category.setName("testowa");
        book.setCategory(category);

        Publishing publishing = new Publishing();
        publishing.setName("Wydawnictwo testowe");
        book.setPublishing(publishing);

        return book;
    }
}