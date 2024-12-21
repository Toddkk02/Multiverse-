package com.todd.multiverse.blocks.entity;

import com.todd.multiverse.recipe.FluidDistillerRecipe;
import com.todd.multiverse.screen.FluidDistillerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FluidDistillerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY); // 2 input slots + 1 output
    private final PropertyDelegate propertyDelegate;
    private static final int INPUT_SLOT1 = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int OUTPUT_SLOT = 2;

    private int progress = 0;
    private int maxProgress = 100;

    public FluidDistillerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_DISTILLER, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FluidDistillerBlockEntity.this.progress;
                    case 1 -> FluidDistillerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FluidDistillerBlockEntity.this.progress = value;
                    case 1 -> FluidDistillerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Fluid Distiller");
    }

    public static void tick(World world, BlockPos pos, BlockState state, FluidDistillerBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        if (hasRecipe(entity)) {
            entity.progress++;
            markDirty(world, pos, state);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, pos, state);
        }
    }

    private static void craftItem(FluidDistillerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(3);
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        if (hasRecipe(entity)) {
            // Find the matching recipe
            World world = entity.getWorld();
            FluidDistillerRecipe recipe = world.getRecipeManager()
                    .getFirstMatch(FluidDistillerRecipe.Type.INSTANCE, inventory, world)
                    .orElse(null);

            if (recipe != null) {
                // Remove items from input slots
                entity.removeStack(INPUT_SLOT1, 1);
                entity.removeStack(INPUT_SLOT2, 1);

                // Set the output item
                entity.setStack(OUTPUT_SLOT, new ItemStack(recipe.getOutput().getItem(),
                        entity.getStack(OUTPUT_SLOT).getCount() + 1));
            }
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(FluidDistillerBlockEntity entity) {
        World world = entity.getWorld();
        SimpleInventory inventory = new SimpleInventory(3);

        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        FluidDistillerRecipe recipe = world.getRecipeManager()
                .getFirstMatch(FluidDistillerRecipe.Type.INSTANCE, inventory, world)
                .orElse(null);

        if (recipe == null) return false;

        // Check if output slot has room
        ItemStack outputStack = entity.getStack(OUTPUT_SLOT);
        if (outputStack.isEmpty()) return true;

        if (!outputStack.isItemEqual(recipe.getOutput())) return false;

        return outputStack.getCount() < outputStack.getMaxCount();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("fluid_distiller.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("fluid_distiller.progress");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FluidDistillerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}