public abstract class User {
    private String userID;
    private String name;
    private String email;
    private String phone;
    private String password;

    public User(String userID, String name, String email, String phone, String password) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
}