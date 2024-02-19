package org.amoseman.certificateauthority.auth;

public class UserBuilder {
    private String username;
    private boolean isExpired;
    private String[] roles;

    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
        return this;
    }

    public UserBuilder setRoles(String... roles) {
        this.roles = roles;
        return this;
    }

    public User build() {
        return new User(username, isExpired, roles);
    }
}
