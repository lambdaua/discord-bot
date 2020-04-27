package messenger.entities;

public abstract class Message {

    protected String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
