package ud.group9.moviemanager.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerMaster {
	static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
    static private Handler consoleHandler;

    /**
     * @brief Logger class setup
     * 
     * This class create and configure the global logger, specifying the output file
     * and logger level
     * @throws IOException A exception if something goes wrong with the input/output
     */
    static public void setup() throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


        logger.setLevel(Level.ALL);
        fileTxt = new FileHandler("log/Logging.txt", true);
        consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
        
        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

    }
    
    /**
     * @brief Convert a StackTrace to String
     * 
     * This function allows us to convert the stackTrace into a String, 
     * necessary to be able to print them to the log due to changes in how Java handles 
     * them internally.
     *
     * @param e Exception of which we want the StackTrace
     * @return StackTrace of the exception in the form of String
     */
    public static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);
        return sWriter.toString();
    }
}
