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

package test.org.springdoc.api.v31.app84;

import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.operation.Builder.operationBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.utils.Constants.OPERATION_ATTRIBUTE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class EmployeeFunctionalConfig {


	@Bean
	EmployeeRepository employeeRepository() {
		return new EmployeeRepository();
	}

	@Bean
	RouterFunction<ServerResponse> getAllEmployeesRoute() {
		return route(GET("/employees").and(accept(MediaType.APPLICATION_JSON)),
				req -> ok().body(
						employeeRepository().findAllEmployees(), Employee.class))
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(EmployeeRepository.class).beanMethod("findAllEmployees"));
	}

	@Bean
	RouterFunction<ServerResponse> getEmployeeByIdRoute() {
		return route(GET("/employees/{id}"),
				req -> ok().body(
						employeeRepository().findEmployeeById(req.pathVariable("id")), Employee.class))
				.withAttribute(OPERATION_ATTRIBUTE,
						operationBuilder().operationId("findEmployeeById").summary("Find purchase order by ID").tags(new String[] { "MyEmployee" })
								.parameter(parameterBuilder().in(ParameterIn.PATH).name("id").description("Employee Id"))
								.response(responseBuilder().responseCode("200").description("successful operation").implementation(Employee.class))
								.response(responseBuilder().responseCode("400").description("Invalid Employee ID supplied"))
								.response(responseBuilder().responseCode("404").description("Employee not found")));
	}


	@Bean
	RouterFunction<ServerResponse> updateEmployeeRoute() {
		return route(POST("/employees/update").and(accept(MediaType.APPLICATION_XML)),
				req -> req.body(BodyExtractors.toMono(Employee.class))
						.doOnNext(employeeRepository()::updateEmployee)
						.then(ok().build()))
				.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(EmployeeRepository.class).beanMethod("updateEmployee"));
	}

	@Bean
	RouterFunction<ServerResponse> composedRoutes() {
		return
				route(GET("/employees-composed"),
						req -> ok().body(
								employeeRepository().findAllEmployees(), Employee.class))
						.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(EmployeeRepository.class).beanMethod("findAllEmployees"))

						.and(route(GET("/employees-composed/{id}"), req -> ok().body(
								employeeRepository().findEmployeeById(req.pathVariable("id")), Employee.class))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(EmployeeRepository.class).beanMethod("findEmployeeById")))

						.and(route(POST("/employees-composed/update"),
								req -> req.body(BodyExtractors.toMono(Employee.class))
										.doOnNext(employeeRepository()::updateEmployee)
										.then(ok().build()))
								.withAttribute(OPERATION_ATTRIBUTE, operationBuilder().beanClass(EmployeeRepository.class).beanMethod("updateEmployee")));
	}

}