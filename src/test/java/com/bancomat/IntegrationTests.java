package com.bancomat;

import com.atm.backend.feign.AdelinaClient;
import com.atm.backend.feign.DianaClient;
import com.atm.backend.infrastructure.dto.SoldInquiryDto;
import com.atm.backend.services.RemoteAtmService;
import com.atm.backend.services.impl.RemoteAtmServiceImpl;
import feign.FeignException;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static io.restassured.RestAssured.get;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {

    @Test
    public void checkRemoteAtmAdelinaOnline(){
        AdelinaClient adelinaClient = Mockito.mock(AdelinaClient.class);
        DianaClient dianaClient = Mockito.mock(DianaClient.class);
        RemoteAtmService tester = new RemoteAtmServiceImpl(adelinaClient, dianaClient);

        Mockito.when(adelinaClient.isOnline()).thenReturn(new ResponseEntity<>("Server is up", HttpStatus.OK));
        assertTrue(tester.isOnlineAdelina());

        Mockito.when(adelinaClient.isOnline()).thenThrow(FeignException.class);
        assertFalse(tester.isOnlineAdelina());
    }

    @Test
    public void checkRemoteAtmDianaOnline(){
        AdelinaClient adelinaClient = Mockito.mock(AdelinaClient.class);
        DianaClient dianaClient = Mockito.mock(DianaClient.class);
        RemoteAtmService tester = new RemoteAtmServiceImpl(adelinaClient, dianaClient);

        Mockito.when(dianaClient.isOnline()).thenReturn(new ResponseEntity<>("Server is up", HttpStatus.OK));
        assertTrue(tester.isOnlineDiana());

        Mockito.when(dianaClient.isOnline()).thenThrow(FeignException.class);
        assertFalse(tester.isOnlineDiana());
    }

    @Test
    public void whenAllRemoteServerOfflineWithdrawalRequestRemoteReturnsNull(){
        AdelinaClient adelinaClient = Mockito.mock(AdelinaClient.class);
        DianaClient dianaClient = Mockito.mock(DianaClient.class);
        RemoteAtmService tester = new RemoteAtmServiceImpl(adelinaClient, dianaClient);

        Mockito.when(dianaClient.isOnline()).thenThrow(FeignException.class);
        Mockito.when(adelinaClient.isOnline()).thenThrow(FeignException.class);

        assertNull(tester.remoteWithdrawalRequest((int)(Math.random() * 100)));
    }

    @Test
    public void whenOneRemoteIsOnlineAndHasEnoughCashAValidDtoIsReturned(){
        AdelinaClient adelinaClient = Mockito.mock(AdelinaClient.class);
        DianaClient dianaClient = Mockito.mock(DianaClient.class);
        RemoteAtmService tester = new RemoteAtmServiceImpl(adelinaClient, dianaClient);

        Mockito.when(dianaClient.isOnline()).thenThrow(FeignException.class);
        Mockito.when(adelinaClient.isOnline()).thenReturn(new ResponseEntity<>("Server is up", HttpStatus.OK));

        Mockito.when(adelinaClient.requestTransaction(150)).thenReturn(new ResponseEntity<>(new SoldInquiryDto(new HashMap<>(), "message"), HttpStatus.OK));

        assertEquals("message", tester.remoteWithdrawalRequest(150).getMessage());
    }

    @Test
    public void withdraw101RestChecker() {
        SoldInquiryDto output = get("http://localhost:8080/api/new-transaction?sum=101").as(SoldInquiryDto.class);
        Map<String, Integer> expected = new TreeMap<>();
        expected.put("ONEHUNDRED_RON(100)", 1);
        expected.put("ONE_RON(1)", 1);
        expected.put("TEN_RON(10)", 0);
        expected.put("FIVE_RON(5)", 0);
        expected.put("FIFTY_RON(50)", 0);

        assertEquals(expected, output.getBills());
        assertEquals("Transaction approved", output.getMessage());
    }

    @Test
    public void withdraw101and1000RestChecker() {
        SoldInquiryDto output = get("http://localhost:8080/api/new-transaction?sum=101").as(SoldInquiryDto.class);
        Map<String, Integer> expected = new TreeMap<>();
        expected.put("ONEHUNDRED_RON(100)", 1);
        expected.put("ONE_RON(1)", 1);
        expected.put("TEN_RON(10)", 0);
        expected.put("FIVE_RON(5)", 0);
        expected.put("FIFTY_RON(50)", 0);
        assertEquals(expected, output.getBills());
        assertEquals("Transaction approved", output.getMessage());

        output = get("http://localhost:8080/api/new-transaction?sum=1000").as(SoldInquiryDto.class);
        expected.clear();
        expected.put("ONEHUNDRED_RON(100)", 10);
        expected.put("ONE_RON(1)", 0);
        expected.put("TEN_RON(10)", 0);
        expected.put("FIVE_RON(5)", 0);
        expected.put("FIFTY_RON(50)", 0);

        assertEquals(expected, output.getBills());
        assertEquals("Transaction approved", output.getMessage());
    }

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
            service("localhost:8080/api")
                    .get("/online")
                    .willReturn(success("Server is up", "text/plain"))
    ));

    @Test
    public void testOnline(){
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/online", String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals(responseEntity.getBody(), "Server is up");
    }
}
