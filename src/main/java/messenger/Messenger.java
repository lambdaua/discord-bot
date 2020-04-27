package messenger;

import messenger.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public interface Messenger {
    void sendMessage(MessageChannel channel, Message message);
}
