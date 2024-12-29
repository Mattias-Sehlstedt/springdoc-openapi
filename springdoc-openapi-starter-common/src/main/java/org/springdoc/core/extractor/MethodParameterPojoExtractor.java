/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2024 the original author or authors.
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
package org.springdoc.core.extractor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.service.AbstractRequestService;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;

import static org.springdoc.core.utils.Constants.DOT;

/**
 * The type Method parameter pojo extractor.
 *
 * @author bnasslahsen, michael.clarke
 */
public class MethodParameterPojoExtractor {

	/**
	 * The constant SIMPLE_TYPE_PREDICATES.
	 */
	private static final List<Predicate<Class<?>>> SIMPLE_TYPE_PREDICATES = new ArrayList<>();

	/**
	 * The constant SIMPLE_TYPES.
	 */
	private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>();

	static {
		SIMPLE_TYPES.add(CharSequence.class);
		SIMPLE_TYPES.add(Optional.class);
		SIMPLE_TYPES.add(OptionalInt.class);
		SIMPLE_TYPES.add(OptionalLong.class);
		SIMPLE_TYPES.add(OptionalDouble.class);
		SIMPLE_TYPES.add(AtomicLong.class);
		SIMPLE_TYPES.add(AtomicInteger.class);
		SIMPLE_TYPES.add(Charset.class);

		SIMPLE_TYPES.add(Map.class);
		SIMPLE_TYPES.add(Iterable.class);
		SIMPLE_TYPES.add(Duration.class);
		SIMPLE_TYPES.add(LocalTime.class);
		SIMPLE_TYPES.add(Class.class);

		SIMPLE_TYPE_PREDICATES.add(Class::isPrimitive);
		SIMPLE_TYPE_PREDICATES.add(Class::isEnum);
		SIMPLE_TYPE_PREDICATES.add(Class::isArray);
		SIMPLE_TYPE_PREDICATES.add(MethodParameterPojoExtractor::isSwaggerPrimitiveType);
		SIMPLE_TYPE_PREDICATES.add(aClass -> aClass.getName().startsWith("org.codehaus.groovy.reflection"));
	}

	/**
	 * Instantiates a new Method parameter pojo extractor.
	 */
	private MethodParameterPojoExtractor() {
	}

	/**
	 * Extract from stream.
	 *
	 * @param clazz the clazz
	 * @return the stream
	 */
	static Stream<MethodParameter> extractFrom(Class<?> clazz) {
		return extractFrom(clazz, "", true);
	}

	/**
	 * Extract from stream.
	 *
	 * @param clazz           the clazz
	 * @param fieldNamePrefix the field name prefix
	 * @param parentRequired  whether the field that hold the class currently being inspected was required or optional
	 * @return the stream
	 */
	private static Stream<MethodParameter> extractFrom(Class<?> clazz, String fieldNamePrefix, boolean parentRequired) {
		return allFieldsOf(clazz).stream()
				.filter(field -> !field.getType().equals(clazz))
				.flatMap(f -> fromGetterOfField(clazz, f, fieldNamePrefix, parentRequired))
				.filter(Objects::nonNull);
	}

	/**
	 * From getter of field stream.
	 *
	 * @param paramClass      the param class
	 * @param field           the field
	 * @param fieldNamePrefix the field name prefix
	 * @param parentRequired  whether the field that holds the class currently being examined was required or optional
	 * @return the stream
	 */
	private static Stream<MethodParameter> fromGetterOfField(Class<?> paramClass, Field field, String fieldNamePrefix, boolean parentRequired) {
		Class<?> type = extractType(paramClass, field);

		if (Objects.isNull(type))
			return Stream.empty();

		if (isSimpleType(type))
			return fromSimpleClass(paramClass, field, fieldNamePrefix, parentRequired);
		else {
			Parameter parameter = field.getAnnotation(Parameter.class);
			Schema schema = field.getAnnotation(Schema.class);
			boolean visible = resolveVisible(parameter, schema);
			if (!visible) {
				return Stream.empty();
			}
			String prefix = fieldNamePrefix + resolveName(parameter, schema).orElse(field.getName()) + DOT;
			Set<String> annotationSimpleNames = Arrays.stream(field.getDeclaredAnnotations())
					.map(Annotation::annotationType)
					.map(Class::getSimpleName)
					.collect(Collectors.toSet());
			boolean notNullAnnotationsPresent = AbstractRequestService.hasNotNullAnnotation(annotationSimpleNames);
			return extractFrom(type, prefix,  resolveRequired(schema, parameter, !notNullAnnotationsPresent));
		}
	}

	private static Optional<String> resolveName(Parameter parameter, Schema schema) {
		if (parameter != null) {
			return resolveNameFromParameter(parameter);
		}
		if (schema != null) {
			return resolveNameFromSchema(schema);
		}
		return Optional.empty();
	}

	private static Optional<String> resolveNameFromParameter(Parameter parameter) {
		if (parameter.name().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(parameter.name());
	}

	private static Optional<String> resolveNameFromSchema(Schema schema) {
		if (schema.name().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(schema.name());
	}

	private static boolean resolveVisible(Parameter parameter, Schema schema) {
		if (parameter != null) {
			return !parameter.hidden();
		}
		if (schema != null) {
			return !schema.hidden();
		}
		return true;
	}

	private static boolean resolveRequired(Schema schema, Parameter parameter, boolean nullable) {
		if (parameter != null) {
			return resolveRequiredFromParameter(parameter, nullable);
		}
		if (schema != null) {
			return resolveRequiredFromSchema(schema, nullable);
		}
		return !nullable;
	}

	private static boolean resolveRequiredFromParameter(Parameter parameter, boolean nullable) {
		if (parameter.required()) {
			return true;
		}
		return !nullable;
	}

	private static boolean resolveRequiredFromSchema(Schema schema, boolean nullable) {
		if (schema.required()) {
			return true;
		}
		else if (schema.requiredMode() == Schema.RequiredMode.REQUIRED) {
			return true;
		}
		else if (schema.requiredMode() == Schema.RequiredMode.NOT_REQUIRED) {
			return false;
		}
		return !nullable;
	}

	/**
	 * Extract the type
	 *
	 * @param paramClass the param class
	 * @param field      the field
	 * @return The revoled type or null if it was not a reifiable type
	 */
	private static Class<?> extractType(Class<?> paramClass, Field field) {
		Class<?> type = field.getType();
		if (field.getGenericType() instanceof TypeVariable<?>) {
			Type fieldType = GenericTypeResolver.resolveType(field.getGenericType(), paramClass);

			if (fieldType instanceof Class<?>)
				type = (Class<?>) fieldType;
			else    // This is the case for not reifiable types
				type = null;
		}

		return type;
	}

	/**
	 * From simple class stream.
	 *
	 * @param paramClass      the param class
	 * @param field           the field
	 * @param fieldNamePrefix the field name prefix
	 * @return the stream
	 */
	private static Stream<MethodParameter> fromSimpleClass(Class<?> paramClass, Field field, String fieldNamePrefix, boolean isParentRequired) {
		Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
		try {
			Parameter parameter = field.getAnnotation(Parameter.class);
			Schema schema = field.getAnnotation(Schema.class);
			boolean visible = resolveVisible(parameter, schema);
			if (!visible) {
				return Stream.empty();
			}

			Set<String> annotationSimpleNames = Arrays.stream(field.getDeclaredAnnotations())
					.map(Annotation::annotationType)
					.map(Class::getSimpleName)
					.collect(Collectors.toSet());
							
			boolean isNotRequired = !resolveRequired(schema, parameter, !AbstractRequestService.hasNotNullAnnotation(annotationSimpleNames));
			Annotation[] notNullFieldAnnotations = Arrays.stream(fieldAnnotations)
					.filter(annotation -> AbstractRequestService.hasNotNullAnnotation(List.of(annotation.annotationType().getSimpleName())))
					.toArray(Annotation[]::new);
			if (paramClass.getSuperclass() != null && paramClass.isRecord()) {
				return Stream.of(paramClass.getRecordComponents())
						.filter(d -> d.getName().equals(field.getName()))
						.map(RecordComponent::getAccessor)
						.map(method -> new MethodParameter(method, -1))
						.map(methodParameter -> DelegatingMethodParameter.changeContainingClass(methodParameter, paramClass))
						.map(param -> new DelegatingMethodParameter(param, fieldNamePrefix + field.getName(), fieldAnnotations, param.getMethodAnnotations(), notNullFieldAnnotations, true, isNotRequired));

			}
			else
				return Stream.of(Introspector.getBeanInfo(paramClass).getPropertyDescriptors())
						.filter(d -> d.getName().equals(field.getName()))
						.map(PropertyDescriptor::getReadMethod)
						.filter(Objects::nonNull)
						.map(method -> new MethodParameter(method, -1))
						.map(methodParameter -> DelegatingMethodParameter.changeContainingClass(methodParameter, paramClass))
						.map(param -> new DelegatingMethodParameter(param, fieldNamePrefix + field.getName(), fieldAnnotations, param.getMethodAnnotations(), notNullFieldAnnotations, true, isNotRequired));
		}
		catch (IntrospectionException e) {
			return Stream.of();
		}
	}

	/**
	 * All fields of list.
	 *
	 * @param clazz the clazz
	 * @return the list
	 */
	private static List<Field> allFieldsOf(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		do {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return fields;
	}

	/**
	 * Is simple type boolean.
	 *
	 * @param clazz the clazz
	 * @return the boolean
	 */
	static boolean isSimpleType(Class<?> clazz) {
		return SIMPLE_TYPE_PREDICATES.stream().anyMatch(p -> p.test(clazz)) ||
				SIMPLE_TYPES.stream().anyMatch(c -> c.isAssignableFrom(clazz));
	}

	/**
	 * Is swagger primitive type boolean.
	 *
	 * @param clazz the clazz
	 * @return the boolean
	 */
	public static boolean isSwaggerPrimitiveType(Class<?> clazz) {
		PrimitiveType primitiveType = PrimitiveType.fromType(clazz);
		return primitiveType != null;
	}

	/**
	 * Add simple type predicate.
	 *
	 * @param predicate the predicate
	 */
	public static void addSimpleTypePredicate(Predicate<Class<?>> predicate) {
		SIMPLE_TYPE_PREDICATES.add(predicate);
	}

	/**
	 * Add simple types.
	 *
	 * @param classes the classes
	 */
	public static void addSimpleTypes(Class<?>... classes) {
		SIMPLE_TYPES.addAll(Arrays.asList(classes));
	}

	/**
	 * Remove simple types.
	 *
	 * @param classes the classes
	 */
	public static void removeSimpleTypes(Class<?>... classes) {
		SIMPLE_TYPES.removeAll(Arrays.asList(classes));
	}

}
