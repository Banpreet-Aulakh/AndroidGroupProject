package com.sfu.cmpt276.coopachievement.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sfu.cmpt276.coopachievement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;

    ArrayList<String> imagePaths = new ArrayList<>();

    LayoutInflater myLayoutInflater;

    public ViewPagerAdapter(Context context, ArrayList<String> imagePaths){
        this.context=context;
        this.imagePaths = imagePaths;
        myLayoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        //return number of images
        return imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object){
         return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //inflate item.xml
        View itemView = myLayoutInflater.inflate(R.layout.galleryitem,container,false);

        //referencing the image view from item.xml
        ImageView imageView = itemView.findViewById(R.id.imageViewMain);

        //set image in imageView
        Bitmap myBitmap = BitmapFactory.decodeFile(imagePaths.get(position));
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotateBitMap = Bitmap.createBitmap(myBitmap,0,0,myBitmap.getWidth(),myBitmap.getHeight(),matrix,true);
        imageView.setImageBitmap(rotateBitMap);

        //adding View
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        container.removeView((LinearLayout) object);
    }
}
