package net.backstube.bomblebee;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public enum ModBindings {

    KEY_PLAYER_CONTROL(() -> BombleBeeKeyMappingBuilder.builder("playerControl")
            .action(BomblebeeHandler::switchControls)
            .build());

    private final Supplier<BombleBeeKeyMapping> lazyMapping;

    ModBindings(Supplier<BombleBeeKeyMapping> mappingSupplier) {
        lazyMapping = Suppliers.memoize(mappingSupplier);
    }

    /**
     * Lazily get the actual {@link BombleBeeKeyMapping} represented by this enum value.
     * <p>
     * Values are constructed if they haven't been already.
     *
     * @return the actual {@link BombleBeeKeyMapping}.
     */
    public BombleBeeKeyMapping get() {
        return lazyMapping.get();
    }

    /**
     * Calls {@code action} using each {@link BombleBeeKeyMapping} owned by this enum.
     * <p>
     * Values are constructed if they haven't been already.
     * <p>
     * Static implementation of {@link Iterable#forEach(Consumer)}.
     */
    public static void forEach(@NotNull Consumer<BombleBeeKeyMapping> action) {
        Objects.requireNonNull(action);
        iterator().forEachRemaining(action);
    }

    /**
     * Static implementation of {@link Iterable#iterator()}.
     */
    public static @NotNull Iterator<BombleBeeKeyMapping> iterator() {
        return stream().iterator();
    }

    /**
     * Static implementation of {@link Iterable#spliterator()}.
     */
    public static @NotNull Spliterator<BombleBeeKeyMapping> spliterator() {
        return stream().spliterator();
    }

    /**
     * Static implementation of {@link Collection#stream()}.
     */
    public static @NotNull Stream<BombleBeeKeyMapping> stream() {
        return Arrays.stream(values()).map(ModBindings::get);
    }
}
