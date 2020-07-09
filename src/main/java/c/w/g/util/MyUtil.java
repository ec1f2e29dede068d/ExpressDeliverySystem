package c.w.g.util;

import org.springframework.util.DigestUtils;

public class MyUtil {
    public String toMd5(String string){
        return DigestUtils.md5DigestAsHex(string.getBytes());
    }
}
