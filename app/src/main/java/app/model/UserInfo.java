package app.model;

public class UserInfo {
    private static final UserInfo ourInstance = new UserInfo();
    private String name;
    private String token;
    private String email;
    private String avatar;
    private int  loginStatus;
    public static UserInfo getInstance() {
        return ourInstance;
    }

    private UserInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avater) {
        this.avatar = avater;
    }

    public String getAvater() {
        return this.avatar;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
