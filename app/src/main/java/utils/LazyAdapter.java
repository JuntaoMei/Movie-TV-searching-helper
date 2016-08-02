package utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juntao.tmdb.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by juntao on 7/29/16.
 */
public  class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private List<HashMap<String, Object>> data;
    private static LayoutInflater inflater= null;



    public LazyAdapter(Activity a, List<HashMap<String, Object>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return  data.size();
    }

    public Object getItem(int position) {
        return  position;
    }

    public long getItemId(int position) {
        return  position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

//        if(MainActivity.mode == true) {
            return getViewForMovie(position, convertView, parent);
//        } else {
//            return getViewForTV(position, convertView, parent);
//        }
    }


    public View getViewForMovie(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item, null);

        TextView title = (TextView)vi.findViewById(R.id.title);
        TextView year = (TextView)vi.findViewById(R.id.year);
        TextView movieVote = (TextView)vi.findViewById(R.id.vote_average);
        ImageView image=(ImageView)vi.findViewById(R.id.list_image);

        HashMap<String, Object> movie =  data.get(position);

        title.setText((CharSequence) movie.get("title").toString());
        year.setText((CharSequence) movie.get("year").toString());
        movieVote.setText(movie.get("vote_average").toString());
        Picasso.with(activity.getApplicationContext()).load((String)movie.get("image")).into(image);
//        Picasso.with(activity.getApplicationContext())
//                .load((String)movie.get("image"))
//                .resize(50, 50)
//                .centerCrop()
//                .into(image);
        //ImageLoader.with(activity.getApplicationContext()).load((String)movie.get("image"), image);
        //imageLoader.DisplayImage((String) movie.get("image"), image);
        return  vi;
    }

}
