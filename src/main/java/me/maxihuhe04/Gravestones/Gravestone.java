package me.maxihuhe04.Gravestones;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Gravestone {
    void remove();
    Inventory getInventory();
    Player getPlayer();
    Block getGraveBlock();
}


