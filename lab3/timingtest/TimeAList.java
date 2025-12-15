package timingtest;
import afu.org.checkerframework.checker.igj.qual.I;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {

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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        System.out.println("Timing table for addLast");
        //现在需要计算times，N，ops，输入进函数即可
        AList Ns = new AList<Integer>();
        AList times = new AList<Double>();
        AList opCounts = new AList<Integer>();
        for (int i = 1000; i <= 128000; i *= 2) {
            Stopwatch sw = new Stopwatch();
            AList lst = new AList<Integer>();
            for (int j = 0; j < i; j++) {
                lst.addLast(1);
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.addLast(i);
            times.addLast(timeInSeconds);
            opCounts.addLast(i);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
