public class Users {
    private String username;
    private String password;
    private String roles;
    // Constructor, getters, setters

    public Users(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}

class Admin extends Users {
    public Admin(String username, String password, String roles) {
        super(username, password, roles);
    }
}

class Manager extends Users {
    public Manager(String username, String password, String roles) {
        super(username, password, roles);
    }
}
