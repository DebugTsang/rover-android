package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.roverlabs.sdk.ui.widgets.Border;
import co.roverlabs.sdk.ui.widgets.BorderedView;
import co.roverlabs.sdk.ui.widgets.BoxModelDimens;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-05-01.
 */
public class UiUtils {

    public static final String TAG = UiUtils.class.getSimpleName();

    public static int getARGBColor(List<Float> colorValues) {

        int red = colorValues.get(0).intValue();
        int green = colorValues.get(1).intValue();
        int blue = colorValues.get(2).intValue();
        int alpha = (int)(colorValues.get(3) * 255);
        return Color.argb(alpha, red, green, blue);
    }

    public static int convertDpToPx(Context con, int dp) {

        final float scale = con.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public static int convertPxToDp(Context con, int px) {

        final float scale = con.getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f);
    }

    public static int getDeviceWidthInDp(Context con) {

        return convertPxToDp(con, con.getResources().getDisplayMetrics().widthPixels);
    }

    public static int getDeviceWidthInPx(Context con) {

        return con.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeightInDp(Context con) {

        return convertPxToDp(con, con.getResources().getDisplayMetrics().heightPixels);
    }

    public static int getDeviceHeightInPx(Context con) {

        return con.getResources().getDisplayMetrics().heightPixels;
    }

    public static void setBorder(BorderedView borderView, Border border) {

        if(border.hasBorder()) {
            borderView.setBorder(border);
            borderView.setVisibility(View.VISIBLE);
        }
        else {
            borderView.setVisibility(View.GONE);
        }
    }

    public static void setPadding(View view, BoxModelDimens padding, Border border) {

        if(border.hasBorder()) {
            padding.top += border.top;
            padding.right += border.right;
            padding.bottom += border.bottom;
            padding.left += border.left;
        }

        view.setPadding(padding.left, padding.top, padding.right, padding.bottom);
    }

    public static void setText(String blockType, LinearLayout layout, String text, List<TextStyle> styles) {

        layout.removeAllViews();

        Document textContent = Jsoup.parseBodyFragment(text);

        for (Element textElement : textContent.getAllElements()) {

            if (textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_H1))) {
                TextView textView = new TextView(layout.getContext());
                layout.addView(textView);
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<h1>(.*?)</h1>").matcher(original);
                if (matcher.find()) {
                    setTextFormat(blockType, textView, matcher.group(1), styles.get(0));
                }
            }

            if (textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_H2))) {
                TextView textView = new TextView(layout.getContext());
                layout.addView(textView);
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<h2>(.*?)</h2>").matcher(original);
                if (matcher.find()) {
                    setTextFormat(blockType, textView, matcher.group(1), styles.get(1));
                }
            }

            if (textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_P))) {
                TextView textView = new TextView(layout.getContext());
                layout.addView(textView);
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<p>(.*?)</p>").matcher(original);
                if (matcher.find()) {
                    setTextFormat(blockType, textView, matcher.group(1), styles.get(2));
                }
            }
        }
    }

    public static void setText(String blockType, TextView textView, String text, TextStyle style) {

        Document textContent = Jsoup.parseBodyFragment(text);

        for (Element textElement : textContent.getAllElements()) {

            if (textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_DIV))) {
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<div>(.*?)</div>", Pattern.DOTALL).matcher(original);
                if (matcher.find()) {
                    setTextFormat(blockType, textView, matcher.group(1), style);
                }
            }
        }
    }

    public static void setTextFormat(String blockType, TextView textView, String text, TextStyle style) {

        textView.setText(Html.fromHtml(text));
        textView.setTextSize(style.size);

        if(style.type.equals(RoverConstants.TEXT_H1) || style.type.equals(RoverConstants.TEXT_H2) || blockType.equals(RoverConstants.VIEW_BLOCK_TYPE_BARCODE)) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else {
            textView.setTypeface(Typeface.DEFAULT);
        }

        if(blockType.equals(RoverConstants.VIEW_BLOCK_TYPE_HEADER)) {
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        else if(style.align.equals(RoverConstants.TEXT_ALIGN_CENTER)) {
            textView.setGravity(Gravity.CENTER);
        }
        else if(style.align.equals(RoverConstants.TEXT_ALIGN_RIGHT)) {
            textView.setGravity(Gravity.RIGHT);
        }
        else if(style.align.equals(RoverConstants.TEXT_ALIGN_LEFT)) {
            textView.setGravity(Gravity.LEFT);
        }

        textView.setTextColor(style.color);
        textView.setLineSpacing(style.lineHeight - textView.getLineHeight(), 1);

        if(blockType.equals(RoverConstants.VIEW_BLOCK_TYPE_TEXT) || blockType.equals(RoverConstants.VIEW_BLOCK_TYPE_BARCODE)) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(style.margin.left, style.margin.top, style.margin.right, style.margin.bottom);
            textView.setLayoutParams(layoutParams);
        }
        else if(blockType.equals(RoverConstants.VIEW_BLOCK_TYPE_BUTTON)) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(style.margin.left, style.margin.top, style.margin.right, style.margin.bottom);
            textView.setLayoutParams(layoutParams);
        }
    }

    public static Bitmap generateBarcode(String contents, int width, int height, int color) {

        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        MultiFormatWriter writer = new MultiFormatWriter();

        BitMatrix result = null;

        try {
            result = writer.encode(contents, BarcodeFormat.CODE_128, width, height, hints);
        }
        catch (WriterException e) {
            Log.e(TAG, "Cannot encode barcode " + e);
        }

        int resultWidth = result.getWidth();
        int resultHeight = result.getHeight();
        int[] pixels = new int[width * height];

        for(int y = 0; y < height; y++) {
            int offset = y * width;
            for(int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? color : Color.TRANSPARENT;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, resultWidth, 0, 0, resultWidth, resultHeight);
        return bitmap;
    }



}