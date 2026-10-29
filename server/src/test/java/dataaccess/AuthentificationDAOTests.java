package dataaccess;

import dataaccess.memory.MemoryAuthentificationDataAccess;
import dataaccess.mysql.MySqlAuthentificationDataAccess;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class AuthentificationDAOTests {
    public enum authType {
        MEMORY,
        MYSQL
    }


    private AuthentificationDataAccess getDataAccessType(authType dataAccessType) throws DataAccessException {
        return switch (dataAccessType) {
            case MEMORY -> new MemoryAuthentificationDataAccess();
            case MYSQL -> new MySqlAuthentificationDataAccess();
        };
    }


    @ParameterizedTest
    @EnumSource(authType.class)
    public void createGetAuthTest(authType dataAccessType) throws TokenInUseException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(authType.MEMORY);
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertEquals(authDAO.getAuth(authToken).authToken(), "Authenticated");
        assertEquals(authDAO.getAuth(authToken).username(), "Bob");
    }

    @ParameterizedTest
    @EnumSource(authType.class)
    public void clearAuthDataTest(authType dataAccessType) throws TokenInUseException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(authType.MEMORY);
        authDAO.clear();
        String authToken1 = "Authenticated1";
        String authToken2 = "Authenticated2";

        authDAO.createAuth(authToken1, "Bob");
        authDAO.createAuth(authToken2, "Sherry");

        authDAO.clear();

        assertNull(authDAO.getAuth(authToken1));
        assertNull(authDAO.getAuth(authToken2));
    }

    @ParameterizedTest
    @EnumSource(authType.class)
    public void cannotCreateMultipleAuthentificationsWithSameToken(authType dataAccessType) throws TokenInUseException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(authType.MEMORY);
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertThrows(
                TokenInUseException.class,
                () -> authDAO.createAuth(authToken, "Sherry")
        );
    }

    @ParameterizedTest
    @EnumSource(authType.class)
    public void deleteAuthDataTest(authType dataAccessType) throws TokenInUseException, UnauthorizedException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(authType.MEMORY);
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        authDAO.deleteAuth(authToken);

        assertNull(authDAO.getAuth(authToken));
    }
}



