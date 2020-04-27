package database.model;

import data.model.Invite;
import database.UserRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DbUser {
    private long userId;
    private String name;
    private List<Invite> invites;
    private String role;

    public DbUser(long userId, String name, String role, List<Invite> invites) {
        this.userId = userId;
        this.name = name;
        this.invites = invites;
        this.role = role;

    }

    public String getRole() {
        return role;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public static DbUser from(Document document) {
        if (document == null) return null;
        long userId = (long) document.get(UserRepository.USER_ID);
        String name = (String) document.get(UserRepository.NAME);
        String role = (String) document.get(UserRepository.ROLE);

        List<Document> dbInvites = (List<Document>) document.getOrDefault(UserRepository.INVITES, new ArrayList<Document>());

        ArrayList<Invite> invites = new ArrayList<>(dbInvites.size());

        for (Document dbInvite : dbInvites) {
            String code = (String) dbInvite.getOrDefault(UserRepository.INVITE_CODE, "");
            int uses = (int) dbInvite.getOrDefault(UserRepository.INVITE_USES, 0);

            Invite invite = new Invite(code, uses);

            invites.add(invite);
        }

        return new DbUser(userId, name, role, invites);
    }

    public static DbUser create(long userId, String name) {
        return new DbUser(userId, name, "", new ArrayList<>());
    }
}
