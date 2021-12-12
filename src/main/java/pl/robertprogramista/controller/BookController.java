package pl.robertprogramista.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.robertprogramista.model.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
class BookController {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublishingRepository publishingRepository;
    private final AuthorRepository authorRepository;

    BookController(BookRepository repository, CategoryRepository categoryRepository,
                   PublishingRepository publishingRepository, AuthorRepository authorRepository) {
        this.bookRepository = repository;
        this.categoryRepository = categoryRepository;
        this.publishingRepository = publishingRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/books")
    ResponseEntity<List<Book>> findAllBook() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/books/search")
    List<Book> findBooksAuthor(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String publishing
    ) {
        if(name == null && author == null && category == null && publishing == null) {
            return new ArrayList<>();
        }
        List<Book> books = bookRepository.searchBooks(name, author, category, publishing);
        return books;
    }

    @GetMapping("/books/{id}")
    ResponseEntity<Book> findBookById(@PathVariable int id) {
        return bookRepository.findById(id).map(book -> ResponseEntity.ok(book)).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/books")
    ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        setCategory(book.getCategory());
        setPublishing(book.getPublishing());
        setAuthors(book.getAuthors());
        Book result = bookRepository.save(book);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@PathVariable int id, @Valid @RequestBody Book book) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        book.setId(id);
        setCategory(book.getCategory());
        setPublishing(book.getPublishing());
        setAuthors(book.getAuthors());
        bookRepository.save(book);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<?> deleteBook(@PathVariable int id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Book book = new Book();
        book.setId(id);
        bookRepository.delete(book);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(HttpStatus.BAD_REQUEST.name(),"Validation error!");
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private void setCategory(Category category) {
        if (category.getName().isBlank()) {
            return;
        }

        Integer categoryId = categoryRepository.getCategoryIdByName(category.getName());
        if (categoryId != null) {
            category.setId(categoryId);
        } else {
            Integer id = categoryRepository.save(category).getId();
            category.setId(id);
        }
    }

    private void setPublishing(Publishing publishing) {
        if (publishing.getName().isBlank()) {
            return;
        }

        Integer publishingId = publishingRepository.getPublishingIdByName(publishing.getName());
        if (publishingId != null) {
            publishing.setId(publishingId);
        } else {
            Integer id = publishingRepository.save(publishing).getId();
            publishing.setId(id);
        }
    }

    private void setAuthors(Set<Author> authors) {
        if (authors.isEmpty()) {
            return;
        }

        authors.forEach(author -> {
            Integer authorId = authorRepository.getAuthorByFullName(author.getName(), author.getSurname());
            if (authorId != null) {
                author.setId(authorId);
            } else {
                Integer id = authorRepository.save(author).getId();
                author.setId(id);
            }
        });
    }
}
