package c.w.g.repository;

        import c.w.g.bean.Deliverer;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.CrudRepository;

        import java.util.ArrayList;

public interface DelivererRepository
        extends CrudRepository<Deliverer, Integer>, JpaRepository<Deliverer, Integer> {

    Deliverer findByName(String name);

    @Query("SELECT d FROM Deliverer d WHERE d.receiveNum+d.dispatchNum=(SELECT MIN(d.dispatchNum+d.receiveNum) FROM Deliverer d WHERE d.area=?1 AND d.state=1) and d.area=?1 AND d.state=1")
    ArrayList<Deliverer> findGivenDelivererByArea(String area);

}
