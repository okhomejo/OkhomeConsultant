package id.co.okhome.consultant.view.activity.etc.photochooser;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;


public class ImageChooserActivity extends OkHomeParentActivity {

	private Uri outputFileUri = null;

	public static final String RESULT_IMAGE_PATH = "resultImagePath";

	public static final int REQ_GET_IMAGE = 50050;
	private static final int REQ_PICK_IMAGE = 10050;
	private static final int REQ_CROP_IMAGE = 10100;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(Color.BLACK);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setContentView(ll, params);


		OkhomeUtil.setSystemBarColor(this, Color.BLACK);

		openImageIntentOnlyCameraAndGallery();
	}



	private List<Intent> createCameraIntents() {

//		File[] arrParentPath = new File[]{
//				Environment.getExternalStorageDirectory()
////				, new File("/Internal storage")
//		};
//
//		File fileSaved = null;
//		for(File fParentPath : arrParentPath){
//			File fileTarget = new File(fParentPath, "OKHOME");
//			if(fParentPath.exists()){
//				if (fileTarget.mkdirs()){
//					fileSaved = fileTarget;
//					break;
//				}
//			}
//		}
//
//		if(fileSaved == null){
//			;
//		}

//		File fileSaved = new File(getCacheDir());


		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
		String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));
		File cropFile = new File(getCacheDir(),  "temp_" + timestamp + ".jpg");

		outputFileUri = Uri.fromFile(cropFile);
		outputFileUri = Uri.fromFile(new File("/sdcard/tmp"));


		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
		for(ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			cameraIntents.add(intent);
		}
		return cameraIntents;
	}



	private void openImageIntentOnlyCameraAndGallery() {
		final List<Intent> cameraIntents = createCameraIntents();
		if(cameraIntents == null){
//			Intent resultIntent = new Intent();
//			resultIntent.putExtra(RESULT_IMAGE_PATH, "에뮬레이터라서안되는에러.jpg");
//			setResult(RESULT_OK, resultIntent);
//			finish();

			Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
			getIntent.setType("image/*");

			Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			pickIntent.setType("image/*");

			Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

			startActivityForResult(chooserIntent, REQ_PICK_IMAGE);
		}else{
			Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			Intent chooserIntent = Intent.createChooser(galleryIntent, null);
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
			startActivityForResult(chooserIntent, REQ_PICK_IMAGE);




		}

	}



	private void openImageIntent() {

		final List<Intent> cameraIntents = createCameraIntents();

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent, null);

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

		startActivityForResult(chooserIntent, REQ_PICK_IMAGE);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQ_PICK_IMAGE && resultCode == RESULT_OK) {

			final boolean isCamera;

			if (data == null) {
				isCamera = true;
			}
			else {
				final String action = data.getAction();

				if (action == null) {
					isCamera = false;
				}
				else {
					isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
				}
			}

			Uri selectedImageUri = null;
			if (isCamera) {
				selectedImageUri = outputFileUri;
			}
			else {
				selectedImageUri = data == null ? null : data.getData();
			}

			//let crop image
			Intent i = new Intent(this, CropImageActivity.class);
			i.putExtra("data-uri", selectedImageUri.toString());
			startActivityForResult(i, REQ_CROP_IMAGE);
		}

		else if (requestCode == REQ_CROP_IMAGE && resultCode == RESULT_OK) {
			String path = data.getStringExtra("cropped-path");
			Intent resultIntent = new Intent();
			resultIntent.putExtra(RESULT_IMAGE_PATH, path);
			setResult(RESULT_OK, resultIntent);
			finish();
		}

		else if (resultCode == RESULT_CANCELED) {
			setResult(RESULT_CANCELED);
			finish();
		}

	}

	public static void recursiveDeleteTempDirectory(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory()) {
			for (File child : fileOrDirectory.listFiles())
				recursiveDeleteTempDirectory(child);
		}
		fileOrDirectory.delete();
	}

}
