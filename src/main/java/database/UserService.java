package database;

import data.DiscordRestService;
import data.model.Role;
import database.model.DbUser;

import java.io.IOException;
import java.util.List;

public class UserService {

    private UserRepository repository;
    private List<Role> guildRoles;
    private DiscordRestService guildInvitesRest;

    public UserService(UserRepository repository, List<Role> guildRoles, DiscordRestService guildInvitesRest) {
        this.repository = repository;
        this.guildRoles = guildRoles;
        this.guildInvitesRest = guildInvitesRest;
    }

    public DbUser getUser(long userId) {
        return repository.get(userId);
    }

    public List<DbUser> getAll() {
        return repository.getAll();
    }

    public DbUser insert(long userId, String name) {
        DbUser dbUser = DbUser.create(userId, name);
        repository.insert(dbUser);
        return dbUser;
    }

    public void addInvite(long userId, String code, int inviteUses) {
        repository.addInvite(userId, code, inviteUses);
    }

    public void updateInvite(long userId, String code, int inviteUses) {
        repository.updateInvite(userId, code, inviteUses);
    }

    public void setRole(String guildId, String botToken, long userId, String roleName) {
        for (Role role : guildRoles) {
            String name = role.getName();
            String roleId = role.getId();

            if (name.equals(roleName)) {
                try {
                    guildInvitesRest.addRole(guildId, userId, roleId, botToken).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                repository.updateRole(userId, roleName);
                return;
            }
        }
    }

    public void removeRole(String guildId, String botToken, long userId, String roleName) {
        for (Role role : guildRoles) {
            if (role.getName().equals(roleName)) {
                guildInvitesRest.removeRole(guildId, userId, role.getId(), botToken);
            }
        }
    }
}
