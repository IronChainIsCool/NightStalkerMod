package com.ironchainmodding.nightstalker.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FastFallingBlock extends FallingBlock {
    public static final MapCodec<FastFallingBlock> CODEC = simpleCodec(FastFallingBlock::new);

    public FastFallingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    protected void falling(FallingBlockEntity entity) {
        super.falling(entity);
        // Push straight down (Y = -1), with 0 movement on X and Z
        entity.push(new Vec3(0, -1, 0));
        entity.hurtMarked = true;

    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        // Color of the dust particles that drip when suspended in mid-air (RGB hex)
        return 0x000000;
    }
}
