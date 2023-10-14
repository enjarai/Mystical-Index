package dev.enjarai.arcane_repository.util.state;

import dev.enjarai.arcane_repository.block.entity.MysticalLecternBlockEntity;

public class PageLecternState {
    public final MysticalLecternBlockEntity lectern;

    public PageLecternState(MysticalLecternBlockEntity lectern) {
        this.lectern = lectern;
    }

    public void onUnload() {
    }
}
