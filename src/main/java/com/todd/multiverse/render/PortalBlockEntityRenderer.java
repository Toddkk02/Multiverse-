package com.todd.multiverse.render;

import com.todd.multiverse.blocks.entity.PortalBlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import com.todd.multiverse.Multiverse;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class PortalBlockEntityRenderer implements BlockEntityRenderer<PortalBlockEntity> {
    private static final Identifier PORTAL_TEXTURE = new Identifier(Multiverse.MOD_ID, "textures/block/portal.png");

    @Override
    public void render(PortalBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // Position 1 block above ground and center in 3x3 area
        matrices.translate(-1.0, 0.0, 0.5);

        // Scale to 3x3
        matrices.scale(3.0f, 3.0f, 1.0f);

        // Rotate texture clockwise
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(entity.getRotation()));

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(PORTAL_TEXTURE));

        renderPortalFace(matrices, vertexConsumer, light, overlay, false);
        renderPortalFace(matrices, vertexConsumer, light, overlay, true);

        matrices.pop();
    }

    private void renderPortalFace(MatrixStack matrices, VertexConsumer vertexConsumer,
                                  int light, int overlay, boolean isBack) {
        // Full texture coordinates
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), 0, 0, isBack ? -0.01f : 0.01f)
                .color(255, 255, 255, 255)
                .texture(0, 0)
                .overlay(overlay)
                .light(light)
                .normal(0, 0, isBack ? -1 : 1)
                .next();

        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), 1, 0, isBack ? -0.01f : 0.01f)
                .color(255, 255, 255, 255)
                .texture(1, 0)
                .overlay(overlay)
                .light(light)
                .normal(0, 0, isBack ? -1 : 1)
                .next();

        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), 1, 1, isBack ? -0.01f : 0.01f)
                .color(255, 255, 255, 255)
                .texture(1, 1)
                .overlay(overlay)
                .light(light)
                .normal(0, 0, isBack ? -1 : 1)
                .next();

        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), 0, 1, isBack ? -0.01f : 0.01f)
                .color(255, 255, 255, 255)
                .texture(0, 1)
                .overlay(overlay)
                .light(light)
                .normal(0, 0, isBack ? -1 : 1)
                .next();
    }
}