package com.todd.multiverse.blocks.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.registry.ModEntities;

public class BulletEntity extends ThrownItemEntity {
    private Entity owner;

    public BulletEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.BULLET, owner, world);
        this.owner = owner;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.world.isClient) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                Direction hitFace = blockHitResult.getSide();
                BlockPos hitPos = blockHitResult.getBlockPos();

                if (this.owner != null) {
                    createPortal(hitPos, hitFace, this.owner);
                }
            }
            this.discard();
        }
    }

    private void createPortal(BlockPos hitPos, Direction hitFace, Entity shooter) {
    if (shooter == null || world == null) return;

    BlockPos portalPos = hitPos.offset(hitFace);

    if (world.getBlockState(portalPos).isAir()) {
        world.setBlockState(portalPos, ModBlocks.PORTAL.getDefaultState()
                .with(Properties.FACING, hitFace));

        BlockEntity blockEntity = world.getBlockEntity(portalPos);
        if (blockEntity instanceof PortalBlockEntity portalEntity) {
            portalEntity.markDirty();
        }
    }
}


    @Override
    protected Item getDefaultItem() {
        return null;
    }

    @Override
    public void setOwner(Entity entity) {
        super.setOwner(entity);
        this.owner = entity;
    }

    @Override
    public Entity getOwner() {
        return this.owner;
    }
}
