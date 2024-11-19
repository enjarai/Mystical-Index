package dev.enjarai.arcane_repository.item.component;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record OverstackingStorageComponent(List<TypeStack> stacks) {
    public static final Codec<OverstackingStorageComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TypeStack.CODEC.listOf().fieldOf("stacks").forGetter(OverstackingStorageComponent::stacks)
    ).apply(instance, OverstackingStorageComponent::new));

    public OverstackingStorageComponent with(List<TypeStack> stacks) {
        return new OverstackingStorageComponent(ImmutableList.copyOf(stacks));
    }

    public List<TypeStack> stacks() {
        return new ArrayList<>(stacks);
    }

    public record TypeStack(ItemStack stack, int count) {
        public static final Codec<TypeStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.CODEC.fieldOf("stack").forGetter(TypeStack::stack),
                Codec.INT.fieldOf("count").forGetter(TypeStack::count)
        ).apply(instance, TypeStack::new));

        public Optional<TypeStack> with(int count) {
            if (count > 0) {
                return Optional.of(new TypeStack(stack, count));
            }
            return Optional.empty();
        }

        public static TypeStack from(ItemStack stack) {
            return new TypeStack(stack.copyWithCount(1), stack.getCount());
        }
    }
}
