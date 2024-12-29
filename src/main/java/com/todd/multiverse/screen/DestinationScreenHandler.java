package com.todd.multiverse.screen;

import com.todd.multiverse.blocks.ModBlocks;
import com.todd.multiverse.blocks.entity.PortalBlockEntity;
import com.todd.multiverse.location.LocationManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DestinationScreenHandler extends ScreenHandler {
    private static final Identifier SAVE_LOCATION_PACKET = new Identifier("multiverse", "save_location");
    private static final Identifier TELEPORT_PACKET = new Identifier("multiverse", "teleport");
    private static final Identifier CREATE_PORTAL_PACKET = new Identifier("multiverse", "create_portal");
    private static final Identifier UPDATE_LOCATIONS_PACKET = new Identifier("multiverse", "update_locations");
    private static final Identifier REQUEST_DIMENSIONS_PACKET = new Identifier("multiverse", "request_dimensions");
    private static final Identifier UPDATE_DIMENSIONS_PACKET = new Identifier("multiverse", "update_dimensions");
    private static final Identifier DELETE_LOCATION_PACKET = new Identifier("multiverse", "delete_location");

    private final PlayerEntity player;
    private final List<LocationManager> savedLocations = new ArrayList<>();
    private final List<String> availableDimensions = new ArrayList<>();

    public DestinationScreenHandler(int syncId, PlayerInventory inventory) {
        super(ModScreenHandlers.DESTINATION_SCREEN_HANDLER, syncId);
        this.player = inventory.player;

        if (player.getServer() != null) {
            player.getServer().execute(() -> {
                LocationManager.loadLocationsFromFile();
                loadSavedLocations();
                loadAvailableDimensions();
            });
        }
    }

    private void loadSavedLocations() {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            savedLocations.clear();
            savedLocations.addAll(LocationManager.getLocations());
            syncLocationsWithClient(serverPlayer);
        }
    }

    private void loadAvailableDimensions() {
        availableDimensions.clear();

        // Add vanilla dimensions
        availableDimensions.add("minecraft:overworld");
        availableDimensions.add("minecraft:the_nether");
        availableDimensions.add("minecraft:the_end");

        // Add modded dimensions
        if (player.getServer() != null) {
            // Get all registered dimension types
            Registry<World> worldRegistry = player.getServer().getWorld(World.OVERWORLD).getRegistryManager().get(Registry.WORLD_KEY);
            for (RegistryKey<World> worldKey : player.getServer().getWorldRegistryKeys()) {
                String dimId = worldKey.getValue().toString();
                if (!availableDimensions.contains(dimId)) {
                    availableDimensions.add(dimId);
                }
            }
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            syncDimensionsWithClient(serverPlayer);
        }
    }

    private void syncLocationsWithClient(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(savedLocations.size());
        for (LocationManager loc : savedLocations) {
            buf.writeString(loc.getName());
            buf.writeDouble(loc.getX());
            buf.writeDouble(loc.getY());
            buf.writeDouble(loc.getZ());
            buf.writeString(loc.getDimensionId());
        }
        ServerPlayNetworking.send(player, UPDATE_LOCATIONS_PACKET, buf);
    }

    private void syncDimensionsWithClient(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(availableDimensions.size());
        for (String dim : availableDimensions) {
            buf.writeString(dim);
        }
        ServerPlayNetworking.send(player, UPDATE_DIMENSIONS_PACKET, buf);
    }

    public List<LocationManager> getSavedLocations() {
        return new ArrayList<>(savedLocations);
    }

    public List<String> getAvailableDimensions() {
        return new ArrayList<>(availableDimensions);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public static void registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(SAVE_LOCATION_PACKET, (server, player, handler, buf, responseSender) -> {
            String name = buf.readString();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            String dimension = buf.readString();

            server.execute(() -> {
                LocationManager.saveLocation(name, x, y, z, dimension);
                if (player.currentScreenHandler instanceof DestinationScreenHandler screenHandler) {
                    screenHandler.loadSavedLocations();
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(TELEPORT_PACKET, (server, player, handler, buf, responseSender) -> {
            String name = buf.readString();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            String dimension = buf.readString();

            server.execute(() -> {
                if (name.equals("random")) {
                    LocationManager randomLoc = new LocationManager(name, x, y, z, dimension);
                    LocationManager.teleportToLocation(player, randomLoc);
                } else {
                    List<LocationManager> locations = LocationManager.getLocations();
                    for (LocationManager loc : locations) {
                        if (loc.getName().equals(name)) {
                            LocationManager.teleportToLocation(player, loc);
                            break;
                        }
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(CREATE_PORTAL_PACKET, (server, player, handler, buf, responseSender) -> {
            double targetX = buf.readDouble();
            double targetY = buf.readDouble();
            double targetZ = buf.readDouble();
            String targetDimension = buf.readString();

            server.execute(() -> {
                BlockPos playerPos = player.getBlockPos();
                World world = player.getWorld();

                world.setBlockState(playerPos, ModBlocks.PORTAL.getDefaultState());
                if (world.getBlockEntity(playerPos) instanceof PortalBlockEntity portal) {
                    portal.setDestination(targetDimension, targetX, targetY, targetZ);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(REQUEST_DIMENSIONS_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player.currentScreenHandler instanceof DestinationScreenHandler screenHandler) {
                    screenHandler.loadAvailableDimensions();
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(DELETE_LOCATION_PACKET, (server, player, handler, buf, responseSender) -> {
            String locationName = buf.readString();

            server.execute(() -> {
                LocationManager.deleteLocation(locationName);
                if (player.currentScreenHandler instanceof DestinationScreenHandler screenHandler) {
                    screenHandler.loadSavedLocations();
                }
            });
        });
    }
}