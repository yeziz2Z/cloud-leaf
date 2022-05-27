package cloud.leaf.oauth2.config;


import cloud.leaf.oauth2.CloudLeafOauth2Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = CloudLeafOauth2Application.class)
@Slf4j
public class WebSecurityConfigTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        log.info("889527 {}", passwordEncoder.encode("889527"));
    }
}
