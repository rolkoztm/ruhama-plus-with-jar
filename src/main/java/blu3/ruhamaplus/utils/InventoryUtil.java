package blu3.ruhamaplus.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class InventoryUtil implements Util {

    public static int findHotbarBlock(final Class clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getMc().player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static int findHotbarItem(final Class clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getMc().player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
                if (stack.getItem() instanceof Item) {
                    final Item item = (stack.getItem());
                    if (clazz.isInstance(item)) {
                        return i;
                    }
                }
            }
        }
        return mc.player.inventory.currentItem;
    }
}
