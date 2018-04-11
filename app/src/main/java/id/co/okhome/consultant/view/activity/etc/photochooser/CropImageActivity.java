package id.co.okhome.consultant.view.activity.etc.photochooser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class CropImageActivity extends OkHomeParentActivity implements OnClickListener {

	CropImageView mCropImageView;
	ImageView iv;
	ViewGroup vgButtonsetForCutting;
	ViewGroup vgButtonSet;

	//
	boolean normalMode = true;
	Bitmap bitmapResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cropimage);

		OkhomeUtil.setSystemBarColor(this, Color.BLACK);


		vgButtonSet = (ViewGroup)findViewById(R.id.actCropImage_llBox);
		vgButtonsetForCutting = (ViewGroup)findViewById(R.id.actCropImage_llBoxForCutting);

		findViewById(R.id.actCropImage_cancel).setOnClickListener(this);
		findViewById(R.id.actCropImage_rotate).setOnClickListener(this);
		findViewById(R.id.actCropImage_done).setOnClickListener(this);
		findViewById(R.id.actCropImage_cut).setOnClickListener(this);
		findViewById(R.id.actCropImage_tvbtnCuttingCancel).setOnClickListener(this);
		findViewById(R.id.actCropImage_tvbtnCuttingOk).setOnClickListener(this);



		iv = (ImageView)findViewById(R.id.actCropImage_iv);
		mCropImageView = (CropImageView) findViewById(R.id.actCropImage_cropImageView);
		mCropImageView.setGuidelines(CropImageView.DEFAULT_ASPECT_RATIO_X);
//		mCropImageView.setAspectRatio(1, 1);
//		mCropImageView.setFixedAspectRatio(true);

		bitmapResult = getBitmapFromIntentPath();
		setBitmap(bitmapResult);

		adaptDateAndView();
	}

	private void adaptDateAndView(){
		vgButtonSet.setVisibility(View.VISIBLE);
		vgButtonsetForCutting.setVisibility(View.VISIBLE);
		iv.setVisibility(View.VISIBLE);
		mCropImageView.setVisibility(View.VISIBLE);

		if(normalMode){
			vgButtonsetForCutting.setVisibility(View.GONE);
			mCropImageView.setVisibility(View.GONE);
		}else{
			vgButtonSet.setVisibility(View.GONE);
			iv.setVisibility(View.GONE);
		}
	}

	//자르기 취소
	private void onCuttingCancel(){
		normalMode = true;
		adaptDateAndView();
	}

	//자르기 완료
	private void onCuttingConfirm(){
		Bitmap croppedBitmap = mCropImageView.getCroppedImage();
		iv.setImageBitmap(croppedBitmap);
		mCropImageView.setImageBitmap(croppedBitmap);

		bitmapResult = croppedBitmap;

		normalMode = true;
		adaptDateAndView();
	}

	//패스에서 비트맵 설정
	private Bitmap getBitmapFromIntentPath(){
		Uri dataUri = Uri.parse(getIntent().getStringExtra("data-uri"));
		String filePath = dataUri.getPath();
		Bitmap bm = null;
		if (dataUri.toString().contains("file://")) {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Config.RGB_565;
			BitmapFactory.decodeFile(filePath, opts);

			try {
				int sampleSize = calculateBitmapSampleSize(opts);
				opts.inSampleSize = sampleSize;
				opts.inJustDecodeBounds = false;

				bm = BitmapFactory.decodeFile(filePath, opts);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (dataUri.toString().contains("content://")) {
			try {

				InputStream is = null;
				Options options = new Options();
				options.inJustDecodeBounds = true;
				try {
					is = getContentResolver().openInputStream(dataUri);
					BitmapFactory.decodeStream(is, null, options); // Just get image size
				} finally {
					is.close();
				}
				int sampleSize = calculateBitmapSampleSize(options);
//				InputStream is = getContentResolver().openInputStream(dataUri);
				is = getContentResolver().openInputStream(dataUri);
				Options option = new Options();
				option.inSampleSize = sampleSize;
				bm = BitmapFactory.decodeStream(is, null, option);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bm;
	}

	private void setBitmap(Bitmap bm){
		mCropImageView.setImageBitmap(bm);
		iv.setImageBitmap(bm);
	}

	private class BitmapCompressTask extends AsyncTask<String, Integer, String> {

		ProgressDialog p;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			p = ProgressDialog.show(CropImageActivity.this, null, "Loading");
		}

		@Override
		protected String doInBackground(String... params) {

			String filePath = null;

			try {
				filePath = compressImage();
			} catch (IOException e) {
				Log.d(CropImageActivity.class.getName(), e.getMessage());
			}
			return filePath;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			p.dismiss();

			if (result != null) {
				Intent i = new Intent();
				i.putExtra("cropped-path", result);
				setResult(RESULT_OK, i);
				finish();
			}
		}
	}

	private int calculateBitmapSampleSize(Options options) throws IOException {

		int maxSize = 2048;
		int sampleSize = 1;
		while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
			sampleSize = sampleSize << 1;
		}
		return sampleSize;
	}

	private String compressImage() throws IOException {
//		Bitmap croppedBitmap = mCropImageView.getCroppedImage();
		Bitmap croppedBitmap = bitmapResult;

		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
		String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));

		File cropFile = new File(getCacheDir(), "crop_"+ timestamp + ".jpg");
		OutputStream outputStream = null;

		outputStream = new FileOutputStream(cropFile);
		croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
		outputStream.close();
		String filePath = cropFile.getPath();
		return filePath;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.actCropImage_cancel:
				setResult(RESULT_CANCELED);
				finish();
				break;

			case R.id.actCropImage_done:
				new BitmapCompressTask().execute("");
				break;

			case R.id.actCropImage_rotate:
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				bitmapResult = Bitmap.createBitmap(bitmapResult, 0, 0, bitmapResult.getWidth(), bitmapResult.getHeight(), matrix, true);
				setBitmap(bitmapResult);

				break;

			case R.id.actCropImage_cut:
				normalMode = false;
				adaptDateAndView();
				break;

			case R.id.actCropImage_tvbtnCuttingCancel:
				onCuttingCancel();
				break;

			case R.id.actCropImage_tvbtnCuttingOk:
				onCuttingConfirm();
				break;


		}
	}
}
