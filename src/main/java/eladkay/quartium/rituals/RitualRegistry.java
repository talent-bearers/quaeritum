package eladkay.quartium.rituals;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RitualRegistry {
    private static RitualRegistry instance = new RitualRegistry();
    public Map<String, IRitual> map = new HashMap<String, IRitual>();
    public List<IRitual> ritualList = Lists.newArrayList();
    public List<List<ItemStack>> requiredItems = Lists.newArrayList();

    public static RitualRegistry getInstance() {
        return instance;
    }

    private RitualRegistry() {
    }
    public RitualRegistry registerRitual(IRitual ritual, String name)  throws Exception {
        System.out.println("Trying to add " + name + " to the RitualRegistry.");
        if(map.get(name) == null) {
            map.put(name, ritual);
            ritualList.add(ritual);
            if(ritual.getRequiredItems() != null && ritual.getRequiredItems() != new ArrayList<ItemStack>())
                requiredItems.add(ritual.getRequiredItems());
        }

        else {
            System.err.println("Ritual " + name + " booked twice!");
            throw new Exception("Ritual of name " + name + " has been booked twice! This is certainly a bug. Ritual details: " + ritual.toString());
        }

        System.out.println("Ritual " + name + " has been added. Welcome home.");
        return instance;
    }
    public IRitual getRitualByName(String name) {
        return map.get(name);
    }
}
