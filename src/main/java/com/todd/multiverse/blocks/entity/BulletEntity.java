package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.item.ModItems;
import com.todd.multiverse.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class BulletEntity extends ThrownItemEntity {
    public BulletEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntities.BULLET, x, y, z, world);
        this.setItem(stack);
    }

    public BulletEntity(World world, PlayerEntity owner, ItemStack stack) {
        super(ModEntities.BULLET, owner, world);
        this.setItem(stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BULLET;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.getWorld().isClient) {
            this.discard();
        }
        super.onCollision(hitResult);
    }
}