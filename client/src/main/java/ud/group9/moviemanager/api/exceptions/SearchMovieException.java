package ud.group9.moviemanager.api.exceptions;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ud.group9.moviemanager.api.LoggerMaster;

public class SearchMovieException extends Exception {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public SearchMovieException(String s) {
        super(s);
        try {
            LoggerMaster.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
        LOGGER.log(Level.SEVERE, "Error while searching for a movie");
    }
}
