package me.creepinson.creepinoutils.api.util;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class PairListUtils {
    public static PairList<Integer, Double> loadPairListDouble(int[] array) {
        PairList<Integer, Double> list = new PairList<>();
        int i = 0;
        while (i < array.length) {
            list.add(array[i], Double.longBitsToDouble((((long) array[i + 1]) << 32) | (array[i + 2] & 0xffffffffL)));
            i += 3;
        }
        return list;
    }

    public static PairList<Integer, Double> loadPairListInteger(int[] array) {
        PairList<Integer, Double> list = new PairList<>();
        int i = 0;
        while (i < array.length) {
            list.add(array[i], (double) array[i + 1]);
            i += 2;
        }
        return list;
    }

    public static PairList<Integer, Double> loadPairListDouble(int[] array, int from, int length) {
        PairList<Integer, Double> list = new PairList<>();
        int i = from;
        while (i < from + length) {
            list.add(array[i], Double.longBitsToDouble((((long) array[i + 1]) << 32) | (array[i + 2] & 0xffffffffL)));
            i += 3;
        }
        return list;
    }

    public static int[] savePairListDouble(PairList<Integer, Double> list) {
        if (list == null)
            return null;

        int[] array = new int[list.size() * 3];
        for (int i = 0; i < list.size(); i++) {
            Pair<Integer, Double> pair = list.get(i);
            array[i * 3] = pair.key;
            long value = Double.doubleToLongBits(pair.value);
            array[i * 3 + 1] = (int) (value >> 32);
            array[i * 3 + 2] = (int) value;
        }
        return array;
    }

    public static int[] savePairListInteger(PairList<Integer, Integer> list) {
        if (list == null)
            return null;

        int[] array = new int[list.size() * 2];
        for (int i = 0; i < list.size(); i++) {
            Pair<Integer, Integer> pair = list.get(i);
            array[i * 2] = pair.key;
            array[i * 2 + 1] = pair.value;
        }
        return array;
    }
}
