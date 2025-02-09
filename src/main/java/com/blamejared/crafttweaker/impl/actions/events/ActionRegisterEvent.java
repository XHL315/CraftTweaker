package com.blamejared.crafttweaker.impl.actions.events;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.blamejared.crafttweaker.impl.events.EventHandlerWrapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.Consumer;

public class ActionRegisterEvent<T extends Event> implements IUndoableAction {
    
    private final Class<T> typeOfT;
    private final Consumer<T> consumer;
    private final EventPriority priority;
    
    public ActionRegisterEvent(Class<T> typeOfT, Consumer<T> consumer, EventPriority priority) {
        
        this.typeOfT = typeOfT;
        this.consumer = new EventHandlerWrapper<>(consumer);
        this.priority = priority;
    }
    
    @Override
    public void apply() {
        //Let's go completely safe and use the type
        MinecraftForge.EVENT_BUS.addListener(priority, false, typeOfT, consumer);
    }
    
    @Override
    public String describe() {
        
        return "Registering event listener for " + typeOfT.getSimpleName() + ".";
    }
    
    @Override
    public void undo() {
        
        MinecraftForge.EVENT_BUS.unregister(consumer);
    }
    
    @Override
    public String describeUndo() {
        
        return "Unregistering event listener for " + typeOfT.getSimpleName() + ".";
    }
    
}
