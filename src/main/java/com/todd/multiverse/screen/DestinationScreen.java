package com.todd.multiverse.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.todd.multiverse.Multiverse;
import com.todd.multiverse.location.LocationManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DestinationScreen extends HandledScreen<DestinationScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Multiverse.MOD_ID, "textures/gui/destination_gui.png");
    private static final Identifier SAVE_LOCATION_PACKET = new Identifier("multiverse", "save_location");
    private static final Identifier TELEPORT_PACKET = new Identifier("multiverse", "teleport");
    private static final Identifier DELETE_LOCATION_PACKET = new Identifier("multiverse", "delete_location");

    private final List<String> dimensions = new ArrayList<>();
    private String selectedDimension = null;
    private LocationManager selectedLocation = null;
    private final List<LocationManager> savedLocations = new ArrayList<>();
    private final int backgroundWidth = 256;
    private final int backgroundHeight = 166;

    private TextFieldWidget nameField;
    private boolean isSaving = false;

    private float scrollPosition = 0;
    private boolean isScrolling = false;
    private static final int LOCATIONS_PER_PAGE = 5;
    private static final int DIMENSIONS_PER_PAGE = 5;
    private List<ButtonWidget> locationButtons = new ArrayList<>();
    private List<ButtonWidget> dimensionButtons = new ArrayList<>();
    private List<ButtonWidget> deleteButtons = new ArrayList<>();

    private float dimensionScrollPosition = 0;

    private LocationManager locationToDelete = null;
    private ButtonWidget confirmDeleteButton;
    private ButtonWidget cancelDeleteButton;
    private boolean showingDeleteConfirmation = false;

    public DestinationScreen(DestinationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        loadDimensions();
        loadSavedLocations();
    }

    public void updateDimensions(List<String> newDimensions) {
        this.dimensions.clear();
        this.dimensions.addAll(newDimensions);
        updateDimensionButtons();
    }

    private void updateDimensionButtons() {
        dimensionButtons.forEach(this::remove);
        dimensionButtons.clear();

        int startIndex = (int)(dimensionScrollPosition * Math.max(0, dimensions.size() - DIMENSIONS_PER_PAGE));
        int endIndex = Math.min(startIndex + DIMENSIONS_PER_PAGE, dimensions.size());

        for (int i = startIndex; i < endIndex; i++) {
            String dim = dimensions.get(i);
            ButtonWidget dimButton = new ButtonWidget(
                    this.x + 10,
                    this.y + 30 + (i - startIndex) * 22,
                    110,
                    20,
                    Text.of(formatDimensionName(dim)),
                    button -> selectDimension(dim)
            );
            dimensionButtons.add(dimButton);
            addDrawableChild(dimButton);
        }
    }

    private void loadDimensions() {
        dimensions.clear();
        dimensions.addAll(handler.getAvailableDimensions());
        if (dimensions.isEmpty()) {
            dimensions.add("minecraft:overworld");
            dimensions.add("minecraft:the_nether");
            dimensions.add("minecraft:the_end");
            dimensions.add("multiverse:crystal_hills");
        }
    }

    private void loadSavedLocations() {
        savedLocations.clear();
        savedLocations.addAll(handler.getSavedLocations());
        updateLocationButtons();
    }

    public void updateLocations(List<LocationManager> newLocations) {
        this.savedLocations.clear();
        this.savedLocations.addAll(newLocations);
        updateLocationButtons();
    }

    private void updateLocationButtons() {
        locationButtons.forEach(this::remove);
        deleteButtons.forEach(this::remove);
        locationButtons.clear();
        deleteButtons.clear();

        if (showingDeleteConfirmation) {
            return;
        }

        int startIndex = (int)(scrollPosition * Math.max(0, savedLocations.size() - LOCATIONS_PER_PAGE));
        int endIndex = Math.min(startIndex + LOCATIONS_PER_PAGE, savedLocations.size());

        for (int i = startIndex; i < endIndex; i++) {
            LocationManager loc = savedLocations.get(i);

            // Location button
            ButtonWidget locationButton = new ButtonWidget(
                    this.x + 136,
                    this.y + 30 + (i - startIndex) * 22,
                    85, // Reduced width to make room for delete button
                    20,
                    Text.of(loc.getName()),
                    btn -> selectLocation(loc)
            );
            locationButtons.add(locationButton);
            addDrawableChild(locationButton);

            // Delete button
            ButtonWidget deleteButton = new ButtonWidget(
                    this.x + 226,
                    this.y + 30 + (i - startIndex) * 22,
                    20,
                    20,
                    Text.of("×"),
                    btn -> showDeleteConfirmation(loc)
            );
            deleteButtons.add(deleteButton);
            addDrawableChild(deleteButton);
        }
    }

    private void showDeleteConfirmation(LocationManager location) {
        locationToDelete = location;
        showingDeleteConfirmation = true;

        // Hide regular buttons
        locationButtons.forEach(btn -> btn.visible = false);
        deleteButtons.forEach(btn -> btn.visible = false);

        // Show confirmation buttons
        confirmDeleteButton.visible = true;
        cancelDeleteButton.visible = true;
    }

    private void confirmDelete() {
        if (locationToDelete != null) {
            var buf = PacketByteBufs.create();
            buf.writeString(locationToDelete.getName());
            ClientPlayNetworking.send(DELETE_LOCATION_PACKET, buf);
        }
        hideDeleteConfirmation();
    }

    private void cancelDelete() {
        hideDeleteConfirmation();
    }

    private void hideDeleteConfirmation() {
        showingDeleteConfirmation = false;
        locationToDelete = null;
        confirmDeleteButton.visible = false;
        cancelDeleteButton.visible = false;
        updateLocationButtons();
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;

        this.nameField = new TextFieldWidget(
                this.textRenderer,
                this.x + 10,
                this.y + 115,
                110,
                20,
                Text.of("Location Name")
        );
        this.nameField.setVisible(false);
        this.addDrawableChild(this.nameField);

        // Add dimension buttons with scrolling functionality
        int dimButtonY = this.y + 30;
        for (int i = (int)(dimensionScrollPosition * Math.max(0, dimensions.size() - DIMENSIONS_PER_PAGE));
             i < Math.min(dimensions.size(), (int)(dimensionScrollPosition * Math.max(0, dimensions.size() - DIMENSIONS_PER_PAGE)) + DIMENSIONS_PER_PAGE);
             i++) {
            String dim = dimensions.get(i);
            ButtonWidget dimButton = new ButtonWidget(
                    this.x + 10,
                    dimButtonY,
                    110,
                    20,
                    Text.of(formatDimensionName(dim)),
                    button -> selectDimension(dim)
            );
            this.addDrawableChild(dimButton);
            dimButtonY += 22;
        }

        // Add confirmation buttons (initially invisible)
        confirmDeleteButton = new ButtonWidget(
                this.x + 136,
                this.y + 90,
                110,
                20,
                Text.of("Confirm Delete"),
                button -> confirmDelete()
        );
        cancelDeleteButton = new ButtonWidget(
                this.x + 136,
                this.y + 115,
                110,
                20,
                Text.of("Cancel"),
                button -> cancelDelete()
        );

        confirmDeleteButton.visible = false;
        cancelDeleteButton.visible = false;

        this.addDrawableChild(confirmDeleteButton);
        this.addDrawableChild(cancelDeleteButton);

        this.addDrawableChild(new ButtonWidget(
                this.x + 10,
                this.y + 140,
                110,
                20,
                Text.of(isSaving ? "Confirm Save" : "Save Location"),
                button -> {
                    if (!isSaving) {
                        startSaving();
                    } else {
                        saveCurrentLocation();
                    }
                }
        ));

        this.addDrawableChild(new ButtonWidget(
                this.x + 136,
                this.y + 140,
                110,
                20,
                Text.of("Teleport"),
                button -> teleport()
        ));

        updateLocationButtons();
    }

    private void startSaving() {
        isSaving = true;
        nameField.setVisible(true);
        nameField.setText("New Location");
        setTextFieldFocus(true);
    }

    private void setTextFieldFocus(boolean focused) {
        if (focused) {
            setFocused(nameField);
        } else {
            setFocused(null);
        }
    }

    private String formatDimensionName(String dimId) {
        return dimId.replace("minecraft:", "").replace("_", " ");
    }

    private void selectDimension(String dimension) {
        this.selectedDimension = dimension;
        this.selectedLocation = null;
    }

    private void selectLocation(LocationManager location) {
        this.selectedLocation = location;
        this.selectedDimension = null;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Scrollbar for dimensions
        if (dimensions.size() > DIMENSIONS_PER_PAGE) {
            int scrollbarY = y + 30 + (int)(dimensionScrollPosition * (100 - 15));
            fill(matrices, x + 248, y + 30, x + 254, y + 130, 0xFF808080);
            fill(matrices, x + 248, scrollbarY, x + 254, scrollbarY + 15, 0xFFCCCCCC);
        }
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= x + 10 && mouseX <= x + 120 && mouseY >= y + 30 && mouseY <= y + 130) {
            // Scrolling dimension list
            dimensionScrollPosition = (float) Math.max(0, Math.min(1, dimensionScrollPosition - (amount * 0.1f)));
            updateDimensionButtons();
            return true;
        } else if (mouseX >= x + 136 && mouseX <= x + 246 && mouseY >= y + 30 && mouseY <= y + 130) {
            // Scrolling location list
            scrollPosition = (float) Math.max(0, Math.min(1, scrollPosition - (amount * 0.1f)));
            updateLocationButtons();
            return true;
        }
        return false;
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        textRenderer.draw(matrices, "Dimensions", x + 10, y + 10, 4210752);
        textRenderer.draw(matrices, "Saved Locations", x + 136, y + 10, 4210752);

        if (showingDeleteConfirmation) {
            String confirmText = "Delete location '" + locationToDelete.getName() + "'?";
            textRenderer.draw(matrices, confirmText, x + 136, y + 70, 16733525);
        } else if (!isSaving) {
            String selectionInfo;
            if (selectedLocation != null) {
                selectionInfo = "Selected: " + selectedLocation.getName();
            } else if (selectedDimension != null) {
                selectionInfo = "Selected: " + formatDimensionName(selectedDimension);
            } else {
                selectionInfo = "No selection";
            }
            textRenderer.draw(matrices, selectionInfo, x + 10, y + 95, 16777215);
        }

        if (nameField.isVisible()) {
            nameField.render(matrices, mouseX, mouseY, delta);
        }

        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private void saveCurrentLocation() {
        if (selectedDimension == null && client.player != null) {
            selectedDimension = client.player.world.getRegistryKey().getValue().toString();
        }

        if (selectedDimension != null && client.player != null) {
            String name = nameField.getText();
            if (name.isEmpty()) {
                name = "Location " + (savedLocations.size() + 1);
            }

            var buf = PacketByteBufs.create();
            buf.writeString(name);
            buf.writeDouble(client.player.getX());
            buf.writeDouble(client.player.getY());
            buf.writeDouble(client.player.getZ());
            buf.writeString(selectedDimension);

            ClientPlayNetworking.send(SAVE_LOCATION_PACKET, buf);

            isSaving = false;
            nameField.setVisible(false);
            setTextFieldFocus(false);
        }
    }

    private void teleport() {
        if (selectedLocation != null || selectedDimension != null) {
            var buf = PacketByteBufs.create();

            if (selectedLocation != null) {
                buf.writeString(selectedLocation.getName());
                buf.writeDouble(selectedLocation.getX());
                buf.writeDouble(selectedLocation.getY());
                buf.writeDouble(selectedLocation.getZ());
                buf.writeString(selectedLocation.getDimensionId());
            } else {
                buf.writeString("random");
                buf.writeDouble(0);
                buf.writeDouble(64);
                buf.writeDouble(0);
                buf.writeString(selectedDimension);
            }

            ClientPlayNetworking.send(TELEPORT_PACKET, buf);
            this.close();
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
