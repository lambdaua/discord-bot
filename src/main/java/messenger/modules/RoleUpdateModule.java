package messenger.modules;

import database.UserService;
import net.dv8tion.jda.core.entities.User;

public class RoleUpdateModule {

    private UserService userService;

    public RoleUpdateModule(UserService userService) {
        this.userService = userService;
    }

    public void handle(User user){
        long userId = user.getIdLong();
    }
}
