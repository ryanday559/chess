package dataaccess;

import dataaccess.memory.MemoryAuthentificationDataAccess;
import dataaccess.mysql.MySqlAuthentificationDataAccess;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MySqlAuthentificationDAOTests {
    private MySqlAuthentificationDataAccess authDAO = new MySqlAuthentificationDataAccess();

    public MySqlAuthentificationDAOTests() throws DataAccessException {
    }

    @Test
    public void createGetAuthTest() throws TokenInUseException, DataAccessException {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertEquals(authDAO.getAuth(authToken).authToken(), "Authenticated");
        assertEquals(authDAO.getAuth(authToken).username(), "Bob");
    }

    @Test
    public void clearAuthDataTest() throws TokenInUseException, DataAccessException {
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
    public void cannotCreateMultipleAuthentificationsWithSameToken() throws TokenInUseException, DataAccessException {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertThrows(
                TokenInUseException.class,
                () -> authDAO.createAuth(authToken, "Sherry")
        );
    }

    @Test
    public void deleteAuthDataTest() throws TokenInUseException, UnauthorizedException, DataAccessException {
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        authDAO.deleteAuth(authToken);

        assertNull(authDAO.getAuth(authToken));
    }
}
