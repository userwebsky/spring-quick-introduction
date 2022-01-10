package pl.robertprogramista.model.publishing.dto;

public class PublishingDto {
    private String name;

    public PublishingDto() {
    }

    public PublishingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
