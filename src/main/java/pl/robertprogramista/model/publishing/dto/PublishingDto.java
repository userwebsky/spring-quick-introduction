package pl.robertprogramista.model.publishing.dto;

import pl.robertprogramista.model.publishing.dao.Publishing;

public class PublishingDto {
    private final String name;

    public PublishingDto(Publishing publishing) {
        this.name = publishing.getName();
    }

    public String getName() {
        return name;
    }
}
