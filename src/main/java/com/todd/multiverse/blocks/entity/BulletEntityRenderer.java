package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.blocks.entity.BulletEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.client.render.VertexConsumerProvider;

public class BulletEntityRenderer extends EntityRenderer<BulletEntity> {
    private static final Identifier TEXTURE = new Identifier("multiverse", "textures/entity/bullet.png");

    public BulletEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BulletEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(0.5F, 0.5F, 0.5F); // Rende il proiettile più piccolo
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }
}
