package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aou.buswhere.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.Station;
import model.Station;

//import com.squareup.picasso.Picasso;


public class CheckinCustomList extends ArrayAdapter<Station> {

    Context context;
    int layoutResourceId;
    ArrayList<Station> items;

    public CheckinCustomList(Context context, ArrayList<Station> items, int layoutResourceId) {

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
            row = inflater.inflate(R.layout.checkin_list_row_item, null);

            holder = new ViewHolder();

            holder.setItemId((TextView) row.findViewById(R.id.listRowNameTextView));

            holder.setCallImgBtn((ImageButton) row.findViewById(R.id.listRowCallImgBtn));
            holder.setCheckinImgBtn((Button) row.findViewById(R.id.listRowCheckinImgBtn));
            holder.setCheckoutImgBtn((Button) row.findViewById(R.id.listRowCheckoutImgBtn));
            holder.setImgView((ImageView) row.findViewById(R.id.listRowImageView));

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Station itemListObject = items.get(position);

        holder.getItemId().setText(itemListObject.getStudentName());
        holder.getCallImgBtn().setTag(itemListObject);
        holder.getCheckinImgBtn().setTag(itemListObject);
        holder.getCheckoutImgBtn().setTag(itemListObject);

        System.out.println("img url : " + itemListObject.getImgUrl());

        Picasso.get().load(itemListObject.getImgUrl()).into(holder.getImgView());




        return row;
    }


    static class ViewHolder {
        TextView itemId;
        ImageView imgView;
        ImageButton callImgBtn;
        Button checkinImgBtn, checkoutImgBtn;

        public TextView getItemId() {
            return itemId;
        }

        public void setItemId(TextView itemId) {
            this.itemId = itemId;
        }

        public ImageView getImgView() {
            return imgView;
        }

        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }

        public ImageButton getCallImgBtn() {
            return callImgBtn;
        }

        public void setCallImgBtn(ImageButton callImgBtn) {
            this.callImgBtn = callImgBtn;
        }

        public Button getCheckinImgBtn() {
            return checkinImgBtn;
        }

        public void setCheckinImgBtn(Button checkinImgBtn) {
            this.checkinImgBtn = checkinImgBtn;
        }

        public Button getCheckoutImgBtn() {
            return checkoutImgBtn;
        }

        public void setCheckoutImgBtn(Button checkoutImgBtn) {
            this.checkoutImgBtn = checkoutImgBtn;
        }
    }

}
