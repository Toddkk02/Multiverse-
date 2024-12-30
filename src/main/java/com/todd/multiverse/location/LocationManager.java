package com.todd.multiverse.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationManager {
    private static final List<LocationManager> locations = new ArrayList<>();
    private static final Random random = new Random();
    private static final File locationsFile = new File("locations.json");

    private final String name;
    private final double x;
    private final double y;
    private final double z;
    private final String dimensionId;

    public LocationManager(String name, double x, double y, double z, String dimensionId) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimensionId = dimensionId;
    }

    public static void teleportToLocation(PlayerEntity player, LocationManager location) {
        if (!(player instanceof ServerPlayerEntity serverPlayer) || !(player.world instanceof ServerWorld)) return;

        ServerWorld targetWorld = player.getServer().getWorld(
                RegistryKey.of(Registry.WORLD_KEY, new Identifier(location.getDimensionId()))
        );

        if (targetWorld == null) return;

        BlockPos safePos;
        if (location.getName().equals("random")) {
            safePos = findRandomSafeLocation(targetWorld);
        } else {
            safePos = findSafeSpot(targetWorld, location.getX(), location.getY(), location.getZ());
        }

        serverPlayer.teleport(
                targetWorld,
                safePos.getX() + 0.5,
                safePos.getY(),
                safePos.getZ() + 0.5,
                player.getYaw(),
                player.getPitch()
        );
    }
    public static void deleteLocation(String name) {
        locations.removeIf(loc -> loc.getName().equals(name));
        saveLocationsToFile();
    }

    public static BlockPos findSafeSpot(ServerWorld world, double x, double y, double z) {
        BlockPos pos = new BlockPos(x, y, z);

        if (isSafeLocation(world, pos)) {
            return pos;
        }

        int radius = 1;
        int maxRadius = 16;

        while (radius <= maxRadius) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos checkPos = new BlockPos(
                            pos.getX() + dx,
                            pos.getY(),
                            pos.getZ() + dz
                    );

                    checkPos = findGroundLevel(world, checkPos);

                    if (isSafeLocation(world, checkPos)) {
                        return checkPos;
                    }
                }
            }
            radius++;
        }

        return world.getSpawnPos();
    }

    public static BlockPos findRandomSafeLocation(ServerWorld world) {
        int attempts = 0;
        int maxAttempts = 50;

        while (attempts < maxAttempts) {
            int x = random.nextInt(10000) - 5000;
            int z = random.nextInt(10000) - 5000;
            BlockPos pos = new BlockPos(x, 64, z);

            pos = findGroundLevel(world, pos);

            if (isSafeLocation(world, pos)) {
                return pos;
            }

            attempts++;
        }

        return world.getSpawnPos();
    }

    private static BlockPos findGroundLevel(ServerWorld world, BlockPos pos) {
        int maxY = world.getTopY();
        int minY = world.getBottomY();

        // Gestione speciale per il Nether
        if (world.getRegistryKey() == World.NETHER) {
            // Nel Nether, cerchiamo un punto sicuro circa a metà dell'altezza
            int netherSearchY = 64; // Altezza di partenza per il Nether

            // Cerca verso l'alto e verso il basso da questo punto
            for (int offset = 0; offset < 32; offset++) {
                // Prova prima sopra
                BlockPos upperCheck = new BlockPos(pos.getX(), netherSearchY + offset, pos.getZ());
                if (isSafeLocation(world, upperCheck)) {
                    return upperCheck;
                }

                // Poi prova sotto
                BlockPos lowerCheck = new BlockPos(pos.getX(), netherSearchY - offset, pos.getZ());
                if (isSafeLocation(world, lowerCheck)) {
                    return lowerCheck;
                }
            }

            // Se non troviamo nulla, torna alla posizione di default nel Nether
            return new BlockPos(pos.getX(), 64, pos.getZ());
        }

        // Per tutti gli altri mondi (superficie)
        for (int y = maxY; y >= minY; y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockPos abovePos = checkPos.up();

            if (world.getBlockState(checkPos).isSolidBlock(world, checkPos) &&
                    world.getBlockState(abovePos).isAir() &&
                    world.getBlockState(abovePos.up()).isAir()) {
                return abovePos;
            }
        }

        return new BlockPos(pos.getX(), 64, pos.getZ());
    }
    private static boolean isSafeLocation(ServerWorld world, BlockPos pos) {
        if (!world.getBlockState(pos).isAir() || !world.getBlockState(pos.up()).isAir()) {
            return false;
        }

        BlockPos below = pos.down();
        if (!world.getBlockState(below).isSolidBlock(world, below)) {
            return false;
        }

        return !world.getBlockState(below).getMaterial().isLiquid() &&
                !world.getBlockState(pos).getMaterial().isLiquid();
    }

    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public String getDimensionId() { return dimensionId; }

    public static void saveLocation(String name, double x, double y, double z, String dimension) {
        locations.add(new LocationManager(name, x, y, z, dimension));
        saveLocationsToFile();
    }

    public static void loadLocationsFromFile() {
        if (!locationsFile.exists()) {
            System.out.println("[Multiverse] No locations file found");
            return;
        }

        try (Reader reader = new FileReader(locationsFile)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<LocationManager>>() {}.getType();
            List<LocationManager> loadedLocations = gson.fromJson(reader, listType);

            if (loadedLocations != null) {
                locations.clear();
                locations.addAll(loadedLocations);
                System.out.println("[Multiverse] Loaded " + loadedLocations.size() + " locations from file");
            }
        } catch (IOException e) {
            System.err.println("[Multiverse] Error loading locations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void saveLocationsToFile() {
        try (Writer writer = new FileWriter(locationsFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(locations, writer);
            System.out.println("[Multiverse] Saved " + locations.size() + " locations to file");
        } catch (IOException e) {
            System.err.println("[Multiverse] Error saving locations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<LocationManager> getLocations() {
        return new ArrayList<>(locations);
    }
}