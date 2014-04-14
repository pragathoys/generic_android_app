package com.pragathoys.lib.adapters;



import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.List;

public class GridAdapter extends BaseAdapter {
	private Context context;

        private List<Bitmap> images;
        private List<Integer> ids;
        
	public GridAdapter(Context c, List<Bitmap> images, List<Integer> ids) {
		context = c;
                this.images = images;
                this.ids = ids;
	}
	
	public int getCount() {
		return images.size();
	}
	
	public Object getItem(int position) {
		return images.get(position);
	}
	
	public long getItemId(int position) {
		return ids.get(position);
	}
	
	public View getView(int position, View view, ViewGroup parent) {
		ImageView iview;
		if (view == null) {
			iview = new ImageView(context);
			iview.setLayoutParams(new GridView.LayoutParams(200,200));
			iview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iview.setPadding(5, 5, 5, 5);
		} else {
			iview = (ImageView) view;	
		}
                iview.setImageBitmap(images.get(position));
		return iview;
	}
}