package me.maxihuhe04.Gravestones;


import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static me.maxihuhe04.Gravestones.Grave.gravestones;

@SuppressWarnings("unused")
public class GravestoneHandler implements Listener, CommandExecutor {

    //Listeners
    /**
     * Creates the grave
     * @param deathEvent Event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent deathEvent) {

        //Make sure that the Entity is a Player
        if (deathEvent.getEntityType().equals(EntityType.PLAYER)) {

            //Set the player
            Player p = deathEvent.getEntity();

            //Make sure that the Graves are enabled
            if (p.getWorld().getGameRuleValue("keepInventory").equalsIgnoreCase("true")) {

                //Create the Grave
                new Grave(p);

                {
                    int items = 0;
                    for (ItemStack item : p.getInventory()) {
                        if (item != null) {
                            items++;
                        }
                    }

                    if (items == 0) {
                        sendMSG(p, MSG.NO_ITEMS);
                    } else {
                        Location loc = p.getLocation();
                        int x = loc.getBlockX();
                        int y = loc.getBlockY();
                        int z = loc.getBlockZ();

                        sendMSG(p, MSG.GRAVE_INFO, x + ", " + y + ", " + z);
                    }
                }

                //Clear the Player's inventory
                p.getInventory().setArmorContents(null);
                p.getInventory().clear();
            }

        }

    }

    /**
     * Destroys the Grave when a Block of the Grave break
     * @param breakEvent Event
     */
    @EventHandler
    public void onBreakBlock(BlockBreakEvent breakEvent) {

        try {

            //Set the event block location as a variable
            Location eventLoc = breakEvent.getBlock().getLocation();

            //Loop through all Gravestones
            for (Grave grave : gravestones) {

                //Loop through the Locations
                for (Location loc : grave.getGraveLocs()) {

                    //Make sure that the Location is not null
                    if (loc != null) {

                        //Check if the Locations match
                        if (loc.equals(eventLoc)) {

                            //Make sure that the player has access to the Grave
                            if (breakEvent.getPlayer().getName().equalsIgnoreCase(grave.getPlayer().getName()) || breakEvent.getPlayer().hasPermission("gravestones.destroyEveryGrave")) {

                                //Destroy the grave
                                grave.destroy();


                            } else {
                                //Send a not belong message
                                sendMSG(breakEvent.getPlayer(), MSG.NOT_BELONG);
                                //Stop the loops
                                return;

                            }

                            //Cancel the event to prevent the block from breaking
                            breakEvent.setCancelled(true);
                            //Stop the loops
                            return;


                        }


                    }


                }


            }


        } catch (Exception e1) {

            e1.printStackTrace();

        }


    }

    /**
     * Opens the Grave when a Player clicks on the Grave
     * @param interactEvent Event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent interactEvent) {
        if(interactEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)) gravestones.forEach(grave -> grave.getGraveBlocks().stream().filter(b -> interactEvent.getClickedBlock().equals(b) && !interactEvent.getPlayer().isSneaking()).forEach(open -> {
            grave.open(interactEvent.getPlayer()); interactEvent.setCancelled(true);}));
    }

    /**
     * Destroys the grave when a grave inventory is empty
     * @param closeEvent Event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent closeEvent) {
        gravestones.stream().filter(grave -> grave.getInventory().getTitle().equalsIgnoreCase(closeEvent.getInventory().getTitle()) && closeEvent.getInventory().getSize() == grave.getInventory().getSize()).forEach(grave -> {
            for(ItemStack item : closeEvent.getInventory()) {
                if(item != null) return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(GravestonesMain.getInstance(), grave::destroy, 10);
        });
    }

    /**
     * Command: /graves [all]
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final String[] gravesMSG = new String[1];

        if((args.length < 1 || (args.length > 0 && !args[0].equalsIgnoreCase("all"))) && sender instanceof Player) {
            ArrayList<Grave> graves = new ArrayList<>();
            Grave.gravestones.stream().filter(grave -> grave.getPlayer().getName().equalsIgnoreCase(sender.getName())).forEach(graves::add);

            gravesMSG[0] = "§6Deine Gräber:\n";

            final int[] index = {1};

            graves.forEach(grave -> {
                Location loc = grave.getGraveBlock().getLocation();
                int x = loc.getBlockX();
                int y = loc.getBlockY();
                int z = loc.getBlockZ();
                gravesMSG[0] += "§6" + index[0] + ": §b§lWelt§b: " + loc.getWorld().getName() + ", §4§lKoordinaten§4: " + x + ", " + y + ", " + z + "§6\n";
                index[0]++;
            });

            if(!gravesMSG[0].contains(",")) {
                gravesMSG[0] = "§aDu hast glücklicherweise noch kein Grab!";
            }

        } else if ((args.length > 0 && args[0].equalsIgnoreCase("all")) || sender instanceof ConsoleCommandSender) {
            if(sender.hasPermission("gravestones.listAllGraves")) {
                gravesMSG[0] = "§6Gräber:\n";

                final int[] index = {1};

                Grave.gravestones.forEach(grave -> {
                    Location loc = grave.getGraveBlock().getLocation();
                    int x = loc.getBlockX();
                    int y = loc.getBlockY();
                    int z = loc.getBlockZ();
                    gravesMSG[0] += "§6" + index[0] + ": §c§l" + grave.getPlayer().getName() + "§c: §b§lWelt: §b" + loc.getWorld().getName() + ", §4§lKoordinaten§4: " + x + ", " + y + ", " + z + "§6\n";
                    index[0]++;
                });

                if(!gravesMSG[0].contains(",")) {
                    gravesMSG[0] = "§aEs gibt glücklicherweise noch kein Grab!";
                }
            } else {
                GravestoneHandler.sendMSG(sender, GravestoneHandler.MSG.NO_PERM);
            }
        }

        sender.sendMessage(gravesMSG[0]);


        return true;
    }

    /**
     * Sends a message to a Player
     * @param player The message will be send to this Player
     * @param type The message type
     */
    static void sendMSG(CommandSender player, MSG type, @Nullable String... coords) {
        if(player != null) {
            if(type.equals(MSG.GRAVE_INFO) && coords != null) {
                player.sendMessage(type.toString().replaceAll("%COORDS%", coords[0]));
                return;
            }
            player.sendMessage(type.toString());
        }
    }

    /**
     * The message types
     */
    enum MSG {

        NOT_BELONG("§cDieses Grab gehört nicht dir!"),
        GRAVE_DESTROYED("§cDein Grab wurde zerstört."),
        GRAVE_INFO("§4Du bist bei §l%COORDS% §4gestorben! §6Geh zum Grab und hol deine Items!"),
        NO_ITEMS("§6Du hattest keine Items bei dir. Deswegen wurde kein Grab erbaut."),
        NO_PERM("§cNanana!! Das darfst du nicht!!!");

        private String msg;

        MSG(String msg) {this.msg = msg;}

        @Override
        public String toString() {
            return this.msg;
        }

    }


}
