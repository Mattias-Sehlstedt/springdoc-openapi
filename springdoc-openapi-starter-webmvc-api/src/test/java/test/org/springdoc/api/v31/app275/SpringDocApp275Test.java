package test.org.springdoc.api.v31.app275;

import org.springdoc.core.utils.Constants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

@TestPropertySource(properties = {
		Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ENABLED + "=true",
		Constants.SPRINGDOC_POLYMORPHIC_CONVERTER_ONE_OF_AS_REF + "=false"
})
public class SpringDocApp275Test extends AbstractSpringDocTest {

	@SpringBootApplication
	@ComponentScan(basePackages = "test.org.springdoc.api.v31.app275")
	static class SpringDocTestApp {
	}
}
