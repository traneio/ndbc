// package io.trane.ndbc.test;
//
// import java.lang.reflect.Field;
// import java.util.Map;
//
// import org.junit.runner.Description;
// import org.junit.runner.notification.RunNotifier;
// import org.junit.runners.BlockJUnit4ClassRunner;
// import org.junit.runners.model.FrameworkMethod;
// import org.junit.runners.model.InitializationError;
//
// import io.trane.ndbc.Config;
// import io.trane.ndbc.DataSource;
//
// public class NdbcTestRunner extends BlockJUnit4ClassRunner {
//
// private String currentLabel;
// public static Config currentConfig;
//
// private final Map<String, Config> configs;
//
// public NdbcTestRunner(Class<?> klass) throws InitializationError,
// InstantiationException, IllegalAccessException {
// super(klass);
// this.configs = ((NdbcTest) klass.newInstance()).configs;
// }
//
// @Override
// public void run(RunNotifier notifier) {
// configs.forEach((name, cfg) -> {
// currentLabel = name;
// currentConfig = cfg;
// super.run(notifier);
// });
// }
//
// @Override
// protected Description describeChild(FrameworkMethod method) {
// Description d = super.describeChild(method);
// Field f;
// try {
// f = Description.class.getDeclaredField("fDisplayName");
// f.setAccessible(true);
// f.set(d, currentLabel + " " + d.getDisplayName());
// } catch (Exception e) {
// throw new RuntimeException(e);
// }
// return d;
// }
//
// @Override
// protected Object createTest() throws Exception {
// NdbcTest test = (NdbcTest) super.createTest();
// test.ds = DataSource.fromConfig(currentConfig);
// return test;
// }
// }
