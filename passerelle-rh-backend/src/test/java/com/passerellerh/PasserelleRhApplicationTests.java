package com.passerellerh;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Nécessite PostgreSQL — exécuter uniquement en intégration")
class PasserelleRhApplicationTests {

    @Test
    void contextLoads() {
    }

}
