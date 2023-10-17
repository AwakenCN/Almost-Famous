package com.lung.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Description: 随机数工具
 *
 * @Author: Jarvis
 * @Since: 2021-09-17 11:00
 */
public class RandomUtil {

    /**
     * random [0, max)
     *
     * @param max
     * @return
     */
    public static int randomInt(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }

    /**
     * random [min, max]
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max) {
        // 检测大小
        if (min > max) {
            int tmp = min;
            min = max;
            max = tmp;
        }
        max = max + 1;

        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * random [0, max)
     *
     * @param max
     * @return
     */
    public static double randomDouble(double max) {
        if (max <= 0.0D) {
            return 0.0D;
        }
        return ThreadLocalRandom.current().nextDouble(max);
    }

    /**
     * random [min, max)
     *
     * @param min
     * @param max
     * @return
     */
    public static double randomDouble(double min, double max) {
        if (min > max) {
            double tmp = min;
            min = max;
            max = tmp;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * 权重随机
     *
     * @param weights
     * @return
     */
    public static int randomWeightIndex(int[] weights) {
        int total = IntStream.of(weights).sum();
        return randomWeightIndex(weights, total);
    }

    /**
     * 权重随机
     *
     * @param weights
     * @param total
     * @return
     */
    public static int randomWeightIndex(int[] weights, int total) {
        if (total <= 0) {
            return -1;
        }
        int index = -1;
        int magicNum = randomInt(total) + 1;
        int p = 0;
        for (int i = 0, size = weights.length; i < size; i++) {
            p += weights[i];
            if (magicNum <= p) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * 根据指定随机算法来随机，权利是左闭右开
     *
     * @param random
     * @param weights
     * @return
     */
    public static int randomWeightIndex(Random random, int[] weights) {
        if (weights == null || weights.length == 0) {
            return -1;
        }

        int index = -1;
        int w = random.nextInt(IntStream.of(weights).sum());

        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
            if (w < sum) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * 权重随机
     *
     * @param weights
     * @return
     */
    public static int randomWeightIndex(List<Integer> weights) {
        int total = weights.stream().reduce(0, Integer::sum);
        return randomWeightIndex(weights, total);
    }

    /**
     * 权重随机
     *
     * @param weights
     * @param total
     * @return
     */
    public static int randomWeightIndex(List<Integer> weights, int total) {
        if (total <= 0) {
            return -1;
        }

        int index = -1;
        int magicNum = randomInt(total) + 1;
        int p = 0;
        for (int i = 0, size = weights.size(); i < size; i++) {
            p += weights.get(i);
            if (magicNum <= p) {
                index = i;
                break;
            }
        }

        return index;
    }


    /**
     * 权重随机
     *
     * @param weights
     * @return
     */
    public static int randomWeightIndex(double[] weights) {
        double total = DoubleStream.of(weights).sum();
        if (total <= 0D) {
            return -1;
        }

        int index = -1;
        double magicNum = randomDouble(total);
        double p = 0;
        for (int i = 0, size = weights.length; i < size; i++) {
            p += weights[i];
            if (magicNum <= p) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * 随机百分比概率
     *
     * @param rate
     * @return
     */
    public static boolean isHit100(int rate) {
        return randomInt(100) < rate;
    }

    /**
     * 随机千分比概率
     *
     * @param rate
     * @return
     */
    public static boolean isHit1000(int rate) {
        return randomInt(1000) < rate;
    }

    /**
     * 随机万分比概率
     *
     * @param rate
     * @return
     */
    public static boolean isHit10000(int rate) {
        return randomInt(10000) < rate;
    }

    /**
     * 随机多个
     *
     * @param maxSize
     * @param randomSize
     * @return
     */
    public static int[] randomIndexes(int maxSize, int randomSize) {
        if (maxSize < randomSize) {
            throw new IllegalArgumentException("need maxSize >= randomSize");
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            list.add(i);
        }
        int[] result = new int[randomSize];
        for (int i = 0; i < randomSize; i++) {
            result[i] = list.remove(randomInt(list.size()));
        }
        return result;
    }

    /**
     * 随机多个
     *
     * @param totals
     * @param randomNum
     * @param <T>
     * @return
     */
    public static <T> List<T> randomKeys(Collection<? extends T> totals, int randomNum) {
        List<T> list = new ArrayList<>(totals);
        Collections.shuffle(list);

        List<T> results = list.stream().limit(randomNum).collect(Collectors.toList());

        return results;
    }

    /**
     * 随机多个
     *
     * @param totals
     * @param randomNum
     * @param <T>
     * @return
     */
    public static <T> List<T> randomKeys(Collection<? extends T> totals, int randomNum, Collection<? extends T> excepts) {
        List<T> list = new ArrayList<>(totals);
        Collections.shuffle(list);
        list.removeAll(excepts);


        List<T> results = list.stream().limit(randomNum).collect(Collectors.toList());

        return results;
    }
}
