package c.w.g.repository;

import c.w.g.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository
        extends CrudRepository<Order, Long>, PagingAndSortingRepository<Order, Long>
        , JpaRepository<Order, Long> {

    List<Order> findBySenderId(Integer senderId);

    List<Order> findByState(String state);

    List<Order> findByReceiveDelivererIdAndState(Integer receiveDelivererId, String state);

    @Query("select o from Order o where o.receiveDelivererId=?1 and (o.state=?2 or o.state=?3)")
    List<Order> findByReceiveDelivererIdAndStateOrState(Integer receiveDelivererId, String state, String state2);

    @Query("select o from Order o where o.dispatchDelivererId=?1 and (o.state = '已递送至物流中心' or o.state='派件失败')")
    List<Order> findByDispatchDelivererIdAndState(Integer dispatchDelivererId);

    List<Order> findByDispatchDelivererIdAndState(Integer dispatchDelivererId, String state);

    List<Order> findByDispatchDelivererIdAndStateOrStateAndDateBetween(Integer delivererId
            , String state1, String state2, String dateStart, String dateEnd);

    List<Order> findByReceiveDelivererIdAndStateAndDateBetween(Integer delivererId
            , String state, String dateStart, String dateEnd);

    @Query("select o from Order o where o.receiveDelivererId=?1 and (o.state=?2 or o.state=?3 or o.state=?6) and o.enterDate between ?4 and ?5")
    List<Order> findByReceiveDelivererIdAndStateOrStateAndEnterDateBetween(Integer delivererId
            , String state, String state2, String dateStart, String dateEnd,String state3);

    List<Order> findByDispatchDelivererIdAndStateAndOutDateBetween(Integer delivererId
            , String state, String dateStart, String dateEnd);

    @Query(value = "SELECT MAX(counts),min(counts),receiveArea,sum(counts) FROM" +
            "(SELECT " +
            "count(`order`.receiveDelivererId) AS counts,`order`.receiveDelivererId,`order`.receiveArea " +
            "FROM `order` WHERE" +
            "(`order`.state='已递送至物流中心' OR `order`.state='已签收') " +
            "AND `order`.date BETWEEN ?1 AND ?2 " +
            "GROUP BY `order`.receiveDelivererId) AS a " +
            "GROUP BY receiveArea", nativeQuery = true)
    String[] queryDelivererReceiveWorkLoad(String dateStare, String dateEnd);

    @Query(value = "SELECT MAX(counts),min(counts),dispatchArea,sum(counts) FROM" +
            "(SELECT " +
            "count(`order`.dispatchDelivererId) AS counts,`order`.dispatchDelivererId,`order`.dispatchArea " +
            "FROM `order` WHERE" +
            "(`order`.state='已签收') " +
            "AND `order`.date BETWEEN ?1 AND ?2 " +
            "GROUP BY `order`.dispatchDelivererId) AS a " +
            "GROUP BY dispatchArea", nativeQuery = true)
    String[] queryDelivererDispatchWorkLoad(String dateStare, String dateEnd);

}
