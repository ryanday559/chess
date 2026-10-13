package service;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthentificationDataAccess;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryServiceTests {
    private MemoryAuthentificationDataAccess authDAO = new MemoryAuthentificationDataAccess();


    @Test
    public void clearAuthDataTest() {
        authDAO.clear();
        String authToken1 = "Authenticated1";
        String authToken2 = "Authenticated2";

        authDAO.createAuth(authToken1, "Bob");
        authDAO.createAuth(authToken2, "Sherry");

        assertThrows(DataAccessException.class, authDAO.getAuth(authToken1));
        assertThrows(DataAccessException.class, authDAO.getAuth(authToken2));
    }

    @Test
    public void createGetAuthTest() {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertEquals(authDAO.getAuth(authToken).authToken(), "Authenticated");
        assertEquals(authDAO.getAuth(authToken).username(), "Bob");
    }
}
