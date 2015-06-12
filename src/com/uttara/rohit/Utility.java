package com.uttara.rohit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author rohit
 *
 */
public class Utility {

	public Utility() {

	}

	/**
	 * This method checks the connectivity of the device , both WIFI and DATA
	 * connection.
	 * 
	 * @param context
	 *            android.content.Context
	 * @return boolean
	 */
	public static boolean checkInternet(Context context) {

		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;

		}
		// Log.d(TAG," mobile"+haveConnectedMobile+"wifi"+haveConnectedWifi);
		return haveConnectedWifi || haveConnectedMobile;
	}

	/**
	 * This method checks if the GPS is enabled in the device or not .
	 * 
	 * @param context
	 *            android.content.Context
	 * @return boolean
	 * 
	 */

	public static boolean checkGPSStatus(Context context) {

		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& !locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			// All location services are disabled
			return false;
		}
		return true;
	}

	/**
	 * This method converts the given address to latitude and longitude
	 * 
	 * @param strAddress
	 *            Address String to get the Latitude and Longitude.
	 * @param c
	 *            android.content.Context
	 * @return LatLng - com.google.android.gms.maps.model.LatLng
	 */
	public static LatLng getLocationFromAddress(String strAddress, Context c) {

		Geocoder coder = new Geocoder(c);
		List<Address> address;
		LatLng p1 = null;

		try {
			address = coder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;

			}
			Address location = address.get(0);
			location.getLatitude();
			location.getLongitude();

			p1 = new LatLng(location.getLatitude(), location.getLongitude());

		} catch (Exception ex) {

			ex.printStackTrace();
		}

		return p1;
	}

	/**
	 * This method Checks for the validity of the future date. Input should be
	 * in the format : dd/MM/yyyy
	 * 
	 * @param date
	 *            java.lang.String
	 * @return String - java.lang.String
	 */
	public static String dateCheckerforFutureDate(String date) {
		System.out.println("DATE = " + date);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String presentDate = format.format(new Date()).toString();
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date);
			d2 = format.parse(presentDate);
			// in milliseconds

			long diff = d2.getTime() - d1.getTime();

			long diffDays = diff / (24 * 60 * 60 * 1000);
			System.out.print(diffDays + " Days");

			// 100 years = 36,467 Days

			int day = (int) diffDays;
			if (day <= 0) {
				return "Success";
			} else {
				return "Failure";
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return " Dear user Kindly provide Date in this FORMAT : dd/MM/yyyy";
		}
	}

	/**
	 * This method Checks for the validity of the past date. Input should be in
	 * the format : dd/MM/yyyy
	 * 
	 * @param date
	 *            java.lang.String
	 * @return String - java.lang.String
	 */
	public static String dateCheckerforPastDate(String date) {
		System.out.println("DATE = " + date);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String presentDate = format.format(new Date()).toString();
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date);
			d2 = format.parse(presentDate);
			// in milliseconds

			long diff = d2.getTime() - d1.getTime();

			long diffDays = diff / (24 * 60 * 60 * 1000);
			System.out.print(diffDays + " Days");

			// 100 years = 36,467 Days

			int day = (int) diffDays;
			if (day <= 0) {
				return "Failure";

			} else {
				return "Success";
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return " Dear user Kindly provide Date in this FORMAT : dd/MM/yyyy";
		}
	}

	/**
	 * This method converts a bitmap image to an Base64 encoded String.
	 * 
	 * @param bitmap
	 *            android.graphics.Bitmap
	 * @return String - java.lang.String
	 */
	public static String getImageString(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
		byte[] image = stream.toByteArray();
		System.out.println("byte array:" + image);
		String imageData = Base64.encodeToString(image, 0);
		return imageData;
	}

	/**
	 * This method converts the given latitude and longitude to address .This
	 * method runs in a separate thread of execution , so when you call this
	 * method you need to create a inner class that extends a Handler to receive
	 * the Address as message.
	 * 
	 * @param latitude
	 *            java.lang.double
	 * @param longitude
	 *            java.lang.double
	 * @param context
	 *            android.content.Context
	 * @param handler
	 *            android.os.Handler
	 */
	public static void getAddressFromLocation(final double latitude,
			final double longitude, final Context context, final Handler handler) {

		Thread thread = new Thread() {
			@Override
			public void run() {
				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
				String result = null;
				try {
					List<Address> addressList = geocoder.getFromLocation(
							latitude, longitude, 1);
					if (addressList != null && addressList.size() > 0) {
						Address address = addressList.get(0);
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
							sb.append(address.getAddressLine(i)).append("\n");
						}
						// sb.append(address.getLocality()).append("\n");
						// sb.append(address.getPostalCode()).append("\n");
						// sb.append(address.getCountryName());
						result = sb.toString();
					}
				} catch (IOException e) {
					Log.e("getAddressFromLocation",
							"Unable connect to Geocoder", e);
				} finally {
					Message message = Message.obtain();
					message.setTarget(handler);
					if (result != null) {
						message.what = 1;
						Bundle bundle = new Bundle();
						bundle.putString("address", result);
						message.setData(bundle);
					} else {
						message.what = 1;
						Bundle bundle = new Bundle();
						result = "Unable to get address for this lat-long.";
						bundle.putString("address", result);
						message.setData(bundle);
					}
					message.sendToTarget();
				}
			}
		};
		thread.start();
	}

	/**
	 * This method posts data to a webserver.Input value should be in the form
	 * of JSON converted to String.
	 * 
	 * @param value
	 *            Data to be posted
	 * @param urlString
	 *            java.lang.String url of the webserver
	 * @param key
	 *            java.lang.String key to retreive the data in webserver
	 * @return String - java.lang.String
	 */
	public static String httpPost(String value, String urlString, String key) {

		OutputStreamWriter wr = null;
		String line = null;
		try {

			// Construct data
			String data = URLEncoder.encode(key, "UTF-8") + "="
					+ URLEncoder.encode(value, "UTF-8");
			// data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" +
			// URLEncoder.encode("value2", "UTF-8");
			System.out.println("DATA" + data);
			// Send data
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			line = rd.readLine();
			return line;

		} catch (Exception e) {
			e.printStackTrace();
			return line;
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return e.getMessage();
				}
			}

		}

	}

	/**
	 * This method is used to send data via request .
	 * 
	 * @param urlString
	 * @return String
	 */
	public static String httpPost(String urlString) {

		String line = null;
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			line = rd.readLine();
			return line;

		} catch (Exception e) {
			e.printStackTrace();
			return line;
		}

	}

	/**
	 * This method checks for blank input or null input.
	 * 
	 * @param data
	 *            java.lang.String
	 * @return boolean
	 */
	public static boolean validateString(String data) {

		if (data != null && data.trim().equals(""))
			return false;
		else
			return true;
	}

	/**
	 * This method checks for the valid email.
	 * 
	 * @param email
	 *            java.lang.String
	 * @return boolean
	 */
	public static boolean validateEmail(String email) {

		if (email == null || email.trim().equals("")) {
			return false;
		} else {
			String mail = email;
			String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+"
					+ "(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
			Boolean b = mail.matches(emailreg);
			return b;
		}
	}

	/**
	 * This method checks if the phone number is valid or not
	 * 
	 * @param phnum
	 *            java.lang.String
	 * @return String - status of phone number
	 */
	public static String validatePhoneNumber(String phnum) {

		String msg = "";
		if (phnum == null || phnum.trim().equals("")) {
			if (phnum.length() >= 10 & phnum.length() <= 0) {
				msg = msg + "Invalid Phone number";
				return msg;
			} else {
				msg = msg + "Phone number cannot be blank";
				return msg;
			}

		}
		return msg;
	}

	/**
	 * @param pass
	 *            password String
	 * @param rpass
	 *            repeat password String
	 * @return boolean
	 */
	public static boolean matchPassword(String pass, String rpass) {

		if (pass.equals(rpass)) {
			return true;
		}
		return false;
	}

	/**
	 * This method makes toast.
	 * 
	 * @param context
	 *            android.content.Context
	 * @param msg
	 *            java.lang.String message to be toasted
	 */
	public static void getToast(Context context, String msg) {

		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * This method builds an alert dialog.
	 * 
	 * @param title
	 *            -Title of the diaolg
	 * @param message
	 *            - Message to be displayed
	 * @param posButtonText
	 *            - Positive button text
	 * @param context
	 *            - android.content.Context
	 * 
	 * @return - AlertDialog
	 */

	public static AlertDialog getAlertDialog(String title, String message,
			String posButtonText, Context context) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		alertDialogBuilder.setTitle(title).setMessage(message)
				.setCancelable(false).setPositiveButton(posButtonText, null);
		AlertDialog alertDialog = alertDialogBuilder.create();
		return alertDialog;

	}

	/**
	 * @param context
	 *            android.content.Context
	 * @param title
	 *            Title for Progressdialog
	 * @param message
	 *            Message for progress dialog
	 * @return - Progress dialog - returns instance of progress dialog.
	 */
	public static ProgressDialog getProgressDialog(Context context,
			String title, String message) {

		ProgressDialog pdialog = new ProgressDialog(context);
		pdialog.setTitle(title);
		pdialog.setMessage(message);
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		return pdialog;
	}

	/**
	 * This method parses the given String and return an JSONObject
	 * 
	 * @param jsonString
	 *            java.lang.String
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject parseStringForJSONObject(String jsonString) {
		JSONObject object = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			object = (JSONObject) parser.parse(jsonString);
			return object;
		} catch (ParseException e) {
			object.put("error", e.getMessage());
			e.printStackTrace();
			return object;
		}
	}

	/**
	 * This method parses the given String and return an JSONArray
	 * 
	 * @param jsonString
	 *            java.lang.String
	 * @return JSONArray
	 */

	public static JSONArray parseStringForJSONArray(String jsonString) {

		JSONParser parser = new JSONParser();
		JSONArray jArray = new JSONArray();
		try {
			jArray = (JSONArray) parser.parse(jsonString);
			return jArray;
		} catch (ParseException e) {

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method returns the editor of the sharedpreference
	 * 
	 * @param prefName
	 *            java.lang.String Shared preferences name
	 * @param context
	 * @return Editor - sharedprefrences editor
	 */
	public static Editor getLocalStorage(String prefName, Context context) {

		SharedPreferences pref = context.getSharedPreferences(prefName,
				Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		return edit;
	}

	/**
	 * This method returns date with Zero's added if the day and month is less
	 * than 10
	 * 
	 * @param day
	 *            int day
	 * @param month
	 *            int month
	 * @param year
	 *            int year
	 * @return date java.lang.String
	 */
	public static String dateCorrector(int day, int month, int year) {

		String month1;
		String day1;
		if (month < 10) {
			month1 = "/0" + month;
		} else {
			month1 = "/" + month;
		}
		if (day < 10) {
			day1 = "0" + day;
		} else {
			day1 = "" + day;
		}

		return day1 + month1 + "/" + year;
	}

	/**
	 * This method encrypts a given String to an encrypted String using ROT13 algorithm
	 * 
	 * @param value java.lang.String
	 * @return String java.lang.String
	 */
	public static String rot13Encryptor(String value) {

		char[] values = value.toCharArray();
		for (int i = 0; i < values.length; i++) {
			char letter = values[i];

			if (letter >= 'a' && letter <= 'z') {
				// Rotate lowercase letters.

				if (letter > 'm') {
					letter -= 13;
				} else {
					letter += 13;
				}
			} else if (letter >= 'A' && letter <= 'Z') {
				// Rotate uppercase letters.

				if (letter > 'M') {
					letter -= 13;
				} else {
					letter += 13;
				}
			}
			values[i] = letter;
		}
		// Convert array to a new String.
		return new String(values);
	}

}
