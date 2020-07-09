package c.w.g.repository;

import c.w.g.bean.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Integer> {
    List<Test> findByIdBetween(Integer startId, Integer endId);
}
