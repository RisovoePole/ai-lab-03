package org.example;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;

public class Config {

    private static final Map<String, String> values = new HashMap<>();

    static {
        try (BufferedReader br = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Пропускаем комментарии и пустые строки
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    values.put(parts[0].trim(), parts[1].trim());
                }
            }
            System.out.println("[Config] .env файл успешно загружен.");
        } catch (IOException e) {
            System.out.println(
                "[Config] .env файл не найден. Используются значения по умолчанию."
            );
        }
    }

    public static int getInt(String key, int def) {
        return values.containsKey(key)
            ? Integer.parseInt(values.get(key))
            : def;
    }

    public static double getDouble(String key, double def) {
        return values.containsKey(key)
            ? Double.parseDouble(values.get(key))
            : def;
    }
}
