package com.smartwebarts.apnamart.dashboard.ui.home;

import com.smartwebarts.apnamart.R;

import java.util.ArrayList;
import java.util.List;

public class SliderImageData2 {

    private int image;

    public SliderImageData2(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public static List<SliderImageData2> getSliderImageData2() {

        List<SliderImageData2> list = new ArrayList<>();
        list.add(new SliderImageData2(R.drawable.slide1));
        list.add(new SliderImageData2(R.drawable.slide2));
        list.add(new SliderImageData2(R.drawable.slide3));
        list.add(new SliderImageData2(R.drawable.slide4));
        list.add(new SliderImageData2(R.drawable.slide5));
        list.add(new SliderImageData2(R.drawable.slide6));
        list.add(new SliderImageData2(R.drawable.slide7));
        return list;
    }
}
