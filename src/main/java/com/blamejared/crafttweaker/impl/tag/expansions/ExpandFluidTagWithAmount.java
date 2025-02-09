package com.blamejared.crafttweaker.impl.tag.expansions;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.CTFluidIngredient;
import com.blamejared.crafttweaker.impl.tag.MCTagWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.fluid.Fluid;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@Document("vanilla/api/tags/ExpandFluidTagWithAmount")
@ZenCodeType.Expansion("crafttweaker.api.tag.MCTagWithAmount<crafttweaker.api.fluid.MCFluid>")
public class ExpandFluidTagWithAmount {
    
    @ZenCodeType.Method
    @ZenCodeType.Caster(implicit = true)
    public static CTFluidIngredient asIngredient(MCTagWithAmount<Fluid> _this) {
        
        return new CTFluidIngredient.FluidTagWithAmountIngredient(_this);
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.OR)
    public static CTFluidIngredient asList(MCTagWithAmount<Fluid> _this, CTFluidIngredient other) {
        
        List<CTFluidIngredient> elements = new ArrayList<>();
        elements.add(asIngredient(_this));
        elements.add(other);
        return new CTFluidIngredient.CompoundFluidIngredient(elements);
    }
    
}
