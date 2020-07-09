package c.w.g.service;

import c.w.g.bean.Test;
import c.w.g.repository.TestPSRepository;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import c.w.g.repository.TestRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class TestService {

    @Resource
    private TestRepository testRepository;
    @Resource
    private TestPSRepository testPSRepository;

    public Test save(Test test) {
        return testRepository.save(test);
    }

    public Iterable<Test> getAll() {
        return testRepository.findAll();
    }

    public String getPageAll(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<Test> testPage = testPSRepository.findAll(pageable);
        List<Test> testList = testPage.getContent();
        Gson gson = new Gson();
        long total = testPSRepository.count();
        String rows = gson.toJson(testList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

    public List<Test> getTestBetween(Integer startId, Integer endId) {
        return testRepository.findByIdBetween(startId, endId);
    }
}
