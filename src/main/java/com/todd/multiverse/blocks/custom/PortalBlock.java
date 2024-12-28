package com.todd.multiverse.blocks.custom;

import com.todd.multiverse.blocks.entity.PortalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PortalBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.values());

    public PortalBlock(Settings settings) {
        super(settings.nonOpaque().noCollision()); // Rende il blocco invisibile e attraversabile
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PortalBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED; // Cambiato per permettere rendering custom
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity.canUsePortals()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PortalBlockEntity portal) {
                portal.teleportEntity(entity);
            }
        }
    }
}