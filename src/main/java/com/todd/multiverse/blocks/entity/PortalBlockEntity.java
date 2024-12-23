package com.todd.multiverse.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PortalBlockEntity extends BlockEntity {
    private float rotation = 0.0f;
    private Direction.Axis portalAxis = Direction.Axis.Y;

    public PortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTAL_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, PortalBlockEntity be) {
        if (!world.isClient()) {
            // Incrementa la rotazione
            be.rotation = (be.rotation + 2.0f) % 360.0f;
            be.markDirty();
            // Sincronizza con il client
            if (world instanceof ServerWorld) {
                ((ServerWorld) world).getChunkManager().markForUpdate(pos);
            }
        }
    }

    public float getRotation() {
        return rotation;
    }

    public Direction.Axis getPortalAxis() {
        return portalAxis;
    }

    public void setPortalAxis(Direction.Axis axis) {
        this.portalAxis = axis;
        markDirty();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putFloat("rotation", rotation);
        nbt.putString("portalAxis", portalAxis.name());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        rotation = nbt.getFloat("rotation");
        portalAxis = Direction.Axis.valueOf(nbt.getString("portalAxis"));
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}