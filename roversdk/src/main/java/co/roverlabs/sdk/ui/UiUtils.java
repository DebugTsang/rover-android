package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static int getDeviceWidth(Context con) {

        return convertPxToDp(con, con.getResources().getDisplayMetrics().widthPixels);
    }

    public static int getDeviceHeight(Context con) {

        return convertPxToDp(con, con.getResources().getDisplayMetrics().heightPixels);
    }

    public static void setBackgroundImage(ImageView imageView, String imageUrl, String imageMode) {

        if(imageUrl != null) {
            imageView.setBackground(null);
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(null);
            PicassoUtils.loadBackgroundImage(imageView, imageUrl, imageMode);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setVisibility(View.GONE);
        }
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

        if(style.type.equals(RoverConstants.TEXT_H1) || style.type.equals(RoverConstants.TEXT_H2)) {
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

        if(blockType.equals(RoverConstants.VIEW_BLOCK_TYPE_TEXT)) {
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

    /*
    public static Bitmap getScaledBitmap(Context con, Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = convertDpToPx(con, width);
        int newHeight = convertDpToPx(con, height);
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    public static void setImageMode(Context con, Bitmap bitmap, ImageView imageView, String imageMode) {

        Bitmap resizedBitmap = getScaledBitmap(con, bitmap);
        BitmapDrawable backgroundDrawable = new BitmapDrawable(con.getResources(), resizedBitmap);

        switch (imageMode) {

            case RoverConstants.IMAGE_MODE_STRETCH:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case RoverConstants.IMAGE_MODE_TILE:
                backgroundDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                imageView.setBackground(backgroundDrawable);
                break;

            case RoverConstants.IMAGE_MODE_FILL:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;

            case RoverConstants.IMAGE_MODE_FIT:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;

            default:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
        }
    }
    */
}