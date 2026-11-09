package com.route4me.sdk.testsupport;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Captures what the SDK logs through Log4j 2 while a unit test runs, at every level, so that
 * {@link MockHttp} can fail a test whose log lines contain the API key.
 *
 * <p>The SDK logs through the Log4j API only and ships no Log4j implementation. For the
 * tests, {@link CapturingContextFactory} is the Log4j logger context factory
 * ({@code log4j2.loggerContextFactory} in {@code src/test/resources/log4j2.component.properties};
 * a {@code Provider} subclass, the newer way, does not compile here: Lombok's annotation
 * processing then needs annotation classes that are not on the test class path). Its
 * loggers are enabled for every level ({@link Level#ALL}), so debug and trace messages are
 * built and checked too. They record each event while a capture is active, and print
 * {@code ERROR} and {@code FATAL} events to the standard error stream, as Log4j's own
 * fallback logger did before.
 *
 * <p>Only one capture is active at a time: a capture started inside another one hides the
 * outer one until it stops, so a test can check a nested {@link MockHttp} rule without
 * failing itself.
 */
public final class Log4jCapture {

    /** The events of the active capture; {@code null} when none is active. */
    private static volatile List<Event> active;

    private final List<Event> events = new CopyOnWriteArrayList<>();
    private final List<Event> previous;

    private Log4jCapture(List<Event> previous) {
        this.previous = previous;
    }

    /**
     * Starts capturing Log4j events, from every thread, until {@link #stop()}.
     *
     * @return the capture
     */
    public static synchronized Log4jCapture start() {
        Log4jCapture capture = new Log4jCapture(active);
        active = capture.events;
        return capture;
    }

    /** Stops capturing, and makes the capture that was active before this one active again. */
    public void stop() {
        synchronized (Log4jCapture.class) {
            if (active == events) {
                active = previous;
            }
        }
    }

    /**
     * Returns the events captured so far.
     *
     * @return the events, in order
     */
    public List<Event> events() {
        return Collections.unmodifiableList(new ArrayList<>(events));
    }

    /**
     * Tells whether the SDK's Log4j loggers are the capturing ones, that is whether the test
     * provider was found.
     *
     * @return {@code true} when {@link CapturingContextFactory} is the Log4j logger context factory
     */
    public static boolean isInstalled() {
        return LogManager.getFactory() instanceof CapturingContextFactory;
    }

    /** Records an event in the active capture, if there is one. */
    static void record(Event event) {
        List<Event> target = active;
        if (target != null) {
            target.add(event);
        }
    }

    /** One captured log event. */
    public static final class Event {
        private final String loggerName;
        private final Level level;
        private final String message;
        private final Throwable thrown;

        Event(String loggerName, Level level, String message, Throwable thrown) {
            this.loggerName = loggerName;
            this.level = level;
            this.message = message;
            this.thrown = thrown;
        }

        /** @return the name of the logger */
        public String getLoggerName() {
            return loggerName;
        }

        /** @return the level of the event */
        public Level getLevel() {
            return level;
        }

        /** @return the formatted message, with its parameters filled in */
        public String getMessage() {
            return message;
        }

        /** @return the throwable logged with the event, or {@code null} */
        public Throwable getThrown() {
            return thrown;
        }
    }

    /** Hands out the one capturing logger context. */
    public static final class CapturingContextFactory implements LoggerContextFactory {
        private static final CapturingContext CONTEXT = new CapturingContext();

        /** Creates the factory; Log4j calls it once. */
        public CapturingContextFactory() {
        }

        @Override
        public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext,
                                        boolean currentContext) {
            return CONTEXT;
        }

        @Override
        public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext,
                                        boolean currentContext, URI configLocation, String name) {
            return CONTEXT;
        }

        @Override
        public void removeContext(LoggerContext context) {
            // the one context lives as long as the test JVM
        }
    }

    /** Creates one capturing logger per name. */
    private static final class CapturingContext implements LoggerContext {
        private final ConcurrentMap<String, ExtendedLogger> loggers = new ConcurrentHashMap<>();

        @Override
        public Object getExternalContext() {
            return null;
        }

        @Override
        public ExtendedLogger getLogger(String name) {
            return getLogger(name, null);
        }

        @Override
        public ExtendedLogger getLogger(String name, MessageFactory messageFactory) {
            return loggers.computeIfAbsent(name, key -> new CapturingLogger(key, messageFactory));
        }

        @Override
        public boolean hasLogger(String name) {
            return loggers.containsKey(name);
        }

        @Override
        public boolean hasLogger(String name, MessageFactory messageFactory) {
            return loggers.containsKey(name);
        }

        @Override
        public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
            return loggers.containsKey(name);
        }
    }

    /**
     * A logger enabled for every level, which records its events and prints only errors.
     *
     * <p>It extends {@link AbstractLogger} rather than Log4j's {@code SimpleLogger}: the
     * constructor of that class takes a {@code PropertiesUtil}, whose class file refers to
     * annotations that are not on the test class path, so javac would warn.
     */
    @SuppressWarnings("checkstyle:ParameterNumber") // the Log4j API methods it overrides take up to 13 parameters
    private static final class CapturingLogger extends AbstractLogger {
        private static final long serialVersionUID = 1L;

        CapturingLogger(String name, MessageFactory messageFactory) {
            super(name, messageFactory);
        }

        @Override
        public Level getLevel() {
            return Level.ALL;
        }

        @Override
        public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
            String text = message == null ? null : message.getFormattedMessage();
            record(new Event(getName(), level, text, thrown));
            if (level.isMoreSpecificThan(Level.ERROR)) {
                System.err.println(level + " " + getName() + " - " + text);
                if (thrown != null) {
                    thrown.printStackTrace(System.err);
                }
            }
        }

        // Every level is enabled, whatever the marker, message and parameters.

        @Override
        public boolean isEnabled(Level level, Marker marker, Message message, Throwable thrown) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable thrown) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, Object message, Throwable thrown) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Throwable thrown) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2,
                                 Object p3) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2,
                                 Object p3, Object p4) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2,
                                 Object p3, Object p4, Object p5) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2,
                                 Object p3, Object p4, Object p5, Object p6) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2,
                                 Object p3, Object p4, Object p5, Object p6, Object p7) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2,
                                 Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
            return true;
        }

        @Override
        public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2,
                                 Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
            return true;
        }
    }
}
