package com.todd.multiverse.render;

import com.todd.multiverse.blocks.entity.BulletEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class ProjectileEntityRenderer extends EntityRenderer<BulletEntity> {
    private final ItemRenderer itemRenderer;

    public ProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.25D, 0.0D);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw())));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F));
        itemRenderer.renderItem(entity.getStack(), net.minecraft.client.render.model.json.ModelTransformation.Mode.GROUND, light, 0, matrixStack, vertexConsumers, 0);
        matrixStack.pop();
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(BulletEntity entity) {
        return new Identifier("multiverse", "textures/entities/bullet.png");
    }
}
