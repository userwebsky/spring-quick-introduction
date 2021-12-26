package pl.robertprogramista.model.category.dto;

import pl.robertprogramista.model.category.dao.Category;

public class CategoryDto {
    private final String name;

    public CategoryDto(Category category) {
        this.name = category.getName();
    }

    public String getName() {
        return name;
    }
}
