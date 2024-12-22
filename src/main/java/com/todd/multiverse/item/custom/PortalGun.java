package com.todd.multiverse.item.custom;

import com.todd.multiverse.blocks.entity.BulletEntity;
import com.todd.multiverse.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

public class PortalGun extends Item {
    public PortalGun(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (!world.isClient) {
            com.todd.multiverse.blocks.entity.BulletEntity projectile = new BulletEntity(world, player, new ItemStack(ModItems.BULLET));
            projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(projectile);

            world.playSound(null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ENTITY_SNOWBALL_THROW,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F);

            player.getItemCooldownManager().set(this, 20);  // 1 second cooldown
        }
        return TypedActionResult.success(itemStack);
    }
}
