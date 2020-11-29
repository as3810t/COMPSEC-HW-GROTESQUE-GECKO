package hu.grotesque_gecko.caffstore.logging;

import hu.grotesque_gecko.caffstore.logging.repositories.CAFFStoreHttpRepository;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpTraceActuatorConfiguration {
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new CAFFStoreHttpRepository();
    }
}
