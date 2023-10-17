import com.lung.utils.RandomUtil;
import com.lung.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestMain {

    private static final int N = 5;

    /**
     * 棋子
     */
    static BingoGird[][] bingoGirds;

    /**
     * 这段代码的作用是获取一个包含5个整数的列表。首先，它创建了一个空列表keys来存储结果。然后，它使用IntStream.range方法创建了一个从1到N的整数列表rows和cols。
     * 接下来，它随机选择了两个斜线，并将结果存储在excepts和hits列表中。然后，它根据hits列表中的值计算出斜线的行和列，并将对应位置的数字添加到keys列表中。
     * 接着，它随机选择一个中心点，并将中心点的数字添加到keys列表中。最后，它将剩余的行和列进行随机排序，并将对应位置的数字添加到keys列表中。最后，它返回了keys列表。
     * 代码的步骤如下：
     * 1. 创建一个空列表keys来存储结果。
     * 2. 创建一个从1到N的整数列表rows和cols。
     * 3. 随机选择两个斜线，并将结果存储在excepts和hits列表中。
     * 4. 根据hits列表中的值计算出斜线的行和列，并将对应位置的数字添加到keys列表中。
     * 5. 从cols列表中随机选择一个中心点，并将中心点的数字添加到keys列表中。
     * 6. 将剩余的行和列进行随机排序。
     * 7. 将对应位置的数字添加到keys列表中。
     * 8. 返回keys列表。
     * @return
     */
    private static List<Integer> get5Keys() {
        List<Integer> keys = new ArrayList<>();

        List<Integer> rows = IntStream.range(1, N + 1).boxed().collect(Collectors.toList());
        List<Integer> cols = IntStream.range(1, N + 1).boxed().collect(Collectors.toList());

        //先随机两条斜线
        List<Integer> excepts = new ArrayList<>(Collections.singletonList(N / 2 + 1));
        List<Integer> hits = RandomUtil.randomKeys(rows, 1, excepts);
        excepts.addAll(hits);
        hits.forEach(h -> excepts.add(N + 1 - h));
        hits.addAll(RandomUtil.randomKeys(rows, 1, excepts));
        rows.removeAll(hits);
        for (int i = 0; i < 2; i++) {
            int r = hits.get(i);
            int c = i == 0 ? r : N - r + 1;

            keys.add(bingoGirds[r - 1][c - 1].getNum());
            cols.remove(Integer.valueOf(c));
        }

        // 去中心点
        {
            Integer c = RandomUtil.randomKeys(cols, 1, Collections.singletonList(N / 2 + 1)).get(0);
            rows.remove(Integer.valueOf(N / 2 + 1));
            cols.remove(c);
            keys.add(bingoGirds[N / 2][c - 1].getNum());
        }


        Collections.shuffle(rows);
        Collections.shuffle(cols);
        for (int i = 0; i < 2; i++) {
            keys.add(bingoGirds[rows.get(i) - 1][cols.get(i) - 1].getNum());
        }

        return keys;
    }

    public static void initBingoBoard() {
        bingoGirds = new BingoGird[5][5];
        //每列随机元素集合
        List<Integer> nums;
        //单列已随机元素,占位
        List<Integer> temp;
        //单列已随机元素,占位
        for (int i = 0; i < bingoGirds.length; i++) {
            temp = new ArrayList<>();
            for (int j = 0; j < bingoGirds.length; j++) {
                if (bingoGirds[j][i] != null) {
                    continue;
                }
                BingoGird bingoGird = new BingoGird(j, i, 0, -1);
                if (i == 2 && j == 2) {
                    bingoGird.setState(1);
                    bingoGirds[j][i] = bingoGird;
                    continue;
                } else {
                    nums = CommonUtils.generateData(i * 15 + 1, (i + 1) * 15 + 1);
                    //去掉占位
                    nums.removeAll(temp);
                    int randomIdx = RandomUtil.randomInt(nums.size());
                    int num = nums.get(randomIdx);
                    bingoGird.setNum(num);
                    temp.add(num);
                }
                bingoGirds[j][i] = bingoGird;
            }
        }
    }

    //打印二维数组
    public static void printElementPositionArray(BingoGird[][] bingoGirds) {
        String[] positionArr = new String[bingoGirds.length];
        StringBuilder sb;
        for (int i = 0; i < bingoGirds.length; i++) {
            sb = new StringBuilder();
            for (int j = 0; j < bingoGirds.length; j++) {
                sb.append("|");
                sb.append(bingoGirds[i][j].getNum());
            }
            sb.append("|").append(System.lineSeparator());
            positionArr[i] = sb.toString();
        }
        System.out.println(Arrays.deepToString(positionArr));
    }

    public static void main(String[] args) {
        initBingoBoard();
        printElementPositionArray(bingoGirds);
        List<Integer> keys = get5Keys();
        System.out.println(keys);
    }
}
