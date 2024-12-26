package net.backstube.bomblebee;

import net.backstube.bomblebee.entities.BombleBeeServerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public final class BomblebeeHandler {
    private static boolean isUsing;

    public static boolean isUsing() {
        return isUsing;
    }

    public static void stopUsing() {

    }

    public static Config config = new Config(FlightMode.CREATIVE, 0.7f, 0.7f, true, BombleBeeClientEntity.CamPerspective.FIRST_PERSON);

    public record Config(FlightMode flightMode, float horizontalSpeed, float verticalSpeed, boolean showHand,
                         BombleBeeClientEntity.CamPerspective perspective) {
    }

    public enum FlightMode {
        CREATIVE,
        DEFAULT
    }

    private static boolean playerControlEnabled = false;
    private static boolean disableNextTick = false;
    private static BombleBeeClientEntity camera;
    private static Perspective rememberedF5 = null;

//    public static void preTick(MinecraftClient mc) {
//        // Disable if the previous tick asked us to,
//        // or Freecam is restricted on the current server
//        if (disableNextTick && isUsing) {
//            toggle();
//        }
//        disableNextTick = false;
//
//        if (isUsing) {
//            // Prevent player from being controlled when freecam is enabled
//            if (mc.player != null && mc.player.input instanceof KeyboardInput && !isPlayerControlEnabled()) {
//                var input = new Input();
//                input.sneaking = mc.player.input.sneaking; // Makes player continue to sneak after freecam is enabled.
//                mc.player.input = input;
//            }
//
//            mc.gameRenderer.setRenderHand(config.showHand);
//        }
//    }

    public static void postTick(MinecraftClient mc) {
        ModBindings.forEach(TextureTickListener::tick);
    }

    public static void onDisconnect() {
        if (isUsing) {
            onDisable();
            onDisabled();
        }
    }

//    public static void toggle() {
//        if (isUsing) {
//            onDisable();
//        } else {
//            onEnableBomblebeecam();
//        }
//        isUsing = !isUsing;
//        if (!isUsing) {
//            onDisabled();
//        }
//    }

    public static void activate(BombleBeeServerEntity serverEntity) {
        isUsing = true;
        onEnable();
        camera = new BombleBeeClientEntity(-420, serverEntity);
        camera.spawn();
        //TODO: update position regularly to the server
        camera.copyPositionAndRotation(MinecraftClient.getInstance().player);
        camera.applyPerspective(
                config.perspective,
                true
        );

        var client = MinecraftClient.getInstance();
        client.setCameraEntity(camera);
    }

    public static void switchControls() {
        if (!isUsing) {
            return;
        }
        var client = MinecraftClient.getInstance();
        if (playerControlEnabled) {
            camera.input = new KeyboardInput(client.options);
        } else {
            client.player.input = new KeyboardInput(client.options);
            camera.input = new Input();
        }
        playerControlEnabled = !playerControlEnabled;
    }

//    private static void onEnableBomblebeecam() {
//        onEnable();
//        camera = new BombleBeeClientEntity(-420);
//        moveToPlayer();
//        camera.spawn();
//
//        var client = MinecraftClient.getInstance();
//        client.setCameraEntity(camera);
//    }

    private static void onEnable() {
        var client = MinecraftClient.getInstance();
        client.chunkCullingEnabled = false;
        client.gameRenderer.setRenderHand(config.showHand);

        rememberedF5 = client.options.getPerspective();
        if (client.gameRenderer.getCamera().isThirdPerson()) {
            client.options.setPerspective(Perspective.FIRST_PERSON);
        }
    }

    public static void onDisable() {
        var client = MinecraftClient.getInstance();
        client.chunkCullingEnabled = true;
        client.gameRenderer.setRenderHand(true);
        client.setCameraEntity(client.player);
        playerControlEnabled = false;
        camera.despawn();
        camera.input = new Input();
        camera = null;

        if (client.player != null) {
            client.player.input = new KeyboardInput(client.options);
        }
    }

    private static void onDisabled() {
        if (rememberedF5 != null) {
            MinecraftClient.getInstance().options.setPerspective(rememberedF5);
        }
    }

    public static void moveToEntity(@Nullable Entity entity) {
        if (camera == null) {
            return;
        }
        if (entity == null) {
            moveToPlayer();
            return;
        }
        camera.copyPositionAndRotation(entity);
    }

    public static void moveToPosition(@Nullable BombleBeePosition position) {
        if (camera == null) {
            return;
        }
        if (position == null) {
            moveToPlayer();
            return;
        }
        camera.applyPosition(position);
    }

    public static void moveToPlayer() {
        if (camera == null) {
            return;
        }
        camera.copyPositionAndRotation(MinecraftClient.getInstance().player);
        camera.applyPerspective(
                config.perspective,
                true
                /*ModConfig.INSTANCE.collision.alwaysCheck || !(ModConfig.INSTANCE.collision.ignoreAll && BuildVariant.getInstance().cheatsPermitted())*/
        );
    }

    public static BombleBeeClientEntity getCamera() {
        return camera;
    }

    public static boolean isPlayerControlEnabled() {
        return playerControlEnabled;
    }
}
