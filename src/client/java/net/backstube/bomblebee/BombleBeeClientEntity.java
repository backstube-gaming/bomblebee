package net.backstube.bomblebee;

import com.mojang.authlib.GameProfile;
import net.backstube.bomblebee.entities.BombleBeeServerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BombleBeeClientEntity extends ClientPlayerEntity {

    private final BombleBeeServerEntity serverEntity;
//    public BombleBeeClientEntity(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting) {
//        super(client, world, networkHandler, stats, recipeBook, lastSneaking, lastSprinting);
//    }

    public BombleBeeClientEntity(int id, BombleBeeServerEntity serverEntity) {
        super(MinecraftClient.getInstance(), Objects.requireNonNull(MinecraftClient.getInstance().world), NETWORK_HANDLER,
                Objects.requireNonNull(MinecraftClient.getInstance().player).getStatHandler(), MinecraftClient.getInstance().player.getRecipeBook(), false, false);
        this.serverEntity = serverEntity;
        setId(id);
        setPose(EntityPose.SWIMMING);
        getAbilities().flying = true;
        input = new KeyboardInput(MinecraftClient.getInstance().options);
    }

    private static final ClientPlayNetworkHandler NETWORK_HANDLER = new ClientPlayNetworkHandler(
            MinecraftClient.getInstance(),
            MinecraftClient.getInstance().getNetworkHandler().getConnection(),
            new ClientConnectionState(
                    new GameProfile(UUID.randomUUID(), "Bomblebee"),
                    MinecraftClient.getInstance().getTelemetryManager().
                            createWorldSession(false, null, null),
                    DynamicRegistryManager.Immutable.EMPTY,
                    FeatureSet.empty(),
                    null,
                    MinecraftClient.getInstance().getCurrentServerEntry(),
                    MinecraftClient.getInstance().currentScreen,
                    new HashMap<>(), null, true)) {
        @Override
        public void sendPacket(Packet<?> packet) {
        }
    };

    @Override
    public void copyPositionAndRotation(Entity entity) {
        applyPosition(new BombleBeePosition(entity));
    }

    public void applyPosition(BombleBeePosition position) {
        refreshPositionAndAngles(position.x, position.y, position.z, position.yaw, position.pitch);
        renderPitch = getPitch();
        renderYaw = getYaw();
        lastRenderPitch = renderPitch; // Prevents camera from rotating upon entering bomblebeeCam.
        lastRenderYaw = renderYaw;
        applyServerEntityPosAndAngles();
    }

    // Mutate the position and rotation based on perspective
    // If checkCollision is true, move as far as possible without colliding
    public void applyPerspective(CamPerspective perspective, boolean checkCollision) {
        BombleBeePosition position = new BombleBeePosition(this);

        switch (perspective) {
            case INSIDE:
                // No-op
                break;
            case FIRST_PERSON:
                // Move just in front of the player's eyes
                moveForwardUntilCollision(position, 0.4, checkCollision);
                break;
            case THIRD_PERSON_MIRROR:
                // Invert the rotation and fallthrough into the THIRD_PERSON case
                position.mirrorRotation();
            case THIRD_PERSON:
                // Move back as per F5 mode
                moveForwardUntilCollision(position, -4.0, checkCollision);
                break;
        }
    }

    // Move bomblebeeCamera forward using bomblebeeCamPosition.moveForward.
    // If checkCollision is true, stop moving forward before hitting a collision.
    // Return true if successfully able to move.
    private boolean moveForwardUntilCollision(BombleBeePosition position, double distance, boolean checkCollision) {
        if (!checkCollision) {
            position.moveForward(distance);
            applyPosition(position);
            return true;
        }
        return moveForwardUntilCollision(position, distance);
    }

    // Same as above, but always check collision.
    private boolean moveForwardUntilCollision(BombleBeePosition position, double maxDistance) {
        boolean negative = maxDistance < 0;
        maxDistance = negative ? -1 * maxDistance : maxDistance;
        double increment = 0.1;

        // Move forward by increment until we reach maxDistance or hit a collision
        for (double distance = 0.0; distance < maxDistance; distance += increment) {
            BombleBeePosition oldPosition = new BombleBeePosition(this);

            position.moveForward(negative ? -1 * increment : increment);
            applyPosition(position);

            if (!wouldNotSuffocateInPose(getPose())) {
                // Revert to last non-colliding position and return whether we were unable to move at all
                applyPosition(oldPosition);
                return distance > 0;
            }
        }

        return true;
    }

    private void applyServerEntityPosAndAngles() {
        this.serverEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), getYaw(), getPitch());
        this.serverEntity.prevPitch = renderPitch;
        this.serverEntity.prevYaw = renderYaw;
    }

    public void spawn() {
        if (clientWorld != null) {
            clientWorld.addEntity(this);
            applyServerEntityPosAndAngles();
        }
    }

    public void despawn() {
        if (clientWorld != null && clientWorld.getEntityById(getId()) != null) {
            clientWorld.removeEntity(getId(), RemovalReason.DISCARDED);
        }
    }

    // Prevents fall damage sound when bomblebeeCamera touches ground with noClip disabled.
    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    // Needed for hand swings to be shown in bomblebeeCam since the player is replaced by bomblebeeCamera in HeldItemRenderer.renderItem()
    @Override
    public float getHandSwingProgress(float tickDelta) {
        return MinecraftClient.getInstance().player.getHandSwingProgress(tickDelta);
    }

    // Needed for item use animations to be shown in bomblebeeCam since the player is replaced by bomblebeeCamera in HeldItemRenderer.renderItem()
    @Override
    public int getItemUseTimeLeft() {
        return MinecraftClient.getInstance().player.getItemUseTimeLeft();
    }

    // Also needed for item use animations to be shown in bomblebeeCam.
    @Override
    public boolean isUsingItem() {
        return MinecraftClient.getInstance().player.isUsingItem();
    }

    // Prevents slow down from ladders/vines.
    @Override
    public boolean isClimbing() {
        return false;
    }

    // Prevents slow down from water.
    @Override
    public boolean isTouchingWater() {
        return false;
    }

    // Makes night vision apply to BomblebeeCamera when Iris is enabled.
    @Override
    public StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> effect) {
        assert MinecraftClient.getInstance().player != null;
        return MinecraftClient.getInstance().player.getStatusEffect(effect);
    }

    // Prevents pistons from moving bomblebeeCamera when collision.ignoreAll is enabled.
    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.NORMAL;/*ModConfig.INSTANCE.collision.ignoreAll ? PushReaction.IGNORE : PushReaction.NORMAL;*/
    }

    // Prevents collision with solid entities (shulkers, boats)
    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }

    // Ensures that the BomblebeeCamera is always in the swimming pose.
    @Override
    public void setPose(EntityPose pose) {
        super.setPose(EntityPose.SWIMMING);
    }

    // Prevents slow down due to being in swimming pose. (Fixes being unable to sprint)
    @Override
    public boolean shouldSlowDown() {
        return false;
    }

    // Prevents water submersion sounds from playing.
    @Override
    protected boolean updateWaterSubmersionState() {
        this.isSubmergedInWater = this.isSubmergedIn(FluidTags.WATER);
        return this.isSubmergedInWater;
    }

    // Prevents water submersion sounds from playing.
    @Override
    protected void onSwimmingStart() {
    }

    @Override
    public void tickMovement() {
        if (BomblebeeHandler.config.flightMode().equals(BomblebeeHandler.FlightMode.DEFAULT)) {
            getAbilities().setFlySpeed(0);
            Motion.doMotion(this, BomblebeeHandler.config.horizontalSpeed(), BomblebeeHandler.config.verticalSpeed());
        } else {
            getAbilities().setFlySpeed(BomblebeeHandler.config.verticalSpeed() / 10.0f);
        }
        //TODO: BOMBLE
        //this.serverEntity.getAbilities().setFlySpeed(this.getAbilities().getFlySpeed());
        //this.serverEntity.getAbilities().flying = true;
        this.serverEntity.setOnGround(false);

        super.tickMovement();
        getAbilities().flying = true;
        setOnGround(false);

        this.serverEntity.setPos(this.getX(), this.getY(), this.getZ());
        this.serverEntity.setVelocity(this.getVelocity());
        this.serverEntity.setYaw(this.getYaw());
        this.serverEntity.setPitch(this.getPitch());
    }

    @Override
    public void setSprinting(boolean sprinting) {
    }

    public enum CamPerspective {
        INSIDE,
        FIRST_PERSON,
        THIRD_PERSON_MIRROR,
        THIRD_PERSON
    }
}
