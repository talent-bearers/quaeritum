package eladkay.quaritum.client.core;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WireSegal
 *         Created at 3:51 PM on 4/16/16.
 */
public class ModelHandler {

    public static String mod;
    public static String modlength;
    public static List<IVariantHolder> variantCache = new ArrayList<>();
    public static Map<String, ModelResourceLocation> resourceLocations = new HashMap<>();

    public static void preInit(String modid) {
        mod = modid;
        modlength = "";
        for (int i = 0; i < mod.length(); i++) modlength += " ";
        FMLLog.info(mod + " | Starting model load");
        variantCache.sort((o1, o2) -> variantToString(o1).compareTo(variantToString(o2)));
        variantCache.forEach(ModelHandler::registerModels);
    }

    private static int getVariantCount(IVariantHolder var) {
        return var instanceof ICustomLogHolder ? ((ICustomLogHolder) var).sortingPrecedence() : var.getVariants().length;
    }

    private static String variantToString(IVariantHolder var) {
        return ((char) (255 - getVariantCount(var))) + (var instanceof ItemBlock ? "b" : "I") + (var instanceof Block ? "" : ((Item) var).getRegistryName().getResourcePath());
    }

    public static void init() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        BlockColors blockcolors = Minecraft.getMinecraft().getBlockColors();
        for (IVariantHolder holder : variantCache) {
            if (holder instanceof IColorProvider && holder instanceof Item) {
                IItemColor color = ((IColorProvider) holder).getColor();
                if (color != null)
                    colors.registerItemColorHandler(color, ((Item) holder));
            }

            if (holder instanceof ItemBlock && ((ItemBlock) holder).block instanceof IBlockColorProvider) {
                IBlockColorProvider provider = (IBlockColorProvider) ((ItemBlock) holder).block;
                IBlockColor color = provider.getBlockColor();
                if (color != null)
                    blockcolors.registerBlockColorHandler(color, (Block) provider);
            } else if (holder instanceof IBlockColorProvider && holder instanceof Block) {
                blockcolors.registerBlockColorHandler(((IBlockColorProvider) holder).getBlockColor(), (Block) holder);
            }
        }
    }

    private static void registerModels(IVariantHolder holder) {
        if (!(holder instanceof Item)) return;
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
            ResourceLocation locName = Block.REGISTRY.getNameForObject(Block.getBlockFromItem(item));

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
            if (var11 == 0) {
                String print = modlength + " | Registering ";
                if (!variants[var11].equals(item.getRegistryName().getResourcePath()) || variants.length != 1)
                    print += "variant" + (variants.length == 1 ? "" : "s") + " of ";
                print += item instanceof ItemBlock ? "block" : "item";
                print += " " + item.getRegistryName().getResourcePath();
                FMLLog.info(print);
                if (item instanceof ICustomLogHolder)
                    FMLLog.info(((ICustomLogHolder) item).customLog());


            }
            if ((!variants[var11].equals(item.getRegistryName().getResourcePath()) || variants.length != 1)) {
                if (item instanceof ICustomLogHolder) {
                    if (((ICustomLogHolder) item).shouldLogForVariant(var11, variants[var11]))
                        FMLLog.info(((ICustomLogHolder) item).customLogVariant(var11 + 1, variants[var11]));
                } else
                    FMLLog.info(modlength + " |  Variant #" + Integer.toString(var11 + 1) + ": " + variants[var11]);
            }

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

                    if (((Enum) e).ordinal() == 0) {
                        String print = modlength + " | Registering ";
                        if (!variantName.equals(item.getRegistryName().getResourcePath()) || enumclazz.getEnumConstants().length != 1)
                            print += "variant" + (enumclazz.getEnumConstants().length == 1 ? "" : "s") + " of ";
                        print += item instanceof ItemBlock ? "block" : "item";
                        print += " " + item.getRegistryName().getResourcePath();
                        FMLLog.info(print);
                        if (item instanceof ICustomLogHolder)
                            FMLLog.info(((ICustomLogHolder) item).customLog());
                    }
                    if ((!((Enum) e).name().equals(item.getRegistryName().getResourcePath()) || enumclazz.getEnumConstants().length != 1)) {
                        if (item instanceof ICustomLogHolder) {
                            if (((ICustomLogHolder) item).shouldLogForVariant(((Enum) e).ordinal(), variantName))
                                FMLLog.info(((ICustomLogHolder) item).customLogVariant(((Enum) e).ordinal() + 1, variantName));
                        } else
                            FMLLog.info(modlength + " |  Variant #" + Integer.toString(((Enum) e).ordinal() + 1) + ": " + variantName);
                    }

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

    public interface IVariantHolder {
        @SideOnly(Side.CLIENT)
        @Nullable
        ItemMeshDefinition getCustomMeshDefinition();

        @Nonnull
        String[] getVariants();
    }

    public interface IExtraVariantHolder extends IVariantHolder {
        @Nonnull
        String[] getExtraVariants();
    }

    // The following is a blatant copy of Psi's ModelHandler.

    public interface IModBlock extends IVariantHolder {
        @Nullable
        Class<Enum> getVariantEnum();

        @Nonnull
        IProperty[] getIgnoredProperties();

        @Nonnull
        String getBareName();

        @Nonnull
        EnumRarity getBlockRarity(ItemStack stack);
    }

    public interface IColorProvider {
        @SideOnly(Side.CLIENT)
        @Nullable
        IItemColor getColor();
    }

    public interface IBlockColorProvider extends IColorProvider {
        @SideOnly(Side.CLIENT)
        @Nullable
        IBlockColor getBlockColor();

        @Override
        @Nullable
        default IItemColor getColor() { return null; }
    }

    public interface ICustomLogHolder extends IVariantHolder {
        String customLog();

        String customLogVariant(int variantID, String variant);

        boolean shouldLogForVariant(int variantID, String variant);

        int sortingPrecedence();
    }
}
