package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.location.LocationManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Random;

public class PortalBlockEntity extends BlockEntity {
    private static final Random random = new Random();
    private String targetDimension = "minecraft:overworld";
    private double targetX;
    private double targetY;
    private double targetZ;
    private boolean hasDestination = false;

    public PortalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTAL_BLOCK_ENTITY, pos, state);
    }

    public void teleportEntity(Entity entity) {
        if (entity.world.isClient) return;

        ServerWorld targetWorld;
        BlockPos targetPos;

        if (!hasDestination) {
            // Random dimension teleport
            targetWorld = getRandomDimension();
            if (targetWorld == null) return;
            targetPos = LocationManager.findRandomSafeLocation(targetWorld);
        } else {
            // Specific destination teleport
            targetWorld = getTargetWorld();
            if (targetWorld == null) return;
            targetPos = LocationManager.findSafeSpot(targetWorld, targetX, targetY, targetZ);
        }

        if (entity instanceof ServerPlayerEntity serverPlayer) {
            // Teleport player
            serverPlayer.teleport(
                    targetWorld,
                    targetPos.getX() + 0.5,
                    targetPos.getY(),
                    targetPos.getZ() + 0.5,
                    entity.getYaw(),
                    entity.getPitch()
            );
        } else {
            // Teleport other entities
            Entity newEntity = entity.getType().create(targetWorld);
            if (newEntity != null) {
                newEntity.copyFrom(entity);
                newEntity.refreshPositionAndAngles(
                        targetPos.getX() + 0.5,
                        targetPos.getY(),
                        targetPos.getZ() + 0.5,
                        entity.getYaw(),
                        entity.getPitch()
                );
                targetWorld.spawnEntity(newEntity);
                entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            }
        }
    }

    private ServerWorld getRandomDimension() {
        if (world == null || world.getServer() == null) return null;

        ServerWorld[] worlds = new ServerWorld[]{
                world.getServer().getWorld(World.OVERWORLD),
                world.getServer().getWorld(World.NETHER),
                world.getServer().getWorld(World.END)
        };

        return worlds[random.nextInt(worlds.length)];
    }

    private ServerWorld getTargetWorld() {
        if (world == null || world.getServer() == null) return null;

        return world.getServer().getWorld(
                RegistryKey.of(Registry.WORLD_KEY, new Identifier(targetDimension))
        );
    }

    public void setTargetDimension(String dimension) {
        this.targetDimension = dimension;
        this.hasDestination = true;
        markDirty();
    }

    public void setTargetCoordinates(double x, double y, double z) {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
        this.hasDestination = true;
        markDirty();
    }

    public void setDestination(String dimension, double x, double y, double z) {
        this.targetDimension = dimension;
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
        this.hasDestination = true;
        markDirty();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("TargetDimension", targetDimension);
        nbt.putDouble("TargetX", targetX);
        nbt.putDouble("TargetY", targetY);
        nbt.putDouble("TargetZ", targetZ);
        nbt.putBoolean("HasDestination", hasDestination);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        targetDimension = nbt.getString("TargetDimension");
        targetX = nbt.getDouble("TargetX");
        targetY = nbt.getDouble("TargetY");
        targetZ = nbt.getDouble("TargetZ");
        hasDestination = nbt.getBoolean("HasDestination");
    }
}