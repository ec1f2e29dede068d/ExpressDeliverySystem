package c.w.g.repository;

import c.w.g.bean.Test;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TestPSRepository extends PagingAndSortingRepository<Test, Integer> {
}
