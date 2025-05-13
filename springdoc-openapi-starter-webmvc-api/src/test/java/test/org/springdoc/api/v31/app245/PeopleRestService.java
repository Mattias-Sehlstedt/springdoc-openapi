/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app245;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Tag(name = "people")
public class PeopleRestService {
	private final Map<String, PersonDTO> people = new ConcurrentHashMap<>();

	@PostMapping(value = "/{email}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create new person", responses = {
			@ApiResponse(content = @Content(
					schema = @Schema(implementation = PersonDTO.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE),
					headers = @Header(name = "Location"),
					responseCode = "201"
			),
			@ApiResponse(responseCode = "409", description = "Person with such e-mail already exists") }
	)
	public ResponseEntity<String> addPerson(
			@Parameter(description = "E-Mail", required = true) @PathVariable("email") final String email,
			@Parameter(description = "Person information", required = true) @RequestParam("person") final PersonRequestDTO personRequestDTO) {

		final PersonDTO person = people.get(email);

		if (person != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Person with such e-mail already exists");
		}

		people.put(email, new PersonDTO(email, personRequestDTO.getFirstName(), personRequestDTO.getLastName()));
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(UUID.randomUUID()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PostMapping(value = "/ids", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create UUID", responses = {
			@ApiResponse(content = @Content(
					schema = @Schema(implementation = UUID.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE),
					headers = @Header(name = "Location"),
					responseCode = "201"
			),
			@ApiResponse(responseCode = "409", description = "Such a UUID already exists") }
	)
	public ResponseEntity<String> addId(
			@Parameter(description = "UUID", required = true) @RequestParam("id") final UUID uuid) {
		return ResponseEntity.status(CREATED).body(uuid.toString());
	}

	@PostMapping(value = "/string-ids", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create UUID", responses = {
			@ApiResponse(content = @Content(
					schema = @Schema(implementation = UUID.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE),
					headers = @Header(name = "Location"),
					responseCode = "201"
			),
			@ApiResponse(responseCode = "409", description = "Such a UUID already exists") }
	)
	public ResponseEntity<String> addStringId(
			@Parameter(description = "UUID", schema = @Schema(format = "hej"), required = true) @RequestParam("id") final String uuid) {
		return ResponseEntity.status(CREATED).body(uuid.toString());
	}

	@PostMapping(value = "/customIds", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create UUID", responses = {
			@ApiResponse(content = @Content(
					schema = @Schema(implementation = UUID.class),
					mediaType = MediaType.APPLICATION_JSON_VALUE),
					headers = @Header(name = "Location"),
					responseCode = "201"
			),
			@ApiResponse(responseCode = "409", description = "Such a UUID already exists") }
	)
	public ResponseEntity<String> addCustomId(
			@Parameter(description = "UUID", required = true) @RequestParam("id") final CustomUUID uuid) {
		return ResponseEntity.status(CREATED).body(uuid.toString());
	}
}
