package net.backstube.bomblebee;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class BombleBeeKeyMapping extends KeyBinding implements TextureTickListener {

    private final Consumer<BombleBeeKeyMapping> onTick;

    /**
     * @apiNote should only be used if overriding {@link #tick()}
     */
    protected BombleBeeKeyMapping(String translationKey, InputUtil.Type type, int code) {
        this(translationKey, type, code, null);
    }

    BombleBeeKeyMapping(String translationKey, InputUtil.Type type, int code, Consumer<BombleBeeKeyMapping> onTick) {
        super("key.freecam." + translationKey, type, code, "category.freecam.freecam");
        this.onTick = onTick;
    }

    @Override
    public void tick() {
        onTick.accept(this);
    }

    /**
     * Reset whether the key was pressed.
     * <p>
     *
     * @implNote Cannot use  KeyMapping#release() because it doesn't work as expected.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void reset() {
        while (wasPressed()) {
        }
    }
}
