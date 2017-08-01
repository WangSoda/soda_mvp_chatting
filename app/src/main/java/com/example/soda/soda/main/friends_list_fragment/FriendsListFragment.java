package com.example.soda.soda.main.friends_list_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.soda.soda.R;
import com.example.soda.soda.chatting.ChattingActivity;
import com.example.soda.soda.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by soda on 2017/7/27.
 * 用于展示用户的朋友列表
 */

public class FriendsListFragment extends BaseFragment implements FriendsListContract.View{


    private FriendsListPresnter mPresenter;
    @Override
    public void setPresenter(FriendsListContract.Presenter presenter) {
        mPresenter = (FriendsListPresnter) presenter;
    }
    private View root;
    @BindView(R.id.friends_list_recycler)
    RecyclerView friendsListRecycler;
    LinearLayoutManager layoutManager;
    List<String> mNameList;
    NameListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friends_list,container,false);
        ButterKnife.bind(this,root);
        mNameList = new ArrayList<>();
        mAdapter = new NameListAdapter();
        layoutManager = new LinearLayoutManager(getContext());
        friendsListRecycler.setAdapter(mAdapter);
        friendsListRecycler.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNameList.size() == 0){
            mPresenter.start();
        }
    }

    @Override
    public void updateFriendsList(List<String> friends) {
        for (int i = 0; i < friends.size(); i++) {
            mNameList.add(friends.get(i));
            Log.d("updateFriendsList","name =" + friends.get(i));
        }
        Log.d("updateFriendsList","nameListSize =" + mNameList.size());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    class NameListAdapter extends RecyclerView.Adapter<NameListAdapter.NameListItemViewHolder>{
        @Override
        public NameListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friends,parent,false);
            NameListItemViewHolder holder = new NameListItemViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(NameListItemViewHolder holder, final int position) {
            holder.friendName.setText(mNameList.get(position));
            {
                holder.friendName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChattingActivity.startActivity(getContext(),mNameList.get(position));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mNameList.size();
        }

        class NameListItemViewHolder extends RecyclerView.ViewHolder{
            TextView friendName;
            public NameListItemViewHolder(View itemView) {
                super(itemView);
                friendName = itemView.findViewById(R.id.nick_name_text_friends_fragment);
            }
        }
    }
}
