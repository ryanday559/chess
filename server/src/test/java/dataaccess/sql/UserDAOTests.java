package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.InUseException;
import dataaccess.UserDataAccess;
import dataaccess.memory.MemoryUserDataAccess;
import dataaccess.mysql.MySqlUserDataAccess;
import model.UserData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {

    private UserDataAccess getDataAccessType(AuthentificationDAOTests.AuthType dataAccessType) throws DataAccessException {
        return switch (dataAccessType) {
            case MEMORY -> new MemoryUserDataAccess();
            case MYSQL -> new MySqlUserDataAccess();
        };
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.AuthType.class)
    public void clearUserData(AuthentificationDAOTests.AuthType dataAccessType) throws InUseException, DataAccessException {
        UserDataAccess userDAO = getDataAccessType(dataAccessType);
        userDAO.clear();
        String username = "Bob";
        String password = "secret";
        String email = "bobsemail@email.com";

        userDAO.createUser(username, password, email);
        userDAO.clear();

        assertNull(userDAO.getUser("Bob"));
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.AuthType.class)
    public void createGetUser(AuthentificationDAOTests.AuthType dataAccessType) throws InUseException, DataAccessException {
        UserDataAccess userDAO = getDataAccessType(dataAccessType);
        userDAO.clear();
        String username = "Bob";
        String password = "secret";
        String email = "bobsemail@email.com";

        userDAO.createUser(username, password, email);
        UserData user = userDAO.getUser(username);

        assertEquals(username, user.username());
        assertTrue(BCrypt.checkpw(password, user.password()));
        assertEquals(email, user.email());
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.AuthType.class)
    public void cannotCreateDuplicateUsernames(AuthentificationDAOTests.AuthType dataAccessType) throws InUseException, DataAccessException {
        UserDataAccess userDAO = getDataAccessType(dataAccessType);
        userDAO.clear();
        String username = "Bob";
        String password1 = "secret1";
        String email1 = "bobsemail1@email.com";
        String password2 = "secret2";
        String email2 = "bobsemail2@email.com";

        userDAO.createUser(username, password1, email1);
        assertThrows(
                InUseException.class,
                () -> userDAO.createUser(username, password2, email2)
        );
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.AuthType.class)
    public void canCreateDuplicatePasswords(AuthentificationDAOTests.AuthType dataAccessType) throws InUseException, DataAccessException {
        UserDataAccess userDAO = getDataAccessType(dataAccessType);
        userDAO.clear();
        String username1 = "Bob";
        String password = "secret";
        String email1 = "bobsemail1@email.com";
        String username2 = "Sherry";
        String email2 = "bobsemail2@email.com";

        userDAO.createUser(username1, password, email1);
        assertDoesNotThrow(
                () -> userDAO.createUser(username2, password, email2)
        );
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.AuthType.class)
    public void canCreateDuplicateEmails(AuthentificationDAOTests.AuthType dataAccessType) throws InUseException, DataAccessException {
        UserDataAccess userDAO = getDataAccessType(dataAccessType);
        userDAO.clear();
        String username1 = "Bob";
        String password1 = "secret1";
        String email = "bobsemail@email.com";
        String username2 = "Sherry";
        String password2 = "secret";

        userDAO.createUser(username1, password1, email);
        assertDoesNotThrow(
                () -> userDAO.createUser(username2, password2, email)
        );
    }
}
