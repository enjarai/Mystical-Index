package dev.enjarai.arcane_repository;

import dev.enjarai.arcane_repository.block.ModBlockEntities;
import dev.enjarai.arcane_repository.client.ClientNetworkListeners;
import dev.enjarai.arcane_repository.client.ItemModelProviders;
import dev.enjarai.arcane_repository.client.event.ClientEvents;
import dev.enjarai.arcane_repository.client.owo_ui.ItemTagComponent;
import dev.enjarai.arcane_repository.client.render.MysticalLecternBlockEntityRenderer;
import io.wispforest.owo.ui.parsing.UIParsing;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import dev.enjarai.arcane_repository.client.render.ChiseledBookshelfBlockEntityRenderer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(value = EnvType.CLIENT)
public class ArcaneRepositoryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientNetworkListeners.registerListeners();
        ClientEvents.register();

        BlockEntityRendererFactories.register(ModBlockEntities.MYSTICAL_LECTERN_BLOCK_ENTITY, MysticalLecternBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityType.CHISELED_BOOKSHELF, ChiseledBookshelfBlockEntityRenderer::new);

        ItemModelProviders.register();

        UIParsing.registerFactory(ArcaneRepository.MOD_ID + ".item_tag", ItemTagComponent::parse);
    }
}
