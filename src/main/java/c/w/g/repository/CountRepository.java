package c.w.g.repository;

import c.w.g.bean.Count;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountRepository extends JpaRepository<Count, Integer> {


}
