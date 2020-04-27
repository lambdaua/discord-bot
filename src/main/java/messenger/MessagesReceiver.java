package messenger;

import data.DiscordRestService;
import database.UserService;
import messenger.modules.*;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

public class MessagesReceiver extends ListenerAdapter {

    private List<Module> modules;
    private JoinModule joinModule;
    private RoleUpdateModule roleUpdateModule;


    public MessagesReceiver(Messenger messenger,
                            UserService userService,
                            DiscordRestService guildRestService,
                            String guildId,
                            String botToken) {

        this.modules = Arrays.asList(
                new InvitesModule(messenger, userService),
                new TopModule(messenger, userService)
        );

        this.joinModule = new JoinModule(messenger, guildRestService, userService, guildId, botToken);
        this.roleUpdateModule = new RoleUpdateModule(userService);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getAuthor().isBot())
            return;

        for (Module module : modules) {
            if (module.canHandle(event)) {
                module.handle(event);
                break;
            }
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        joinModule.handle();
    }

    @Override
    public void onReady(ReadyEvent event) {
        joinModule.handle();
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        User user = event.getUser();
        roleUpdateModule.handle(user);
    }
}
