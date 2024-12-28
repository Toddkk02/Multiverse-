package com.todd.multiverse.item.custom;

import com.todd.multiverse.blocks.entity.BulletEntity;
import com.todd.multiverse.item.ModItems;
import com.todd.multiverse.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.server.network.ServerPlayerEntity;

public class PortalGun extends Item {
    public PortalGun(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (player.isSneaking()) {
            // Controlla se è lato server per aprire la GUI
            if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                        (syncId, inventory, playerEntity) ->
                                ModScreenHandlers.DESTINATION_SCREEN_HANDLER.create(syncId, inventory),
                        Text.of("Select Destination")
                ));
            }
            return TypedActionResult.success(itemStack);
        } else {
            // Logica per sparare il proiettile
            if (!world.isClient) {
                BulletEntity projectile = new BulletEntity(world, player, new ItemStack(ModItems.BULLET));
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

                player.getItemCooldownManager().set(this, 20);
            }
            return TypedActionResult.success(itemStack);
        }
    }
}
