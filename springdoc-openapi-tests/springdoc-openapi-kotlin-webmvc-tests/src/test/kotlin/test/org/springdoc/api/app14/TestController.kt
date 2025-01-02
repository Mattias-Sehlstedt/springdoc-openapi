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

package test.org.springdoc.api.app14

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Schema(description = "Generic description")
data class KeyValue(
	val key: String,
	val value: String,
)

@Schema
data class SomeDTO(
	@Schema(description = "Description A", allOf = [KeyValue::class]) val field_a: KeyValue,
	@Schema(description = "Description B", allOf = [KeyValue::class]) val field_b: KeyValue,
)

@RestController
@RequestMapping("/test")
class TestController {

	@PostMapping("/test")
	fun create(@RequestBody some: SomeDTO) {

	}
}