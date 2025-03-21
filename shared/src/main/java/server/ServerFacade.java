package server;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import request.*;
import result.*;
import exception.ResponseException;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class);
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);
    }

    public CreateResult create(CreateRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateResult.class);
    }

    public ListResult list(ListRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, request, ListResult.class);
    }

    public JoinResult join(JoinRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, request, JoinResult.class);
    }

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, LogoutResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);

            // set the header with an auth token if there is one
            String classString = request.getClass().toString();
            if (classString.equals("class request.ListRequest") ||
                    classString.equals("class request.JoinRequest") ||
                    classString.equals("class request.LogoutRequest") ||
                    classString.equals("class request.CreateRequest")) {
                ListRequest tempList;
                JoinRequest tempJoin;
                LogoutRequest tempLog;

                switch (request.getClass().toString()) {
                    case "class request.ListRequest":
                        tempList = (ListRequest) request;
                        http.addRequestProperty("Authorization", tempList.authToken());
                        break;
                    case "class request.JoinRequest":
                        tempJoin = (JoinRequest) request;
                        http.addRequestProperty("Authorization", tempJoin.authToken());
                        break;
                    case "class request.LogoutRequest":
                        tempLog = (LogoutRequest) request;
                        http.addRequestProperty("Authorization", tempLog.authToken());
                        break;
                }
            }
            // only set the body if there is one
            if (!(classString.equals("class request.ListRequest") ||
                    classString.equals("class request.LogoutRequest"))) {
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes());
                }
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException("other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
