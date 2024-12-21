package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.Multiverse;
import net.minecraft.entity.projectile.ProjectileEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.entity.damage.DamageSource;

public class BulletEntity extends ProjectileEntity {
    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity owner) {
        this(Multiverse.BULLET_ENTITY, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY(), owner.getZ());
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    return;
    }
    protected void onBlockHit(BlockHitResult blockHitResult) {
    // Get the block position where the bullet hit
    BlockPos pos = blockHitResult.getBlockPos();

    // Get the world where the bullet is in
    World world = this.world;
}

    @Override
    protected void initDataTracker() {

    }

}