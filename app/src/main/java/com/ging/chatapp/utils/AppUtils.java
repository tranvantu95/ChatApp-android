package com.ging.chatapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ging.chatapp.BuildConfig;
import com.ging.chatapp.R;

public class AppUtils {

    public static boolean sendEmail(Context context, String to, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + to));
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email_with)));
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void shareApp(Context context, String appId) {
        shareText(context, context.getString(R.string.text_share_my_app) + ": " + getAppWebUrl(appId));
    }

    public static void shareText(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_with)));
    }

    public static void copyText(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if(clipboard != null) {
            ClipData clip = ClipData.newPlainText("simple text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static void openApp(Context context, String appId) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(appId);
        if(intent != null) context.startActivity(intent);
        else openAppInMarket(context, appId);
    }

    public static void openAppInMarket(Context context, String appId) {
        if(!openUrl(context, getAppMarketUrl(appId))) openUrl(context, getAppWebUrl(appId));
    }

    public static boolean openUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getMyAppMarketUrl(Context context) {
        return getAppMarketUrl(getMyAppId(context));
    }

    public static String getMyAppWebUrl(Context context) {
        return getAppWebUrl(getMyAppId(context));
    }

    public static String getAppMarketUrl(String appId) {
        return "market://details?id=" + appId;
    }

    public static String getAppWebUrl(String appId) {
        return "https://play.google.com/store/apps/details?id=" + appId;
    }

    public static String getMyAppId(Context context) {
        return BuildConfig.APPLICATION_ID;
        // or
//        return context.getPackageName();
    }

    public static String getMyAppName(Context context) {
        return context.getResources().getString(R.string.app_name);
    }

    // keyboard
    public static boolean isShowKeyboard(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager != null && inputMethodManager.isAcceptingText();
    }

    public static void showKeyboard(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideKeyboard(Activity context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = context.getCurrentFocus();
        if(inputMethodManager != null && currentFocus != null && currentFocus.getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            currentFocus.clearFocus();
        }
    }

    // resources
    public static Bitmap getBitmapFromVectorDrawable(Resources resources, int resId) {
        return getBitmapFromDrawable(getVectorDrawable(resources, resId));
    }

    public static Drawable getVectorDrawable(Resources resources, int resId) {
        if(currentVersionSupportVectorDrawable()) return resources.getDrawable(resId, null);
        return VectorDrawableCompat.create(resources, resId, null);
    }

    public static Bitmap getBitmapFromDrawable(Resources resources, int resId) {
        return getBitmapFromDrawable(resources.getDrawable(resId));
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if(drawable == null) return null;

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    // Fragment
    public static int getPagerPosition(Fragment fragment) {
        int index = -1;
        String tag = fragment.getTag();
        if(tag != null) index = tag.lastIndexOf(":");

        if(index >= 0)
            try {
                index = Integer.parseInt(tag.substring(index + 1));
            }
            catch (Exception e) {
                e.printStackTrace();
                index = -1;
            }

        return index;
    }

    public static Fragment findFragment(FragmentManager fm, int viewPagerId, int pagerPosition) {
        return fm.findFragmentByTag("android:switcher:" + viewPagerId + ":" + pagerPosition);
    }

    @Nullable
    public static Fragment getFragment(FragmentManager fm, Class<? extends Fragment> clazz, int viewPagerId, int pagerPosition) {
        Fragment fragment = findFragment(fm, viewPagerId, pagerPosition);
        if(fragment != null) return fragment;
        return newInstanceFragment(clazz);
    }

    @Nullable
    public static Fragment getFragment(FragmentManager fragmentManager, Class<? extends Fragment> clazz) {
        Fragment fragment = fragmentManager.findFragmentByTag(clazz.getName());
        if(fragment != null) return fragment;
        return newInstanceFragment(clazz);
    }

    @Nullable
    private static Fragment newInstanceFragment(Class<? extends Fragment> clazz) {
        Fragment fragment = null;

        try {
            fragment = clazz.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return fragment;
    }

    @Nullable
    public static Fragment addFragment(FragmentManager fragmentManager, @IdRes int containerViewId, Class<? extends Fragment> clazz, boolean addToBackStack) {
        Fragment fragment = getFragment(fragmentManager, clazz);
        if(fragment != null) addFragment(fragmentManager, containerViewId, fragment, addToBackStack);
        return fragment;
    }

    public static void addFragment(FragmentManager fragmentManager, @IdRes int containerViewId, Fragment fragment, boolean addToBackStack) {
        addFragment(fragmentManager, containerViewId, fragment, fragment.getClass().getName(), addToBackStack);
    }

    public static void addFragment(FragmentManager fragmentManager, @IdRes int containerViewId, Fragment fragment, String tag, boolean addToBackStack) {
        if(fragment.isAdded()) return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        if(addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Nullable
    public static Fragment replaceFragment(FragmentManager fragmentManager, @IdRes int containerViewId, Class<? extends Fragment> clazz, boolean addToBackStack) {
        Fragment fragment = getFragment(fragmentManager, clazz);
        if(fragment != null) replaceFragment(fragmentManager, containerViewId, fragment, addToBackStack);
        return fragment;
    }

    public static void replaceFragment(FragmentManager fragmentManager, @IdRes int containerViewId, Fragment fragment, boolean addToBackStack) {
        replaceFragment(fragmentManager, containerViewId, fragment, fragment.getClass().getName(), addToBackStack);
    }

    public static void replaceFragment(FragmentManager fragmentManager, @IdRes int containerViewId, Fragment fragment, String tag, boolean addToBackStack) {
        if(fragment.isAdded()) return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        if(addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void removeFragment(FragmentManager fragmentManager, Class<? extends Fragment> clazz) {
        removeFragment(fragmentManager, clazz.getName());
    }

    public static void removeFragment(FragmentManager fragmentManager, String tag) {
        removeFragment(fragmentManager, fragmentManager.findFragmentByTag(tag));
    }

    public static void removeFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    // Options menu
    public static void clearChecked(@NonNull Menu menu) {
        for(int i = menu.size() - 1; i >= 0; i--) {
            setChecked(menu.getItem(i), false);
        }
    }

    public static void setChecked(@Nullable MenuItem item, boolean isChecked) {
        if(item == null) return;
        item.setChecked(isChecked);

        SpannableString spannable = new SpannableString(item.getTitle());
        spannable.setSpan(new ForegroundColorSpan(isChecked ? Color.BLUE : Color.BLACK),
                0, spannable.length(), 0);
        item.setTitle(spannable);
    }

    // Activity
    public static Intent makeMainIntent(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        return intent;
    }

    public static Intent makeHomeIntent(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setAction(Intent.ACTION_MAIN);
        return intent;
    }

    // Service
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null)
            for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE))
                if(serviceClass.getName().equals(service.service.getClassName())) return true;

        return false;
    }

    // Widget
    public static AppWidgetManager getAppWidgetManager(Context context) {
        return AppWidgetManager.getInstance(context);
    }

    public static int[] getAppWidgetIds(Context context, AppWidgetManager appWidgetManager, Class<?> widgetClass) {
        return appWidgetManager.getAppWidgetIds(new ComponentName(context, widgetClass));
    }

    // check support
    public static boolean currentVersionSupportLockScreenControls() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean currentVersionSupportBigNotification() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean currentVersionSupportVectorDrawable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
