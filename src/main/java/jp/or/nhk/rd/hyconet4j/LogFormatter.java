package jp.or.nhk.rd.hyconet4j;

import java.io.StringWriter;
import java.io.PrintWriter;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.concurrent.atomic.AtomicInteger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneId;

import java.util.logging.Level;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;

public class LogFormatter extends Formatter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
    private static final Map<Level, String> levelMsgMap = Collections.unmodifiableMap(
        new HashMap<Level, String>() {{
            put(Level.SEVERE,  "ERROR");
            put(Level.WARNING, "WARN ");
            put(Level.INFO,    "INFO ");
            put(Level.CONFIG,  "CONF ");
            put(Level.FINE,    "DEBUG");
            put(Level.FINER,   "DEBUG");
            put(Level.FINEST,  "DEBUG");
        }}
    );

//    private AtomicInteger nameColumnWidth = new AtomicInteger(16);

    public static void applyToRoot() {
        applyToRoot(new ConsoleHandler());
    }

    public static void applyToRoot(Handler handler) {
        handler.setFormatter(new LogFormatter());
        Logger root = Logger.getLogger("");
        root.setUseParentHandlers(false);
        for (Handler h : root.getHandlers()) {
            if (h instanceof ConsoleHandler)
            root.removeHandler(h);
        }
        root.addHandler(handler);
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder(200);

        Instant instant = Instant.ofEpochMilli(record.getMillis());
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        sb.append(formatter.format(ldt));
        sb.append(" ");

        sb.append(levelMsgMap.get(record.getLevel()));
        sb.append(" ");

        String category;
        category =record.getLoggerName();
        sb.append(category);
        sb.append(" ");

        sb.append(formatMessage(record));

        sb.append(System.lineSeparator());
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
            }
        }

        return sb.toString();
    }
}
