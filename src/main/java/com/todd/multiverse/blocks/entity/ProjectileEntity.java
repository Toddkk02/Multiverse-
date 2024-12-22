package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ProjectileEntity extends ThrownItemEntity {
    public ProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ProjectileEntity(World world, double x, double y, double z, ItemStack stack) {
        super(EntityType.SNOWBALL, x, y, z, world);
        this.setItem(stack);
    }

    public ProjectileEntity(World world, PlayerEntity owner, ItemStack stack) {
        super(EntityType.SNOWBALL, owner, world);
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
