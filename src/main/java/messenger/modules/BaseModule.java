package messenger.modules;

import messenger.Messenger;

public abstract class BaseModule implements Module {

    protected Messenger messenger;

    public BaseModule(Messenger messenger) {
        this.messenger = messenger;
    }
}
