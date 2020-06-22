package me.creepinson.creepinoutils.util.animation.event;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AnimationEvent implements Comparable<AnimationEvent> {

    private static HashMap<String, Class<? extends AnimationEvent>> eventTypes = new HashMap<>();
    private static HashMap<Class<? extends AnimationEvent>, String> eventTypeInv = new HashMap<>();
    private static List<String> typeNames = new ArrayList<>();
    private static List<String> typeNamesTranslated = new ArrayList<>();

    public static <T extends AnimationEvent> void registerAnimationEventType(String id, Class<T> eventClass) {
        if (eventTypes.containsKey(id))
            throw new IllegalArgumentException("Animation type '" + id + "' already exists!");
        eventTypes.put(id, eventClass);
        eventTypeInv.put(eventClass, id);
        typeNames.add(id);
        typeNamesTranslated.add("animation.event." + id + ".name");
    }

    public static Class<? extends AnimationEvent> getType(String id) {
        return eventTypes.get(id);
    }

    public static String getId(Class<? extends AnimationEvent> classEvent) {
        return eventTypeInv.get(classEvent);
    }

    public static List<String> typeNames() {
        return typeNames;
    }

    public static AnimationEvent create(int tick, String id) {
        Class<? extends AnimationEvent> eventClass = getType(id);
        if (eventClass == null)
            throw new RuntimeException("Found invalid AnimationEvent type '" + id + "'!");

        try {
            AnimationEvent event = eventClass.getConstructor(int.class).newInstance(tick);
            return event;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static AnimationEvent loadFromNBT(CompoundNBT nbt) {
        Class<? extends AnimationEvent> eventClass = getType(nbt.getString("id"));
        if (eventClass == null) {
            System.out.println("Found invalid AnimationEvent type '" + nbt.getString("id") + "'!");
            return null;
        }

        try {
            AnimationEvent event = eventClass.getConstructor(int.class).newInstance(nbt.getInt("tick"));
            event.activated = nbt.getBoolean("activated");
            event.read(nbt);
            return event;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    static {

    }

    private int tick;
    private boolean activated = false;

    public AnimationEvent(int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }

    public void reset() {
        activated = false;
    }

    public boolean shouldBeProcessed(int tick) {
        return this.tick <= tick && !activated;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        nbt.putString("id", getId(this.getClass()));
        nbt.putInt("tick", tick);
        nbt.putBoolean("activated", activated);
        write(nbt);
        return nbt;
    }

    protected abstract void write(CompoundNBT nbt);

    protected abstract void read(CompoundNBT nbt);

    public boolean process(Entity controller) {
        if (run(controller)) {
            activated = true;
            return true;
        }
        return false;
    }

    protected abstract boolean run(Entity controller);

    @Override
    public int compareTo(AnimationEvent o) {
        return Integer.compare(this.tick, o.tick);
    }
}
