package service;

import dataaccess.TokenInUseException;
import dataaccess.memory.MemoryAuthentificationDataAccess;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryAuthentificationServiceTests {
    private MemoryAuthentificationDataAccess authDAO = new MemoryAuthentificationDataAccess();

    @Test
    public void createGetAuthTest() throws TokenInUseException {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertEquals(authDAO.getAuth(authToken).authToken(), "Authenticated");
        assertEquals(authDAO.getAuth(authToken).username(), "Bob");
    }

    @Test
    public void clearAuthDataTest() throws TokenInUseException {
        authDAO.clear();
        String authToken1 = "Authenticated1";
        String authToken2 = "Authenticated2";

        authDAO.createAuth(authToken1, "Bob");
        authDAO.createAuth(authToken2, "Sherry");

        authDAO.clear();

        assertNull(authDAO.getAuth(authToken1));
        assertNull(authDAO.getAuth(authToken2));
    }

    @Test
    public void cannotCreateMultipleAuthentificationsWithSameToken() throws TokenInUseException {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertThrows(
                TokenInUseException.class,
                () -> authDAO.createAuth(authToken, "Sherry")
        );
    }

    @Test
    public void deleteAuthDataTest() throws TokenInUseException {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        authDAO.deleteAuth(authToken);

        assertNull(authDAO.getAuth(authToken));
    }
}
