package dev.enjarai.arcane_repository.mixin.accessor;

import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(ChiseledBookshelfBlock.class)
public interface ChiseledBookshelfBlockInvoker {
    @Invoker("getHitPos")
    static Optional<Vec2f> invokeGetHitPos(BlockHitResult hit, Direction facing) {
        throw new AssertionError("Has cosmic background radiation flipped some bits? What.");
    }

    @Invoker("getSlotForHitPos")
    static int invokeGetSlotForHitPos(Vec2f hitPos) {
        throw new AssertionError("Hello sirmadam this is adam from microsoft tech support services, your computer has virus");
    }
}
