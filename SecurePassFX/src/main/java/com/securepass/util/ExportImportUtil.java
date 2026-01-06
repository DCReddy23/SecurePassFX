// src/main/java/com/securepass/util/ExportImportUtil.java
package com.securepass.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.securepass.model.Credential;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class ExportImportUtil {
    private static final Gson gson = new Gson();

    public static void exportToJson(List<Credential> credentials, File file) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(credentials, writer);
        }
    }

    public static List<Credential> importFromJson(File file) throws IOException {
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Credential>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
    }
}
