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

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package test.org.springdoc.api.v31.app2.api;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import test.org.springdoc.api.v31.app2.model.Order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The interface Store api.
 */
@jakarta.annotation.Generated(value = "org.springdoc.demo.app2.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

@Tag(name = "store", description = "the store API")
public interface StoreApi {

	/**
	 * Gets delegate.
	 *
	 * @return the delegate
	 */
	default StoreApiDelegate getDelegate() {
		return new StoreApiDelegate() {
		};
	}

	/**
	 * Delete order response entity.
	 *
	 * @param orderId the order id
	 * @return the response entity
	 */
	@Operation(summary = "Delete purchase order by ID", tags = { "store" })
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Order not found") })
	@DeleteMapping(value = "/store/order/{orderId}")
	@ResponseBody
	default ResponseEntity<Void> deleteOrder(
			@Parameter(description = "ID of the order that needs to be deleted", required = true) @PathVariable("orderId") String orderId) {
		return getDelegate().deleteOrder(orderId);
	}

	/**
	 * Gets inventory.
	 *
	 * @return the inventory
	 */
	@Operation(summary = "Returns pet inventories by status", description = "Returns a map of status codes to quantities", security = {
			@SecurityRequirement(name = "api_key") }, tags = { "store" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Map.class)))) })
	@GetMapping(value = "/store/inventory", produces = { "application/json" })
	@ResponseBody
	default ResponseEntity<Map<String, Integer>> getInventory() {
		return getDelegate().getInventory();
	}

	/**
	 * Gets order by id.
	 *
	 * @param orderId the order id
	 * @return the order by id
	 */
	@Operation(summary = "Find purchase order by ID", tags = { "store" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Order.class))),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Order not found") })
	@GetMapping(value = "/store/order/{orderId}", produces = { "application/xml", "application/json" })
	@ResponseBody
	default ResponseEntity<Order> getOrderById(
			@Min(1L) @Max(5L) @Parameter(description = "ID of pet that needs to be fetched", required = true) @PathVariable("orderId") Long orderId) {
		return getDelegate().getOrderById(orderId);
	}

	/**
	 * Place order response entity.
	 *
	 * @param order the order
	 * @return the response entity
	 */
	@Operation(summary = "Place an order for a pet", tags = { "store" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Order.class))),
			@ApiResponse(responseCode = "400", description = "Invalid Order") })
	@PostMapping(value = "/store/order", produces = { "application/xml", "application/json" }, consumes = {
			"application/json" })
	@ResponseBody
	default ResponseEntity<Order> placeOrder(
			@Parameter(description = "order placed for purchasing the pet", required = true) @Valid @RequestBody Order order) {
		return getDelegate().placeOrder(order);
	}

}
