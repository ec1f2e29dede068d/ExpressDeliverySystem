package c.w.g.repository;

import c.w.g.SpringBootCourseDesignApplication;
import c.w.g.bean.Count;
import c.w.g.bean.Deliverer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootCourseDesignApplication.class)
public class DelivererRepositoryTest {

    @Autowired
    DelivererRepository delivererRepository;

    @Test
    public void testGetCount() {
        List<Count> counts = delivererRepository.findByName("deliverer").getCounts();
        for (Count count : counts) {
            System.out.println(count.getWork() + " " + count.getRest() + " " + count.getWorkOverTime());
        }
    }

    @Test
    public void findGivenDelivererByAreaTest() {
        ArrayList<Deliverer> deliverers = delivererRepository.findGivenDelivererByArea("太平镇");
        for (Deliverer deliverer :
                deliverers) {
            System.out.println(deliverer.getId());
        }
    }

}
