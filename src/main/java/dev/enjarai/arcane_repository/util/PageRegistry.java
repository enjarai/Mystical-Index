package dev.enjarai.arcane_repository.util;

import dev.enjarai.arcane_repository.item.custom.page.PageItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

public class PageRegistry {
    public static final HashMap<Identifier, PageItem> REGISTERED_PAGES = new HashMap<>();

    public static void registerPage(Identifier itemId, Item item) {
        if (item instanceof PageItem pageItem)
            REGISTERED_PAGES.put(itemId, pageItem);
    }

    public static PageItem getPage(Identifier itemId) {
        return REGISTERED_PAGES.get(itemId);
    }

    public static <T extends PageItem> List<T> getPages(Class<T> clazz) {
        return REGISTERED_PAGES.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }
}
