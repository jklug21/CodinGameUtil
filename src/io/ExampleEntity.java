package io;

/**
 * Based on the order of the input handling code provided by the codingame platform, define the indexes alike:<br>
 * <br>
 * <pre>
 *   int X = in.nextInt(); // -> 0
 *   int Y = in.nextInt(); // -> 1
 *   int hSpeed = in.nextInt(); // -> 2
 *   int vSpeed = in.nextInt(); // -> 3
 * </pre>
 */
public class ExampleEntity {
    @ScanIndex(0)
    private int x;
    @ScanIndex(1)
    private int y;
    @ScanIndex(2)
    private int hSpeed;
    @ScanIndex(3)
    private int vSpeed;
}
