package th.or.dga.royaljitarsa.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
	private final String preferenceName = "JITARSA";
	private final String SCREEN_WIDTH = "screenWidth";
	private final String SCREEN_HEIGHT = "screenHeight";
	private final String SKIP_HOW_TO_PLAY = "skipHowToPlay";
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String LOGIN_STATUS = "loginStatus";
	private final String USER_ID = "userID";
	private final String FULLNAME = "fullname";
	private final String IDENTIFICATION_CARD = "identificationCard";
	private final String LASER_CODE = "laserCode";
	private final String EMAIL = "email";
	private final String BIRTH_DATE = "birthDate";
	private final String AGE = "age";
	private final String PHONE = "phone";
	private final String PROFILE_IMAGE = "profileImage";
	private final String QR_CODE = "qrCode";
	private final String ADDRESS = "address";
	private final String PROVINCE_ID = "provinceID";
	private final String DISTRICT_ID = "districtID";
	private final String SUB_DISTRICT_ID = "subDistrictID";
	private final String ZIP_CODE = "zipCode";
	private final String STATUS_USER = "statusUser";
	private final String LANGUAGE = "language";
	private final String FACEBOOK_TOKEN = "facebookToken";
	private final String FACEBOOK_USER_ID = "facebookUserID";
	private final String FACEBOOK_APPLICATION_ID = "facebookApplicationID";
	
	private SharedPreferences preference = null;
	private Context context = null;
	private static AppPreference appPreference = null;
	
	public AppPreference(Context context) {
		this.context = context;
		int mode = Context.MODE_PRIVATE;
		this.preference = this.context.getSharedPreferences(this.preferenceName, mode);
	}
	
	public static AppPreference getInstance(Context context) {
		if(appPreference == null) {
			appPreference = new AppPreference(context);
		}
		return appPreference;
	}

	public void setScreenWidth(int screenWidth) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putInt(this.SCREEN_WIDTH, screenWidth);
		editor.commit();
	}

	public int getScreenWidth() {
		int screenWidth = this.preference.getInt(this.SCREEN_WIDTH, 0);
		return screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putInt(this.SCREEN_HEIGHT, screenHeight);
		editor.commit();
	}

	public int getScreenHeight() {
		int screenHeight = this.preference.getInt(this.SCREEN_HEIGHT, 0);
		return screenHeight;
	}
	
	public void setUsername(String username) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.USERNAME, username);
		editor.commit();
	}
	
	public String getUsername() {
		String username = this.preference.getString(this.USERNAME, "");
		return username;
	}

	public void setPassword(String password) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.PASSWORD, password);
		editor.commit();
	}

	public String getPassword() {
		String password = this.preference.getString(this.PASSWORD, "");
		return password;
	}

	public void setLoginStatus(int loginStatus) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putInt(this.LOGIN_STATUS, loginStatus);
		editor.commit();
	}

	public int getLoginStatus() {
		int loginStatus = this.preference.getInt(this.LOGIN_STATUS, 0);
		return loginStatus;
	}

	public void setUserID(String userID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.USER_ID, userID);
		editor.commit();
	}

	public String getUserID() {
		String userID = this.preference.getString(this.USER_ID, "");
		return userID;
	}

	public void setSkipHowToPlay(boolean skipHowToPlay) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.SKIP_HOW_TO_PLAY, skipHowToPlay);
		editor.commit();
	}

	public boolean getSkipHowToPlay() {
		boolean skipHowToPlay = this.preference.getBoolean(this.SKIP_HOW_TO_PLAY, false);
		return skipHowToPlay;
	}

	public void setFullname(String fullname) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FULLNAME, fullname);
		editor.commit();
	}

	public String getFullname() {
		String fullname = this.preference.getString(this.FULLNAME, "จิตอาสา");
		return fullname;
	}

	public void setIdentificationCard(String identificationCard) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.IDENTIFICATION_CARD, identificationCard);
		editor.commit();
	}

	public String getIdentificationCard() {
		String identificationCard = this.preference.getString(this.IDENTIFICATION_CARD, "");
		return identificationCard;
	}

	public void setLaserCode(String laserCode) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.LASER_CODE, laserCode);
		editor.commit();
	}

	public String getLaserCode() {
		String laserCode = this.preference.getString(this.LASER_CODE, "");
		return laserCode;
	}

	public void setEmail(String email) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.EMAIL, email);
		editor.commit();
	}

	public String getEmail() {
		String email = this.preference.getString(this.EMAIL, "");
		return email;
	}

	public void setBirthDate(String birthDate) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.BIRTH_DATE, birthDate);
		editor.commit();
	}

	public String getBirthDate() {
		String birthDate = this.preference.getString(this.BIRTH_DATE, "");
		return birthDate;
	}

	public void setAge(String age) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.AGE, age);
		editor.commit();
	}

	public String getAge() {
		String age = this.preference.getString(this.AGE, "");
		return age;
	}

	public void setPhone(String phone) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.PHONE, phone);
		editor.commit();
	}

	public String setPhone() {
		String phone = this.preference.getString(this.PHONE, "");
		return phone;
	}

	public void setProfileImage(String profileImage) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.PROFILE_IMAGE, profileImage);
		editor.commit();
	}

	public String getProfileImage() {
		String profileImage = this.preference.getString(this.PROFILE_IMAGE, "");
		return profileImage;
	}

	public void setQRCode(String qrCode) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.QR_CODE, qrCode);
		editor.commit();
	}

	public String getQRCode() {
		String qrCode = this.preference.getString(this.QR_CODE, "");
		return qrCode;
	}

	public void setAddress(String address) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.ADDRESS, address);
		editor.commit();
	}

	public String getAddress() {
		String address = this.preference.getString(this.ADDRESS, "");
		return address;
	}

	public void setProvinceID(String provinceID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.PROVINCE_ID, provinceID);
		editor.commit();
	}

	public String getProvinceID() {
		String provinceID = this.preference.getString(this.PROVINCE_ID, "");
		return provinceID;
	}

	public void setDistrictID(String districtID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.DISTRICT_ID, districtID);
		editor.commit();
	}

	public String getDistrictID() {
		String districtID = this.preference.getString(this.DISTRICT_ID, "");
		return districtID;
	}

	public void setSubDistrictID(String subDistrictID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.SUB_DISTRICT_ID, subDistrictID);
		editor.commit();
	}

	public String getSubDistrictID() {
		String subDistrictID = this.preference.getString(this.SUB_DISTRICT_ID, "");
		return subDistrictID;
	}

	public void setZipCode(String zipCode) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.ZIP_CODE, zipCode);
		editor.commit();
	}

	public String getZipCode() {
		String zipCode = this.preference.getString(this.ZIP_CODE, "");
		return zipCode;
	}

	public void setStatusUser(String statusUser) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.STATUS_USER, statusUser);
		editor.commit();
	}

	public String getStatusUser() {
		String statusUser = this.preference.getString(this.STATUS_USER, "");
		return statusUser;
	}

	public void setLanguage(String language) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.LANGUAGE, language);
		editor.commit();
	}

	public String getLanguage() {
		String language = this.preference.getString(this.LANGUAGE, "");
		return language;
	}

	public void setFacebookToken(String facebookToken) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_TOKEN, facebookToken);
		editor.commit();
	}

	public String getFacebookToken() {
		String facebookToken = this.preference.getString(this.FACEBOOK_TOKEN, "");
		return facebookToken;
	}

	public void setFacebookUserID(String facebookUserID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_USER_ID, facebookUserID);
		editor.commit();
	}

	public String getFacebookUserID() {
		String facebookUserID = this.preference.getString(this.FACEBOOK_USER_ID, "");
		return facebookUserID;
	}

	public void setFacebookApplicationID(String facebookApplicationID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_APPLICATION_ID, facebookApplicationID);
		editor.commit();
	}

	public String getFacebookApplicationID() {
		String facebookApplicationID = this.preference.getString(this.FACEBOOK_APPLICATION_ID, "");
		return facebookApplicationID;
	}
}
