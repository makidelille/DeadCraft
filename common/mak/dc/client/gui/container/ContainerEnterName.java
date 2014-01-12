package mak.dc.client.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ContainerEnterName extends ContainerDeadCraft {

    private ItemStack is;
    private InventoryPlayer invplayer;

    public ContainerEnterName (InventoryPlayer invPlayer, ItemStack itemStack) {
        super(invPlayer, null);
        this.invplayer = invPlayer;
        this.is = itemStack;

        for (int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(invPlayer, x, 8 + (18 * x), 142));

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 9; x++)
                addSlotToContainer(new Slot(invPlayer, x + (y * 9) + 9, 8 + (18 * x), 84 + (y * 18)));

    }

    public void setName(String s) {
        if(!invplayer.player.worldObj.isRemote) {
            NBTTagCompound tag = is.getTagCompound();
            if (tag != null) {
                tag.setString("lastEntry", s); //TODO ??
                is.setTagCompound(tag);
            }
            
        }
    }


}
