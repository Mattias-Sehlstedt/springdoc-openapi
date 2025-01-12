/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package test.org.springdoc.api.v31.app2.api;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Store api controller.
 */
@jakarta.annotation.Generated(value = "org.springdoc.demo.app2.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

@Controller
@RequestMapping("${openapi.openAPIPetstore.base-path:/}")
class StoreApiController implements StoreApi {

	/**
	 * The Delegate.
	 */
	private final StoreApiDelegate delegate;

	/**
	 * Instantiates a new Store api controller.
	 *
	 * @param delegate the delegate
	 */
	public StoreApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) StoreApiDelegate delegate) {
		this.delegate = Optional.ofNullable(delegate).orElse(new StoreApiDelegate() {
		});
	}

	/**
	 * Gets delegate.
	 *
	 * @return the delegate
	 */
	@Override
	public StoreApiDelegate getDelegate() {
		return delegate;
	}

}
