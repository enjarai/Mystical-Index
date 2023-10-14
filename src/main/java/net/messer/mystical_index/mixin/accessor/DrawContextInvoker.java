package net.messer.mystical_index.mixin.accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawContext.class)
public interface DrawContextInvoker {
    @Invoker("<init>")
    static DrawContext constructor(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        throw new AssertionError("Your pc is haunted, go find an exorcist");
    }
}
