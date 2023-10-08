package com.lung.utils.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author haoyitao
 * @implSpec 冒泡排序
 * @since 2023/10/8 - 14:37
 * @version 1.0
 */
@Slf4j
public class BubbleSort {

    public static void sort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (arr[i] < arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            log.info("排序中：i={}, {}", i, Arrays.toString(arr));
        }
    }

    //[9, 13, 16, 8, 2, 5, 21, 3, 4, 1, 64, 6]

    public static void bubbleSortSwapped(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 要将 `List<Integer>` 转换为 `int[]` 数组，您可以使用以下方法：
     *
     * 1. 使用循环遍历 `List<Integer>` 并将其每个元素转换为对应的 `int` 类型，然后将其存储在新的 `int[]` 数组中。
     *
     *    ```java
     *    List<Integer> list = new ArrayList<>();
     *    // 假设您的List<Integer>已经被填充了一些整数值
     *
     *    int[] array = new int[list.size()];
     *    for (int i = 0; i < list.size(); i++) {
     *        array[i] = list.get(i);
     *    }
     *    ```
     *
     * 2. 使用 Java 8+ 的流式操作和方法引用来转换 `List<Integer>` 至 `int[]` 数组。
     *
     *    ```java
     *    List<Integer> list = new ArrayList<>();
     *    // 假设您的List<Integer>已经被填充了一些整数值
     *
     *    int[] array = list.stream().mapToInt(Integer::intValue).toArray();
     *    ```
     *
     * 无论您选择哪种方法，请确保在转换之前检查 `List<Integer>` 是否不为空，并且包含足够的元素以避免 `NullPointerException` 或其他异常。
     *
     * 希望这可以帮助到您。如有其他问题，请随时提问。
     * @param args
     */
    public static void main(String[] args) {
        List<Integer> list = IntStream.of(1, 2, 3, 4, 5, 6, 8, 9, 13, 16, 21, 64).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        int[] array = list.stream().mapToInt(Integer::intValue).toArray();
        System.out.println("排序前：" + list);
        bubbleSort(array);
        System.out.println("排序后：" + Arrays.toString(array));
    }



}
