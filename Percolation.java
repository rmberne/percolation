import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF grid;
    private final WeightedQuickUnionUF fullness;
    private final int size;
    private final boolean[] open;
    private final int virtualTop;
    private final int virtualBot;
    private int openedCount;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        size = n;
        openedCount = 0;
        grid = new WeightedQuickUnionUF(size * size + 2);
        fullness = new WeightedQuickUnionUF(size * size + 1);
        open = new boolean[size * size];
        virtualTop = index(size, size) + 1;
        virtualBot = index(size, size) + 2;
    }

    public void open(int row, int col) {
        validateIndices(row, col);

        int index = index(row, col);
        if (isOpen(row, col)) {
            return;
        }
        open[index] = true;
        openedCount++;

        // first row
        if (row == 1) {
            grid.union(virtualTop, index);
            fullness.union(virtualTop, index);
        }

        // last row
        if (row == size) {
            grid.union(virtualBot, index);
        }

        // connecting left node if open
        if (isValid(row, col - 1) && isOpen(row, col - 1)) {
            grid.union(index(row, col - 1), index);
            fullness.union(index(row, col - 1), index);
        }

        // connecting right node if open
        if (isValid(row, col + 1) && isOpen(row, col + 1)) {
            grid.union(index(row, col + 1), index);
            fullness.union(index(row, col + 1), index);
        }

        // connecting top node if open
        if (isValid(row - 1, col) && isOpen(row - 1, col)) {
            grid.union(index(row - 1, col), index);
            fullness.union(index(row - 1, col), index);
        }

        // connecting bottom node if open
        if (isValid(row + 1, col) && isOpen(row + 1, col)) {
            grid.union(index(row + 1, col), index);
            fullness.union(index(row + 1, col), index);
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return open[index(row, col)];
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        return fullness.connected(index(row, col), virtualTop);
    }

    public int numberOfOpenSites() {
        return openedCount;
    }

    public boolean percolates() {
        return grid.connected(virtualTop, virtualBot);
    }

    private int index(int row, int col) {
        validateIndices(row, col);
        return (size * (row - 1) + col) - 1;
    }

    private void validateIndices(int i, int j) {
        if (!isValid(i, j)) throw new IllegalArgumentException("Invalid indices provided.");
    }

    private boolean isValid(int i, int j) {
        i -= 1;
        j -= 1;
        return i >= 0 && j >= 0 && i < size && j < size;
    }
}