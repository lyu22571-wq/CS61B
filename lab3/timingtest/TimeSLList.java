package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        System.out.println("Timing table for getLast");
        //现在需要计算times，N，ops，输入进函数即可
        AList Ns = new AList<Integer>();
        AList times = new AList<Double>();
        AList opCounts = new AList<Integer>();
        for (int i = 1000; i <= 128000; i *= 2) {
            SLList lst = new SLList<Integer>();
            for (int j = 0; j < i; j++) {
                lst.addLast(1);
            }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < 10000; k++) {
                lst.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.addLast(i);
            times.addLast(timeInSeconds);
            opCounts.addLast(10000);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
