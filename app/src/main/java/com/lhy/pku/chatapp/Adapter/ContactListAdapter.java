package com.lhy.pku.chatapp.Adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lhy.pku.chatapp.R;
import com.lhy.pku.chatapp.BR;
import com.lhy.pku.chatapp.model.Contact;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{

    private List<Contact> dataList;
    private final PublishSubject<Contact> onClickSubject = PublishSubject.create();


    public ContactListAdapter(List<Contact> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        protected T binding;

        public ViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public T getBinding() {
            return binding;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contact_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        holder.getBinding().setVariable(BR.contact ,dataList.get(pos));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(dataList.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
    public Observable<Contact> getPositionClicks(){
        return onClickSubject;
    }
}
