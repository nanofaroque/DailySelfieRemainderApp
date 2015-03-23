package finalproject.part2.coursera.com.dailyselfieremainderapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ofaro on 3/22/2015.
 */
public class PhotoAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private final ArrayList<String> listOfImages;

    public PhotoAdapter(Activity context, ArrayList<String> listOfImages) {
        super(context, R.layout.layout_list_view_photo, listOfImages);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.listOfImages = listOfImages;

    }


    public View getView(int position,View view,ViewGroup parent) {

        ViewHolder holder;

        if(view == null)
        {
            LayoutInflater inflater=context.getLayoutInflater();
            view =inflater.inflate(R.layout.layout_list_view_photo, null,true);

            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            holder.txtTitle = (TextView) view.findViewById(R.id.image_name);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }


        Bitmap bitmap = BitmapFactory.decodeFile(listOfImages.get(position));
        File f = new File(listOfImages.get(position));

        holder.txtTitle.setText(f.getName());
        holder.imageView.setImageBitmap(bitmap);

        return view;

    };



}

class ViewHolder {
    TextView txtTitle;
    ImageView imageView;
}


