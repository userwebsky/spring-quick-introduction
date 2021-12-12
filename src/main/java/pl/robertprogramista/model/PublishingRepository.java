package pl.robertprogramista.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublishingRepository extends JpaRepository<Publishing, Integer> {
    @Query(nativeQuery = true, value = "select id from publishing where name=:name")
    Integer getPublishingIdByName(@Param("name") String name);
}
