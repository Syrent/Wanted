package ir.syrent.wanted.GUI.menu;

import ir.syrent.wanted.GUI.SGMenu;
import org.bukkit.entity.Player;

public class SGOpenMenu {

    private final SGMenu gui;
    private final Player player;

    public SGOpenMenu(SGMenu gui, Player player) {
        this.gui = gui;
        this.player = player;
    }

    public SGMenu getGUI() {
        return this.gui;
    }

    public Player getPlayer() {
        return this.player;
    }

}
