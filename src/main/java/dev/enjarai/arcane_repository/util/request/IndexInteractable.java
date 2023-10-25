package dev.enjarai.arcane_repository.util.request;

import java.util.List;
import java.util.Set;

public interface IndexInteractable {
    Set<IndexSource> getSources();

    default void onInteractionComplete() {
    }
}
