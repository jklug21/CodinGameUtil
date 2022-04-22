import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassSquasher {
    public static final String SOURCE_FOLDER = "../CodinGameSpringChallenge2022/src";
    public static final String MAIN_CLASS = "Player.java";

    public static void main(String[] args) throws IOException {
        String main = MAIN_CLASS;
        String source = SOURCE_FOLDER;

        List<String> imports = Files.list(Paths.get(source))
                .flatMap(ClassSquasher::readAllLines)
                .filter(line -> line.matches("import [\\w.]+;"))
                .distinct()
                .collect(Collectors.toList());

        Path sourcePath = Paths.get(source, main);
        List<String> mainFile = readAllLines(sourcePath)
                .filter(line -> !line.startsWith("package"))
                .filter(line -> !line.matches("import [\\w.]+;"))
                .collect(Collectors.toList());

        mainFile.remove(mainFile.size() - 1); // remove last line

        List<String> otherClasses = Files.list(Paths.get(source))
                .filter(p -> !p.toFile().getName().equals(main))
                .flatMap(ClassSquasher::readAllLines)
                .filter(line -> !line.startsWith("package"))
                .filter(line -> !line.matches("import [\\w.]+;"))
                .map(ClassSquasher::mutateClassDeclaration)
                .collect(Collectors.toList());

        Path outFile = Paths.get("src", main);
        System.out.println(outFile.toAbsolutePath());
        Files.deleteIfExists(outFile);
        Files.createFile(outFile);
        Files.write(outFile, imports);
        Files.write(outFile, mainFile, StandardOpenOption.APPEND);
        Files.write(outFile, otherClasses, StandardOpenOption.APPEND);
        Files.write(outFile, List.of("}"), StandardOpenOption.APPEND);
    }

    private static String mutateClassDeclaration(String line) {
        if (line.matches("(public )?class \\w+\\s*\\{\\s*")) {
            return "static " + line.replaceAll("public\\s?", "");
        } else {
            return line;
        }
    }

    private static Stream<? extends String> readAllLines(Path f) {
        try {
            return Files.readAllLines(f).stream();
        } catch (IOException e) {
            // crash and burn
            throw new RuntimeException("Well that sucks", e);
        }
    }
}
