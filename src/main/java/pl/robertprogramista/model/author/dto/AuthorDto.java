package pl.robertprogramista.model.author.dto;

public class AuthorDto {
    private String name;
    private String surname;

    public AuthorDto() {
    }

    public AuthorDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
