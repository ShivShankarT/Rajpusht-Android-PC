package in.rajpusht.pc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;

public class ContextWrapper extends android.content.ContextWrapper {

    public ContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context) {
        AppPreferencesHelper appPreferencesHelper = AppPreferencesHelper.getAppPreferencesHelper();
        if (appPreferencesHelper == null)
            appPreferencesHelper = new AppPreferencesHelper(context, AppPreferencesHelper.PREF_NAME);
        boolean english = appPreferencesHelper.isEnglish();
        Locale newLocale = new Locale(english ? "en" : "hi");
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);

            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);

            context = context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);

        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }

        return new ContextWrapper(context);
    }

    public static void setLocale(Activity context) {

        try {
            AppPreferencesHelper appPreferencesHelper = AppPreferencesHelper.getAppPreferencesHelper();
            if (appPreferencesHelper == null)
                appPreferencesHelper = new AppPreferencesHelper(context, AppPreferencesHelper.PREF_NAME);
            boolean english = appPreferencesHelper.isEnglish();
            Locale newLocale = new Locale(english ? "en" : "hi");
            Resources res = context.getResources();
            Configuration configuration = res.getConfiguration();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale);

                LocaleList localeList = new LocaleList(newLocale);
                LocaleList.setDefault(localeList);
                configuration.setLocales(localeList);

                context.createConfigurationContext(configuration);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(newLocale);
                context.createConfigurationContext(configuration);

            } else {
                configuration.locale = newLocale;
                res.updateConfiguration(configuration, res.getDisplayMetrics());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  context.recreate();//todo
//        this.setContentView(R.layout.activity_profile);
    }

}