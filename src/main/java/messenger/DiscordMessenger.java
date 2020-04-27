package messenger;

import messenger.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class DiscordMessenger implements Messenger{
    @Override
    public void sendMessage(MessageChannel channel, Message message) {
        channel.sendMessage(message.getMessage()).queue();
    }
}
