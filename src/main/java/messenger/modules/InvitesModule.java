package messenger.modules;

import data.model.Invite;
import database.UserService;
import database.model.DbUser;
import dictionary.Role;
import messenger.Messenger;
import messenger.entities.TextMessage;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Collections;

import java.util.List;

public class InvitesModule extends BaseModule {

    private UserService userService;

    public InvitesModule(Messenger messenger, UserService userService) {
        super(messenger);
        this.userService = userService;
    }

    @Override
    public boolean canHandle(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        return message.equals("!invites");
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();

        User author = event.getMessage().getAuthor();
        String name = author.getName();
        long userId = author.getIdLong();

        DbUser dbUser = userService.getUser(userId);

        if (dbUser == null) {
            messenger.sendMessage(channel, new TextMessage("You don't have any invites yet"));
            return;
        }

        List<Invite> invites = dbUser.getInvites();
        int userInvites = Collections.count(invites, Invite::getUses);

        Role role = Role.forInvites(userInvites);

        Role nextRole = role.getNextRole();
        if (nextRole != null) {
            int leftToNext = nextRole.getCount() - userInvites;
            messenger.sendMessage(channel, new TextMessage(String.format("%s\n You have %d invites\n %d more until you become a %s member.",
                    name, userInvites, leftToNext, nextRole.getName())));
        } else {
            messenger.sendMessage(channel, new TextMessage(String.format("%s\n You have %d invites", name, userInvites)));
        }
    }
}
