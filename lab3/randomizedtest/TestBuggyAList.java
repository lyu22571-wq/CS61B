package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing lstN = new AListNoResizing<Integer>();
        BuggyAList lstR = new BuggyAList<Integer>();
        lstN.addLast(4);
        lstN.addLast(5);
        lstN.addLast(6);


        lstR.addLast(4);
        lstR.addLast(5);
        lstR.addLast(6);


        assertEquals(lstN.size(), lstR.size());
        assertEquals(lstN.removeLast(), lstR.removeLast());
        assertEquals(lstN.removeLast(), lstR.removeLast());
        assertEquals(lstN.removeLast(), lstR.removeLast());
    }

    @Test
    public void testRamdom() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L1 = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L1.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size1 = L1.size();
                assertEquals(size, size1);
            } else if (operationNumber == 2) {
                if (L.size() > 0 && L1.size() > 0) {
                    int x = L.removeLast();
                    int x1 = L1.removeLast();
                    assertEquals(x, x1);
                }
            }
        }
    }

}
