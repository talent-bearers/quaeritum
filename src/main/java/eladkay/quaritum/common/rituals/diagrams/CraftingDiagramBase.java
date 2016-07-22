package eladkay.quaritum.common.rituals.diagrams;

import com.google.common.collect.ImmutableList;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.RitualRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class CraftingDiagramBase implements IDiagram {
    public final ItemStack output;
    final String name;
    final ImmutableList<ItemStack> input;
    final int animus;
    final int rarity;
    final boolean onPlayers;
    final boolean requiress;
    public CraftingDiagramBase(String name, ItemStack[] input, ItemStack output, int animus, boolean onPlayers, int rarity, boolean requiress) {
        this.name = name;
        this.input = ImmutableList.copyOf(input);
        this.output = output;
        this.animus = animus;
        this.rarity = rarity;
        this.onPlayers = onPlayers;
        this.requiress = requiress;
    }
    public CraftingDiagramBase(String name, ItemStack[] input, Item output, int animus, boolean onPlayers, int rarity, boolean requiress) {
        this.name = name;
        this.input = ImmutableList.copyOf(input);
        this.output = new ItemStack(output);
        this.animus = animus;
        this.rarity = rarity;
        this.onPlayers = onPlayers;
        this.requiress = requiress;
        RitualRegistry.registerDiagram(this, name);
    }

    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return name;
    }

    @Override
    public void run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), output);
        if (requiress)
            if (onPlayers)
                Helper.consumeAnimusForRitual(tile, true, animus, 0);
            else
                Helper.takeAnimus(animus, rarity, tile, 4, true);

        for (EntityItem stack : Helper.entitiesAroundAltar(tile, 4)) {
            if (!Helper.isEntityItemInList(stack, input)) continue;
            WorldServer server = (WorldServer) tile.getWorld();
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.getPosition().getX() + 0.5, stack.getPosition().getY() + 1, stack.getPosition().getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
            stack.setDead();
        }
        System.out.println(item.getEntityItem().getItem());
        world.spawnEntityInWorld(item);
    }

    @Override
    public boolean canRitualRun(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return !requiress || (onPlayers ? Helper.consumeAnimusForRitual(tile, false, animus, rarity) : Helper.takeAnimus(animus, rarity, tile, 4, false));
    }

    @Override
    public boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
        return Helper.matches(Helper.stacksAroundAltar(tile, 4), input);
    }
}
