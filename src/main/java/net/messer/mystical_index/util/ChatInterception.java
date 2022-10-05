package net.messer.mystical_index.util;

import net.messer.mystical_index.MysticalIndex;
import net.messer.mystical_index.block.entity.MysticalLecternBlockEntity;
import net.messer.mystical_index.item.ModItems;
import net.messer.mystical_index.item.custom.book.MysticalBookItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import static net.messer.mystical_index.block.entity.MysticalLecternBlockEntity.LECTERN_DETECTION_RADIUS;

public enum ChatInterception {
    BOOK(MysticalIndex.id("textures/gui/book_interception.png"), "book"),
    LECTERN(MysticalIndex.id("textures/gui/lectern_interception.png"), "lectern");

    public final Identifier texture;
    public final String name;

    ChatInterception(Identifier texture, String name) {
        this.texture = texture;
        this.name = name;
    }

    public static ChatInterception shouldIntercept(PlayerEntity player, String message) {
        ItemStack stack = null;
        for (Hand hand : Hand.values()) {
            stack = player.getStackInHand(hand);
            if (stack.isOf(ModItems.MYSTICAL_BOOK)) {
                break;
            }
        }

        if (stack != null &&
                stack.getItem() instanceof MysticalBookItem book &&
                book.interceptsChatMessage(stack, player, message)) {
            return BOOK;
        } else {
            MysticalLecternBlockEntity lectern = LecternTracker.findNearestLectern(player, LECTERN_DETECTION_RADIUS);
            if (lectern != null &&
                    lectern.getBook().getItem() instanceof MysticalBookItem book &&
                    book.lectern$interceptsChatMessage(lectern, player, message)) {
                return LECTERN;
            }
        }
        return null;
    }
}
