package eladkay.quartium.util;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IVariantHolder {

    public String[] getVariants();

    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getCustomMeshDefinition();

}