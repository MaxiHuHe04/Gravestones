package me.maxihuhe04.Gravestones;

import me.maxihuhe04.Gravestones.util.OldBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface Gravestone {
    void remove();
    Inventory getInventory();
    Player getPlayer();
    Block getGraveBlock();
    List<Block> getGraveBlocks();
    List<OldBlock> getOldBlocks();
    void open(Player player);
}


