package c.w.g.repository;

import c.w.g.bean.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository
        extends CrudRepository<Member, Integer>, JpaRepository<Member, Integer> {
    Member findByName(String name);
}
