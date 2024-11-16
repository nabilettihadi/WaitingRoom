package ma.nabil.WRM.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "wrm")
public class WrmProperties {
    private Scheduling scheduling;
    private MaxCapacity maxCapacity;

    @Data
    public static class Scheduling {
        private List<String> algorithms;
    }

    @Data
    public static class MaxCapacity {
        private Integer defaultValue;
    }
}