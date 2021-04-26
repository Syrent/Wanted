package ir.syrent.wanted.Core;

import org.bukkit.entity.Player;

public class WPlayer {

    private Player Killer = null;
    private Player Victim = null;

    public Player getKiller() {
        return Killer;
    }

    public void setKiller(Player killer) {
        Killer = killer;
    }

    public Player getVictim() {
        return Victim;
    }

    public void setVictim(Player victim) {
        Victim = victim;
    }

}
