package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author lyu
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    //只需要实现 tilt，所以它应该是没用了
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    /**
     * 用于收集 null tile 的参数信息。
     */
   class nullTileInfoCollector {
       public static int nullCol;
       public static int nullRow;
    }

    public Tile findDestination(int startCol, int startRow) {
        //从上往下找
        for (int i = board.size() - 1; i > startRow; i -= 1) {
            Tile currTile = board.tile(startCol, i);
            if (currTile == null) {
                nullTileInfoCollector.nullCol = startCol;
                nullTileInfoCollector.nullRow = i;
                return currTile;
            } else if (currTile.value() == board.tile(startCol, startRow).value()) {
                return currTile;
            }
        }
        //最后肯定会找到自己，原地 TP
        return board.tile(startCol, startRow);
    }

    public boolean tilt(Side side) {
        boolean changed;
        changed = false;
        board.setViewingPerspective(side);

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        //i 是列 x，j 是行 y
        //依次尝试向上格数的上限是递增的
        for (int i = 0; i < board.size(); i += 1) {
            //最上面一行永远不用检查
            for (int j = board.size() - 2; j >= 0; j -= 1) {
                Tile t = board.tile(i, j);
                //空格不必检查
                if (t == null) {
                    continue;
                }
                Tile destinationTile = findDestination(i, j);

                if (destinationTile == null) {
                    board.move(nullTileInfoCollector.nullCol,nullTileInfoCollector.nullRow, t);
                    changed = true;
                    //产生移动
                } else if (board.move(destinationTile.col(),destinationTile.row(), t)) {
                    //实现 merge
                    int prevValue = t.value();
                    t = t.merge(i, j, destinationTile);
                    if (prevValue != t.value()) {
                        score += t.value();
                        changed = true;
                    }
                    //检查 board 是否发生了改变。
                    //改变：产生 2 种 merge 的其中一种，或产生移动->move
                }
            }
        }

        //TODO: 尚未完成，还需引入只能merge一次的机制，还有调整方向没有完成
        //似乎完成了，让我们测试一下
        /*
        这是一个 case，会产生多次 merge：
        2 16 2
        2 2  4
        4 2  8
        8 4  2
        可能的思路：
        1. 如果上一次有 merge，下一次迭代上界直接在上一次 merge 的行数之下
         */
        //首先实现向上，然后将其作为 north 情况，泛化到各方向。
        //这里还原方向的代码的放置位置存疑
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        for (int i = 0; i < b.size(); i += 1) {
            for (int j = 0; j < b.size(); j += 1) {
                if (b.tile(i, j) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        for (int i = 0; i < b.size(); i += 1) {
            for (int j = 0; j < b.size(); j += 1) {
                if (b.tile(i, j) != null && b.tile(i, j).value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b)) {
            return true;
        }
        for (int i = 0; i < b.size(); i += 1) {
            for (int j = 0; j < b.size(); j += 1) {
                if (i < b.size() - 1 && j < b.size() -1) {
                    if (b.tile(i, j).value() == b.tile(i + 1, j).value() || b.tile(i, j).value() == b.tile(i, j + 1).value()) {
                        return true;
                    }
                } else if (i == b.size() - 1 && j < b.size() - 1) {
                    if (b.tile(i, j).value() == b.tile(i, j + 1).value()) {
                        return true;
                    }
                } else if (i < b.size() - 1 && j == b.size() - 1) {
                    if (b.tile(i, j).value() == b.tile(i + 1, j).value()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
