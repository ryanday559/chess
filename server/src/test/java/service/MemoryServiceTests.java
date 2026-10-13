package service;

import dataaccess.memory.MemoryAuthentificationDataAccess;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryServiceTests {
    private MemoryAuthentificationDataAccess authDAO = new MemoryAuthentificationDataAccess();


    @Test
    public void createGetAuthTest() {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertEquals(authDAO.getAuth(authToken).authToken(), "Authenticated");
        assertEquals(authDAO.getAuth(authToken).username(), "Bob");
    }
}
