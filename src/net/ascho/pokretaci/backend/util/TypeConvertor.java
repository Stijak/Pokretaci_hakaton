package net.ascho.pokretaci.backend.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

/**
 * Skup metoda za konverziju izmedju tipova
 * @author milan
 *
 */
public class TypeConvertor {

	
	
	public static byte[] stringToByteArray(String value) {
		if (value == null) {
			return null;
		}
		String[] byteValues = value.substring(1, value.length() - 1).split(",");
		byte[] bytes = new byte[byteValues.length];

		for (int i=0, len=bytes.length; i<len; i++) {
		   bytes[i] = Byte.valueOf(byteValues[i].trim());     
		}
		return bytes;
	}
	
	
	
	public static String byteArrayToString(byte[] bytes) {
		return bytes != null ? Arrays.toString(bytes) : null;
	}
	
	
    
    /**
     * Converts input stream to string.
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) throws IOException {
		StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = bufferedReader.readLine();
        while(line != null){
            inputStringBuilder.append(line);inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        
        return inputStringBuilder.toString();
    }
    
    /**
     * Converts view to bitmap with passed width and height.
     * @param view
     * @param width
     * @param height
     * @return
     * @throws RuntimeException
     */
    public static Bitmap viewToBitmap (View view, int width, int height) throws RuntimeException {
    	if (width<=0 || height<=0) {
    		return null;
    	}
		view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
		view.measure(MeasureSpec.makeMeasureSpec(view.getLayoutParams().width, MeasureSpec.EXACTLY),
		MeasureSpec.makeMeasureSpec(view.getLayoutParams().height, MeasureSpec.EXACTLY));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap overAllDrawing = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overAllDrawing);
		view.draw(canvas);
		return overAllDrawing;
    }
    
	
	
	
	
}
