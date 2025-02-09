package com.blamejared.crafttweaker.impl.actions.brewing;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.impl_native.potion.ExpandPotion;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ActionRemoveBrewingRecipeByPotionInput extends ActionBrewingBase {
    
    private final List<PotionBrewing.MixPredicate<Potion>> removed = new ArrayList();
    private final Potion input;
    
    public ActionRemoveBrewingRecipeByPotionInput(List<IBrewingRecipe> recipes, Potion input) {
        
        super(recipes);
        this.input = input;
    }
    
    @Override
    public void apply() {
        
        Iterator<PotionBrewing.MixPredicate<Potion>> iterator = PotionBrewing.POTION_TYPE_CONVERSIONS.iterator();
        while(iterator.hasNext()) {
            PotionBrewing.MixPredicate<Potion> mix = iterator.next();
            
            IRegistryDelegate<Potion> potionInput = mix.input;
            if(potionInput == null) {
                throw new RuntimeException("Error getting potion from mix: " + mix + "! Please make an issue on the issue tracker!");
            }
            if(potionInput.get().getRegistryName().equals(input.getRegistryName())) {
                removed.add(mix);
                iterator.remove();
            }
        }
    }
    
    @Override
    public void undo() {
        
        for(PotionBrewing.MixPredicate<Potion> potion : removed) {
            Ingredient itemReagent = getItemReagent(potion);
            IRegistryDelegate<Potion> potionOutput = potion.output;
            if(potion.input == null || itemReagent == null || potionOutput == null) {
                CraftTweakerAPI.logError("Error getting mix entries! potionInput: %s, itemReagent: %s, potionOutput: %s", potion.input, itemReagent, potionOutput);
                continue;
            }
            PotionBrewing.addMix(potion.input.get(), itemReagent.getMatchingStacks()[0].getItem(), potionOutput.get());
        }
    }
    
    @Override
    public String describe() {
        
        return "Removing Brewing recipes that have an input of: " + ExpandPotion.getCommandString(input);
    }
    
    @Override
    public String describeUndo() {
        
        return "Undoing removal of Brewing recipes that have an input of: " + ExpandPotion.getCommandString(input);
    }
    
}
