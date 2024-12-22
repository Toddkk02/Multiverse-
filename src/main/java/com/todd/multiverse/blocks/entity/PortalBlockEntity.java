package com.todd.multiverse.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PortalBlockEntity extends BlockEntity {
    private float rotationDegrees = 0;
    private Direction facing = Direction.NORTH;
    private boolean isFloor = false;

    public PortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTAL_BLOCK_ENTITY, pos, state);
    }

    public void tick() {
        if (world != null && !world.isClient) {
            rotationDegrees = (rotationDegrees + 2) % 360;
            markDirty();
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putFloat("rotation", rotationDegrees);
        nbt.putInt("facing", facing.getId());
        nbt.putBoolean("isFloor", isFloor);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        rotationDegrees = nbt.getFloat("rotation");
        facing = Direction.byId(nbt.getInt("facing"));
        isFloor = nbt.getBoolean("isFloor");
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
        markDirty();
    }

    public void setFloor(boolean floor) {
        isFloor = floor;
        markDirty();
    }
}