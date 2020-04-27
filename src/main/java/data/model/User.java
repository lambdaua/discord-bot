package data.model;

public class User {
    private String username;
    private String discriminator;
    private String id;
    private String avatar;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }
}
