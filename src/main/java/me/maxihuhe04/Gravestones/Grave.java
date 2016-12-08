package me.maxihuhe04.Gravestones;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * The Grave Class
 */

public class Grave {

    /**
     * List of the Graves
     */
    static ArrayList<Grave> gravestones = new ArrayList<>();

    //Variables
    private Inventory inventory;
    private Player p;
    private Block graveBlock;
    private List<OldBlock> oldBlocks;
    private List<Block> graveBlocks;

    /**
     * Constructor
     *
     * @param player Dead player
     */
    Grave(Player player) {
        this.p = player;

        place();
    }


    /**
     * Place the grave
     */
    @SuppressWarnings("deprecation")
    private void place() {


        //Inventory size
        final int[] invSize = {0};

        p.getInventory().forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() != Material.AIR) invSize[0]++;
        });

        //Don't place the grave when the inventory is empty
        if (invSize[0] == 0) return;

        int graveSize = 9;


        if (invSize[0] > 8) {
            graveSize = 18;
        }
        if (invSize[0] > 17) {
            graveSize = 27;
        }
        if (invSize[0] > 26) {
            graveSize = 36;
        }
        if (invSize[0] > 35) {
            graveSize = 45;
        }
        if (invSize[0] > 44) {
            graveSize = 54;
        }

        //Create the grave inventory
        Inventory playerInv = Bukkit.createInventory(null, graveSize, "§cGrab von §4" + p.getDisplayName());

        //Add the items to the inventory
        p.getInventory().forEach(item -> {
            if (item != null) playerInv.addItem(item);
        });

        //Create the player skull
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.setDisplayName("§cKopf von §4" + p.getName());

        skullMeta.setOwner(p.getName());

        skull.setItemMeta(skullMeta);

        //Add the player skull to the last slot of the grave inventory
        playerInv.setItem(playerInv.getSize() - 1, skull);

        //Variables
        List<Block> tempOldBlocks = new ArrayList<>();

        List<OldBlock> oldBlocks = new ArrayList<>();

        List<Block> newBlocks = new ArrayList<>();


        final boolean[] occupied = {false};
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(p.getLocation().clone().add(-1, 0, 0));
        locations.add(p.getLocation().clone().add(-1, -1, 0));
        locations.add(p.getLocation().clone().add(-1, -1, 0).add(-1, 0, 0));
        locations.add(p.getLocation().clone().add(-1, 0, -1));
        locations.add(p.getLocation().clone().add(-1, 0, -1).add(0, 0, 2));
        locations.add(p.getLocation().clone().add(-1,-1,0).add(0, -1, 0));
        locations.add(p.getLocation().clone().add(-1,-1,0).add(-1,0,0).add(0, -1, 0));
        locations.add(p.getLocation().clone().add(0, -1, 0));
        locations.forEach(blockLoc -> gravestones.forEach(grave -> grave.getGraveLocs().stream().filter(location -> locEqual(location, blockLoc)).forEach(location -> occupied[0] = true)));


        boolean x = new Random().nextBoolean();
        boolean z = new Random().nextBoolean();
        int y = new Random().nextInt(2);
        final Location loc = occupied[0] ?p.getLocation().add(x?2:-2,3+y,z?-2:2):p.getLocation();


        //Build the Grave
        Location signLoc = loc.clone().add(-1, 0, 0);
        Location gravelLoc = loc.clone().add(-1, -1, 0);
        Location gravelLoc2 = loc.clone().add(-1, -1, 0).add(-1, 0, 0);
        Location roseLoc = loc.clone().add(-1, 0, -1);
        Location dandLoc = loc.clone().add(-1, 0, -1).add(0, 0, 2);
        Location bedLoc1 = gravelLoc.clone().add(0, -1, 0);
        Location bedLoc2 = gravelLoc2.clone().add(0, -1, 0);
        Location bedLoc3 = loc.clone().add(0, -1, 0);

        tempOldBlocks.add(loc.getBlock());
        tempOldBlocks.add(signLoc.getBlock());
        tempOldBlocks.add(gravelLoc.getBlock());
        tempOldBlocks.add(gravelLoc2.getBlock());
        tempOldBlocks.add(roseLoc.getBlock());
        tempOldBlocks.add(dandLoc.getBlock());
        tempOldBlocks.add(bedLoc1.getBlock());
        tempOldBlocks.add(bedLoc2.getBlock());
        tempOldBlocks.add(bedLoc3.getBlock());

        tempOldBlocks.stream().filter(Objects::nonNull).forEach(block -> oldBlocks.add(new OldBlock(block)));

        loc.getBlock().setType(Material.COBBLESTONE_STAIRS, false);
        loc.getBlock().setData((byte) 1);


        signLoc.getBlock().setType(Material.WALL_SIGN, false);
        signLoc.getBlock().setData((byte) 4);


        if (signLoc.getBlock().getState() instanceof Sign) {
            Sign ripSign = (Sign) signLoc.getBlock().getState();

            if (ripSign != null) {
                ripSign.setLine(1, "R.I.P.");
                ripSign.setLine(2, p.getDisplayName());
                ripSign.update();
            }
        }


        if (
                !gravelLoc.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR) &&
                        !gravelLoc2.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR) &&
                        !roseLoc.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR) &&
                        !dandLoc.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR)
                ) {


            gravelLoc.getBlock().setType(Material.GRAVEL, false);
            gravelLoc2.getBlock().setType(Material.GRAVEL, false);


            roseLoc.getBlock().setType(Material.YELLOW_FLOWER, false);
            dandLoc.getBlock().setType(Material.RED_ROSE, false);


            newBlocks.add(roseLoc.getBlock());
            newBlocks.add(dandLoc.getBlock());

        } else {
            gravelLoc.getBlock().setType(Material.MOSSY_COBBLESTONE, false);
            gravelLoc2.getBlock().setType(Material.COBBLESTONE, false);
        }

        bedLoc1.getBlock().setType(Material.BEDROCK, false);
        bedLoc2.getBlock().setType(Material.BEDROCK, false);
        bedLoc3.getBlock().setType(Material.BEDROCK, false);


        newBlocks.add(loc.getBlock());
        newBlocks.add(signLoc.getBlock());
        newBlocks.add(gravelLoc.getBlock());
        newBlocks.add(gravelLoc2.getBlock());
        newBlocks.add(bedLoc1.getBlock());
        newBlocks.add(bedLoc2.getBlock());
        newBlocks.add(bedLoc3.getBlock());


        this.graveBlock = loc.getBlock();
        this.graveBlocks = newBlocks;
        this.inventory = playerInv;
        this.oldBlocks = oldBlocks;

        //Register the grave
        gravestones.add(this);

    }


    /**
     * Destroy the Grave
     */
    void destroy() {

        //Destroy the Sign to prevent it for dropping
        this.getGraveBlocks().stream().filter(block -> block.getType().equals(Material.WALL_SIGN)).forEach(block -> block.setType(Material.AIR, false));

        //Destroy the Grave
        this.getGraveBlocks().forEach(block -> block.setType(Material.AIR, false));

        //Set the old Blocks
        this.oldBlocks.forEach(OldBlock::place);

        //Drop the items
        dropItems(graveBlock.getLocation());

        //Send a message to the Grave Player that the Grave was removed
        GravestoneHandler.sendMSG(p, GravestoneHandler.MSG.GRAVE_DESTROYED);

        //Unregister the grave
        gravestones.remove(this);

    }


    /**
     * Open the Grave Inventory
     *
     * @param p Player where the Inventory should open
     */
    void open(Player p) {
        if (p.getName().equalsIgnoreCase(this.p.getName()) || p.hasPermission("gravestones.openEveryGrave")) {
            p.openInventory(inventory);
        } else {
            GravestoneHandler.sendMSG(p, GravestoneHandler.MSG.NOT_BELONG);
        }
    }


    /**
     * Drop the Grave's Inventory
     *
     * @param dropLocation Location where the Items should drop
     */
    private void dropItems(Location dropLocation) {
        //Loop through the Items of the Grave
        for (ItemStack item : this.inventory.getContents()) {

            //Make sure that the Item exists
            if (item != null) {

                //Drop the item
                p.getWorld().dropItemNaturally(dropLocation, item);
            }

        }

    }


    /**
     * Returns the Player's Inventory
     *
     * @return Inventory
     */
    Inventory getInventory() {
        return inventory;
    }


    /**
     * @return The Player
     */
    Player getPlayer() {
        return p;
    }


    /**
     * @return The main Block
     */
    Block getGraveBlock() {
        return graveBlock;
    }


    /**
     * @return The Grave Blocks
     */
    List<Block> getGraveBlocks() {
        return graveBlocks;
    }


    /**
     * @return The old Blocks
     */
    public List<OldBlock> getOldBlocks() {
        return oldBlocks;
    }

    /**
     * @param location First Location
     * @param anotherLocation Second Location
     * @return True if both Locations are equal
     */
    private static boolean locEqual(Location location, Location anotherLocation) {
        return !(location == null || anotherLocation == null) && location.getBlockX() == anotherLocation.getBlockX() && location.getBlockY() == anotherLocation.getBlockY() && location.getBlockZ() == anotherLocation.getBlockZ();
    }


    /**
     * @return The Grave Block Locations
     */
    List<Location> getGraveLocs() {
        List<Location> list = new ArrayList<>();
        graveBlocks.forEach(b -> list.add(b.getLocation()));
        return list;
    }


    /**
     * OldBlock class
     */
    @SuppressWarnings("deprecation")
    public static class OldBlock {
        //Variables
        private Material material;
        private byte data;
        private MaterialData materialData;
        private BlockState state;
        private Location location;

        /**
         * Constructor for the OldBlock object.
         * Saves a Block and its current state to an object.
         *
         * @param block Block to save to the object
         */
        OldBlock(Block block) {
            this.material = block.getType();
            this.data = block.getData();
            this.materialData = block.getState().getData();
            this.location = block.getLocation();
        }

        /**
         * Place back the Block with the right state.
         */
        void place() {
            //Make sure the Location and Material are not null.
            if (this.location == null || this.material == null) {
                return;
            }

            //Place the Block and set the data.
            Block block = this.location.getBlock();
            block.setType(material, false);
            block.setData(data);


            //If the Block had MaterialData, set it.
            try {
                if (this.materialData != null) {
                    block.getState().setData(materialData);
                }
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }


        }
    }
}
