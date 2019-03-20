package pl.coderstrust;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.ReversedLinesFileReader;

public class FileHelper {

  public void create(String filePath) throws IOException {
    if (exists(filePath) == true) {
      File file = new File(filePath);
      file.createNewFile();
      System.out.println("File is created");
      FileWriter writer = new FileWriter(file);
      writer.write("");
      writer.close();
    } else {
      System.out.println("File already exists.");
    }
  }

  public void delete(String filePath) {
    File file = new File(filePath);

    if (file.delete()) {
      System.out.println("File deleted successfully");
    } else {
      System.out.println("Failed to delete the file");
    }
  }

  public boolean exists(String filePath) {
    if (filePath == null) {
      return false;
    } else {
      return true;
    }
  }

  public boolean isEmpty(String filePath) throws IOException {
    File file = new File(filePath);
    if (file.length() == 0) {
      return true;
    } else {
      return false;
    }
  }

  public void clear(String filePath) throws IOException {
    if (filePath == null) {
      throw new IOException("File path cannot be null");
    }

    FileWriter fw = new FileWriter(" ");
    PrintWriter pw = new PrintWriter(fw);
    pw.write("");
    pw.close();
  }

  public void writeLine(String filePath, String line) throws IOException {

    if (filePath == null) {
      throw new IOException("File path cannot be null");
    }
    if (line == null) {
      throw new IOException("Line in file cannot be null");
    }
    PrintWriter printWriter = new PrintWriter(new FileWriter(filePath));
    printWriter.println(line);
    printWriter.close();
  }

  public List<String> readLines(String filePath) throws IOException {
    if (filePath == null) {
      throw new IOException("File path cannot be null");
    }
    List<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(filePath));

    while (reader.ready()) {
      lines.add(reader.readLine());
    }
    return lines;
  }

  public String readLastLine(String filePath) throws IOException {

    if (filePath == null) {
      throw new IOException("File path cannot be null");
    }

    ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(filePath));
    String line = reader.readLine();
    String lastLine;
    while (line != null) {
    }
    lastLine = reader.readLine();

    reader.close();
    return lastLine;
  }

  public void removeLine(String filePath, int lineNumber) {}
}

