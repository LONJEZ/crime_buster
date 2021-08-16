package com.lonjeztech.crimemanagementsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO Auto-generated method stub

    isNetworkThere(context);
  }

  public static boolean isNetworkThere(Context ctx) {
    // TODO Auto-generated method stub

    ConnectivityManager conn = (ConnectivityManager)
        ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = conn.getActiveNetworkInfo();

    // checks to see if the device has a Wi-Fi connection.
    if (networkInfo != null && networkInfo.isConnectedOrConnecting() &&
        networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
      // If device has its Wi-Fi connection
      // Then first check if the notification is available in the db before
      //starting the service to download new notifications from the server

      //Constants.isNetworkOn = true;
      return true;

    }
    // Then If Mobile Data is Available and there is a network connection
    else if (networkInfo != null && networkInfo.isConnectedOrConnecting() &&
        networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

      // Then start the service to download the notifications from the server
      //Constants.isNetworkOn = true;
      return true;
    } else
    // Otherwise, the app can't download content--either because there is no network
    // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
    // is no Wi-Fi connection.
    {
      return false;
      //Constants.isNetworkOn = false;
      //Toast.makeText(context, R.string.network_disconnected, Toast.LENGTH_SHORT).show();
    }
  }

}
