import com.ruoyi.common.utils.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {

    @Test
    public void test(){
        System.out.println("1111111111111111111111    "+SecurityUtils.encryptPassword("123456"));
    }
}
