package me.maxihuhe04.Gravestones.util;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public class OldBlock {
    private Material material;
    private byte data;
    private MaterialData materialData;
    private Location location;

    /**
     * Constructor for the OldBlock object.
     * Saves a Block and its current state to an object.
     *
     * @param block Block to save to the object
     */
    public OldBlock(Block block) {
        this.material = block.getType();
        this.data = block.getData();
        this.materialData = block.getState().getData();
        this.location = block.getLocation();
    }

    /**
     * Place back the Block with the right state.
     */
    public void place() {
        //Make sure the Location and Material are not null.
        if (this.location == null || this.material == null) {
            return;
        }

        //Place the Block and set the data.
        Block block = this.location.getBlock();
        block.setType(material);
        block.setData(data);

        //If the Block had MaterialData, set it.
        if (this.materialData != null) {
            block.getState().setData(materialData);
        }
    }

}
