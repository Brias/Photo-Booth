package de.braeuer.matthias.photobooth;

import java.util.ArrayList;

/**
 * Created by Matze on 11.06.2016.
 */
public class EmailAddressManager {
    private static ArrayList<String> emailAddresses;

    static {
        emailAddresses = new ArrayList<String>();
    }

    public static ArrayList<String> getEmailAddresses(){
        return emailAddresses;
    }

    public static void setEmailAdresses(ArrayList<String> emails){
        emailAddresses = emails;
    }

    public static void addEmailAddress(String emailAddress){
        emailAddresses.add(emailAddress);
    }

    public static void reset(){
        emailAddresses.clear();
    }
}
