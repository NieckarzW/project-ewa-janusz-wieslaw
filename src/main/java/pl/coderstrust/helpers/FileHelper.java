package pl.coderstrust.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
@Repository
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
      throw new IllegalArgumentException("Line cannot be less or equal to zero");
    }
    File file = new File(filePath);
    FileUtils.writeLines(file, ENCODING, Collections.singleton(line), true);
  }

  public List<String> readLines(String filePath) throws IOException {
    if (filePath == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    File file = new File(filePath);
    return FileUtils.readLines(file, ENCODING);
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
    if (lineNumber <= 0) {
      throw new IllegalArgumentException("Line number cannot be less or equal to zero.");
    }
    File file = new File(filePath);
    List<String> lines = FileUtils.readLines(file, ENCODING);
    lines.remove(lineNumber - 1);
    FileUtils.writeLines(file, ENCODING, lines, false);
  }
}
