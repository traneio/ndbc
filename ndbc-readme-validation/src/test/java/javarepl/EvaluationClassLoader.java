package javarepl;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.io.URLs.toURL;

import java.net.URL;
import java.net.URLClassLoader;

import com.googlecode.totallylazy.Sequence;

// This is a patch so we can set the parent classloader (see constructor)
public class EvaluationClassLoader extends URLClassLoader {
  private Sequence<URL> registeredUrls = sequence();

  private EvaluationClassLoader(EvaluationContext context) {
    super(new URL[] { toURL().apply(context.outputDirectory()) }, Thread.currentThread().getContextClassLoader());
  }

  public static EvaluationClassLoader evaluationClassLoader(EvaluationContext context) {
    return new EvaluationClassLoader(context);
  }

  public void registerURL(URL url) {
    if (!sequence(getURLs()).contains(url)) {
      addURL(url);
      registeredUrls = registeredUrls.append(url);
    }
  }

  public Sequence<URL> registeredUrls() {
    return registeredUrls;
  }

  public boolean isClassLoaded(String name) {
    return findLoadedClass(name) != null;
  }
}
