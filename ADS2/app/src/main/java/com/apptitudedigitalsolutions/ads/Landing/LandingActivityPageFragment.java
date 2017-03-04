package com.apptitudedigitalsolutions.ads.Landing;

/**
 * Created by Elliot on 19/10/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apptitudedigitalsolutions.ads.R;
import com.squareup.picasso.Picasso;

public class LandingActivityPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        ImageView im = (ImageView) rootView.findViewById(R.id.imageView);
        Picasso.with(this.getContext()).load("http://"+ "apptitudedigitalsolutions.com:8080" + "/" + 4 + "/" + "slide1.png").into(im);
        return rootView;
    }
}
