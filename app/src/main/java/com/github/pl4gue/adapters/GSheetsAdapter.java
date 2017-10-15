package com.github.pl4gue.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pl4gue.R;
import com.github.pl4gue.data.entity.HomeWorkEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public class GSheetsAdapter extends RecyclerView.Adapter<GSheetsAdapter.HomeWorkDataHolder> {
    private List<HomeWorkEntry> mHomeWorkList;
    //private Context context;

    //    public GSheetsAdapter(List<HomeWorkEntry> homeWorkEntryList,Context context) {
//        mHomeWorkList = homeWorkEntryList;
//    }


    public GSheetsAdapter(List<HomeWorkEntry> homeWorkEntryList) {
        mHomeWorkList = homeWorkEntryList;
    }

    @Override
    public HomeWorkDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_homework, parent, false);
        return new HomeWorkDataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeWorkDataHolder holder, int position) {
        holder.mHomeWork.setText(mHomeWorkList.get(position).getHomework());
        holder.mHomeWorkDueDate.setText(mHomeWorkList.get(position).getHomeworkDueDate());
        holder.mHomeWorkEntryDate.setText(mHomeWorkList.get(position).getHomeworkEntryDate());
        holder.mHomeWorkSubject.setText(mHomeWorkList.get(position).getHomeworkSubject());
    }

    @Override
    public int getItemCount() {
        if (mHomeWorkList == null)
            return 0;
        return mHomeWorkList.size();
    }

    public class HomeWorkDataHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.homework)
        TextView mHomeWork;
        @BindView(R.id.homeworkDueDate)
        TextView mHomeWorkDueDate;
        @BindView(R.id.homeworkEntryDate)
        TextView mHomeWorkEntryDate;
        @BindView(R.id.homeworkSubject)
        TextView mHomeWorkSubject;

        public HomeWorkDataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
