package io.trane.ndbc.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javarepl.console.ConsoleConfig;
import javarepl.console.ConsoleLog;
import javarepl.console.ConsoleResult;
import javarepl.console.SimpleConsole;

public class ReadmeTest {

  private final Pattern       snippetPattern = Pattern.compile("```java(.*)```", Pattern.DOTALL);
  private final String        commentPattern = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";
  private final Path          readmePath;
  private final SimpleConsole c;

  public ReadmeTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    String fileName = "README.md";
    File file = new File(fileName);
    if (!file.exists())
      file = new File("../" + fileName);
    readmePath = file.toPath();
    c = new SimpleConsole(ConsoleConfig.consoleConfig());
  }

  @Before
  public void setup() {
    for (URL url : ((URLClassLoader) getClass().getClassLoader()).getURLs())
      execute(":cp " + url);
  }

  @After
  public void tearDown() {
    c.shutdown();
  }

  @Test
  public void verifyReadmeSnippets() throws IOException {
    String readme = new String(Files.readAllBytes(readmePath), "UTF-8").replaceAll(commentPattern, "");
    Matcher matcher = snippetPattern.matcher(readme);
    while (matcher.find())
      executeStatements(matcher.group(1));
  }

  private void executeStatements(String snippet) {
    int openParens = 0;
    int openCurlies = 0;
    String statement = "";
    for (int i = 0; i < snippet.length(); i++) {
      char c = snippet.charAt(i);
      statement = statement + c;
      if (c == '(') {
        openParens++;
      } else if (c == ')') {
        openParens--;
      } else if (c == '{') {
        openCurlies++;
      } else if (c == '}') {
        openCurlies--;
        if (openParens == 0 && openCurlies == 0) {
          execute(statement);
          statement = "";
        }
      } else if (c == ';' && openParens == 0 && openCurlies == 0) {
        execute(statement);
        statement = "";
      }
    }
  }

  private void execute(String statement) {
    String sanitized = statement.replaceAll("\n", " ");
    ConsoleResult r = c.execute(sanitized);
    System.out.println("javarepl: " + r);
    if (!r.logs().exists(l -> l.type() == ConsoleLog.Type.SUCCESS))
      throw new RuntimeException("Failed to validate readme snippet:\n" + r);
  }
}
