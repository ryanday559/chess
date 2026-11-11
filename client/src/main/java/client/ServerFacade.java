package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.GameJoinData;
import model.UserData;
import result.LoginResult;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private String serverUrl;

    public ServerFacade() {
        this.serverUrl = "http://localhost:8080";
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        handleResponse(response, LoginResult.class);
    }

    public LoginResult register(UserData user) throws ResponseException {
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public LoginResult login(UserData user) throws ResponseException {
        var request = buildRequest("POST", "/session", user);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void logout(String authToken) throws ResponseException {
        var request = buildRequestWithHeader("DELETE", "/session", null, "authorization", authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void createGame(String authToken, GameData gameData) throws ResponseException {
        var request = buildRequestWithHeader("POST", "/game", gameData, "authorization", authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        var request = buildRequestWithHeader("GET", "/game", null, "authorization", authToken);
        var response = sendRequest(request);
        var mappedResult = handleResponse(response, Map.class);
        return new Gson().fromJson(new Gson().toJson(mappedResult.get("games")), GameData[].class);
    }

    public void joinGame(String authToken, GameJoinData gameJoinData) throws ResponseException {
        var request = buildRequestWithHeader("PUT", "/game", gameJoinData, "authorization", authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest buildRequestWithHeader(String method, String path, Object body, String headerName, String headerValue) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .header(headerName, headerValue)
                .method(method, makeRequestBody(body));
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (ConnectException e) {
            throw new ResponseException("Error: Cannot connect to Chess Server!");
        }
        catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException("Error: Response Failure!");
        }
        if (responseClass != null) {
            var body = response.body();
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status == 200;
    }
}
