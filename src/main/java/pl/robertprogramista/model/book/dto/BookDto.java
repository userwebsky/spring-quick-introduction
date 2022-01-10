package pl.robertprogramista.model.book.dto;

import pl.robertprogramista.model.author.dto.AuthorDto;
import pl.robertprogramista.model.book.dao.Book;
import pl.robertprogramista.model.category.dto.CategoryDto;
import pl.robertprogramista.model.publishing.dto.PublishingDto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BookDto {
    private int id;
    private String title;
    private Set<AuthorDto> authors;
    private Date dateOfPublication;
    private String isbn;
    private CategoryDto category;
    private PublishingDto publishing;

    public BookDto() {
    }

    public BookDto(Book book) {
        id = book.getId();
        title = book.getTitle();
        Set<AuthorDto> authorsDto = new HashSet<>();
        book.getAuthors().forEach(author -> authorsDto.add(new AuthorDto(author.getName(), author.getSurname())));
        authors = authorsDto;
        dateOfPublication = book.getDateOfPublication();
        isbn = book.getIsbn();
        category = new CategoryDto(book.getCategory().getName());
        publishing = new PublishingDto(book.getPublishing().getName());
    }

    public int getId() {
        return id;
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
