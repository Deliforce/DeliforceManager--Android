/*
 * Copyright (c) 2015 Akexorcist
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.app.deliforcemanager.LocalizationActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;



public class LanguageSetting {

    private static final String PREFERENCE_LANGUAGE = "pref_language";
    private static final String KEY_LANGUAGE = "key_language";
    private static String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();
    private static String currentLanguage = Locale.ENGLISH.getLanguage();

    public static void setDefaultLanguage(String language) {
        DEFAULT_LANGUAGE = language;
    }

    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    public static void setLanguage(Context context, String language) {
        currentLanguage = language;
        SharedPreferences.Editor editor = getLanguagePreference(context).edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public static void setLanguage(Context context, Locale locale) {
        setLanguage(context, locale.getLanguage());
    }

    public static String getLanguage() {
        return currentLanguage;
    }

    private static String getLanguage(Context context) {

        String returnLang;
        LoginPrefManager myPref = new LoginPrefManager(context);
        SharedPreferences shardpref = myPref.getShaPref();

        String lang = shardpref.getString("Lang", null);

        if (lang != null) {
            returnLang = lang;
        }else{
            returnLang = getLanguagePreference(context).getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
        }

        return returnLang;
    }

    private static SharedPreferences getLanguagePreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_LANGUAGE, Activity.MODE_PRIVATE);
    }

    public static void setDefaultLanguage(Locale locale) {
        LanguageSetting.setDefaultLanguage(locale.getLanguage());
    }

    private static Locale getLocale() {
        return getLocale(getLanguage());
    }

    public static Locale getLocale(Context context) {
        return getLocale(getLanguage(context));
    }

    private static Locale getLocale(String language) {
        return new Locale(language.toLowerCase(Locale.getDefault()));
    }



    public static String GetCurrencyValue(String currencyAmmount){
        return NumberFormat.getCurrencyInstance(getLocale()).format(currencyAmmount);
    }


    private static NumberFormat getNumberFormat(String currencyCode)
    {
        Currency currency = Currency.getInstance(currencyCode);

        Locale[] locales = NumberFormat.getAvailableLocales();

        for (Locale locale : locales)
        {
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

            if (numberFormat.getCurrency() == currency)
                return numberFormat;
        }

        return null;
    }

}