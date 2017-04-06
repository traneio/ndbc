package io.trane.ndbc.util;

public class NamedThreadFactory implements java.util.concurrent.ThreadFactory {

  private final String name;
  private final boolean daemon;

  public NamedThreadFactory(String name, boolean daemon) {
    super();
    this.name = name;
    this.daemon = daemon;
  }

  @Override
  public Thread newThread(Runnable r) {
    return null;
  }
}
