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

    public static boolean registerBlock(IModObject block, boolean client) {
        if(!(block instanceof Block)) return false;
        GameRegistry.register((Block) block, new ResourceLocation(References.MODID + ":" + block.getName()));
        GameRegistry.register(new ItemBlock((Block) block).setRegistryName(block.getName()), new ResourceLocation(References.MODID, block.getName()));
        if (client) registerBlockTextures(block);
        return true;
    }

    public static void registerBlockAlt(Block block, String name) {
        GameRegistry.register(block.setRegistryName(name));
    }

    // The following is a blatant copy of NaturalPledge's ModelHandler, which is a blatant copy of Psi's ModelHandler.
    // Thanks to wiresegal for porting that thing's over for me from Kotlin. I can't read kotlin!
    public static boolean registerBlockTextures(IModObject block) {
        if (!(block instanceof Block)) return false;
        return ModelHandler
    }

}
