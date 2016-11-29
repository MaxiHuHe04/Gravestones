package me.maxihuhe04.Gravestones;


import me.maxihuhe04.Gravestones.util.OldBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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

            oldBlocks.forEach(b -> this.oldBlocks.add(new OldBlock(b)));
        }

        Inventory playerInventory;
        Player player;
        Block graveBlock;
        List<OldBlock> oldBlocks;
        List<Block> graveBlocks;

        /**
         * Removes the Grave
         */
        @Override
        public void remove() {
            oldBlocks.forEach(OldBlock::place);

            for(ItemStack item : playerInventory.getContents()) {
                if(item != null) {
                    Item drop = player.getWorld().dropItemNaturally(player.getLocation(), item);
                    drop.setVelocity(drop.getVelocity().zero());
                }
            }

            gravestones.remove(this);

            if(player.isOnline()) {
                player.sendMessage("§cDein Grab wurde zerstört.");
            }
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

        public List<OldBlock> getOldBlocks() {
            return oldBlocks;
        }

        public void open(Player p) {
            if(p.getName().equalsIgnoreCase(player.getName()) || p.hasPermission("gravestones.openEveryGrave")) {
                p.openInventory(playerInventory);
            } else {
                p.sendMessage("§cDieses Grab gehört nicht dir!");
            }
        }

    }


    //Listeners
    @EventHandler
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
                } else if (invSize < 54) {
                    graveSize = 54;
                }


                Inventory playerInv = Bukkit.createInventory(null, graveSize, p.getDisplayName());

                playerInv.addItem(p.getInventory().getContents());
                playerInv.addItem(p.getInventory().getArmorContents());

                ItemStack skull = new ItemStack(Material.SKULL_ITEM);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                skullMeta.setOwner(p.getName());
                playerInv.setItem(playerInv.getSize(), skull);


                List<Block> oldBlocks = new ArrayList<>();
                List<Block> newBlocks = new ArrayList<>();




                //Build the Grave

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

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        gravestones.forEach(gravestone -> gravestone.getGraveBlocks().stream().filter(b -> b.getLocation().equals(e.getBlock().getLocation())).forEach(block -> {
            e.setCancelled(true);
            if (e.getPlayer().getName().equalsIgnoreCase(gravestone.getPlayer().getName()) || e.getPlayer().hasPermission("gravestones.destroyEveryGrave")) {
                if (gravestone.getPlayer().isOnline()) {
                    gravestone.remove();
                    gravestone.getPlayer().sendMessage("§cDein Grab wurde zerstört.");
                }
            } else {
                e.getPlayer().sendMessage("§cDieses Grab gehört nicht dir!");
            }
        }));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            gravestones.forEach(gravestone -> {
                if(e.getClickedBlock().equals(gravestone.getGraveBlock())) {
                    gravestone.open(e.getPlayer());
                }
            });
        }
    }




}
