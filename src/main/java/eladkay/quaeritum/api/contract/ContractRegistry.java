package eladkay.quaeritum.api.contract;

import com.google.common.collect.ImmutableList;
import eladkay.quaeritum.api.misc.RegistryEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static com.teamwizardry.librarianlib.features.helpers.CommonUtilMethods.getCurrentModId;

/**
 * @author WireSegal
 *         Created at 9:19 PM on 2/10/17.
 */
public final class ContractRegistry {
    private static final RegistryNamespaced<ResourceLocation, IContractOath> oaths = new RegistryNamespaced<>();
    private static int lastId = 0;

    public static int getMaxId() {
        return lastId;
    }

    @NotNull
    private static ResourceLocation getKey(@NotNull String id) {
        return new ResourceLocation(getCurrentModId(), id);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull ResourceLocation id, @NotNull IContractOath oath) {
        oaths.register(lastId++, id, oath);
        return oath;
    }

    @NotNull
    public static IContractOath registerOath(@NotNull String id, @NotNull IContractOath oath) {
        return registerOath(getKey(id), oath);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull String id, @NotNull String name,
                                             @NotNull Predicate<EntityPlayer> unlocked,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire,
                                             @NotNull String... lines) {
        return registerOath(getKey(id), name, unlocked, fire, lines);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull String id, @NotNull String name,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire,
                                             @NotNull String... lines) {
        return registerOath(getKey(id), name, fire, lines);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull ResourceLocation id, @NotNull String name,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire,
                                             @NotNull String... lines) {
        return registerOath(id, name, (player) -> true, fire, lines);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull ResourceLocation id, @NotNull String name,
                                             @NotNull Predicate<EntityPlayer> unlocked,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire,
                                             @NotNull String... lines) {
        List<String> listOf = ImmutableList.copyOf(lines);
        return registerOath(id, new IContractOath() {
            @NotNull
            @Override
            public List<String> getUnlocText(@NotNull ItemStack stack) {
                return listOf;
            }

            @NotNull
            @Override
            public String getUnlocName(@NotNull ItemStack stack) {
                return name;
            }

            @Override
            public boolean unlocked(@NotNull EntityPlayer player) {
                return unlocked.test(player);
            }

            @Override
            public void fireContract(@Nullable EntityPlayer player, @NotNull ItemStack stack, @NotNull World world, @NotNull BlockPos pos) {
                fire.consume(player, stack, world, pos);
            }
        });
    }

    @NotNull
    public static IContractOath registerOath(@NotNull ResourceLocation id, int length,
                                             @NotNull Predicate<EntityPlayer> unlocked,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire) {
        String modid = id.getResourceDomain();
        String name = id.getResourcePath();
        String[] lines = new String[length];
        for (int i = 0; i < length; i++) lines[i] = modid + ".oath." + name + i;
        return registerOath(id, modid + ".oath." + name, unlocked, fire, lines);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull String id, int length,
                                             @NotNull Predicate<EntityPlayer> unlocked,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire) {
        return registerOath(getKey(id), length, unlocked, fire);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull ResourceLocation id, int length,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire) {
        return registerOath(id, length, (player) -> true, fire);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull String id, int length,
                                             @NotNull QuadConsumer<EntityPlayer, ItemStack, World, BlockPos> fire) {
        return registerOath(getKey(id), length, fire);
    }

    @NotNull
    public static IContractOath registerOath(@NotNull String id, int length) {
        return registerOath(getKey(id), length, (entityPlayer, itemStack, world, blockPos) -> {});
    }

    @Nullable
    public static IContractOath getOathFromName(@NotNull ResourceLocation id) {
        return oaths.getObject(id);
    }

    @Nullable
    public static IContractOath getOathFromId(int id) {
        return oaths.getObjectById(id);
    }

    @Nullable
    public static ResourceLocation getNameFromOath(@NotNull IContractOath oath) {
        return oaths.getNameForObject(oath);
    }

    public static int getIdFromOath(@NotNull IContractOath oath) {
        return oaths.getIDForObject(oath);
    }

    @NotNull
    public static Iterator<RegistryEntry<ResourceLocation, IContractOath>> getOaths() {
        return RegistryEntry.iterateOverRegistry(oaths);
    }

}
