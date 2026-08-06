package test.org.springdoc.api.v31.app1337;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
        "logging.level.root=DEBUG",
        "logging.level.org.springdoc=WARN"
})
public class SpringDocApp1337Test extends AbstractSpringDocTest {

    ListAppender<ILoggingEvent> appender;

    @BeforeEach
    void attachAppender() {
        appender = new ListAppender<>();
        appender.start();
        ((Logger) LoggerFactory.getLogger("org.springdoc.core.utils.SpringDocUtils"))
                .addAppender(appender);
    }

    @Test
    void apiDocsRequestLogsNoJsonProcessingWarning() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v3/api-docs"))
                .andExpect(status().isOk());

        List<String> warnMessages = appender.list.stream()
                .filter(e -> e.getLevel() == Level.WARN)
                .map(ILoggingEvent::getFormattedMessage)
                .toList();

        assertThat(warnMessages)
                .noneMatch(m -> m.contains("Json Processing Exception occurred"));
    }

    @SpringBootApplication
	static class SpringDocTestApp {}
}
