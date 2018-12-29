package com.lhy.pku.chatapp.Adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lhy.pku.chatapp.Config.UserInfo;
import com.lhy.pku.chatapp.R;
import com.lhy.pku.chatapp.Util.DateHelper;
import com.lhy.pku.chatapp.model.Message;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private List<Message> dataList;

    public ChatMessageAdapter(List<Message> dataList) {
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout myselfLayout;
        private ConstraintLayout othersLayout;
        private TextView messageOther;
        private TextView timeOther;
        private TextView messageMyself;
        private TextView timeMyself;
        public ViewHolder(View convertView) {
            super(convertView);
            messageOther = convertView.findViewById(R.id.other_message_content);
            timeOther = convertView.findViewById(R.id.other_message_time);
            messageMyself = convertView.findViewById(R.id.myself_message_content);
            timeMyself = convertView.findViewById(R.id.myself_message_time);
            myselfLayout = convertView.findViewById(R.id.room_message_myself_layout);
            othersLayout = convertView.findViewById(R.id.room_message_other_layout);        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room_message, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(dataList.get(position).getFrom().getId().equals(UserInfo.getInstance().getUserSnapshot().getId())){
            holder.myselfLayout.setVisibility(View.VISIBLE);
            holder.othersLayout.setVisibility(View.GONE);
            holder.messageMyself.setText(dataList.get(position).getContent());
            holder.timeMyself.setText(DateHelper.formatDateToString(dataList.get(position).getTime()));
        }else {
            holder.othersLayout.setVisibility(View.VISIBLE);
            holder.myselfLayout.setVisibility(View.GONE);
            holder.messageOther.setText(dataList.get(position).getContent());
            holder.timeOther.setText(DateHelper.formatDateToString(dataList.get(position).getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}