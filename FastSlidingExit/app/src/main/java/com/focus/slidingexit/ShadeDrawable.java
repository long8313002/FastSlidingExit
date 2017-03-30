package com.focus.slidingexit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by zhangzheng on 2017/3/28.
 */

public class ShadeDrawable extends BitmapDrawable{

    private static ShadeDrawable shadeDrawable;

    public static final String SHADE_PIC_STR ="iVBORw0KGgoAAAANSUhEUgAAAEIAAACACAYAAAC/dL9XAAAABHNCSVQICAgIfAhkiAAAAQdJREFU\n" +
            "eJzt0EEKwzAMAEHF/390D12CEwil5DoDwkg37zFXxzbrtj/d1/au2/50f7v/uv/9jzXMzLcKI8RJ\n" +
            "iAgRISJEhIgQESJCRIgIESEiRISIEBEiQkSICBEhIkSEiBARIkJEiAgRISJEhIgQESJCRIgIESEi\n" +
            "RISIEBEiQkSICBEhIkSEiBARIkJEiAgRISJEhIgQESJCRIgIESEiRISIEBEiQkSICBEhIkSEiBAR\n" +
            "IkJEiAgRISJEhIgQESJCRIgIESEiRISIEBEiQkSICBEhIkSEiBARIkJEiAgRISJEhIgQESJCRIgI\n" +
            "ESEiRISIEBEiQkSICBEhIkSEiBARIkJEiAgRISJEPrruAV0vPNddAAAAAElFTkSuQmCC\n";


    public ShadeDrawable() {
        super(Utils.convertStringToIcon(SHADE_PIC_STR));
    }

    @Override
    public int getIntrinsicWidth() {
        return getBitmap().getWidth();
    }


    public static ShadeDrawable getShadeDrawable(){
        if(shadeDrawable == null){
            shadeDrawable = new ShadeDrawable();
        }
        return shadeDrawable;
    }
}
