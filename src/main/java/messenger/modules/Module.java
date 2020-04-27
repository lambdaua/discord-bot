package messenger.modules;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Module {

    boolean canHandle(MessageReceivedEvent event);

    void handle(MessageReceivedEvent event);
}
