package dataaccess;

import dataaccess.memory.MemoryUserDataAccess;
import model.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDAOTests {
    private MemoryUserDataAccess userDAO = new MemoryUserDataAccess();

    @Test
    public void clearUserData() throws InUseException {
        userDAO.clear();
        String username = "Bob";
        String password = "secret";
        String email = "bobsemail@email.com";

        userDAO.createUser(username, password, email);
        userDAO.clear();

        assertNull(userDAO.getUser("Bob"));
    }

    @Test
    public void createGetUser() throws InUseException {
        userDAO.clear();
        String username = "Bob";
        String password = "secret";
        String email = "bobsemail@email.com";

        userDAO.createUser(username, password, email);
        UserData user = userDAO.getUser(username);

        assertEquals(username, user.username());
        assertEquals(password, user.password());
        assertEquals(email, user.email());
    }

    @Test
    public void cannotCreateDuplicateUsernames() throws InUseException {
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

    @Test
    public void canCreateDuplicatePasswords() throws InUseException {
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

    @Test
    public void canCreateDuplicateEmails() throws InUseException {
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
