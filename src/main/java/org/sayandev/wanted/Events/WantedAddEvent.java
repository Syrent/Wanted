package org.sayandev.wanted.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WantedAddEvent extends Event {

    private final Player setter;
    private final Player target;
    private final Integer amount;

    public WantedAddEvent(Player setter, Player target, Integer amount) {
        this.setter = setter;
        this.target = target;
        this.amount = amount;
    }

    public Player getSetter() {
        return setter;
    }

    public Player getTarget() {
        return target;
    }

    public Integer getAmount() {
        return amount;
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
