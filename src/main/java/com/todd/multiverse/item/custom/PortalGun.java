package com.todd.multiverse.item.custom;

import com.todd.multiverse.blocks.entity.BulletEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PortalGun extends Item {
    public PortalGun(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            BulletEntity bullet = new BulletEntity(world, player);
            bullet.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 0.5f, 0.25f);
            world.spawnEntity(bullet); // Assicurati di spawnare l'entità nel mondo
        }

        // Restituisci il risultato corretto includendo lo stack dell'oggetto
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }
}

