package pl.robertprogramista.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.robertprogramista.model.author.dao.Author;
import pl.robertprogramista.model.author.dao.AuthorRepository;
import pl.robertprogramista.model.book.dao.Book;
import pl.robertprogramista.model.book.dao.BookRepository;
import pl.robertprogramista.model.book.dto.BookDto;
import pl.robertprogramista.model.category.dao.Category;
import pl.robertprogramista.model.category.dao.CategoryRepository;
import pl.robertprogramista.model.publishing.dao.Publishing;
import pl.robertprogramista.model.publishing.dao.PublishingRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Books service
 */
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublishingRepository publishingRepository;
    private final AuthorRepository authorRepository;

    /**
     * Constructor that sets dependencies
     * @param bookRepository book repository
     * @param categoryRepository category repository
     * @param publishingRepository publishing repository
     * @param authorRepository author repository
     */
    public BookService(
            BookRepository bookRepository,
            CategoryRepository categoryRepository,
            PublishingRepository publishingRepository,
            AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.publishingRepository = publishingRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * Fetches all books from the repository
     * @return books
     */
    public ResponseEntity<List<BookDto>> findAllBook() {
        List<BookDto> books = new ArrayList<>();
        bookRepository.findAll().forEach(book -> books.add(new BookDto(book)));
        return ResponseEntity.ok(books);
    }

    /**
     * Retrieves books from the repository that meet the criteria
     * @param name books title
     * @param author books author
     * @param category books category
     * @param publishing books publishing
     * @return book list
     */
    public ResponseEntity<List<BookDto>> findBooks(String name, String author, String category, String publishing) {
        List<BookDto> books = new ArrayList<>();
        bookRepository.searchBooks(name, author, category, publishing).forEach(book -> books.add(new BookDto(book)));
        return ResponseEntity.ok(books);
    }

    /**
     * Get book by id
     * @param id books id
     * @return book
     */
    public ResponseEntity<BookDto> findBookById(int id) {
        return bookRepository.findById(id).map(
                book -> ResponseEntity.ok(new BookDto(book))).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save book
     * @param book object book
     * @return book
     */
    public ResponseEntity<Book> saveBook(Book book) {
        setCategory(book.getCategory());
        setPublishing(book.getPublishing());
        setAuthors(book.getAuthors());
        Book result = bookRepository.save(book);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    /**
     * Update book
     * @param id books id
     * @param book object book
     * @return response object
     */
    public ResponseEntity<?> updateBook(int id, Book book) {
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

    /**
     * Delete book
     * @param id books id
     * @return response object
     */
    public ResponseEntity<?> deleteBook(int id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Book book = new Book();
        book.setId(id);
        bookRepository.delete(book);
        return ResponseEntity.noContent().build();
    }

    private void setCategory(Category category) {
        if (category.getName().isBlank()) {
            return;
        }

        Integer categoryId = categoryRepository.getCategoryIdByName(category.getName());
        if (categoryId != null) {
            category.setId(categoryId);
        } else {
            int id = categoryRepository.save(category).getId();
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
            int id = publishingRepository.save(publishing).getId();
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
                int id = authorRepository.save(author).getId();
                author.setId(id);
            }
        });
    }
}
