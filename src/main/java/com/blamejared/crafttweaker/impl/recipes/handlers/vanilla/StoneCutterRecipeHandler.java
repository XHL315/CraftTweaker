package com.blamejared.crafttweaker.impl.recipes.handlers.vanilla;

import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
import com.blamejared.crafttweaker.api.util.StringUtils;
import com.blamejared.crafttweaker.impl.helper.ItemStackHelper;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.StonecuttingRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@IRecipeHandler.For(StonecuttingRecipe.class)
public final class StoneCutterRecipeHandler implements IRecipeHandler<StonecuttingRecipe> {
    
    @Override
    public String dumpToCommandString(final IRecipeManager manager, final StonecuttingRecipe recipe) {
        
        return String.format(
                "stoneCutter.addRecipe(%s, %s, %s);",
                StringUtils.quoteAndEscape(recipe.getId()),
                ItemStackHelper.getCommandString(recipe.getRecipeOutput()),
                IIngredient.fromIngredient(recipe.getIngredients().get(0)).getCommandString()
        );
    }
    
    @Override
    public Optional<Function<ResourceLocation, StonecuttingRecipe>> replaceIngredients(final IRecipeManager manager, final StonecuttingRecipe recipe, final List<IReplacementRule> rules) {
        
        return IRecipeHandler.attemptReplacing(recipe.getIngredients().get(0), Ingredient.class, recipe, rules)
                .map(input -> id -> new StonecuttingRecipe(id, recipe.getGroup(), input, recipe.getRecipeOutput()));
    }
    
}
