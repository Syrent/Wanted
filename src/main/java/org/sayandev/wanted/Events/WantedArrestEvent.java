package org.sayandev.wanted.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WantedArrestEvent extends Event {

    private final Player by;
    private final Player target;

    public WantedArrestEvent(Player by, Player target) {
        this.by = by;
        this.target = target;
    }

    public Player getBy() {
        return by;
    }

    public Player getTarget() {
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
