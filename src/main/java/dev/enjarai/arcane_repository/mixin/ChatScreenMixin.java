package dev.enjarai.arcane_repository.mixin;

import dev.enjarai.arcane_repository.client.render.InterceptionWidget;
import dev.enjarai.arcane_repository.util.ChatInterception;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Inject(
            method = "render",
            at = @At(value = "HEAD")
    )
    private void mysticalIndex$renderInterceptionWidget(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        var interception = ChatInterception.shouldIntercept(MinecraftClient.getInstance().player, "");

        if (interception != null) {
            var thisScreen = (ChatScreen) (Object) this;

            InterceptionWidget.render(context, thisScreen.width, thisScreen.height, delta, interception);
        }
    }
}
