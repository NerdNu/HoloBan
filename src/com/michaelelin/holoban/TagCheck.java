package com.michaelelin.holoban;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Has {@link #checkValid(ItemStack)} which checks an ItemStack for banned NBT
 * tags.
 *
 * Creative mode clients get to tell the server what items the player has,
 * meaning players can conjure up items with arbitrary NBT tags.
 */
public class TagCheck implements ItemCheck {

    private final Map<Material, List<String>> bannedTags = new HashMap<>();

    /**
     * Method handle of o.b.c.<ver>.CraftItemStack.asNMSCopy(ItemStack).
     */
    private MethodHandle mh_CraftItemStack_asNMSCopy;

    /**
     * Method handle of n.m.s.<ver>.ItemStack.getTag().
     */
    private MethodHandle mh_NMSItemStack_getTag;

    /**
     * Method handle of n.m.s.<ver>.NBTTagCompound.hasKey(String).
     */
    private MethodHandle mh_NBTTagCompound_hasKey;

    /**
     * Return the NMS NBTTagCompound of an ItemStack.
     *
     * @param item the Bukkit ItemStack.
     * @return NMS NBTTagCompound containing all the NBT tags.
     * @throws Throwable
     */
    private Object getTag(ItemStack item) throws Throwable {
        // Since we're not casting to statically correct types, invokeExact()
        // will throw WrongMethodTypeException, so use MethodHandle.invoke().
        Object nmsItemStack = mh_CraftItemStack_asNMSCopy.invoke(item);
        return mh_NMSItemStack_getTag.invoke(nmsItemStack);
    }

    private boolean hasBannedTag(ItemStack item, List<String> tagsToCheck) {
        try {
            Object baseTag = getTag(item);
            if (baseTag != null) {
                for (String tagToCheck : tagsToCheck) {
                    if ((boolean) mh_NBTTagCompound_hasKey.invoke(baseTag, tagToCheck)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Throwable e) {
            HoloBan.instance.getLogger().severe("Exception in HoloBan checking tags (plugin needs updating?): " +
                                                e.getClass().getName() + ": " + e.getMessage());
            // Fail safe. Will also draw attention to plugin failure.
            return true;
        }
    }

    /**
     * Constructor.
     *
     * This caches method handles for fast reflective access to CraftBukkit and
     * NMS methods.
     *
     * Check {@link #isOk()} for successful initialisation before using
     * instance.
     */
    public TagCheck() {
        try {
            Class<?> class_CraftItemStack = Reflection.getCraftBukkitClass("inventory.CraftItemStack");
            Class<?> class_NMSItemStack = Reflection.getNMSClass("ItemStack");
            Class<?> class_NBTTagCompound = Reflection.getNMSClass("NBTTagCompound");

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            // n.m.s.<ver>.ItemStack CraftItemStack.asNMSCopy(ItemStack)
            mh_CraftItemStack_asNMSCopy = Reflection.findStatic(lookup, class_CraftItemStack, "asNMSCopy", class_NMSItemStack, ItemStack.class);

            // n.m.s.<ver>.NBTCompound n.m.s.<ver>.ItemStack.getTag()
            mh_NMSItemStack_getTag = Reflection.findVirtual(lookup, class_NMSItemStack, "getTag", class_NBTTagCompound);

            // boolean n.m.s.<ver>.NBTCompound.hasKey(String)
            mh_NBTTagCompound_hasKey = Reflection.findVirtual(lookup, class_NBTTagCompound, "hasKey", Boolean.TYPE, String.class);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            HoloBan.instance.getLogger().severe("Exception initialising HoloBan reflection (plugin needs updating?): " +
                                                e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Return true if all method handles were initialised in the constructor.
     *
     * @return true if all method handles were initialised in the constructor.
     */
    public boolean isOk() {
        return mh_NBTTagCompound_hasKey != null;
    }

    public void addBannedTags(Material mat, List<String> tags) {
        bannedTags.put(mat, tags);
    }

    @Override
    public boolean checkValid(ItemStack item) {
        if (!isOk()) {
            // Fail safe. Will also draw attention to plugin failure.
            return false;
        }

        Material mat = item.getType();
        List<String> tagsForMaterial = bannedTags.get(mat);
        return tagsForMaterial == null || !hasBannedTag(item, tagsForMaterial);
    }
}
