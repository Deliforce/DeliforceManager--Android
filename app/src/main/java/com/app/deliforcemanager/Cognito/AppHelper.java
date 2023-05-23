/*
 * Copyright 2013-2017 Amazon.com,
 * Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the
 * License. A copy of the License is located at
 *
 *      http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, express or implied. See the License
 * for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.deliforcemanager.Cognito;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AppHelper {
    private static final String TAG = "AppHelper";
    // App settings

    private static AppHelper appHelper;
    private static CognitoUserPool userPool;
    private static String user;

    private static Map<String, String> firstTimeLogInUserAttributes;
    private static List<String> firstTimeLogInRequiredAttributes;

    /**
     * App secret associated with your app id - if the App id does not have an associated App secret,
     * set the App secret to null.
     * e.g. clientSecret = null;
     *  Change the next three lines of code to run this demo on your user pool
     */
    private static final String userPoolId = "ap-south-1_zNMeUGg0o";
    private static final String APP_CLIENT_ID = "5nmfivl3t9c7ud855qid7mf57j";
    private static final String clientSecret = "1frhcj0h9itsan97g7aggp1jokabd3q95noqouog7bta99h5if7j";

    /**
     * Set Your User Pools region.
     * e.g. if your user pools are in US East (N Virginia) then set cognitoRegion = Regions.US_EAST_1.
     */
    private static final Regions cognitoRegion = Regions.AP_SOUTH_1;
//    private static final String userPoolId = "ap-south-1_AQJJphEx4";

    public static void init(Context context) {
        //setData();

        if (appHelper != null && userPool != null) {
            return;
        }

        if (appHelper == null) {
            appHelper = new AppHelper();
        }

        if (userPool == null) {
            try {
                userPool = new CognitoUserPool(context, userPoolId, APP_CLIENT_ID, clientSecret, cognitoRegion);
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    public static CognitoUserPool getPool() {
        return userPool;
    }

    public static String getCurrUser() {
        return user;
    }

    public static void setUser(String newUser) {
        user = newUser;
    }

    public static String formatException(Exception exception) {
        String formattedString = "Internal Error";
        Log.getStackTraceString(exception);

        String temp = exception.getMessage();

        if (temp != null && temp.length() > 0) {
            formattedString = temp.split("\\(")[0];
            if (formattedString != null && formattedString.length() > 0) {
                return formattedString;
            }
        }

        return formattedString;
    }


    public static void setUserAttributeForDisplayFirstLogIn(Map<String, String> currAttributes, List<String> requiredAttributes) {
        firstTimeLogInUserAttributes = currAttributes;
        firstTimeLogInRequiredAttributes = requiredAttributes;
//        refreshDisplayItemsForFirstTimeLogin();
    }

    /*private static void refreshDisplayItemsForFirstTimeLogin() {

        for (Map.Entry<String, String> attr : firstTimeLogInUserAttributes.entrySet()) {
            if ("phone_number_verified".equals(attr.getKey()) || "email_verified".equals(attr.getKey())) {
                continue;
            }
            String message = "";
            if ((firstTimeLogInRequiredAttributes != null) && (firstTimeLogInRequiredAttributes.contains(attr.getKey()))) {
                message = "Required";
            }
            ItemToDisplay item = new ItemToDisplay(attr.getKey(), attr.getValue(), message, Color.BLACK, Color.DKGRAY, Color.parseColor("#329AD6"), 0, null);
            firstTimeLogInRequiredAttributes.size();
            //firstTimeLogInItemsCount++;
        }

        for (String attr : firstTimeLogInRequiredAttributes) {
            if (!firstTimeLogInUserAttributes.containsKey(attr)) {
                ItemToDisplay item = new ItemToDisplay(attr, "", "Required", Color.BLACK, Color.DKGRAY, Color.parseColor("#329AD6"), 0, null);
                //firstTimeLogInItemsCount++;
            }
        }
    }
*/

}
