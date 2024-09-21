package java.com.offerbrowserprototype.features;

import com.offerbrowserprototype.OfferBrowserPrototypeApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("integration")
@SpringBootTest(classes = OfferBrowserPrototypeApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class BaseIntegrationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);


}
