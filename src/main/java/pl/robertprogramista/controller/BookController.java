package pl.robertprogramista.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.robertprogramista.annotation.AuditLog;
import pl.robertprogramista.model.book.dao.Book;
import pl.robertprogramista.model.book.dto.BookDto;
import pl.robertprogramista.service.BookService;

import javax.validation.Valid;
import java.util.*;

/**
 * Controller for books API
 */
@RestController
class BookController {

    private final BookService bookService;

    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Get all books
     * @return all books list
     */
    @GetMapping("/books")
    ResponseEntity<List<BookDto>> findAllBook() {
        return bookService.findAllBook();
    }

    /**
     * Searches for a book according to the given criteria
     * @param name title book
     * @param author author book
     * @param category category book
     * @param publishing publishing book
     * @return List of eligible books
     */
    @GetMapping("/books/search")
    ResponseEntity<List<BookDto>> findBooks (
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String publishing
    ) {
        if(name == null && author == null && category == null && publishing == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return bookService.findBooks(name, author, category, publishing);
    }

    /**
     * Get book by id
     * @param id book id
     * @return book
     */
    @GetMapping("/books/{id}")
    ResponseEntity<BookDto> findBookById(@PathVariable int id) {
        return bookService.findBookById(id);
    }

    /**
     * Save book
     * @param book object book
     * @return book
     */
    @PostMapping("/books")
    @Timed(value="save.book", histogram = true)
    ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        return bookService.saveBook(book);
    }

    /**
     * Update book
     * @param id book id
     * @param book object book
     * @return response status
     */
    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@PathVariable int id, @Valid @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    /**
     * Remove book
     * @param id book id
     * @return response status
     */
    @DeleteMapping("/books/{id}")
    @AuditLog(notOnlyArgs = true)
    ResponseEntity<?> deleteBook(@PathVariable int id) {
        return bookService.deleteBook(id);
    }

    /**
     * Handles request errors
     * @param exception exception
     * @return errors
     */
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
