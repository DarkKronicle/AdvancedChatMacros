package io.github.darkkronicle.advancedchatmacros.util;

import com.electronwill.nightconfig.core.file.FileConfig;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class TomlUtils {

    public FileConfig loadFile(File file) {
        FileConfig config = FileConfig.of(file);
        config.load();
        return config;
    }

    // https://mkyong.com/java/how-to-find-files-with-certain-extension-only/
    public List<Path> getFilesWithExtension(Path directory, String extension) throws IOException {
        if (!directory.toFile().isDirectory()) {
            throw new IllegalArgumentException("Directory has to be directory!");
        }

        List<Path> paths;

        try (Stream<Path> walk = Files.walk(directory)) {
            paths = walk
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().endsWith(extension))
                    .collect(Collectors.toList());
        }
        return paths;
    }

    public Optional<List<Path>> getFilesWithExtensionCaught(Path directory, String extension) {
        try {
            return Optional.of(getFilesWithExtension(directory, extension));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}