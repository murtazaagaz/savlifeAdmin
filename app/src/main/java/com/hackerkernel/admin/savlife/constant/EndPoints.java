package com.hackerkernel.admin.savlife.constant;

public class EndPoints {
    private static final String SERVER_URL = "http://api.hackerkernel.com/savlife/";
    private static final String VERSION = "v1admin/";
    private static final String BASE_URL = SERVER_URL + VERSION;
    public static final String LOGIN = BASE_URL + "login.php",
            SEARCH_DONOR = BASE_URL + "searchDonor.php",
            IMAGE_BASE_URL = SERVER_URL,
            ADD_DONOR = BASE_URL + "addDonor.php",
            ADD_DEAL = BASE_URL + "addDeals.php",
            ADD_ADMIN =  BASE_URL + "addAdmin.php",
            DEAL_LIST = BASE_URL + "getDealsList.php",
            SEND_NOTIFICATION = BASE_URL + "sendPushNotification.php",
            GET_NEXT_BILL_DATE = BASE_URL + "getNextBilldate.php",
            CHECK_BILL_DATE = BASE_URL + "checkBilldate.php";
}
