package pl.robertprogramista.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(nativeQuery = true, value = "select id from categories where name=:name")
    Integer getCategoryIdByName(@Param("name") String name);
}
