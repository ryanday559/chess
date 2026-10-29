package dataaccess.sql;

import dataaccess.AuthentificationDataAccess;
import dataaccess.DataAccessException;
import dataaccess.TokenInUseException;
import dataaccess.UnauthorizedException;
import dataaccess.memory.MemoryAuthentificationDataAccess;
import dataaccess.mysql.MySqlAuthentificationDataAccess;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class AuthentificationDAOTests {
    public enum AuthType {
        MEMORY,
        MYSQL
    }

    private AuthentificationDataAccess getDataAccessType(AuthType dataAccessType) throws DataAccessException {
        return switch (dataAccessType) {
            case MEMORY -> new MemoryAuthentificationDataAccess();
            case MYSQL -> new MySqlAuthentificationDataAccess();
        };
    }

    @ParameterizedTest
    @EnumSource(AuthType.class)
    public void createGetAuthTest(AuthType dataAccessType) throws TokenInUseException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(dataAccessType);
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertEquals(authDAO.getAuth(authToken).authToken(), "Authenticated");
        assertEquals(authDAO.getAuth(authToken).username(), "Bob");
    }

    @ParameterizedTest
    @EnumSource(AuthType.class)
    public void clearAuthDataTest(AuthType dataAccessType) throws TokenInUseException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(dataAccessType);
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
    @EnumSource(AuthType.class)
    public void cannotCreateMultipleAuthentificationsWithSameToken(AuthType dataAccessType) throws TokenInUseException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(dataAccessType);
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        assertThrows(
                TokenInUseException.class,
                () -> authDAO.createAuth(authToken, "Sherry")
        );
    }

    @ParameterizedTest
    @EnumSource(AuthType.class)
    public void deleteAuthDataTest(AuthType dataAccessType) throws TokenInUseException, UnauthorizedException, DataAccessException {
        AuthentificationDataAccess authDAO = getDataAccessType(dataAccessType);
        authDAO.clear();
        String authToken = "Authenticated";

        authDAO.createAuth(authToken, "Bob");
        authDAO.deleteAuth(authToken);

        assertNull(authDAO.getAuth(authToken));
    }
}



