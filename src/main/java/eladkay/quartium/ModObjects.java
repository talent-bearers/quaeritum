package eladkay.quartium;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModObjects {
    public static boolean registerTE(IModObject tileEntity) {
        if(!(tileEntity instanceof TileEntity)) return false;
        GameRegistry.registerTileEntity((Class<? extends TileEntity>) tileEntity.getClass(), tileEntity.getName());
        return true;
    }
    public static boolean registerBlock(IModObject block) {
        if(!(block instanceof Block)) return false;
        GameRegistry.register(new ItemBlock((Block) block).setRegistryName(new ResourceLocation(References.MODID, block.getName())));
        return true;
    }
}
