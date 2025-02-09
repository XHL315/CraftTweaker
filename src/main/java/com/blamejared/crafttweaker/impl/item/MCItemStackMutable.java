package com.blamejared.crafttweaker.impl.item;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.data.IData;
import com.blamejared.crafttweaker.api.ingredient.PartialNBTIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.util.AttributeUtil;
import com.blamejared.crafttweaker.impl.data.MapData;
import com.blamejared.crafttweaker.impl.helper.ItemStackHelper;
import com.blamejared.crafttweaker.impl.util.EnchantmentUtil;
import com.blamejared.crafttweaker.impl.util.text.MCTextComponent;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.StringTextComponent;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * An {@link MCItemStackMutable} object is the same as any other {@link IItemStack}.
 * The only difference is that changes made to it will not create a new ItemStack, but instead modify the stack given.
 *
 * This is useful for example when you are dealing with Event Handlers and need to shrink the stack the
 * player is using without assigning a new stack.
 *
 * @docParam this <item:minecraft:dirt>.mutable()
 */
@ZenRegister
@ZenCodeType.Name("crafttweaker.api.item.MCItemStackMutable")
@Document("vanilla/api/items/MCItemStackMutable")
public class MCItemStackMutable implements IItemStack {
    
    private final ItemStack internal;
    
    public MCItemStackMutable(ItemStack internal) {
        
        this.internal = internal;
    }
    
    @Override
    public IItemStack copy() {
        
        return new MCItemStackMutable(getInternal().copy());
    }
    
    @Override
    public IItemStack setDisplayName(String name) {
        
        getInternal().setDisplayName(new StringTextComponent(name));
        return this;
    }
    
    @Override
    public IItemStack withDisplayName(MCTextComponent text) {
        
        getInternal().setDisplayName(text.getInternal());
        return this;
    }
    
    @Override
    public IItemStack setAmount(int amount) {
        
        getInternal().setCount(amount);
        return this;
    }
    
    @Override
    public IItemStack grow(int amount) {
        
        getInternal().grow(amount);
        return this;
    }
    
    @Override
    public IItemStack shrink(int amount) {
        
        getInternal().shrink(amount);
        return this;
    }
    
    @Override
    public IItemStack withDamage(int damage) {
        
        getInternal().setDamage(damage);
        return this;
    }
    
    @Override
    public IItemStack withAttributeModifier(Attribute attribute, String uuid, String name, double value, AttributeModifier.Operation operation, EquipmentSlotType[] slotTypes, boolean preserveDefaults) {
        
        for(EquipmentSlotType slotType : slotTypes) {
            if(preserveDefaults) {
                AttributeUtil.addAttributeModifier(getInternal(), attribute, new AttributeModifier(UUID.fromString(uuid), name, value, operation), slotType);
            } else {
                getInternal().addAttributeModifier(attribute, new AttributeModifier(UUID.fromString(uuid), name, value, operation), slotType);
            }
        }
        return this;
    }
    
    @Override
    public IItemStack withAttributeModifier(Attribute attribute, String name, double value, AttributeModifier.Operation operation, EquipmentSlotType[] slotTypes, boolean preserveDefaults) {
        
        for(EquipmentSlotType slotType : slotTypes) {
            if(preserveDefaults) {
                AttributeUtil.addAttributeModifier(getInternal(), attribute, new AttributeModifier(name, value, operation), slotType);
            } else {
                getInternal().addAttributeModifier(attribute, new AttributeModifier(name, value, operation), slotType);
            }
        }
        return this;
    }
    
    @Override
    public IItemStack withTag(IData tag) {
        
        if(!(tag instanceof MapData)) {
            tag = new MapData(tag.asMap());
        }
        getInternal().setTag(((MapData) tag).getInternal());
        return this;
    }
    
    @Override
    public IItemStack withoutTag() {
        
        getInternal().setTag(null);
        return this;
    }
    
    @Override
    public String getCommandString() {
        
        return ItemStackHelper.getCommandString(this.getInternal(), true);
    }
    
    @Override
    public ItemStack getInternal() {
        
        return internal;
    }
    
    @Override
    public ItemStack getImmutableInternal() {
        
        return internal.copy();
    }
    
    @Override
    public int getDamage() {
        
        return getInternal().getDamage();
    }
    
    
    @Override
    public IItemStack setEnchantments(Map<Enchantment, Integer> enchantments) {
        
        EnchantmentUtil.setEnchantments(enchantments, getInternal());
        return this;
    }
    
    @Override
    public IItemStack withEnchantment(Enchantment enchantment, int level) {
        
        Map<Enchantment, Integer> enchantments = getEnchantments();
        enchantments.put(enchantment, level);
        EnchantmentUtil.setEnchantments(enchantments, getInternal());
        return this;
    }
    
    @Override
    public IItemStack removeEnchantment(Enchantment enchantment) {
        
        Map<Enchantment, Integer> enchantments = getEnchantments();
        enchantments.remove(enchantment);
        EnchantmentUtil.setEnchantments(enchantments, getInternal());
        return this;
    }
    
    @Override
    public IItemStack mutable() {
        
        return this;
    }
    
    @Override
    public IItemStack asImmutable() {
        
        return new MCItemStack(getInternal().copy());
    }
    
    @Override
    public boolean isImmutable() {
        
        return false;
    }
    
    @Override
    public Ingredient asVanillaIngredient() {
        
        if(getInternal().isEmpty()) {
            return Ingredient.EMPTY;
        }
        // You shouldn't be able to change a mutable stack after converting it to an ingredient
        if(!getInternal().hasTag()) {
            return Ingredient.fromStacks(getImmutableInternal());
        }
        return new PartialNBTIngredient(getImmutableInternal());
    }
    
    @Override
    public String toString() {
        
        return getCommandString();
    }
    
    @Override
    public IItemStack[] getItems() {
        
        return new IItemStack[] {this};
    }
    
    @Override
    @ZenCodeType.Operator(ZenCodeType.OperatorType.EQUALS)
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        //Implemented manually instead of using ItemStack.areItemStacksEqual to ensure it
        // stays the same as hashCode even if MC's impl would change
        final ItemStack thatStack = ((MCItemStackMutable) o).getInternal();
        final ItemStack thisStack = getInternal();
        
        if(thisStack.isEmpty()) {
            return thatStack.isEmpty();
        }
        
        if(thisStack.getCount() != thatStack.getCount()) {
            return false;
        }
        
        if(!Objects.equals(thisStack.getItem(), thatStack.getItem())) {
            return false;
        }
        
        if(!Objects.equals(thisStack.getTag(), thatStack.getTag())) {
            return false;
        }
        
        return thisStack.areCapsCompatible(thatStack);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hash(getInternal().getCount(), getInternal().getItem(), getInternal().getTag());
    }
    
}
