package com.todd.multiverse.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class FluidDistillerRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public FluidDistillerRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;

        // Verifica che gli oggetti nei primi due slot corrispondano agli ingredienti
        return recipeItems.get(0).test(inventory.getStack(0)) &&
                recipeItems.get(1).test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        // Restituisce una copia dell'output per evitare modifiche accidentali
        return output.copy();
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2; // Richiede almeno 2 slot disponibili
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FluidDistillerRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "fluid_distilling";
    }

    public static class Serializer implements RecipeSerializer<FluidDistillerRecipe> {
        private Serializer() {}
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "fluid_distilling";

        @Override
        public FluidDistillerRecipe read(Identifier id, JsonObject json) {
            // Legge l'output della ricetta
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            // Legge gli ingredienti dagli slot di input
            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new FluidDistillerRecipe(id, output, inputs);
        }

        @Override
        public FluidDistillerRecipe read(Identifier id, PacketByteBuf buf) {
            // Legge gli ingredienti dal buffer
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            // Legge l'output dal buffer
            ItemStack output = buf.readItemStack();
            return new FluidDistillerRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, FluidDistillerRecipe recipe) {
            // Scrive gli ingredienti nel buffer
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }

            // Scrive l'output nel buffer
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
