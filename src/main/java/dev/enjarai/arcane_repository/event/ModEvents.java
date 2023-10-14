package dev.enjarai.arcane_repository.event;

import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;
import dev.enjarai.arcane_repository.item.ModItems;
import dev.enjarai.arcane_repository.item.custom.book.MysticalBookItem;
import dev.enjarai.arcane_repository.util.request.IndexInteractable;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import dev.enjarai.arcane_repository.util.LecternTracker;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class ModEvents {
    public static void register() {
        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register(ModEvents::onLoadBlockEntity);
        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register(ModEvents::onUnloadBlockEntity);
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ModEvents::onChatMessage);
    }

    private static void onLoadBlockEntity(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof IndexInteractable library) {
            LecternTracker.tryRegisterToLectern(library, false);
        }
    }

    private static void onUnloadBlockEntity(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof MysticalLecternBlockEntity lectern) {
            LecternTracker.removeIndexLectern(lectern);

            if (lectern.actionState != null) lectern.actionState.onUnload();
            if (lectern.typeState != null) lectern.typeState.onUnload();
        }
        if (blockEntity instanceof IndexInteractable interactable) {
            LecternTracker.unRegisterFromLectern(interactable);
        }
    }

    private static boolean onChatMessage(SignedMessage message, ServerPlayerEntity player, MessageType.Parameters params) {
        var server = player.getServer();
        var messageString = message.getContent().getString();

        if (!(messageString.startsWith("/") || player.isSpectator()) && server != null) {
            // TODO: unify with ChatInterception.shouldIntercept()

            ItemStack book = null;
            for (Hand hand : Hand.values()) {
                book = player.getStackInHand(hand);
                if (book.isOf(ModItems.MYSTICAL_BOOK)) {
                    break;
                }
            }

            if (book != null && book.isOf(ModItems.MYSTICAL_BOOK) &&
                    ((MysticalBookItem) book.getItem()).interceptsChatMessage(book, player, messageString)) {
                ItemStack finalBook = book;
                server.execute(() -> {
                    ((MysticalBookItem) finalBook.getItem()).onInterceptedChatMessage(finalBook, player, messageString);
                });
                return false;
            } else {
                MysticalLecternBlockEntity lectern = LecternTracker.findNearestLectern(player, MysticalLecternBlockEntity.LECTERN_DETECTION_RADIUS);
                if (lectern != null &&
                        ((MysticalBookItem) lectern.getBook().getItem()).lectern$interceptsChatMessage(lectern, player, messageString)) {
                    server.execute(() -> {
                        ((MysticalBookItem) lectern.getBook().getItem()).lectern$onInterceptedChatMessage(lectern, player, messageString);
                    });
                    return false;
                }
            }
        }

        return true;
    }
}
