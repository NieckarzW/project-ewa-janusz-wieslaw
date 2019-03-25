package pl.coderstrust.helpers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileHelperTestIT {

  private static final String INPUT_FILE = "src/test/resources/helpers/input_file";
  private static final String EXPECTED_FILE = "src/test/resources/helpers/expected_file";
  private static final String ENCODING = "UTF-8";
  private FileHelper fileHelper;


  @BeforeEach
  void setup() {
    fileHelper = new FileHelper();
    File inputFile = new File(INPUT_FILE);
    if (inputFile.exists()) {
      inputFile.delete();
    }

    File expectedFile = new File(EXPECTED_FILE);
    if (expectedFile.exists()) {
      expectedFile.delete();
    }
  }

  @Test
  void shouldCreateFile() throws IOException {
    //When
    fileHelper.create(INPUT_FILE);

    //Then
    assertTrue(new File(INPUT_FILE).exists());
  }

  @Test
  void shouldDeleteFile() throws IOException {
    //When
    fileHelper.delete(INPUT_FILE);

    //Then
    assertFalse(new File(INPUT_FILE).exists());
  }

  @Test
  void shouldClearFile() throws IOException {
    //Given
    File expectedFile = new File(EXPECTED_FILE);
    expectedFile.createNewFile();
    FileUtils.write(expectedFile, "", ENCODING);
    File inputFile = new File(INPUT_FILE);

    //When
    fileHelper.writeLine(INPUT_FILE, " ");

    //Then
    assertTrue(FileUtils.contentEquals(expectedFile, inputFile));
  }

  @Test
  void shouldReturnTrueIfFileExists() throws IOException {
    //Given
    File file = new File(INPUT_FILE);
    file.createNewFile();

    //When
    boolean result = fileHelper.exists(INPUT_FILE);

    //Then
    assertTrue(result);
  }

  @Test
  void shouldReturnTrueIfFileIsEmpty() throws IOException {
    //Given
    File file = new File(INPUT_FILE);
    file.length();

    //When
    boolean result = fileHelper.isEmpty(INPUT_FILE);

    //Then
    assertTrue(result);
  }

  @Test
  void shouldWriteLineToFile() throws IOException {
    //Given
    File expectedFile = new File(EXPECTED_FILE);
    expectedFile.createNewFile();
    FileUtils.writeLines(expectedFile, ENCODING, Collections.singleton("test test"), true);
    File inputFile = new File(INPUT_FILE);

    //When
    fileHelper.writeLine(INPUT_FILE, "test test");

    //Then
    assertTrue(FileUtils.contentEquals(expectedFile, inputFile));
  }

  @Test
  void shouldReadLinesFromFile() throws IOException {
    //Given
    File expectedFile = new File(EXPECTED_FILE);
    expectedFile.createNewFile();
    FileUtils.readLines(expectedFile, ENCODING);
    File inputFile = new File(INPUT_FILE);

    //When
    fileHelper.readLines(INPUT_FILE);

    //Then
    assertTrue(FileUtils.contentEquals(expectedFile, inputFile));
  }

  @Test
  void shouldReadLastLine() throws IOException {
    //Given
    File expectedFile = new File(EXPECTED_FILE);
    expectedFile.createNewFile();
    ReversedLinesFileReader reader = new ReversedLinesFileReader(expectedFile, Charset.defaultCharset());
    reader.readLine();
    File inputFile = new File(INPUT_FILE);

    //When
    fileHelper.readLastLine(INPUT_FILE);

    //Then
    assertTrue(FileUtils.contentEquals(expectedFile, inputFile));
  }

  @Test
  void shouldRemoveLine() throws IOException {
    //Given
    File expectedFile = new File(EXPECTED_FILE);
    expectedFile.createNewFile();
    List<String> lines = Collections.singletonList(FileUtils.readLines(expectedFile, ENCODING).remove(2));
    FileUtils.writeLines(expectedFile, ENCODING, lines, false);
    File inputFile = new File(INPUT_FILE);

    //When
    fileHelper.removeLine(INPUT_FILE, 2);

    //Then
    assertTrue(FileUtils.contentEquals(expectedFile, inputFile));
  }
}
