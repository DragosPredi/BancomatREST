package com.bancomat;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static org.junit.jupiter.api.Assertions.*;

public class AtmApiTests {

    TestRestTemplate restTemplate = new TestRestTemplate();
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
            service("localhost:8080/api")
                    .get("/online")
                    .willReturn(success("Server is up", "text/plain"))
    ));

    @Test
    public void testOnline(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/online", String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals(responseEntity.getBody(), "Server is up");
    }
}
