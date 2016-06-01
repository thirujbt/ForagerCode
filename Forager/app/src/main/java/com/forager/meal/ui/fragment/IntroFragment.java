package com.forager.meal.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.forager.meal.ui.R;

public class IntroFragment extends Fragment {

    int[] res = {
            R.drawable.intro_one,
            R.drawable.intro_two,
            R.drawable.intro_three,
            R.drawable.intro_four,
            R.drawable.intro_five,
            R.drawable.intro_six,
            R.drawable.intro_seven,
    };

  ImageView mImageView;
    ImageButton mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.viewpager_image, null);
        Bundle args = getArguments();
        int pos = args.getInt("Pos");
        mImageView = (ImageView) root.findViewById(R.id.viewpager_image);
        mImageView.setImageResource(res[pos]);
       /* if(pos == 6) {
            mButton = (ImageButton) root.findViewById(R.id.imageButton_skip);
            mButton.setVisibility(View.VISIBLE);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), HomeScreen.class);
                    startActivity(intent);
                }
            });
        }*/
        return root;
    }
}
