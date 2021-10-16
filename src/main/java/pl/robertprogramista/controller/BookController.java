package pl.robertprogramista.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.robertprogramista.model.Book;
import pl.robertprogramista.model.BookRepository;

import java.net.URI;
import java.util.List;

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
    ResponseEntity<Book> saveBook(@RequestBody Book book) {
        Book result = repository.save(book);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@PathVariable int id, @RequestBody Book book) {
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
}
