package eladkay.quaeritum.api.animus;

import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import eladkay.quaeritum.api.internal.InternalHandler;
import eladkay.quaeritum.api.lib.LibMisc;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class AnimusHelper {

    public static boolean hasMinimum(@NotNull ItemStack stack, int animus, @NotNull EnumAnimusTier tier) {
        return getAnimus(stack) >= animus && getTier(stack).ordinal() >= tier.ordinal();
    }

    public static boolean requestAnimus(@NotNull ItemStack stack, int animus, @NotNull EnumAnimusTier tier, boolean drain) {
        if (!hasMinimum(stack, animus, tier)) return false;
        if (drain) addAnimus(stack, -animus);
        return true;
    }

    @NotNull
    public static ItemStack setAnimus(@NotNull ItemStack stack, int animus) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone)) {
            Network.setAnimus(((INetworkProvider) stack.getItem()).getPlayer(stack), animus);
            return stack;
        }
        if (!(stack.getItem() instanceof ISoulstone))
            return stack;
        ((ISoulstone) stack.getItem()).setAnimus(stack, animus);
        return stack;
    }

    @NotNull
    public static ItemStack setTier(@NotNull ItemStack stack, @NotNull EnumAnimusTier tier) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone)) {
            Network.setTier(((INetworkProvider) stack.getItem()).getPlayer(stack), tier);
            return stack;
        }
        if (!(stack.getItem() instanceof ISoulstone))
            return stack;
        ((ISoulstone) stack.getItem()).setAnimusTier(stack, tier);
        return stack;
    }

    @NotNull
    public static ItemStack addAnimus(@NotNull ItemStack stack, int animus) {
        return setAnimus(stack, getAnimus(stack) + animus);
    }

    @NotNull
    public static EnumAnimusTier getTier(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone))
            return Network.getTier(((INetworkProvider) stack.getItem()).getPlayer(stack));
        if (!(stack.getItem() instanceof ISoulstone)) return EnumAnimusTier.VERDIS;
        return ((ISoulstone) stack.getItem()).getAnimusTier(stack);
    }

    public static int getAnimus(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone))
            return Network.getAnimus(((INetworkProvider) stack.getItem()).getPlayer(stack));
        if (!(stack.getItem() instanceof ISoulstone)) return 0;
        return ((ISoulstone) stack.getItem()).getAnimusLevel(stack);
    }

    public static void damageItem(@NotNull ItemStack stack, int damageToApply, @NotNull EntityLivingBase entity, int animusPerDamage, @NotNull EnumAnimusTier tier) {
        int animusToDrain = damageToApply * animusPerDamage;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) return;
        boolean shouldDamage = !(entity instanceof EntityPlayer) || !Network.requestAnimus((EntityPlayer) entity, animusToDrain, tier, true);
        if (shouldDamage)
            stack.damageItem(damageToApply, entity);
    }

    public static final class Network {
        private static final String TAG_ANIMUS_LEVEL = "animusLevel";
        private static final String TAG_ANIMUS_RARITY = "animusRarity";
        private static final String TAG_LAST_KNOWN_USERNAME = "lastUsername";
        private static final Map<UUID, Integer> cachedColors = new HashMap<>();

        public static int getAnimusColor(@NotNull EntityPlayer player) {
            return getAnimusColor(player.getUniqueID());
        }

        public static int getAnimusColor(@Nullable UUID uuid) {
            if (uuid == null)
                return -1;
            if (cachedColors.containsKey(uuid)) return cachedColors.get(uuid);
            Random random = new Random(uuid.getLeastSignificantBits() ^ (uuid.getMostSignificantBits() * 31));
            int color = Color.HSBtoRGB(random.nextFloat(), (random.nextFloat() / 2) + 0.5f, 1f);
            cachedColors.put(uuid, color);
            return color;
        }

        public static void addInformation(@NotNull ItemStack stack, @NotNull List<String> tooltip) {
            UUID uuid = ((INetworkProvider) stack.getItem()).getPlayer(stack);
            if (uuid != null) {
                String username = getLastKnownUsername(uuid);
                if (username != null)
                    TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".animus_bound", username);
                else
                    TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".not_bound");
            } else
                TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".not_bound");
        }

        public static void setAnimus(@NotNull EntityPlayer player, int animus) {
            setAnimus(player.getUniqueID(), animus);
        }

        public static void setAnimus(@Nullable UUID uuid, int animus) {
            if (animus == 0)
                setTier(uuid, EnumAnimusTier.VERDIS);
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_LEVEL, animus);
            InternalHandler.getInternalHandler().markSaveDataDirty();
        }

        public static void setTier(@NotNull EntityPlayer player, @NotNull EnumAnimusTier tier) {
            setTier(player.getUniqueID(), tier);
        }

        public static void setTier(@Nullable UUID uuid, @NotNull EnumAnimusTier tier) {
            if (getAnimus(uuid) == 0) return;
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_RARITY, tier.ordinal());
            InternalHandler.getInternalHandler().markSaveDataDirty();
        }

        public static int getAnimus(@NotNull EntityPlayer player) {
            return getAnimus(player.getUniqueID());
        }

        public static int getAnimus(@Nullable UUID uuid) {
            return getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_LEVEL, 0);
        }

        @NotNull
        public static EnumAnimusTier getTier(@NotNull EntityPlayer player) {
            return getTier(player.getUniqueID());
        }

        @NotNull
        public static EnumAnimusTier getTier(@Nullable UUID uuid) {
            return EnumAnimusTier.fromMeta(getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_RARITY, 0));
        }

        public static void addAnimus(@NotNull EntityPlayer player, int animus) {
            addAnimus(player.getUniqueID(), animus);
        }

        public static void addAnimus(@Nullable UUID uuid, int animus) {
            setAnimus(uuid, animus + getAnimus(uuid));
        }

        public static void addTier(@NotNull EntityPlayer player, @NotNull EnumAnimusTier tier) {
            addTier(player.getUniqueID(), tier);
        }

        public static void addTier(@Nullable UUID uuid, @NotNull EnumAnimusTier tier) {
            setTier(uuid, EnumAnimusTier.fromMeta(Math.max(tier.ordinal(), getTier(uuid).ordinal())));
        }

        public static boolean requestAnimus(@NotNull EntityPlayer player, int animus, @NotNull EnumAnimusTier tier, boolean drain) {
            return requestAnimus(player.getUniqueID(), animus, tier, drain);
        }

        public static boolean requestAnimus(@Nullable UUID uuid, int animus, @NotNull EnumAnimusTier tier, boolean drain) {
            if (getAnimus(uuid) < animus) return false;
            if (getTier(uuid).ordinal() < tier.ordinal()) return false;
            if (drain) addAnimus(uuid, -animus);
            return true;
        }

        public static void updatePlayerName(@NotNull EntityPlayer player) {
            NBTTagCompound compound = getPersistentCompound(player.getUniqueID());
            if (!player.getName().equals(getStringSafe(compound, TAG_LAST_KNOWN_USERNAME, null))) {
                compound.setString(TAG_LAST_KNOWN_USERNAME, player.getName());
                InternalHandler.getInternalHandler().markSaveDataDirty();
            }
        }

        @Nullable
        public static String getLastKnownUsername(@Nullable UUID uuid) {
            return getStringSafe(getPersistentCompound(uuid), TAG_LAST_KNOWN_USERNAME, null);
        }

        private static boolean getBooleanSafe(@NotNull NBTTagCompound compound, @NotNull String tag, boolean fallback) {
            if (!compound.hasKey(tag)) return fallback;
            return compound.getBoolean(tag);
        }

        private static int getIntegerSafe(@NotNull NBTTagCompound compound, @NotNull String tag, int fallback) {
            if (!compound.hasKey(tag, 3)) return fallback;
            return compound.getInteger(tag);
        }

        @Nullable
        private static String getStringSafe(@NotNull NBTTagCompound compound, @NotNull String tag, @Nullable String fallback) {
            if (!compound.hasKey(tag, 8)) return fallback;
            return compound.getString(tag);
        }

        @NotNull
        private static NBTTagCompound getPersistentCompound(@Nullable UUID uuid) {
            if (uuid == null) return new NBTTagCompound();

            Map<UUID, NBTTagCompound> saveData = InternalHandler.getInternalHandler().getSaveData();

            if (!saveData.containsKey(uuid))
                saveData.put(uuid, new NBTTagCompound());

            return saveData.get(uuid);
        }

        public static class EventHandler {
            public EventHandler() {
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void onEntityTick(LivingEvent.LivingUpdateEvent e) {
                if (e.getEntityLiving() instanceof EntityPlayer) {
                    AnimusHelper.Network.updatePlayerName((EntityPlayer) e.getEntityLiving());
                }
            }
        }
    }
}
