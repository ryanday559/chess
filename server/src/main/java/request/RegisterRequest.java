package request;

import com.google.gson.Gson;
import model.UserData;

public class RegisterRequest {
    UserData userData;

    public RegisterRequest(String request) {
        parseRequest(request);
    }

    private void parseRequest(String request) {
        userData = new Gson().fromJson(request, UserData.class);
    }

    public UserData getData() {
        return userData;
    }
}
