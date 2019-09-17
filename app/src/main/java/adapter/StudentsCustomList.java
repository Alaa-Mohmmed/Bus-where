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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.Student;

//import com.squareup.picasso.Picasso;


public class StudentsCustomList extends ArrayAdapter<Student> {

    Context context;
    int layoutResourceId;
    ArrayList<Student> items;

    public StudentsCustomList(Context context, ArrayList<Student> items, int layoutResourceId) {

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
            row = inflater.inflate(R.layout.student_list_row_item, null);

            holder = new ViewHolder();

            holder.setItemId((TextView) row.findViewById(R.id.list_row_name1));
            holder.setItemName((TextView) row.findViewById(R.id.list_row_name2));

            holder.setCallImgBtn((ImageButton) row.findViewById(R.id.listRowCallImgBtn));
            holder.setDeleteImgBtn((ImageButton) row.findViewById(R.id.list_row_map));

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        Student itemListObject = items.get(position);

        holder.getItemId().setText(itemListObject.getName());
        holder.getItemName().setText(itemListObject.getBusName());
        holder.getCallImgBtn().setTag(itemListObject);
        holder.getDeleteImgBtn().setTag(itemListObject);
        holder.getItemId().setTag(itemListObject);




        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return row;
    }


    static class ViewHolder {
        TextView itemId, itemName;
        ImageButton callImgBtn, deleteImgBtn;

        public TextView getItemId() {
            return itemId;
        }

        public void setItemId(TextView itemId) {
            this.itemId = itemId;
        }

        public TextView getItemName() {
            return itemName;
        }

        public void setItemName(TextView itemName) {
            this.itemName = itemName;
        }

        public ImageButton getCallImgBtn() {
            return callImgBtn;
        }

        public void setCallImgBtn(ImageButton callImgBtn) {
            this.callImgBtn = callImgBtn;
        }

        public ImageButton getDeleteImgBtn() {
            return deleteImgBtn;
        }

        public void setDeleteImgBtn(ImageButton deleteImgBtn) {
            this.deleteImgBtn = deleteImgBtn;
        }
    }

}
