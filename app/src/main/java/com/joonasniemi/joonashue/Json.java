package com.joonasniemi.joonashue;

public class Json {
    private static final Json ourInstance = new Json();
    private String bridgeIp;
    private String username;
    private String jsonString;

    public static Json getInstance() {
        return ourInstance;
    }

    private Json() {
    }

    public String getBridgeIp() {
        return bridgeIp;
    }

    public void setBridgeIp(String bridgeIp) {
        this.bridgeIp = bridgeIp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
