package org.sayandev.wanted.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WantedClearEvent extends Event {

    private final Player setter;
    private final String target;

    public WantedClearEvent(Player setter, String target) {
        this.setter = setter;
        this.target = target;
    }

    public Player getSetter() {
        return setter;
    }

    public String getTarget() {
        return target;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
