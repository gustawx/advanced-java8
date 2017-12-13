package com.kustosz;

import com.kustosz.flatmap.Flatmap;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static Path getFilePath(String file){
        return Paths.get(Flatmap.class
                .getResource(file)
                .getPath()
                .replaceFirst("/",""));
    }

}
