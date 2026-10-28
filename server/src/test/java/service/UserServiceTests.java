package service;

import dataaccess.DataAccessException;
import dataaccess.InUseException;
import dataaccess.UnauthorizedException;
import model.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private UserService userService = new UserService();

    public UserServiceTests() throws DataAccessException {
    }

    @Test
    public void testClear() throws InUseException, DataAccessException {
        String username = "Bob";
        String password = "pass";
        UserData user = new UserData(username, password, "email");
        userService.register(user);

        userService.clear();

        assertThrows(UnauthorizedException.class, ()->userService.login(user));
    }

    @Test
    public void testRegister() throws InUseException, DataAccessException {
        String username = "Bob";
        String password = "pass";
        UserData user = new UserData(username, password, "email");
        LoginResult result = userService.register(user);

        assertEquals(username, result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void testLogin() throws InUseException, DataAccessException, UnauthorizedException {
        String username = "Bob";
        String password = "pass";
        UserData user = new UserData(username, password, "email");
        userService.register(user);
        LoginResult result = userService.login(user);

        assertEquals(username, result.username());
        assertNotNull(result.authToken());
    }

    @ParameterizedTest
    @CsvSource({
            "bob, , ",
            ", password, ",
            ", , email@email.com"
    })
    public void testMissingFieldsRegister(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        assertThrows(DataAccessException.class, ()->userService.register(user));
    }

    @ParameterizedTest
    @CsvSource({
            "bob, ",
            ", password"
    })
    public void testMissingFieldsLogin(String username, String password) throws InUseException, DataAccessException, UnauthorizedException {
        UserData registerUserData = new UserData("bob", "password", "email");
        userService.register(registerUserData);
        UserData loginUserData = new UserData(username, password, "email");
        assertThrows(DataAccessException.class, ()-> userService.login(loginUserData));
    }

    @Test
    public void testLoginWithNoRegisteredUser() throws InUseException, DataAccessException, UnauthorizedException {
        String username = "Bob";
        String password = "pass";
        UserData user = new UserData(username, password, "email");
        assertThrows(UnauthorizedException.class, ()->userService.login(user));
    }

    @Test
    public void testIncorrectPassword() throws InUseException, DataAccessException, UnauthorizedException {
        String username = "Bob";
        String password = "pass";
        UserData registerUserData = new UserData(username, password, "email");
        userService.register(registerUserData);
        UserData loginUserData = new UserData(username, "WrongPassword", "email");
        assertThrows(UnauthorizedException.class, ()->userService.login(loginUserData));
    }
}
