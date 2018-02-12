package customclass;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by welcome on 16-03-2016.
 */
public class Font_RobotoRegular extends TextView {

    public Font_RobotoRegular(Context context, AttributeSet attr){

        super(context,attr);

        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/RobotoRegular_0.ttf"));
    }




}
