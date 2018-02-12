package customclass;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by welcome on 12-10-2016.
 */
public class Font_StyleHeading extends TextView {


    public Font_StyleHeading(Context context,AttributeSet attr){

        super(context,attr);

        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Medium.ttf"));
    }

}
