
package test.org.springdoc.api.v30.app12;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.v30.AbstractSpringDocTest;

@SpringBootTest
@TestPropertySource(properties = {"springdoc.api-docs.version=openapi_3_0"})
public class SpringDocApp12Test extends AbstractSpringDocTest {

    @SpringBootApplication
    @ComponentScan(basePackages = "test.org.springdoc.api.v30.app12")
    static class SpringDocTestApp {
    }
}