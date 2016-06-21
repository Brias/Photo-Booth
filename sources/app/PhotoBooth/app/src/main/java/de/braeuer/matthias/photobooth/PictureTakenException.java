package de.braeuer.matthias.photobooth;

/**
 * Created by Matze on 21.06.2016.
 */
public class PictureTakenException extends Exception {
    private static final String MESSAGE = "Picture taken";

    public PictureTakenException() {
        super(MESSAGE);
    }
}
