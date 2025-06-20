/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.ui.app24;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
		"springdoc.api-docs.enabled=false",
		"springdoc.api-docs.path=/api-docs",
		"springdoc.swagger-ui.url=/api-docs/xxx/v1/openapi.yml",
})
@Import(SpringDocConfig.class)
public class SpringDocApp24Test extends AbstractSpringDocTest {

	@Test
	void test_apidocs_disabled() {
		webTestClient.get().uri("/api-docs/swagger-config").exchange()
				.expectStatus().isOk().expectBody()
				.jsonPath("$.url").isEqualTo("/api-docs/xxx/v1/openapi.yml")
				.jsonPath("$.configUrl").isEqualTo("/api-docs/swagger-config")
				.jsonPath("$.validatorUrl").isEqualTo("")
				.jsonPath("$.oauth2RedirectUrl").isEqualTo("/swagger-ui/oauth2-redirect.html");
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}