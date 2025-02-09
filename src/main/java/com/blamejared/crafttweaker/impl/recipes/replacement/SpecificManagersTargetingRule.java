package com.blamejared.crafttweaker.impl.recipes.replacement;

import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.ITargetingRule;
import net.minecraft.item.crafting.IRecipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class SpecificManagersTargetingRule implements ITargetingRule {
    
    private final Collection<IRecipeManager> recipeManagers;
    
    private SpecificManagersTargetingRule(final Collection<IRecipeManager> recipeManagers) {
        
        this.recipeManagers = Collections.unmodifiableCollection(recipeManagers);
    }
    
    public static SpecificManagersTargetingRule of(final Collection<IRecipeManager> recipeManagers) {
        
        if(recipeManagers.isEmpty()) {
            throw new IllegalArgumentException("Unable to create a specific managers targeting rule without any targets");
        }
        return new SpecificManagersTargetingRule(recipeManagers);
    }
    
    public static SpecificManagersTargetingRule of(final IRecipeManager... recipes) {
        
        return of(new HashSet<>(Arrays.asList(recipes)));
    }
    
    @Override
    public boolean shouldBeReplaced(final IRecipe<?> recipe, final IRecipeManager manager) {
        
        return this.recipeManagers.contains(manager);
    }
    
    @Override
    public String describe() {
        
        return this.recipeManagers.stream()
                .map(IRecipeManager::getCommandString)
                .collect(Collectors.joining(", ", "managers {", "}"));
    }
    
}
