package service;

import dataaccess.TokenInUseException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static service.AuthentificationService.generateAuthToken;

public class AuthentificationServiceTests {
    private AuthentificationService authService = new AuthentificationService();

    @Test
    public void testGenerateAuthToken() {
        assertNotNull(generateAuthToken());
    }

    @Test
    public void testUpdateAuth() throws TokenInUseException {
        String authToken = generateAuthToken();
        String username = "bob";
        authService.updateAuth(authToken, username);
        assertDoesNotThrow(()->authService.authenticate(authToken));
    }

    @Test
    public void testClear() throws TokenInUseException {
        String authToken = generateAuthToken();
        String username = "bob";
        authService.updateAuth(authToken, username);
        authService.clear();
        assertThrows(UnauthorizedException.class, ()->authService.authenticate(authToken));
    }

    @Test
    public void testRemoveAuth() throws TokenInUseException, UnauthorizedException {
        String authToken = generateAuthToken();
        String username = "bob";
        authService.updateAuth(authToken, username);
        authService.removeAuth(authToken);
        assertThrows(UnauthorizedException.class, ()->authService.authenticate(authToken));
    }

    @Test
    public void testGetAuthData() throws TokenInUseException {
        String authToken = generateAuthToken();
        String username = "bob";
        authService.updateAuth(authToken, username);
        AuthData authData = authService.getAuthData(authToken);
        assertEquals(authData.authToken(), authToken);
        assertEquals(authData.username(), username);
    }
}
