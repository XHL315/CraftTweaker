package com.blamejared.crafttweaker.impl.util.text;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @docParam this MCTextComponent.createStringTextComponent("Hello World!")
 */
@ZenRegister
@ZenCodeType.Name("crafttweaker.api.util.text.MCTextComponent")
@Document("vanilla/api/util/text/MCTextComponent")
@ZenWrapper(wrappedClass = "net.minecraft.util.text.ITextComponent", conversionMethodFormat = "%s.getInternal()", displayStringFormat = "%s.toString()")
public class MCTextComponent {
    
    private final ITextComponent internal;
    
    public MCTextComponent(ITextComponent internal) {
        
        this.internal = internal;
    }
    
    
    @ZenCodeType.Method
    @ZenCodeType.Getter("style")
    public MCStyle getStyle() {
        
        return new MCStyle(getInternal().getStyle());
    }
    
    @ZenCodeType.Method
    public MCTextComponent setStyle(MCStyle style) {
        
        IFormattableTextComponent newInternal = getInternal().deepCopy();
        newInternal.setStyle(newInternal.getStyle()
                .mergeStyle(style.getInternal()));
        return new MCTextComponent(newInternal);
    }
    
    @ZenCodeType.Method
    public static MCTextComponent createStringTextComponent(String text) {
        
        return new MCTextComponent(new StringTextComponent(text));
    }
    
    @ZenCodeType.Method
    public static MCTextComponent createTranslationTextComponent(String translationKey) {
        
        return new MCTextComponent(new TranslationTextComponent(translationKey));
    }
    
    @ZenCodeType.Method
    public static MCTextComponent createTranslationTextComponent(String translationKey, Object... args) {
        
        return new MCTextComponent(new TranslationTextComponent(translationKey, args));
    }
    
    @ZenCodeType.Method
    public String getUnformattedComponentText() {
        
        return getInternal().getUnformattedComponentText();
    }
    
    @ZenCodeType.Getter("unformattedComponentText")
    public String getUnformattedComponentTextGetter() {
        
        return getUnformattedComponentText();
    }
    
    @ZenCodeType.Method
    public String getString() {
        
        return getInternal().getString();
    }
    
    @ZenCodeType.Caster
    public String asString() {
        
        return getString();
    }
    
    @ZenCodeType.Method
    public String getStringTruncated(int maxLen) {
        
        return getInternal().getStringTruncated(maxLen);
    }
    
    @ZenCodeType.Getter("siblings")
    public List<MCTextComponent> getSiblings() {
        
        return getInternal().getSiblings()
                .stream()
                .map(MCTextComponent::new)
                .collect(Collectors.toList());
    }
    
    @ZenCodeType.Method
    public MCTextComponent appendSibling(MCTextComponent component) {
        
        IFormattableTextComponent newInternal = getInternal().deepCopy();
        newInternal.getSiblings().add(component.getInternal());
        return new MCTextComponent(newInternal);
    }
    
    @ZenCodeType.Method
    public MCTextComponent copyRaw() {
        
        return new MCTextComponent(getInternal().copyRaw());
    }
    
    @ZenCodeType.Method
    public MCTextComponent deepCopy() {
        
        return new MCTextComponent(getInternal().deepCopy());
    }
    
    
    @ZenCodeType.Method
    public MCTextComponent appendText(String text) {
        
        IFormattableTextComponent newInternal = getInternal().deepCopy();
        newInternal.getSiblings().add(new StringTextComponent(text));
        return new MCTextComponent(newInternal);
    }
    
    @ZenCodeType.Getter("formattedText")
    public String getFormattedText() {
        
        return getInternal().getString();
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.CAT)
    public MCTextComponent opAppend(MCTextComponent component) {
        
        return appendSibling(component);
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.ADD)
    public MCTextComponent opAdd(MCTextComponent component) {
        
        return appendSibling(component);
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.SHL)
    public MCTextComponent opShLeft(MCTextComponent component) {
        
        return appendSibling(component);
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.CAT)
    public MCTextComponent opCat(String text) {
        
        return appendText(text);
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.ADD)
    public MCTextComponent opAdd(String text) {
        
        return appendText(text);
    }
    
    @ZenCodeType.Operator(ZenCodeType.OperatorType.SHL)
    public MCTextComponent opLShift(String text) {
        
        return appendText(text);
    }
    
    public ITextComponent getInternal() {
        
        return internal;
    }
    
    /**
     * If this is a Translation Text Component, return the actual translation key used for localization.
     * If this is not a Translation Text Component, returns an empty string.
     *
     * @return the translation key or an empty string.
     */
    @ZenCodeType.Method
    @ZenCodeType.Getter("translationKey")
    public String getTranslationKey() {
        
        if(getInternal() instanceof TranslationTextComponent) {
            return ((TranslationTextComponent) getInternal()).getKey();
        }
        
        return "";
    }
    
}
