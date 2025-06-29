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

package test.org.springdoc.api.v30.app82;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.utils.Constants.OPERATION_ATTRIBUTE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoutingConfiguration {

	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(UserHandler userHandler) {
		return route(GET("/api/user/index").and(accept(APPLICATION_JSON)), userHandler::getAll)
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("getAllUsers"))

				.and(route(GET("/api/user/{id}").and(accept(APPLICATION_JSON)), userHandler::getUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("getUserById")))

				.and(route(POST("/api/user/post").and(accept(APPLICATION_JSON)), userHandler::postUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("saveUser")))

				.and(route(PUT("/api/user/put").and(accept(APPLICATION_JSON)), userHandler::putUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("putUser")))

				.and(route(DELETE("/api/user/delete/{id}").and(accept(APPLICATION_JSON)), userHandler::deleteUser)
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(UserRepository.class).beanMethod("deleteUser")));
	}

}