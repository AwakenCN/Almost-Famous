import lombok.Getter;

/**
 * @author noseparte
 * @link github.com/noseparte
 * @since 2023/1/16 - 14:47
 * @implSpec Bingo格子
 */
@Getter
public class BingoGird {

    private int x;
    private int y;
    private int state;
    private int num;

    /**
     * default constructor
     */
    public BingoGird() {
    }

    public BingoGird(int x, int y, int state, int num) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.num = num;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPosition() {
        return 5 * x + (y + 1);
    }



}
