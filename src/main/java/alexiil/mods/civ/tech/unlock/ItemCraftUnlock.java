package alexiil.mods.civ.tech.unlock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import alexiil.mods.civ.CivCraft;
import alexiil.mods.civ.tech.TechTree.Tech;
import alexiil.mods.civ.utils.TechUtils;
import alexiil.mods.lib.EChatColours;
import alexiil.mods.lib.item.IChangingItemString;

public class ItemCraftUnlock extends TechUnlockable implements IChangingItemString, IItemBlocker {
    private final List<IItemComparator> items;
    private ItemStack singleItem = null;
    private boolean singleItemFlag = true;
    
    public ItemCraftUnlock(String name, Tech... techs) {
        super(name, techs, true);
        items = new ArrayList<IItemComparator>();
    }
    
    /** Adds all variants of these items as now craftable */
    public ItemCraftUnlock addUnlocked(Item... items) {
        for (final Item item : items)
            addUnlocked(item);
        return this;
    }
    
    /** Adds all variants of this item as now craftable */
    public ItemCraftUnlock addUnlocked(final Item item) {
        if (item == null)
            return this;
        if (singleItemFlag) {
            if (singleItem != null) {
                singleItem = null;
                singleItemFlag = false;
            }
            else
                singleItem = new ItemStack(item);
        }
        items.add(new IItemComparator() {
            @Override public boolean isConsideredEqual(ItemStack toCompare) {
                if (toCompare == null && item != null)
                    return false;
                return toCompare.getItem() == item;
            }
        });
        return this;
    }
    
    public ItemCraftUnlock addUnlocked(Block... blocks) {
        for (Block block : blocks)
            addUnlocked(ItemBlock.getItemFromBlock(block));
        return this;
    }
    
    public ItemCraftUnlock addUnlocked(ItemStack... stacks) {
        for (final ItemStack stack : stacks)
            addUnlocked(stack);
        return this;
    }
    
    public ItemCraftUnlock addUnlocked(final ItemStack stack) {
        if (stack == null)
            return this;
        if (singleItemFlag) {
            if (singleItem != null) {
                singleItem = null;
                singleItemFlag = false;
            }
            else
                singleItem = stack;
        }
        items.add(new IItemComparator() {
            @Override public boolean isConsideredEqual(ItemStack toCompare) {
                return OreDictionary.itemMatches(stack, toCompare, false);
            }
        });
        return this;
    }
    
    public ItemCraftUnlock addUnlocked(IItemComparator compare) {
        singleItemFlag = false;
        items.add(compare);
        return this;
    }
    
    public boolean itemMatches(ItemStack stack) {
        for (IItemComparator i : items)
            if (i.isConsideredEqual(stack))
                return true;
        return false;
    }
    
    @Override public void unlock(EntityPlayer player) {}
    
    @Override public String getUnlocalisedName() {
        return "civcraft.unlock.itemcraft." + getName();
    }
    
    @Override public String getLocalisedName() {
        String superL = super.getLocalisedName();
        if (superL.equals(getUnlocalisedName()) && singleItemFlag)
            return CivCraft.instance.format("civcraft.unlock.itemcraft.pre") + " "
                    + CivCraft.instance.format(singleItem.getUnlocalizedName() + ".name");
        return superL;
    }
    
    @Override public String[] getString(ItemStack i, EntityPlayer player) {
        if (itemMatches(i)) {
            String[] strings = new String[1 + requiredTechs().length];
            strings[0] = EChatColours.GRAY + "Requires these techs to make:";
            int idx = 1;
            for (Tech t : requiredTechs()) {
                strings[idx] = TechUtils.getColour(t) + " -" + t.getLocalizedName();
                idx++;
            }
            return strings;
        }
        return new String[0];
    }
    
    /* civcraft.unlock.itemcraft.agriculture_wood=Craft a Wooden Hoe civcraft.unlock.itemcraft.agriculture_stone=Craft a
     * Stone Hoe civcraft.unlock.itemcraft.agriculture_iron=Craft an Iron Hoe
     * civcraft.unlock.itemcraft.agriculture_gold=Craft a Golden Hoe civcraft.unlock.itemcraft.agriculture_diamond=Craft
     * a Diamond Hoe civcraft.unlock.itemcraft.sailing=Craft a Boat civcraft.unlock.itemcraft.animal_husbandry=Craft
     * Saddles and Leashes civcraft.unlock.itemcraft.archery_wood=Craft a Bow
     * civcraft.unlock.itemcraft.mining_wood=Craft a Wooden Pickaxe civcraft.unlock.itemcraft.mining_stone=Craft a Stone
     * Pickaxe civcraft.unlock.itemcraft.mining_iron=Craft an Iron Pickaxe civcraft.unlock.itemcraft.mining_gold=Craft a
     * Golden Pickaxe civcraft.unlock.itemcraft.mining_diamond=Craft a Diamond Pickaxe */
    
    @Override public boolean doesBlockItem(ItemStack item) {
        return itemMatches(item);
    }
}
