package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aou.buswhere.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.CheckIn;
import model.Station;

//import com.squareup.picasso.Picasso;


public class TripDetailsParentCustomList extends ArrayAdapter<CheckIn> {

    Context context;
    int layoutResourceId;
    ArrayList<CheckIn> items;

    public TripDetailsParentCustomList(Context context, ArrayList<CheckIn> items, int layoutResourceId) {

        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
        // this.data = data;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {

            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.parent_checkin_list_row_item, null);

            holder = new ViewHolder();

            holder.setDateTextView((TextView) row.findViewById(R.id.timeTextView));
            holder.setTripType((TextView) row.findViewById(R.id.tripTypeTextView));

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        CheckIn itemListObject = items.get(position);

        holder.getDateTextView().setText(itemListObject.getTime());
        if (itemListObject.getType().equalsIgnoreCase("CheckOut")) {
            holder.getTripType().setText("Drop off");

        } else {
            holder.getTripType().setText("Pick up");

        }


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return row;
    }


    static class ViewHolder {
        TextView dateTextView, tripType;

        public TextView getDateTextView() {
            return dateTextView;
        }

        public void setDateTextView(TextView dateTextView) {
            this.dateTextView = dateTextView;
        }

        public TextView getTripType() {
            return tripType;
        }

        public void setTripType(TextView tripType) {
            this.tripType = tripType;
        }
    }

}
