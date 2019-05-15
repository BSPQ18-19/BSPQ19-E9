package ud.group9.moviemanager.api.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchMovieException extends Exception {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = LogManager.getRootLogger();

	public SearchMovieException(String s) {
        super(s);
       
        LOGGER.warn(s.toString());
    }
}
