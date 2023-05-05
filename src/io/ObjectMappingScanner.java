package io;

import log.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Important: Currently only supports integer values!<br>
 * <br>
 * This class provides an object mapping from Codingame's stdio mechanism to provide the game state <br>
 * Instead of:<br>
 * <pre>
 * // Example of a default input handling provided by the platform
 * Scanner in = new Scanner(System.in);
 * int surfaceN = in.nextInt();
 * for (int i = 0; i < surfaceN; i++) {
 *   int landX = in.nextInt();
 *   int landY = in.nextInt();
 * }
 *
 * // game loop
 * while (true) {
 *   int X = in.nextInt();
 *   int Y = in.nextInt();
 *   int hSpeed = in.nextInt();
 *   int vSpeed = in.nextInt();
 * }
 * </pre>
 * <p>
 * you can then use:
 *
 * <pre>
 * ObjectMappingScanner in = new ObjectMappingScanner(new Scanner(System.in));
 * int surfaceN = in.nextInt(); // still possible if single values shall be read
 * for (int i = 0; i < surfaceN; i++) {
 *   LandCoordinate coordinate = in.getObjectFromInput(LandCoordinate.class); // see ExampleEntity for this particular one
 * }
 *
 * // game loop
 * while (in.hasNext()) {
 *   GameState gameState = in.getObjectFromInput(GameState.class);
 * }
 * </pre>
 * see <code>ExampleEntity</code> for how to setup the POJOs for use with this class
 */
public class ObjectMappingScanner {
    private final Scanner scanner;

    public ObjectMappingScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public <T> T getObjectFromInput(Class<T> type) {
        Field[] fields = type.getDeclaredFields();
        Map<Integer, Field> indexToFieldMap = new HashMap<>();
        int maxIndex = -1;
        for (Field field : fields) {
            ScanIndex index = field.getAnnotation(ScanIndex.class);
            if (index != null) {
                indexToFieldMap.put(index.value(), field);
                maxIndex = Math.max(index.value(), maxIndex);
            }
        }
        try {
            Constructor<T> defaultConstructor = type.getConstructor();
            T instance = defaultConstructor.newInstance();
            for (int i = 0; i <= maxIndex; i++) {
                int v = scanner.nextInt();
                Log.captureInput(v);
                Field field = indexToFieldMap.get(i);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(instance, v);
                }
            }
            return instance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("whoopsies", e);
        }
    }

    public int nextInt() {
        return scanner.nextInt();
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }
}
