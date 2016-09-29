package de.braeuer.matthias.photobooth;

import java.util.ArrayList;

/**
 * This class stores email addresses
 *
 * LICENSE: This file is subject of the GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 *
 * @author Matthias Bräuer
 * @version $Id: EmailAddressManager.java,v 1.0 2016/09/29 16:56:00 Exp $
 */
public class EmailAddressManager {
    private static ArrayList<String> emailAddresses;

    static {
        emailAddresses = new ArrayList<String>();
    }

    public static ArrayList<String> getEmailAddresses() {
        return emailAddresses;
    }

    public static void setEmailAdresses(ArrayList<String> emails) {
        emailAddresses = emails;
    }

    public static void addEmailAddress(String emailAddress) {
        emailAddresses.add(0, emailAddress);
    }

    public static void reset() {
        emailAddresses.clear();
    }

    public static String addressesToString() {
        String s = "";

        for (String email : emailAddresses) {
            if (!s.equals("")) {
                s += ",";
            }

            s += email;
        }

        return s;
    }
}
