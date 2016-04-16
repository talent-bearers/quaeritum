package eladkay.quaritum.client.core;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WireSegal
 *         Created at 3:51 PM on 4/16/16.
 */
public class ModelHandler {

    public interface IVariantHolder {
        ItemMeshDefinition getCustomMeshDefinition();

        String[] getVariants();
    }

    public interface IExtraVariantHolder extends IVariantHolder {
        String[] getExtraVariants();
    }

    public interface IModBlock extends IVariantHolder {
        Class<Enum> getVariantEnum();
        IProperty[] getIgnoredProperties();
        String getBareName();

        EnumRarity getBlockRarity(ItemStack stack);
    }

    public interface IColorProvider {
        IItemColor getColor();
    }

    private static String mod;

    public static List<IVariantHolder> variantCache = new ArrayList<>();

    public static Map<String, ModelResourceLocation> resourceLocations = new HashMap<>();

    public static void preInit(String modid) {
        mod = modid;
        variantCache.forEach(ModelHandler::registerModels);
    }

    public static void init() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        variantCache.stream()
                .filter(holder -> holder instanceof IColorProvider)
                .forEach(holder -> colors.registerItemColorHandler(((IColorProvider) holder).getColor(), ((Item) holder)));
    }

    // The following is a blatant copy of Psi's ModelHandler.

    private static void registerModels(IVariantHolder holder) {
        ItemMeshDefinition def = holder.getCustomMeshDefinition();
        if (def != null) {
            ModelLoader.setCustomMeshDefinition((Item) holder, def);
        } else {
            Item i = (Item) holder;
            registerModels(i, holder.getVariants(), false);
            if (holder instanceof IExtraVariantHolder) {
                registerModels(i, ((IExtraVariantHolder) holder).getExtraVariants(), true);
            }
        }

    }

    private static void registerModels(Item item, String[] variants, boolean extra) {
        if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof IModBlock) {
            ResourceLocation locName = Block.blockRegistry.getNameForObject(Block.getBlockFromItem(item));

            IModBlock i = (IModBlock) ((ItemBlock) item).getBlock();
            Class<Enum> name = i.getVariantEnum();
            IProperty[] loc = i.getIgnoredProperties();
            if (loc != null && loc.length > 0) {
                StateMap.Builder builder = new StateMap.Builder();
                for (IProperty p : loc) {
                    builder.ignore(p);
                }
                ModelLoader.setCustomStateMapper((Block) i, builder.build());
            }

            if (name != null) {
                registerVariantsDefaulted(locName.toString(), item, name, "variant");
                return;
            }
        }

        for (int var11 = 0; var11 < variants.length; var11++) {
            ModelResourceLocation var13 = new ModelResourceLocation(new ResourceLocation(mod, variants[var11]).toString(), "inventory");
            if (!extra) {
                ModelLoader.setCustomModelResourceLocation(item, var11, var13);
                resourceLocations.put(getKey(item, var11), var13);
            } else {
                ModelBakery.registerItemVariants(item, var13);
                resourceLocations.put(variants[var11], var13);
            }
        }

    }

    private static void registerVariantsDefaulted(String key, Item item, Class<Enum> enumclazz, String variantHeader) {
        if (enumclazz.getEnumConstants() != null)
            for (Object e : enumclazz.getEnumConstants()) {
                if (e instanceof IStringSerializable && e instanceof Enum) {
                    String variantName = variantHeader + "=" + ((IStringSerializable) e).getName();
                    ModelResourceLocation loc = new ModelResourceLocation(key, variantName);
                    int i = ((Enum) e).ordinal();
                    ModelLoader.setCustomModelResourceLocation(item, i, loc);
                    resourceLocations.put(getKey(item, i), loc);
                }
            }

    }

    private static String getKey(Item item, int meta) {
        return "i_" + item.getRegistryName() + "@" + meta;
    }
}
