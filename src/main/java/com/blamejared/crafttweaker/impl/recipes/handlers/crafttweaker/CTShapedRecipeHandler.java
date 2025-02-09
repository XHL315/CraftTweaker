package com.blamejared.crafttweaker.impl.recipes.handlers.crafttweaker;

import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
import com.blamejared.crafttweaker.api.recipes.ReplacementHandlerHelper;
import com.blamejared.crafttweaker.api.util.StringUtils;
import com.blamejared.crafttweaker.impl.recipes.CTRecipeShaped;
import com.blamejared.crafttweaker.impl.recipes.helper.CraftingTableRecipeConflictChecker;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@IRecipeHandler.For(CTRecipeShaped.class)
public final class CTShapedRecipeHandler implements IRecipeHandler<CTRecipeShaped> {
    
    @Override
    public String dumpToCommandString(final IRecipeManager manager, final CTRecipeShaped recipe) {
        
        return String.format(
                "craftingTable.addShaped(%s, %s, %s%s);",
                StringUtils.quoteAndEscape(recipe.getId()),
                recipe.getCtOutput().getCommandString(),
                Arrays.stream(recipe.getCtIngredients())
                        .map(row -> Arrays.stream(row)
                                .map(IIngredient::getCommandString)
                                .collect(Collectors.joining(", ", "[", "]")))
                        .collect(Collectors.joining(", ", "[", "]")),
                recipe.getFunction() == null ? "" : ", (usualOut, inputs) => { ... }"
        );
    }
    
    @Override
    public Optional<Function<ResourceLocation, CTRecipeShaped>> replaceIngredients(final IRecipeManager manager, final CTRecipeShaped recipe, final List<IReplacementRule> rules) {
        
        return ReplacementHandlerHelper.replaceIngredientArray(
                this.flatten(recipe.getCtIngredients(), recipe.getRecipeWidth(), recipe.getRecipeHeight()),
                IIngredient.class,
                recipe,
                rules,
                newIngredients -> id -> new CTRecipeShaped(
                        id.getPath(),
                        recipe.getCtOutput(),
                        this.inflate(newIngredients, recipe.getRecipeWidth(), recipe.getRecipeHeight()),
                        recipe.getMirrorAxis(),
                        recipe.getFunction()
                )
        );
    }
    
    @Override
    public <U extends IRecipe<?>> boolean doesConflict(final IRecipeManager manager, final CTRecipeShaped firstRecipe, final U secondRecipe) {
        
        return CraftingTableRecipeConflictChecker.checkConflicts(manager, firstRecipe, secondRecipe);
    }
    
    private IIngredient[] flatten(final IIngredient[][] ingredients, final int width, final int height) {
        
        final IIngredient[] flattened = new IIngredient[width * height];
        for(int i = 0; i < flattened.length; ++i) {
            flattened[i] = ingredients[i / width][i % width];
        }
        return flattened;
    }
    
    private IIngredient[][] inflate(final IIngredient[] flattened, final int width, final int height) {
        
        final IIngredient[][] inflated = new IIngredient[width][height];
        for(int i = 0; i < flattened.length; ++i) {
            inflated[i / width][i % width] = flattened[i];
        }
        return inflated;
    }
    
}
