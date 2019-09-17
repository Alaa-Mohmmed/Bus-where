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

import model.Bus;

//import com.squareup.picasso.Picasso;


public class BusesCustomList extends ArrayAdapter<Bus> {

    Context context;
    int layoutResourceId;
    ArrayList<Bus> items;

    public BusesCustomList(Context context, ArrayList<Bus> items, int layoutResourceId) {

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
            row = inflater.inflate(R.layout.bus_list_row_item, null);

            holder = new ViewHolder();

            holder.setItemId((TextView) row.findViewById(R.id.list_row_name1));

//            holder.setDeleteImgBtn((ImageButton) row.findViewById(R.id.list_row_delete));

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Bus itemListObject = items.get(position);

        holder.getItemId().setText(itemListObject.getName());
//        holder.getDeleteImgBtn().setTag(itemListObject);
        holder.getItemId().setTag(itemListObject);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return row;
    }


    static class ViewHolder {
        TextView itemId;
//        ImageButton deleteImgBtn;

        public TextView getItemId() {
            return itemId;
        }

        public void setItemId(TextView itemId) {
            this.itemId = itemId;
        }

//        public ImageButton getDeleteImgBtn() {
//            return deleteImgBtn;
//        }
//
//        public void setDeleteImgBtn(ImageButton deleteImgBtn) {
//            this.deleteImgBtn = deleteImgBtn;
//        }
    }

}
