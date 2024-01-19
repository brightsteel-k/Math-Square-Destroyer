package com.mathsquare.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mathsquare.objects.Board;

public class DataManager {
    public static Board deserializeBoard(String path) {
        String json = readFile(path);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Board board = gson.fromJson(json, Board.class);
        return BoardPatternManager.processBoard(board);
    }

    // REQUIRES: path leads to a place on device where a directory & file can be made
    // MODIFIES: device disk
    // EFFECTS: creates a new file at the given path
    public static void makeFile(String pathName) {
        File f = new File(pathName);
        try {
            f.createNewFile();
        } catch (Exception e) {
            throw new RuntimeException("DataManager.makeFile() failed. Path: " + pathName, e);
        }
    }

    // REQUIRES: path leads to an existing file
    // EFFECTS: returns the contents of the given file, assuming it was encoded text, in a String
    public static String readFile(String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("DataManager.readFile() failed. Path: " + path, e);
        }
    }

    // REQUIRES: path leads to an existing file
    // MODIFIES: device disk
    // EFFECTS: write the given contents String to the given file
    public static void writeFile(String path, String contents) {
        byte[] encoded = contents.getBytes(StandardCharsets.UTF_8);
        try {
            Files.write(Paths.get(path), encoded);
        } catch (Exception e) {
            throw new RuntimeException("DataManager.writeFile() failed. Path: " + path, e);
        }
    }
}
