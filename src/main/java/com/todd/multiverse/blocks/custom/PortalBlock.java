package com.todd.multiverse.blocks.custom;

import com.todd.multiverse.blocks.entity.PortalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PortalBlock extends BlockWithEntity {
    public PortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PortalBlockEntity(pos, state);
    }
}
