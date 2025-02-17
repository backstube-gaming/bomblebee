package net.backstube.bomblebee;


import net.minecraft.client.util.InputUtil;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;

public class BombleBeeKeyMappingBuilder {
    private final String translationKey;
    private InputUtil.Type type = InputUtil.Type.KEYSYM;
    private int keyCode = GLFW_KEY_UNKNOWN;
    private Runnable action;
    private HoldAction holdAction;
    private long maxTicks = 10;

    private BombleBeeKeyMappingBuilder(String translationKey) {
        this.translationKey = translationKey;
    }

    /**
     * Start building a {@link BombleBeeKeyMapping key} with the translation key provided.
     *
     * @param translationKey key to be appended onto {@code "key.freecam."}
     * @return a {@link BombleBeeKeyMapping} builder
     */
    public static BombleBeeKeyMappingBuilder builder(String translationKey) {
        return new BombleBeeKeyMappingBuilder(translationKey);
    }

    public BombleBeeKeyMappingBuilder type(InputUtil.Type type) {
        this.type = type;
        return this;
    }

    public BombleBeeKeyMappingBuilder maxHoldTicks(long ticks) {
        this.maxTicks = ticks;
        return this;
    }

    public BombleBeeKeyMappingBuilder defaultKey(int keyCode) {
        this.keyCode = keyCode;
        return this;
    }

    public BombleBeeKeyMappingBuilder action(Runnable action) {
        this.action = action;
        return this;
    }

    public BombleBeeKeyMappingBuilder holdAction(HoldAction action) {
        holdAction = action;
        return this;
    }

    /**
     * Build the {@link BombleBeeKeyMapping key mapping}.
     * <p>
     * If an {@link #action(Runnable) action} was defined, it will be run when the key is <strong>pressed</strong>.
     * <p>
     * If a {@link #holdAction(HoldAction) hold action} was defined, it will be run while the key is <strong>held</strong>
     * (each tick).
     * <p>
     * If both were defined, a {@link BombleBeeComboKeyMapping combo key} is provided where the {@link #holdAction(HoldAction) hold action}
     * is run as normal and the {@link #action(Runnable) action} is run when the key is <strong>released</strong>.
     * <br>
     * If the key was held for {@link #maxHoldTicks(long) max hold ticks} or longer (default 10) then {@link #action(Runnable) action}
     * is <strong>not run</strong>. It is also not run if any {@link #holdAction(HoldAction) hold action} returned
     * {@code true} since the key last released.
     *
     * @return the {@link BombleBeeKeyMapping keybind}.
     */
    public BombleBeeKeyMapping build() {
        if (action != null && holdAction != null) {
            return new BombleBeeComboKeyMapping(translationKey, type, keyCode, action, holdAction, maxTicks);
        }
        if (action != null) {
            return new BombleBeeKeyMapping(translationKey, type, keyCode, self -> {
                while (self.wasPressed()) {
                    action.run();
                }
            });
        }
        if (holdAction != null) {
            return new BombleBeeKeyMapping(translationKey, type, keyCode, self -> {
                if (self.isPressed()) {
                    holdAction.run();
                }
            });
        }
        throw new IllegalStateException("No action defined.");
    }
}
