package data.model;

import java.time.OffsetDateTime;

public class Invite {
    private String code;
    private Channel channel;
    private boolean expanded;
    private Guild guild;
    private User inviter;
    private int maxAge;
    private int maxUses;
    private boolean temporary;
    private OffsetDateTime timeCreated;
    private int uses;

    public Invite(String code, int uses) {
        this.code = code;
        this.uses = uses;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public Guild getGuild() {
        return guild;
    }

    public User getInviter() {
        return inviter;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public OffsetDateTime getTimeCreated() {
        return timeCreated;
    }

    public Invite() {
    }

    public String getCode() {
        return code;
    }

    public int getUses() {
        return uses;
    }
}
