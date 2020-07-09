package jp.or.nhk.rd.hyconet4j;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.IOException;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.SimpleFormatter;
import java.util.logging.LogManager;
//trace, debug, info, warn, error
//FINEST - 非常に詳細なトレースメッセージ
//FINER - かなり詳細なトレースメッセージ
//FINE - 詳細なトレースメッセージ
//CONFIG - 静的な構成メッセージ
//INFO - 情報メッセージ
//WARNING - 警告メッセージ
//SEVERE - 重大なメッセージ

/**
 * HyconetLogger Class.
 * Logger for hyconet4j.
 */
public class _Logger  {
	private Logger logger = null;
	public static Level level = Level.FINE;

	public _Logger(String name) {
		logger = Logger.getLogger(name);
		logger.setUseParentHandlers(false);
		logger.setLevel(level);
		for (Handler h : logger.getHandlers()) {
			if (h instanceof ConsoleHandler) {
				logger.removeHandler(h);
			}
		}

		Handler handler = new ConsoleHandler();
		handler.setFormatter(new LogFormatter());
		handler.setLevel(level);
		logger.addHandler(handler);
	};

	public void error(String msg) {
		if(logger!=null) {
			logger.severe(msg);
		}
	}
	public void error(Exception ee) {
		if(logger!=null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ee.printStackTrace(pw);
			pw.flush();
			String str = sw.toString();
			logger.severe(str);
		}
	}
	public void warn(String msg) {
		if(logger!=null) {
			logger.warning(msg);
		}
	};
	public void info(String msg) {
		if(logger!=null) {
			logger.info(msg);
		}
	};

	public void debug(String msg) {
		if(logger!=null) {
			logger.fine(msg);
		}
	};

	public void trace(String msg) {
		if(logger!=null) {
			logger.finer(msg);
		}
	};
}
