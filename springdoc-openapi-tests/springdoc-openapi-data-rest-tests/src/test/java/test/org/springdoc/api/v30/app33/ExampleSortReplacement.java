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

package test.org.springdoc.api.v30.app33;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.models.Sort;

public class ExampleSortReplacement extends Sort {

	@Parameter(description = "Anything")
	private int something;

	/**
	 * Instantiates a new Sort.
	 *
	 * @param sort the sort
	 */
	public ExampleSortReplacement(List<String> sort) {
		super(sort);
	}

	public int getSomething() {
		return something;
	}

	public void setSomething(int something) {
		this.something = something;
	}
}
