package me.creepinson.creepinoutils.util.animation;

import net.minecraft.nbt.CompoundNBT;

import java.util.Set;

import dev.throwouterror.util.Pair;
import dev.throwouterror.util.PairList;
import dev.throwouterror.util.math.Rotation;
import dev.throwouterror.util.math.Tensor;

public class AnimationState {

    private PairList<AnimationKey, Double> values = new PairList<>();

    public double get(AnimationKey key) {
        Double value = values.getValue(key);
        if (value == null)
            return key.getDefault();
        return value;
    }

    public AnimationState set(AnimationKey key, double value) {
        Pair<AnimationKey, Double> pair = values.getPair(key);

        if (key.getDefault() == value) {
            if (pair != null)
                values.removeKey(key);
            return this;
        }

        if (pair != null)
            pair.setValue(value);
        else
            values.add(key, value);
        return this;
    }

    public AnimationState(CompoundNBT nbt) {
        for (AnimationKey key : AnimationKey.getKeys())
            if (nbt.contains(key.name))
                values.add(key, nbt.getDouble(key.name));
    }

    public AnimationState() {

    }

    public Tensor getRotation() {
        return new Tensor((float) get(AnimationKey.rotX), (float) get(AnimationKey.rotY), (float) get(AnimationKey.rotZ));
    }

    public Tensor getOffset() {
        return new Tensor((float) get(AnimationKey.offX), (float) get(AnimationKey.offY), (float) get(AnimationKey.offZ));
    }

    public void clear() {
        values.clear();
    }

    public Set<AnimationKey> keys() {
        return values.keys();
    }

    public PairList<AnimationKey, Double> getValues() {
        return values;
    }

    public boolean isAligned() {
        for (Pair<AnimationKey, Double> pair : values)
            if (!pair.key.isAligned(pair.value))
                return false;
        return true;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        for (Pair<AnimationKey, Double> pair : values)
            nbt.putDouble(pair.key.name, pair.value);
        return nbt;
    }

    public void transform(Rotation rotation) {
        PairList<AnimationKey, Double> newPairs = new PairList<>();
        for (Pair<AnimationKey, Double> pair : values) {
            Pair<AnimationKey, Double> result = pair.key.transform(rotation, pair.value);
            if (result != null)
                newPairs.add(result);
            else
                newPairs.add(pair);
        }
        this.values = newPairs;
    }
}
