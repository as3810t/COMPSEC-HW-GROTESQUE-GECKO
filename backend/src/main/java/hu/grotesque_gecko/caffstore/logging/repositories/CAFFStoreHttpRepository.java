package hu.grotesque_gecko.caffstore.logging.repositories;

import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;

public class CAFFStoreHttpRepository extends InMemoryHttpTraceRepository {
    public CAFFStoreHttpRepository() {
        setCapacity(10000);
    }
}
