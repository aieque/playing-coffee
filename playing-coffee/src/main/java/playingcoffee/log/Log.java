package playingcoffee.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class Log {

	public static final String ANSI_RESET = "\033[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	private static FileWriter fileWriter;
	
	private static boolean useColors = false;
	
	public static void init() {
		try {
			File file = new File("log.txt");
			file.createNewFile();
			
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		info("Initialized logger");
	}
	
	public static void close() {
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String format, Object... args) {
		log("Info", ANSI_RESET, format, args);
	}
	
	public static void warn(String format, Object... args) {
		log("Warning", ANSI_YELLOW, format, args);
	}
	
	public static void error(String format, Object... args) {
		log("Error", ANSI_RED, format, args);
	}
	
	public static void fatal(String format, Object... args) {
		log("Fatal", ANSI_RED, format, args);
	}
	
	private static void log(String type, String ansiColor, String format, Object... args) {
		String finalMessage = "[" + LocalTime.now() + "] " + type + ": " + String.format(format, args);
		
		ansiColor = useColors ? ansiColor : "";
		
		System.out.println(ansiColor + finalMessage);
		try {
			fileWriter.write(finalMessage + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
