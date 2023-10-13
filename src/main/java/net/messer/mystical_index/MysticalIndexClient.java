package net.messer.mystical_index;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.messer.mystical_index.block.ModBlockEntities;
import net.messer.mystical_index.client.ClientNetworkListeners;
import net.messer.mystical_index.client.ItemModelProviders;
import net.messer.mystical_index.client.event.ClientEvents;
import net.messer.mystical_index.client.render.ChiseledBookshelfBlockEntityRenderer;
import net.messer.mystical_index.client.render.MysticalLecternBlockEntityRenderer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(value = EnvType.CLIENT)
public class MysticalIndexClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientNetworkListeners.registerListeners();
        ClientEvents.register();

        BlockEntityRendererFactories.register(ModBlockEntities.MYSTICAL_LECTERN_BLOCK_ENTITY, MysticalLecternBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityType.CHISELED_BOOKSHELF, ChiseledBookshelfBlockEntityRenderer::new);

        ItemModelProviders.register();
    }
}
