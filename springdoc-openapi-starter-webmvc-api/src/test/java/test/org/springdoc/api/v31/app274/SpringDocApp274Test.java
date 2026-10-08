package test.org.springdoc.api.v31.app274;

import org.springdoc.core.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

@TestPropertySource(properties = {
		Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ENABLED + "=true",
		Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ONE_OF_AS_REF + "=true"
})
public class SpringDocApp274Test extends AbstractSpringDocTest {

	@SpringBootApplication
	@ComponentScan(basePackages = "test.org.springdoc.api.v31.app274")
	static class SpringDocTestApp {
	}
}
