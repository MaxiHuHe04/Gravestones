package me.maxihuhe04.Gravestones;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GravestoneHandler implements Listener {

    protected List<Gravestone> gravestones = new ArrayList<>();

    public class GravestoneClass implements Gravestone {

        public GravestoneClass(Inventory inv, Player player, Block graveBlock, List<Block> oldBlocks, List<Block> graveBlocks) {
            this.playerInventory = inv;
            this.player = player;
            this.graveBlock = graveBlock;
            this.graveBlocks = graveBlocks;
            this.oldBlocks = oldBlocks;
        }

        Inventory playerInventory;
        Player player;
        Block graveBlock;
        List<Block> oldBlocks;
        List<Block> graveBlocks;

        /**
         * Removes the Grave
         */
        @Override
        public void remove() {

            for(Block b : oldBlocks) {
                Block b1 = b.getLocation().getBlock();

                //FIXME Block replacenpl
            }




            gravestones.remove(this);
        }

        /**
         * Returns the Player's Inventory
         * @return Inventory
         */
        @Override
        public Inventory getInventory() {
            return playerInventory;
        }

        /**
         * Returns the Player
         * @return Player
         */
        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public Block getGraveBlock() {
            return graveBlock;
        }

        public List<Block> getGraveBlocks() {
            return graveBlocks;
        }

        public List<Block> getOldBlocks() {
            return oldBlocks;
        }



        @Override
        public String toString() {
            return "Gravestone{" +
                    "playerInventory=" + playerInventory +
                    ", player=" + player +
                    ", graveBlock=" + graveBlock +
                    ", oldBlocks=" + oldBlocks +
                    ", graveBlocks=" + graveBlocks +
                    '}';
        }

    }


    public void onDeath(PlayerDeathEvent deathEvent) {
        if(deathEvent.getEntityType().equals(EntityType.PLAYER)) {
            Player p = deathEvent.getEntity();
            if(p.getWorld().getGameRuleValue("keepInventory").equalsIgnoreCase("true")) {


                int invSize = p.getInventory().getSize();

                int graveSize = 9;

                if(invSize < 9) {
                    graveSize = 9;
                } else if (invSize < 18) {
                    graveSize = 18;
                } else if (invSize < 27) {
                    graveSize = 27;
                } else if (invSize < 36) {
                    graveSize = 36;
                } else if (invSize < 45) {
                    graveSize = 45;
                } else if (invSize < 64) {
                    graveSize = 64;
                }

                Inventory playerInv = Bukkit.createInventory(null, graveSize, p.getDisplayName());

                playerInv.addItem(p.getInventory().getContents());
                playerInv.addItem(p.getInventory().getArmorContents());


                List<Block> oldBlocks = new ArrayList<>();
                List<Block> newBlocks = new ArrayList<>();



                /**
                 * Build the Grave
                 */

                final Location loc = p.getLocation();


                oldBlocks.add(loc.getBlock());
                loc.getBlock().setType(Material.COBBLESTONE_STAIRS, false);
                newBlocks.add(loc.getBlock());

                oldBlocks.add(loc.add(1,0,0).getBlock());
                loc.add(1,0,0).getBlock().setType(Material.SIGN, false);
                newBlocks.add(loc.add(1,0,0).getBlock());


                ((Sign) loc.add(1,0,0).getBlock().getState()).setLine(2, "R.I.P.");
                ((Sign) loc.add(1,0,0).getBlock().getState()).setLine(3, p.getDisplayName());


                oldBlocks.add(loc.add(1,-1,0).getBlock());
                loc.add(1,-1,0).getBlock().setType(Material.GRAVEL, false);
                newBlocks.add(loc.add(1,-1,0).getBlock());

                oldBlocks.add(loc.add(2,-1,0).getBlock());
                loc.add(2,-1,0).getBlock().setType(Material.GRAVEL, false);
                newBlocks.add(loc.add(2,-1,0).getBlock());

                oldBlocks.add(loc.add(1,0,1).getBlock());
                loc.add(1,0,1).getBlock().setType(Material.YELLOW_FLOWER);
                newBlocks.add(loc.add(1,0,1).getBlock());

                oldBlocks.add(loc.add(1,0,-1).getBlock());
                loc.add(1,0,-1).getBlock().setType(Material.RED_ROSE);
                newBlocks.add(loc.add(1,0,-1).getBlock());




                Gravestone grave = new GravestoneClass(playerInv, p, loc.getBlock(), oldBlocks, newBlocks);



                gravestones.add(grave);
            }
        }
    }


}
