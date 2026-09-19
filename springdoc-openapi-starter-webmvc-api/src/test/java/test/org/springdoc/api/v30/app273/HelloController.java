package test.org.springdoc.api.v30.app273;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;

@RestController
public class HelloController {

	@PostMapping("/time1")
	public TimeDtoNoAnnotations postTime1(
			@RequestBody TimeDtoNoAnnotations timeDto) {
		return timeDto;
	}

	public record TimeDtoNoAnnotations(LocalDateTime localDateTime,
									   LocalDate localDate, LocalTime localTime,
									   YearMonth yearMonth, MonthDay monthDay,
									   Year year) {
	}
}