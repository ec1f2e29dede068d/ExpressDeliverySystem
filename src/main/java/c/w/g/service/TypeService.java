package c.w.g.service;

import c.w.g.bean.Type;
import c.w.g.repository.TypeRepository;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class TypeService {

    @Resource
    private TypeRepository typeRepository;

    /**
     * 返回分页的类型列表
     *
     * @param pageNum 页数
     * @param size    每页大小
     * @return JSON数据
     */
    public String queryTypePaged(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<Type> typePage = typeRepository.findAll(pageable);
        List<Type> typeList = typePage.getContent();
        Gson gson = new Gson();
        long total = typeRepository.count();
        String rows = gson.toJson(typeList);
        return "{" +
                "\"rows\":" + rows + ","
                + "\"total\":" + total
                + "}";
    }

}
