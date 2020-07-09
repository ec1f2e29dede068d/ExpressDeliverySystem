package c.w.g.repository;

import c.w.g.bean.Type;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TypeRepository extends CrudRepository<Type, String>, PagingAndSortingRepository<Type, String> {

    @Modifying
    @Query("update Type t set t.name=?1 where t.name=?2")
    Integer updateType(String newValue, String oldValue);
}
