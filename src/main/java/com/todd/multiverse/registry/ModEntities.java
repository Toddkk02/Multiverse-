package com.todd.multiverse.registry;

import com.todd.multiverse.Multiverse;
import com.todd.multiverse.blocks.entity.BulletEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<BulletEntity> BULLET = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Multiverse.MOD_ID, "bullet"),
            FabricEntityTypeBuilder.<BulletEntity>create(SpawnGroup.MISC, BulletEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void registerEntities() {
        Multiverse.LOGGER.info("Registering entities for " + Multiverse.MOD_ID);
    }
}
