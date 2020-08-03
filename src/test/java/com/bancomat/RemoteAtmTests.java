package com.bancomat;

import com.atm.backend.feign.AdelinaClient;
import com.atm.backend.feign.DianaClient;
import com.atm.backend.infrastructure.SoldInquiryDto;
import com.atm.backend.services.RemoteAtmService;
import com.atm.backend.services.impl.RemoteAtmServiceImpl;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class RemoteAtmTests {

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
}
