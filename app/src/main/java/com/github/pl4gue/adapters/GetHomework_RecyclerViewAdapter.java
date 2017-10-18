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

public class GetHomework_RecyclerViewAdapter extends RecyclerView.Adapter<GetHomework_RecyclerViewAdapter.HomeWorkDataHolder> {
    private List<HomeWorkEntry> mHomeWorkList;
    private Context context;

    public GetHomework_RecyclerViewAdapter(List<HomeWorkEntry> homeWorkEntryList, Context context) {
        mHomeWorkList = homeWorkEntryList;
        this.context = context;
    }

    @Override
    public HomeWorkDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_homework, parent, false);
        return new HomeWorkDataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeWorkDataHolder holder, int position) {
        holder.mHomeWorkSubject.setText(mHomeWorkList.get(position).getHomeworkSubject());
        holder.mHomeWorkEntryDate.setText(mHomeWorkList.get(position).getHomeworkEntryDate());
        holder.mHomeWorkDueDate.setText(mHomeWorkList.get(position).getHomeworkDueDate());
        holder.mHomeWork.setText(mHomeWorkList.get(position).getHomework());
    }

    @Override
    public int getItemCount() {
        if (mHomeWorkList == null)
            return 0;
        return mHomeWorkList.size();
    }

    class HomeWorkDataHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.showHomeworkCardViewHomework)
        TextView mHomeWork;
        @BindView(R.id.showHomeworkCardViewDueDate)
        TextView mHomeWorkDueDate;
        @BindView(R.id.showHomeworkCardViewEntryDate)
        TextView mHomeWorkEntryDate;
        @BindView(R.id.showHomeworkCardViewSubject)
        TextView mHomeWorkSubject;

        public HomeWorkDataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
