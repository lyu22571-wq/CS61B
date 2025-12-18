package gh2;

import deque.Deque;
import deque.ArrayDeque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    // TODO: uncomment the following line once you're ready to start this portion
     private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        // TODO: Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this division operation into an int. For
        //       better accuracy, use the Math.round() function before casting.
        //       Your should initially fill your buffer array with zeros.
        // 创建一个容量为 SR / frequency 的缓冲区。你需要将此除法操作的结果强制转换为 int。
        // 为了获得更高的精度，建议在强制转换前使用 Math.round() 函数。
        // 缓冲区数组应最初用零填充。
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayDeque<>();
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }

    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // TODO: Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        // 将缓冲区中的所有元素出队，并用 -0.5 到 0.5 之间的随机数替换。
        // 你可以这样生成随机数：double r = Math.random() - 0.5;
        //
        // 确保每个随机数都不一样。你不需要检查它们是否不同，
        // 只需为每个数组索引重复调用 Math.random() - 0.5 即可生成新随机数。
        for (int i = 0; i < buffer.size(); i++) {
            buffer.removeFirst();
            buffer.addLast(Math.random() - 5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        // 出队缓冲区的第一个采样值，并将一个新采样值入队，
        // 新采样值为前两个采样值的平均值再乘以 DECAY 衰减因子。
        // **不要调用 StdAudio.play()。**
        double prev1 = buffer.removeFirst();
        double prev2 = buffer.get(0);
        double next = (prev1 + prev2) / 2 * DECAY;
        buffer.addLast(next);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}
