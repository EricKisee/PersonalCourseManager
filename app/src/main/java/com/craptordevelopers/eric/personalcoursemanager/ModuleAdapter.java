package com.craptordevelopers.eric.personalcoursemanager;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.craptordevelopers.eric.personalcoursemanager.Database.Models.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.MyViewHolder> {

    private List<Module> moduleArrayList = new ArrayList<>();
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_module, tv_semester, tv_dot, tv_marks;

        public MyViewHolder(View view) {
            super(view);
            tv_module = view.findViewById(R.id.ed_module);
            tv_dot = view.findViewById(R.id.dot);
            tv_semester = view.findViewById(R.id.semester);
            tv_marks = view.findViewById(R.id.marks);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ModuleAdapter(Context context , List<Module> myDataset) {
        this.context = context;
        moduleArrayList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ModuleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.module_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Module module = moduleArrayList.get(position);
        holder.tv_dot.setText(Html.fromHtml("&#8226;"));
        holder.tv_module.setText(module.getModule());
        holder.tv_semester.setText("semester: "+ module.getSemester());
        holder.tv_marks.setText("marks: "+module.getMarks());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return moduleArrayList.size();
    }
}