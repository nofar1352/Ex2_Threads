import java.util.ArrayList;

/**
 * Checks whether a subset of integers in an {@code ArrayList<Integer>}
 * can produce a given target sum.
 * Implements Runnable to allow execution by multiple threads.
 */
public class ThreadCheckArray implements Runnable {

    /** Indicates whether this thread found a solution. */
    private boolean flag;

    /** Stores the selected elements of a valid solution. */
    private boolean[] winArray;

    /** Shared data used by the threads. */
    SharedData sd;

    /** The list of integers to be checked. */
    ArrayList<Integer> array;

    /** The target sum. */
    int b;

    /**
     * Constructs a ThreadCheckArray object using shared data.
     * Initializes the boolean array according to the list size.
     *
     * @param sd the shared data containing the integer list and target sum
     */
    public ThreadCheckArray(SharedData sd) {
        this.sd = sd;
        synchronized (sd) {
            array = sd.getArray();
            b = sd.getB();
        }
        winArray = new boolean[array.size()];
    }

    /**
     * Recursively checks whether a subset of the first n elements
     * can produce the remaining target sum.
     *
     * @param n the number of elements considered
     * @param b the remaining target sum
     */
    void rec(int n, int b) {
        synchronized (sd) {
            if (sd.getFlag()) return;
        }

        if (n == 1) {
            if (b == 0 || b == array.get(n - 1)) {
                flag = true;
                synchronized (sd) {
                    sd.setFlag(true);
                }
            }
            if (b == array.get(n - 1))
                winArray[n - 1] = true;
            return;
        }

        rec(n - 1, b - array.get(n - 1));

        if (flag)
            winArray[n - 1] = true;

        synchronized (sd) {
            if (sd.getFlag()) return;
        }

        rec(n - 1, b);
    }

    /**
     * Executes the subset-sum search.
     * The two threads explore different possibilities for the last element.
     * If a solution is found, the results are stored in the shared data.
     */
    @Override
    public void run() {
        if (array.size() != 1)
            if (Thread.currentThread().getName().equals("thread1"))
                rec(array.size() - 1, b - array.get(array.size() - 1));
            else
                rec(array.size() - 1, b);

        if (array.size() == 1)
            if (b == array.get(0) && !flag) {
                winArray[0] = true;
                flag = true;
                synchronized (sd) {
                    sd.setFlag(true);
                }
            }

        if (flag) {
            if (Thread.currentThread().getName().equals("thread1"))
                winArray[array.size() - 1] = true;

            synchronized (sd) {
                sd.setWinArray(winArray);
            }
        }
    }
}