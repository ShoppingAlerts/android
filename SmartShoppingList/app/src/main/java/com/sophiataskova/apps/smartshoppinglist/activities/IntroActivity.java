package com.sophiataskova.apps.smartshoppinglist.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.sophiataskova.apps.smartshoppinglist.R;

public class IntroActivity extends ActionBarActivity
{

    public static int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
        prepareBitmaps();
        prepareEditText();
    }

    private void prepareEditText() {
        final EditText editText = (EditText) findViewById(R.id.i_need);
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Intent i = new Intent(IntroActivity.this, ListsActivity.class);
                    i.putExtra("shoppingitem", editText.getText());
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });
    }

    private void prepareBitmaps() {
        getScreenWidth();
        BitmapFactory.Options options = new BitmapFactory.Options();
        ImageView campingImageView = (ImageView) findViewById(R.id.camping_image);
        ImageView schoolImageView = (ImageView) findViewById(R.id.school_image);
        ImageView travelImageView = (ImageView) findViewById(R.id.travel_image);
        options.inJustDecodeBounds = true;
        Resources resources = getResources();
        float itemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, resources.getDisplayMetrics());
        campingImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        schoolImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        travelImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        campingImageView.setImageBitmap(
                decodeSampledBitmapFromResource(resources, R.drawable.camping_fullsize, screenWidth, Math.round(itemHeight)));
        schoolImageView.setImageBitmap(
                decodeSampledBitmapFromResource(resources, R.drawable.back_to_school, screenWidth, Math.round(itemHeight)));
        travelImageView.setImageBitmap(
                decodeSampledBitmapFromResource(resources, R.drawable.travel, screenWidth, Math.round(itemHeight)));
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public void getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }
}
