package net.backstube.bomblebee.entities;

import net.backstube.bomblebee.Bomblebee;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BombleBeeServerEntity extends BeeEntity {
    public static final EntityType<BombleBeeServerEntity> ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(Bomblebee.MOD_ID, "bomblebee"),
            EntityType.Builder.create(BombleBeeServerEntity::new, SpawnGroup.MISC)
                    .makeFireImmune()
                    .build());

    public static void register() {
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, BeeEntity.createBeeAttributes());
    }

    public BombleBeeServerEntity(EntityType<BombleBeeServerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    protected void initGoals() {
    }

    @Override
    public boolean tryAttack(Entity target) {
        return false;
    }

    @Nullable
    public BlockPos getFlowerPos() {
        return null;
    }

    @Override
    protected void mobTick() {
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
            @Override
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }

            @Override
            public void tick() {
            }
        };
        return birdNavigation;
    }

    @Override
    protected void updateGoalControls() {
    }
}
