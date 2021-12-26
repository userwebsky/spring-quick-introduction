package pl.robertprogramista.model.author.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository  extends JpaRepository<Author, Integer> {
    @Query(nativeQuery = true, value = "select id from authors where name=:name and surname=:surname")
    Integer getAuthorByFullName(@Param("name") String name, @Param("surname") String surname);
}
