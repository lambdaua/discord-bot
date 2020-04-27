package messenger.modules;

import database.UserService;
import database.model.DbUser;
import data.model.Invite;
import messenger.Messenger;
import messenger.entities.TextMessage;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Collections;

import java.util.List;

public class TopModule extends BaseModule {

    private UserService userService;

    public TopModule(Messenger messenger, UserService userService) {
        super(messenger);
        this.userService = userService;
    }

    @Override
    public boolean canHandle(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        return message.equals("!top10");
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();

        StringBuilder message = new StringBuilder();

        List<DbUser> allUsers = userService.getAll();
        if (allUsers == null || allUsers.size() == 0) {
            messenger.sendMessage(channel, new TextMessage("Nobody made invites yet"));
            return;
        }

        allUsers.sort((o1, o2) -> {
            int count1 = Collections.count(o2.getInvites(), Invite::getUses);
            int count2 = Collections.count(o1.getInvites(), Invite::getUses);
            return Integer.compare(count1, count2);
        });

        allUsers = Collections.safeSubList(allUsers, 10);

        for (int i = 0; i < allUsers.size(); i++) {
            DbUser user = allUsers.get(i);
            message.append(String.format("%d. %s\n", i + 1, user.getName()));
        }

        messenger.sendMessage(channel, new TextMessage(message.toString()));
    }
}
