package pl.robertprogramista.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.robertprogramista.model.Book;
import pl.robertprogramista.model.BookRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
class BookController {

    private final BookRepository repository;

    BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/books")
    ResponseEntity<List<Book>> findAllBook() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/books/{id}")
    ResponseEntity<Book> findBookById(@PathVariable int id) {
        return repository.findById(id).map(book -> ResponseEntity.ok(book)).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/books")
    ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        Book result = repository.save(book);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@PathVariable int id, @Valid @RequestBody Book book) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        book.setId(id);
        repository.save(book);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<?> deleteBook(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Book book = new Book();
        book.setId(id);
        repository.delete(book);
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
}
