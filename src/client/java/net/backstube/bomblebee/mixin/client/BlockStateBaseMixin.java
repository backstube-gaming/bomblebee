package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BombleBeeClientEntity;
import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlockStateBaseMixin {

    @Shadow
    public abstract Block getBlock();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void bomblebee$getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityShapeContext entityShapeContext && entityShapeContext.getEntity() instanceof BombleBeeClientEntity) {
            // Return early if "Always Check Initial Collision" is on and BomblebeeCam isn't enabled yet
            if (/*ModConfig.INSTANCE.collision.alwaysCheck && */ !BomblebeeHandler.isUsing()) {
                return;
            }
            // Otherwise, check the collision config
            /* if (CollisionBehavior.isIgnored(getBlock())) {
                cir.setReturnValue(Shapes.empty());
            }*/
        }
    }
}
