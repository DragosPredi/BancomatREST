package com.bancomat;

import com.atm.backend.feign.AdelinaClient;
import com.atm.backend.feign.DianaClient;
import com.atm.backend.services.RemoteAtmService;
import com.atm.backend.services.RemoteAtmServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

public class RemoteAtmTests {

    @Test
    public void checkRemoteAtmAdelinaOnline(){
        AdelinaClient adelinaClient = Mockito.mock(AdelinaClient.class);
        DianaClient dianaClient = Mockito.mock(DianaClient.class);
        RemoteAtmService tester = new RemoteAtmServiceImpl(adelinaClient, dianaClient);

        Mockito.when(adelinaClient.isOnline()).thenReturn(new ResponseEntity<>("Server is up", HttpStatus.OK));
        assertTrue(tester.isOnlineAdelina());

        Mockito.when(adelinaClient.isOnline()).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        assertFalse(tester.isOnlineAdelina());
    }

    @Test
    public void checkRemoteAtmDianaOnline(){
        AdelinaClient adelinaClient = Mockito.mock(AdelinaClient.class);
        DianaClient dianaClient = Mockito.mock(DianaClient.class);
        RemoteAtmService tester = new RemoteAtmServiceImpl(adelinaClient, dianaClient);

        Mockito.when(dianaClient.isOnline()).thenReturn(new ResponseEntity<>("Server is up", HttpStatus.OK));
        assertTrue(tester.isOnlineDiana());

        Mockito.when(dianaClient.isOnline()).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        assertFalse(tester.isOnlineDiana());
    }
}
