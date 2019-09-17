package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aou.buswhere.R;

import java.util.ArrayList;

import model.Violation;

//import com.squareup.picasso.Picasso;


public class ViolationsCustomList extends ArrayAdapter<Violation> {

    Context context;
    int layoutResourceId;
    ArrayList<Violation> items;

    public ViolationsCustomList(Context context, ArrayList<Violation> items, int layoutResourceId) {

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
            row = inflater.inflate(R.layout.violations_list_row_item, null);

            holder = new ViewHolder();

            holder.setItemId((TextView) row.findViewById(R.id.list_row_name1));
            holder.setItemTime((TextView) row.findViewById(R.id.list_row_name2));

            holder.setMapBtn((ImageButton) row.findViewById(R.id.list_row_map));

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Violation itemListObject = items.get(position);

        holder.getItemId().setText(itemListObject.getTime());
        holder.getItemTime().setText(itemListObject.getSpeed() + "");
        holder.getMapBtn().setTag(itemListObject);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return row;
    }


    static class ViewHolder {
        TextView itemId, itemTime;
        ImageButton mapBtn;

        public TextView getItemId() {
            return itemId;
        }

        public void setItemId(TextView itemId) {
            this.itemId = itemId;
        }

        public TextView getItemTime() {
            return itemTime;
        }

        public void setItemTime(TextView itemTime) {
            this.itemTime = itemTime;
        }


        public ImageButton getMapBtn() {
            return mapBtn;
        }

        public void setMapBtn(ImageButton mapBtn) {
            this.mapBtn = mapBtn;
        }
    }

}
