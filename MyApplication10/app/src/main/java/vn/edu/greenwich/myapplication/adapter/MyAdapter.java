package vn.edu.greenwich.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.greenwich.myapplication.R;
import vn.edu.greenwich.myapplication.UpdateActivity;
import vn.edu.greenwich.myapplication.model.Expense;
import vn.edu.greenwich.myapplication.model.Staff;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    static Context context;
    ArrayList<Staff> staffList;
    ArrayList<Staff> staffOriginalList;
    public ArrayList<Expense> expenseList;
    final private Activity activity;
    protected MyAdapter.ItemFilter _itemFilter = new MyAdapter.ItemFilter();

    public MyAdapter(Activity activity,Context context, ArrayList<Staff> staff){
        this.context = context;
        this.staffList = staff;
        this.activity = activity;
        this.staffOriginalList = staff;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Staff staff = this.staffList.get(position);
        holder.id_txt.setText(String.valueOf(staff.getId()));
        holder.name_txt.setText(String.valueOf(staff.getName()));
        holder.destination_txt.setText(String.valueOf(staff.getDestination()));
        holder.date_txt.setText(String.valueOf(staff.getDate()));


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(staff.getId()));
                intent.putExtra("name", String.valueOf(staff.getName()));
                intent.putExtra("destination", String.valueOf(staff.getDestination()));
                intent.putExtra("date", String.valueOf(staff.getDate()));
                intent.putExtra("tripName", String.valueOf(staff.getBusiness_trip_name()));
                intent.putExtra("description", String.valueOf(staff.getDescription()));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_txt, name_txt, destination_txt, date_txt;


        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_txt = itemView.findViewById(R.id.id_txt);
            name_txt = itemView.findViewById(R.id.name_txt);
            destination_txt = itemView.findViewById(R.id.destination_txt);
            date_txt = itemView.findViewById(R.id.date_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Staff> list = staffOriginalList;
            final ArrayList<Staff> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (Staff staff : list) {

                if (staff.getName().toLowerCase().contains(filterString)
                        ||staff.getDestination().toLowerCase().contains(filterString)
                        ||staff.getDate().toLowerCase().contains(filterString)
                        ||staff.getId().toString().contains(filterString))nlist.add(staff);
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            staffList = (ArrayList<Staff>) results.values;
            notifyDataSetChanged();
        }
    }
}
