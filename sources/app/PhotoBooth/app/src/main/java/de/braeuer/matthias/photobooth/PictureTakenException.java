package de.braeuer.matthias.photobooth;

/**
 * This exception should be thrown when a picture is taken
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: PictureTakenException.java,v 1.0 2016/09/29 17:04:00 Exp $
 */
public class PictureTakenException extends Exception {
    private static final String MESSAGE = "Picture taken";

    public PictureTakenException() {
        super(MESSAGE);
    }
}
