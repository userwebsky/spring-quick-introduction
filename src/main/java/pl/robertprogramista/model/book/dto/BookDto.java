package pl.robertprogramista.model.book.dto;

import pl.robertprogramista.model.author.dto.AuthorDto;
import pl.robertprogramista.model.book.dao.Book;
import pl.robertprogramista.model.category.dto.CategoryDto;
import pl.robertprogramista.model.publishing.dto.PublishingDto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BookDto {
    private final String title;
    private final Set<AuthorDto> authors;
    private final Date dateOfPublication;
    private final String isbn;
    private final CategoryDto category;
    private final PublishingDto publishing;

    public BookDto(Book book) {
        title = book.getTitle();
        Set<AuthorDto> authorsDto = new HashSet<>();
        book.getAuthors().forEach(author -> authorsDto.add(new AuthorDto(author)));
        authors = authorsDto;
        dateOfPublication = book.getDateOfPublication();
        isbn = book.getIsbn();
        category = new CategoryDto(book.getCategory());
        publishing = new PublishingDto(book.getPublishing());
    }

    public String getTitle() {
        return title;
    }

    public Set<AuthorDto> getAuthors() {
        return authors;
    }

    public Date getDateOfPublication() {
        return dateOfPublication;
    }

    public String getIsbn() {
        return isbn;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public PublishingDto getPublishing() {
        return publishing;
    }
}
