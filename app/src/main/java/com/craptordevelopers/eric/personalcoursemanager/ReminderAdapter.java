package com.craptordevelopers.eric.personalcoursemanager;


        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.text.Html;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Reminder;

        import java.util.ArrayList;
        import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {

    private List<Reminder> reminderArrayList = new ArrayList<>();
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_reminder , tv_time , tv_dot;

        public MyViewHolder(View view) {
            super(view);
            tv_reminder = view.findViewById(R.id.tv_reminder);
            tv_dot = view.findViewById(R.id.tv_dot);
            tv_time = view.findViewById(R.id.tv_time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReminderAdapter(Context context , List<Reminder> myDataset) {
        this.context = context;
        reminderArrayList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReminderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Reminder reminder = reminderArrayList.get(position);
        holder.tv_dot.setText(Html.fromHtml("&#8226;"));
        holder.tv_reminder.setText(reminder.getReminder());
        holder.tv_time.setText(reminder.getTimestamp());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reminderArrayList.size();
    }
}