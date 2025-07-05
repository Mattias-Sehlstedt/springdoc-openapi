package test.org.springdoc.api.v30.app244;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("springdoc.custom")
public class SpringDocProperties {

    private String title = "API";
    private String version = "v0.0.1";
    private List<ServerInfo> servers = List.of();

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }

    public List<ServerInfo> getServers() {
        return servers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setServers(List<ServerInfo> servers) {
        this.servers = servers;
    }

    public static class ServerInfo {

        private String url;
        private String description;

        public String getUrl() {
            return url;
        }

        public String getDescription() {
            return description;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
