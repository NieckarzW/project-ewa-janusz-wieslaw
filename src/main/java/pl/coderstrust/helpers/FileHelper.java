package pl.coderstrust.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class FileHelper {
  private static final String ENCODING = "UTF-8";

  public void create(String filePath) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    File file = new File(filePath);
    if (!file.exists()) {
      file.createNewFile();
    }
  }

  public void delete(String filePath) {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    File file = new File(filePath);
    if (file.exists()) {
      file.delete();
    }
  }

  public boolean exists(String filePath) {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    return new File(filePath).exists();
  }

  public boolean isEmpty(String filePath) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    File file = new File(filePath);
    if (!file.exists()) {
      throw new FileNotFoundException("Cannot find file");
    }

    return file.length() == 0;
  }

  public void clear(String filePath) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    File file = new File(filePath);
    FileUtils.write(file, "", ENCODING);
  }

  public void writeLine(String filePath, String line) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    if (line == null) {
      throw new IllegalArgumentException("Line in file cannot be null");
    }
    File file = new File(filePath);
    FileUtils.writeLines(file, ENCODING, Collections.singleton(line), true);
  }

  public List<String> readLines(String filePath) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    File file = new File(filePath);
    FileUtils.readLines(file, ENCODING, " ", true);
    List<String> lines;
    return lines;
  }

  public String readLastLine(String filePath) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }

    try (ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(filePath), Charset.defaultCharset())) {
      return reader.readLine();
    }
  }

  public void removeLine(String filePath, int lineNumber) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    if (lineNumber == 0) {
      throw new IllegalArgumentException("Line number cannot be null");
    }
    File file = new File(filePath);
    FileUtils.readLines(file, ENCODING).remove(lineNumber);
    FileUtils.writeLines(file, ENCODING, Collections<List> lines, true);
  }
}

