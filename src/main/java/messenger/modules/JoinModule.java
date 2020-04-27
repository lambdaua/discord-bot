package messenger.modules;

import data.DiscordRestService;
import data.model.Invite;
import data.model.User;
import database.UserService;
import database.model.DbUser;
import dictionary.Role;
import messenger.Messenger;
import utils.Collections;

import java.io.IOException;
import java.util.List;

public class JoinModule {

    public static final String BOT = "Bot ";

    private Messenger messenger;
    private DiscordRestService guildInvitesRest;
    private UserService userService;
    private String guildId;
    private String botToken;

    public JoinModule(Messenger messenger, DiscordRestService guildInvitesRest, UserService userService,
                      String guildId, String botToken) {
        this.messenger = messenger;
        this.userService = userService;
        this.botToken = botToken;
        this.guildId = guildId;
        this.guildInvitesRest = guildInvitesRest;
    }

    public void handle() {
        try {
            List<Invite> invites = guildInvitesRest.getGuildInvites(guildId, BOT + botToken).execute().body();

            if (invites == null) return;

            addOrUpdateInvites(invites);
            checkRolesToSet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addOrUpdateInvites(List<Invite> invites) {
        for (Invite invite : invites) {
            String inviteCode = invite.getCode();
            User inviter = invite.getInviter();

            String userName = inviter.getUsername();
            long userId = Long.parseLong(inviter.getId());

            int inviteUses = invite.getUses();

            DbUser dbUser = userService.getUser(userId);
            if (dbUser == null) {
                dbUser = userService.insert(userId, userName);
            }

            List<Invite> dbInvites = dbUser.getInvites();

            //TODO: REDO
            if (dbInvites.stream().anyMatch(in -> in.getCode().equals(inviteCode))) {
                userService.updateInvite(userId, inviteCode, inviteUses);
                continue;
            }

            userService.addInvite(userId, inviteCode, inviteUses);
        }
    }

    private void checkRolesToSet() {
        List<DbUser> allUsers = userService.getAll();

        for (DbUser dbUser : allUsers) {
            String dbRoleName = dbUser.getRole();
            long dbUserId = dbUser.getUserId();

            List<Invite> invites = dbUser.getInvites();
            int invitesNumber = Collections.count(invites, Invite::getUses);

            Role dictionaryRole = Role.forInvites(invitesNumber);
            Role dbRole = Role.from(dbRoleName);

            if (dbRole != dictionaryRole) {
                String roleName = dictionaryRole.getName();
                int invitesForRole = dictionaryRole.getCount();

                if (invitesNumber >= invitesForRole) {
                    userService.removeRole(guildId, BOT + botToken, dbUserId, dbRoleName);
                    userService.setRole(guildId, BOT + botToken, dbUserId, roleName);
                    return;
                }

            }
        }
    }
}
