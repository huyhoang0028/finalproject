package vn.edu.greenwich.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.greenwich.myapplication.R;
import vn.edu.greenwich.myapplication.UpdateActivity;
import vn.edu.greenwich.myapplication.model.Expense;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    static Context context;
    public ArrayList<Expense> expenseList;
    final private Activity activity;




    public Adapter(Context context,Activity activity,ArrayList<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        this.activity = activity;
    }

    public Adapter(ArrayList<Expense> expenseList, Activity activity) {
        this.expenseList = expenseList;
        this.activity = activity;
    }



    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_list_expense, parent, false);
        return new Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Expense expense = this.expenseList.get(position);
        holder.typeOfCost.setText(String.valueOf(expense.getType_of_cost()));
        holder.Expense.setText(String.valueOf(expense.getCost()));
        holder.Date.setText(String.valueOf(expense.getDateOfCost()));
        holder.Time.setText(String.valueOf(expense.getTimeOfCost()));
        holder.AdditionalComment.setText(String.valueOf(expense.getAdditional_Comment()));


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(expense.getId()));
                intent.putExtra("typeOfCost", String.valueOf(expense.getType_of_cost()));
                intent.putExtra("date", String.valueOf(expense.getDateOfCost()));
                intent.putExtra("cost", String.valueOf(expense.getCost()));
                intent.putExtra("time", String.valueOf(expense.getTimeOfCost()));
                intent.putExtra("date", String.valueOf(expense.getAdditional_Comment()));
                intent.putExtra("additionalComment", String.valueOf(expense.getAdditional_Comment()));
                intent.putExtra("staffID", String.valueOf(expense.getStaffId()));
                activity.startActivityForResult(intent, 1);
            }
        });
    }



    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView typeOfCost, Date, Time, Expense, AdditionalComment;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            typeOfCost = itemView.findViewById(R.id.showTypeOfCost);
            Date = itemView.findViewById(R.id.showDate);
            Time = itemView.findViewById(R.id.showTime);
            Expense = itemView.findViewById(R.id.showExpense);
            AdditionalComment = itemView.findViewById(R.id.showAdditionalComment);
            mainLayout = itemView.findViewById(R.id.mainLayout2);

        }
    }
}
