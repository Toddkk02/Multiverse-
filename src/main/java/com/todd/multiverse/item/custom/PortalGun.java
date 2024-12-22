package com.todd.multiverse.item.custom;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

public class PortalGun extends Item {
    private static final float SHOOT_VELOCITY = 1.5F;  // Velocità del proiettile
    private static final Item PROJECTILE_ITEM = Items.ENDER_PEARL;

    public PortalGun(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            // Crea il proiettile
            ItemStack projectile = new ItemStack(PROJECTILE_ITEM);
            ItemEntity itemEntity = new ItemEntity(world,
                    player.getX(),
                    player.getEyeY() - 0.1,
                    player.getZ(),
                    projectile);

            // Imposta la direzione del proiettile basata sulla direzione del giocatore
            Vec3d look = player.getRotationVector();
            itemEntity.setVelocity(
                    look.x * SHOOT_VELOCITY,
                    look.y * SHOOT_VELOCITY,
                    look.z * SHOOT_VELOCITY
            );

            // Configurazione proiettile
            itemEntity.setNoGravity(true);
            itemEntity.setPickupDelay(40);

            // Suono di sparo
            world.playSound(null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ENTITY_ARROW_SHOOT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F);

            // Spawna il proiettile
            world.spawnEntity(itemEntity);
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }
}