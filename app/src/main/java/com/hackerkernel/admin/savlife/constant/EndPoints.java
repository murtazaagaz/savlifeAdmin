package com.hackerkernel.admin.savlife.constant;

/**
 * Created by Murtaza on 6/1/2016.
 */
public class EndPoints {
    private static final String SERVER_URL = "http://api.hackerkernel.com/savlife/";
    private static final String VERSION = "v1admin/";
    private static final String BASE_URL = SERVER_URL + VERSION;
    public static final String LOGIN = BASE_URL + "login.php",
            SEARCH_DONOR = BASE_URL + "searchDonor.php",
            IMAGE_BASE_URL = SERVER_URL;

}
