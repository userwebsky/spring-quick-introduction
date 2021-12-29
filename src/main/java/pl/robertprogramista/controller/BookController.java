package pl.robertprogramista.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.robertprogramista.model.book.dao.Book;
import pl.robertprogramista.model.book.dto.BookDto;
import pl.robertprogramista.service.BookService;

import javax.validation.Valid;
import java.util.*;

@RestController
class BookController {

    private final BookService bookService;

    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    ResponseEntity<List<BookDto>> findAllBook() {
        return bookService.findAllBook();
    }

    @GetMapping("/books/search")
    ResponseEntity<List<BookDto>> findBooksAuthor(
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

    @GetMapping("/books/{id}")
    ResponseEntity<BookDto> findBookById(@PathVariable int id) {
        return bookService.findBookById(id);
    }

    @PostMapping("/books")
    ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> updateBook(@PathVariable int id, @Valid @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<?> deleteBook(@PathVariable int id) {
        return bookService.deleteBook(id);
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
