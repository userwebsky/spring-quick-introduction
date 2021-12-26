package pl.robertprogramista.model.book.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.robertprogramista.model.book.dto.BookDto;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query(nativeQuery = true, value =
            "select b.* from books  b " +
            "join authors a on a.id in (select author_id from books_authors ba where ba.book_id=b.id) " +
            "join categories c on c.id=b.category_id " +
            "join publishing p on p.id=b.publishing_id " +
            "where concat(a.name, ' ', a.surname) like concat('%',:author,'%') " +
            "and c.name like concat('%',:category,'%') " +
            "and p.name like concat('%',:publishing,'%') " +
            "and b.title like concat('%',:name,'%')")
    List<BookDto> searchBooks(@Param("name") String name, @Param("author") String author,
                              @Param("category") String category, @Param("publishing") String publishing);
}
