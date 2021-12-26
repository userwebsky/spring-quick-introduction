package pl.robertprogramista.model.author.dto;

import pl.robertprogramista.model.author.dao.Author;

public class AuthorDto {
    private final String name;
    private final String surname;

    public AuthorDto(Author author) {
        this.name = author.getName();
        this.surname = author.getSurname();
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
