package dev.enjarai.arcane_repository.client.tooltip;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import dev.enjarai.arcane_repository.util.ContentsIndex;
import net.minecraft.client.gui.tooltip.TooltipComponent;

@Environment(EnvType.CLIENT)
public class ItemStorageTooltipData implements ConvertibleTooltipData {
    final ContentsIndex contents;
    final int size;

    public ItemStorageTooltipData(ContentsIndex contents) {
        this.contents = contents;
        this.size = contents.getAll().size();
    }

    @Override
    public TooltipComponent getComponent() {
        return new ItemStorageTooltipComponent(this);
    }
}
