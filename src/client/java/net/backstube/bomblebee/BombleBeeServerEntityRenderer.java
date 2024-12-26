package net.backstube.bomblebee;

import net.backstube.bomblebee.entities.BombleBeeServerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class BombleBeeServerEntityRenderer extends LivingEntityRenderer<BombleBeeServerEntity, BomblebeeEntityModel> {
    private static final Identifier BOMBLEBEE_TEXTURE = new Identifier(Bomblebee.MOD_ID, "textures/entity/bomblebee.png");

    public BombleBeeServerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BomblebeeEntityModel(context.getPart(EntityModelLayers.BEE)), 0.4F);
    }

    public Identifier getTexture(BombleBeeServerEntity beeEntity) {
        return BOMBLEBEE_TEXTURE;
    }
}
