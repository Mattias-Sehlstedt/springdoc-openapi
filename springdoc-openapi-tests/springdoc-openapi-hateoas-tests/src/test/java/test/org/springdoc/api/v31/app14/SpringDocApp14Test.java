
package test.org.springdoc.api.v31.app14;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

@SpringBootTest
@TestPropertySource(properties = {"springdoc.api-docs.version=openapi_3_1"})
public class SpringDocApp14Test extends AbstractSpringDocTest {

    @SpringBootApplication
    @ComponentScan(basePackages = "test.org.springdoc.api.v31.app14")
    static class SpringDocTestApp {
    }
}