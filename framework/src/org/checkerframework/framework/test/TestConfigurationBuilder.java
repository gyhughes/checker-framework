package org.checkerframework.framework.test;

import org.checkerframework.framework.util.PluginUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Used to create an instance of TestConfiguration, TestConfigurationBuilder follows the standard
 * builder pattern.  That is, it returns itself after every call so you can string together
 * configuration methods as follows:
 *
 * {@code
 *    new TestConfigurationBuilder()
 *        .addOption("-Awarns")
 *        .addSourceFile("src1.java")
 *        .addDiagnosticFile("src1.out")
 * }
 *
 * @see TestConfiguration
 */
public class TestConfigurationBuilder {

    // Presented first are static helper methods that reduce configuration building to a method call
    // However, if you need more complex configuration or custom configuration, use the
    // constructors provided below

    /**
     * This creates a builder for the default configuration used by Checker Framework JUnit tests.
     * @param testSourcePath the path to the Checker test file sources, usually this is the directory of Checker's tests
     * @param outputClassDirectory the directory to place classes compiled for testing
     * @param classPath the classpath to use for compilation
     * @param testSourceFiles the Java files that compose the test
     * @param processors the checkers or other annotation processors to run over the testSourceFiles
     * @param options the options to the compiler/processors
     * @param shouldEmitDebugInfo whether or not debug information should be emitted
     * @return the builder that will create an immutable test configuration
     */
    public static TestConfigurationBuilder getDefaultConfigurationBuilder(
            String testSourcePath,
            File outputClassDirectory,
            String classPath,
            Iterable<File> testSourceFiles,
            Iterable<String> processors,
            List<String> options,
            boolean shouldEmitDebugInfo) {

        TestConfigurationBuilder configBuilder = new TestConfigurationBuilder()
                .setShouldEmitDebugInfo(shouldEmitDebugInfo)
                .addProcessors(processors)
                .addOption("-Xmaxerrs", "9999")
                .addOption("-g")
                .addOption("-Xlint:unchecked")
                .addOption("-XDrawDiagnostics")  // use short javac diagnostics
                .addOption("-AprintErrorStack")
                .addSourceFiles(testSourceFiles);

        if (outputClassDirectory != null) {
            configBuilder.addOption("-d", outputClassDirectory.getAbsolutePath());
        }

        // Use the annotated jdk for the compile bootclasspath
        // This is set by build.xml
        String jdkJarPath = getJdkJarPathFromProperty();
        if (notNullOrEmpty(jdkJarPath)) {
            configBuilder.addOption("-Xbootclasspath/p:" + jdkJarPath);
        }

        configBuilder
                .addOptionIfValueNonEmpty("-sourcepath", testSourcePath)
                .addOption("-implicit:class")
                .addOption("-classpath", classPath);

        configBuilder.addOptions(options);
        return configBuilder;
    }

    /**
     * This returns a builder for the default configuration used by Checker Framework JUnit tests.
     * @param testSourcePath the path to the Checker test file sources, usually this is the directory of Checker's tests
     * @param testFile a single test java file to compile
     * @param checkerName a single Checker to include in the processors field
     * @param options the options to the compiler/processors
     * @param shouldEmitDebugInfo whether or not debug information should be emitted
     * @return the builder that will create an immutable test configuration
     */
    public static TestConfigurationBuilder getDefaultConfigurationBuilder(String testSourcePath,
                                                                          File testFile,
                                                                          String checkerName,
                                                                          List<String> options,
                                                                          boolean shouldEmitDebugInfo) {

        String classpath = getDefaultClassPath();
        File outputDir = getOutputDirFromProperty();
        List<File> javaFiles = Arrays.asList(testFile);
        List<String> processors = Arrays.asList(checkerName);
        return getDefaultConfigurationBuilder(testSourcePath, outputDir, classpath, javaFiles, processors,
                options, shouldEmitDebugInfo);
    }

    /**
     * This is the default configuration used by Checker Framework JUnit tests.
     * @param testSourcePath the path to the Checker test file sources, usually this is the directory of Checker's tests
     * @param outputClassDirectory the directory to place classes compiled for testing
     * @param classPath the classpath to use for compilation
     * @param testSourceFiles the Java files that compose the test
     * @param processors the checkers or other annotation processors to run over the testSourceFiles
     * @param options the options to the compiler/processors
     * @param shouldEmitDebugInfo whether or not debug information should be emitted
     * @return a TestConfiguration with input parameters added plus the normal default options, compiler,
     *         and file manager used by Checker Framework tests
     */
    public static TestConfiguration buildDefaultConfiguration(String testSourcePath,
                                                              File outputClassDirectory,
                                                              String classPath,
                                                              Iterable<File> testSourceFiles,
                                                              Iterable<String> processors,
                                                              List<String> options,
                                                              boolean shouldEmitDebugInfo) {
        TestConfigurationBuilder builder =
                getDefaultConfigurationBuilder(testSourcePath, outputClassDirectory, classPath,
                        testSourceFiles, processors, options, shouldEmitDebugInfo);
        return builder.validateThenBuild(true);
    }

    private static boolean notNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * This is the default configuration used by Checker Framework JUnit tests.
     * @param testSourcePath the path to the Checker test file sources, usually this is the directory of Checker's tests
     * @param testFile a single test java file to compile
     * @param checkerName a single Checker to include in the processors field
     * @param options the options to the compiler/processors
     * @param shouldEmitDebugInfo whether or not debug information should be emitted
     * @return a TestConfiguration with input parameters added plus the normal default options, compiler,
     *         and file manager used by Checker Framework tests
     */
    public static TestConfiguration buildDefaultConfiguration(String testSourcePath,
                                                              File testFile,
                                                              String checkerName,
                                                              List<String> options,
                                                              boolean shouldEmitDebugInfo) {

        TestConfigurationBuilder builder =
                getDefaultConfigurationBuilder(testSourcePath, testFile, checkerName, options, shouldEmitDebugInfo);
        return builder.validateThenBuild(true);
    }

    /** The list of files that contain Java diagnostics to compare against */
    private List<File> diagnosticFiles;

    /** The set of Java files to test against */
    private List<File> testSourceFiles;

    /** The set of Checker Framework processors to test with */
    private Set<String> processors;

    /** The set of options to the Javac command line used to run the test */
    private SimpleOptionMap options;

    /** Should the Javac options be output before running the test */
    private boolean shouldEmitDebugInfo;

    /**
     * Note: There are static helper methods named buildConfiguration and buildConfigurationBuilder
     * that can be used to create the most common types of configurations
     */
    public TestConfigurationBuilder() {
        diagnosticFiles = new ArrayList<>();
        testSourceFiles = new ArrayList<>();
        processors = new LinkedHashSet<>();
        options = new SimpleOptionMap();
        shouldEmitDebugInfo = false;
    }

    /**
     * Create a builder that has all of the optoins in initialConfig
     */
    public TestConfigurationBuilder(TestConfiguration initialConfig) {
        this.diagnosticFiles = new ArrayList<>(initialConfig.getDiagnosticFiles());
        this.testSourceFiles = new ArrayList<>(initialConfig.getTestSourceFiles());
        this.processors = new LinkedHashSet<>(initialConfig.getProcessors());
        this.options = new SimpleOptionMap();
        this.addOptions(initialConfig.getOptions());

        this.shouldEmitDebugInfo = initialConfig.shouldEmitDebugInfo();
    }

    /**
     * Ensures that the minimum requirements for running a test are met.
     * These requirements are:
     * <ul>
     *    <li> There is at least one source file
     *    <li> There is at least one processor (if requireProcessors has been set to true)
     *    <li> There is an output directory specified for class files
     *    <li> There is no -processor option in the optionMap (it should be added by addProcessor instead)
     * </ul>
     * @param requireProcessors whether or not to require that there is at least one processor
     * @return a list of errors found while validating this configuration
     */
    public List<String> validate(boolean requireProcessors) {
        List<String> errors = new ArrayList<>();
        if (testSourceFiles == null || !testSourceFiles.iterator().hasNext()) {
            errors.add("No source files specified!");
        }

        if (requireProcessors && !processors.iterator().hasNext()) {
            errors.add("No processors were specified!");
        }

        final Map<String, String> optionMap = options.getOptions();
        if (!optionMap.containsKey("-d") || optionMap.get("-d") == null) {
            errors.add("No output directory was specified.");
        }

        if (optionMap.containsKey("-processor")) {
            errors.add("Processors should not be added to the options list");
        }

        return errors;
    }

    public TestConfigurationBuilder adddToPathOption(String key, String toAppend) {
        options.addToPathOption(key, toAppend);
        return this;
    }

    public TestConfigurationBuilder addDiagnosticFile(File diagnostics) {
        this.diagnosticFiles.add(diagnostics);
        return this;
    }

    public TestConfigurationBuilder addDiagnosticFiles(Iterable<File> diagnostics) {
        this.diagnosticFiles = catListAndIterable(diagnosticFiles, diagnostics);
        return this;
    }

    public TestConfigurationBuilder setDiagnosticFiles(List<File> diagnosticFiles) {
        this.diagnosticFiles = new ArrayList<>(diagnosticFiles);
        return this;
    }

    public TestConfigurationBuilder addSourceFile(File sourceFile) {
        testSourceFiles.add(sourceFile);
        return this;
    }

    public TestConfigurationBuilder addSourceFiles(Iterable<File> sourceFiles) {
        testSourceFiles = catListAndIterable(testSourceFiles, sourceFiles);
        return this;
    }

    public TestConfigurationBuilder setSourceFiles(List<File> sourceFiles) {
        this.testSourceFiles = new ArrayList<>(sourceFiles);
        return this;
    }


    public TestConfigurationBuilder setOptions(Map<String, String> options) {
        this.options.setOptions(options);
        return this;
    }

    public TestConfigurationBuilder addOption(String option) {
        this.options.addOption(option);
        return this;
    }

    public TestConfigurationBuilder addOption(String option, String value) {
        this.options.addOption(option, value);
        return this;
    }


    public TestConfigurationBuilder addOptionIfValueNonEmpty(String option, String value) {
        if (value != null && !value.isEmpty()) {
            return addOption(option, value);
        }

        return this;
    }

    public TestConfigurationBuilder addOptions(Map<String, String> options) {
        this.options.addOptions(options);
        return this;
    }

    public TestConfigurationBuilder addOptions(Iterable<String> newOptions) {
        this.options.addOptions(newOptions);
        return this;
    }

    public TestConfigurationBuilder setProcessors(Iterable<String> processors) {
        this.processors.clear();
        for (String proc : processors) {
            this.processors.add(proc);
        }
        return this;
    }

    public TestConfigurationBuilder addProcessor(String processor) {
        this.processors.add(processor);
        return this;
    }

    public TestConfigurationBuilder addProcessors(Iterable<String> processors) {
        for (String processor : processors) {
            this.processors.add(processor);
        }

        return this;
    }

    public TestConfigurationBuilder emitDebugInfo() {
        this.shouldEmitDebugInfo = true;
        return this;
    }

    public TestConfigurationBuilder dontEmitDebugInfo() {
        this.shouldEmitDebugInfo = false;
        return this;
    }

    public TestConfigurationBuilder setShouldEmitDebugInfo(boolean shouldEmitDebugInfo) {
        this.shouldEmitDebugInfo = shouldEmitDebugInfo;
        return this;
    }

    /**
     * Creates a TestConfiguration using the settings in this builder.  The settings are NOT
     * validated first
     */
    public TestConfiguration build() {
        return new ImmutableTestConfiguration(diagnosticFiles, testSourceFiles, new ArrayList<>(processors),
                                              options.getOptions(), shouldEmitDebugInfo);
    }

    /**
     * Creates a TestConfiguration using the settings in this builder.  The settings are
     * first validated and a runtime exception is thrown if any errors are found
     * @param requireProcessors whether or not there should be at least 1 processor specified, see method validate
     */
    public TestConfiguration validateThenBuild(boolean requireProcessors) {
        List<String> errors = validate(requireProcessors) ;
        if (errors.isEmpty()) {
            return build();
        }

        throw new RuntimeException("Attempted to build invalid test configuration:\n"
                                 + "Errors:\n"
                                 + PluginUtil.join("\n", errors) + "\n"
                                 + this.toString() + "\n");
    }

    /**
     * @return the set of Javac options as a flat list
     */
    public List<String> flatOptions() {
        return options.getOptionsAsList();
    }

    @Override
    public String toString() {
        return "TestConfigurationBuilder:\n"
             + "testSourceFiles="  + ( testSourceFiles == null ? "null" : PluginUtil.join(" ", testSourceFiles) ) + "\n"
             + "processors="       + ( processors == null      ? "null" : PluginUtil.join(", ", processors)     ) + "\n"
             + "options="          + ( options == null         ? "null" : PluginUtil.join(", ", options.getOptionsAsList())  ) + "\n"
             + "shouldEmitDebugInfo=" + shouldEmitDebugInfo;
    }

    /**
     * @return a list that first has the items from parameter list then the items from iterable
     */
    private static <T> List<T> catListAndIterable(final List<T> list, final Iterable<? extends T> iterable) {
        final List<T> newList = new ArrayList<T>();

        for (T listObject : list) {
            newList.add(listObject);
        }

        for (T iterObject : iterable) {
            newList.add(iterObject);
        }

        return newList;
    }

    public static final String TESTS_OUTPUTDIR = "tests.outputDir";
    public static File getOutputDirFromProperty() {
        return new File(System.getProperty("tests.outputDir",
                        "tests" + File.separator + "build" + File.separator + "testclasses"));
    }

    public static String getDefaultClassPath() {
        String classpath = System.getProperty("tests.classpath",
                "tests" + File.separator + "build");
        String globalclasspath = System.getProperty("java.class.path", "");
        return "build" + File.pathSeparator +
               "junit-4.12.jar" + File.pathSeparator +
               "hamcrest-core-1.3.jar" + File.pathSeparator +
               classpath + File.pathSeparator +
               globalclasspath;
    }

    /**
     * Uses the system property "JDK_JAR" to find the annotated JDK
     */
    public static String getJdkJarPathFromProperty() {
        return System.getProperty("JDK_JAR");
    }

}
