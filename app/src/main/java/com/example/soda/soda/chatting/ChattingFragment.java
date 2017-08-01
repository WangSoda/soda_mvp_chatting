package com.example.soda.soda.chatting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soda.soda.R;
import com.example.soda.soda.R2;
import com.example.soda.soda.app.AppInit;
import com.hyphenate.chat.EMMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hyphenate.chat.EMMessage.*;
import static com.hyphenate.chat.EMMessage.Type.*;

/**
 * Created by soda on 2017/7/29.
 * chattingFragment仅负责实时接收和展示聊天内容
 */

public class ChattingFragment extends Fragment implements ChattingContract.View{
    private ChattingContract.Presenter mPresenter;

    //显示页面中用于判断与那一个id的用户进行聊天行为的字段
    private String mChattingToUserId;

    public void setmChattingToUserId(String mChattingToUserId) {
        this.mChattingToUserId = mChattingToUserId;
    }
    @Override
    public String getmChattingToUserId() {
        return mChattingToUserId;
    }

    //映射 时间与消息所属者的关系
    private Map<Long,String> mChattingUsersMap;
    //映射 时间与聊天 文字内容的关系
    private Map<Long,String> mChattingTXTContentsMap;
    //映射 时间与图片链接的关系 但是首先 时间对应的是图片类型
    private Map<Long,String> mChattingIMAGEUrlMap;
    //映射 时间与内容类型的关系
    private Map<Long,EMMessage.Type> mChattingTypeMap;
    //存储时间的顺序
    private List<Long> mChattingTimeList;
    @Override
    public void setPresenter(ChattingContract.Presenter presenter) {
        mPresenter = presenter;
    }
    @BindView(R2.id.chatting_content_recycler)
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ChattingFragmentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.from(getContext()).inflate(R.layout.fragment_chatting,container,false);
        ButterKnife.bind(this,root);

        initRecyclerView();

        return root;
    }

    private void initRecyclerView() {
        if (mLayoutManager == null) mLayoutManager = new LinearLayoutManager(getContext());
        if (mChattingUsersMap == null) mChattingUsersMap = new HashMap<>();
        if (mChattingTXTContentsMap == null) mChattingTXTContentsMap = new HashMap<>();
        if (mChattingTypeMap == null)mChattingTypeMap = new HashMap<>();
        if (mChattingIMAGEUrlMap == null)mChattingIMAGEUrlMap = new HashMap<>();

        if (mChattingTimeList == null)mChattingTimeList = new LinkedList<>();
        if (mAdapter == null)mAdapter = new ChattingFragmentAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onSendMessage(String toUserId, String inputContent, long currentTimeMillis) {
        addTxtToAdapterList("mine",inputContent,currentTimeMillis, TXT);
        notifyAdapter();
    }

    @Override
    public void OnGetImageMessage(String fromUserId, String thumbnailUrl, long msgTime) {
        addImageToAdapterList(fromUserId,thumbnailUrl,msgTime, IMAGE);
        notifyAdapter();

    }



    @Override
    public void OnGetTxtMessage(String fromUserId, String txtContent, long msgTime) {
        addTxtToAdapterList(fromUserId,txtContent,msgTime, TXT);
        notifyAdapter();
    }

    private void addImageToAdapterList(String fromUserId, String thumbnailUrl, long msgTime, Type type) {
        mChattingTypeMap.put(msgTime,type);
        mChattingUsersMap.put(msgTime,fromUserId);
        mChattingIMAGEUrlMap.put(msgTime,thumbnailUrl);
        mChattingTimeList.add(msgTime);
    }

    /**
     * 因为信息是成组存在的，所以存储信息时单独设立一个方法来保证存储信息不会因为检查疏漏而出现错误
     * 而当信息添加完成时，必定需要更新ui
     * @param fromUserId
     * @param txtContent
     * @param msgTime
     */
    void addTxtToAdapterList(String fromUserId, String txtContent, long msgTime, Type type){
        mChattingTypeMap.put(msgTime,type);
        mChattingUsersMap.put(msgTime,fromUserId);
        mChattingTXTContentsMap.put(msgTime,txtContent);
        mChattingTimeList.add(msgTime);
    }

    private void notifyAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mChattingTimeList.size());
            }
        });
    }


    class ChattingFragmentAdapter extends RecyclerView.Adapter<ChattingFragmentAdapter.ChattingItemViewHolder>{
        @Override
        public ChattingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.item_chatting,parent,false);
            ChattingItemViewHolder holder = new ChattingItemViewHolder(root);
            return holder;
        }

        @Override
        public void onBindViewHolder(ChattingItemViewHolder holder, int position) {
            Long keyPositon = mChattingTimeList.get(position);
            Type messageType = mChattingTypeMap.get(keyPositon);

            switch (messageType){
                case TXT:
                    setTextMessage(holder,position);
                    break;
                case IMAGE:
                    setImageMessage(holder,position);
                    break;
            }
        }

        private void setImageMessage(ChattingItemViewHolder holder, int position) {
            Long keyTime = mChattingTimeList.get(position);

            String messageFrom = mChattingUsersMap.get(keyTime);

            String imageUrl = mChattingIMAGEUrlMap.get(keyTime);

            int time = (int) ((System.currentTimeMillis() - keyTime) / 1000);
            time = Math.abs(time);
            holder.messageTime.setText(time + "秒前");

            if (messageFrom.equals(mChattingToUserId)){
                //设定界面能显示的空间
                holder.messageTime.setVisibility(View.VISIBLE);
                holder.mineMessageLayout.setVisibility(View.GONE);
                holder.othersImage.setVisibility(View.VISIBLE);
                holder.othersMessageContent.setVisibility(View.GONE);
                holder.othersMessageLayout.setVisibility(View.VISIBLE);
                //设定显示的内容
                Glide.with(AppInit.getInstance()).load(imageUrl).into(holder.othersImage);
            }else {
                holder.messageTime.setVisibility(View.VISIBLE);
                holder.othersMessageLayout.setVisibility(View.GONE);
                holder.mineMessageLayout.setVisibility(View.VISIBLE);
                holder.mineImage.setVisibility(View.VISIBLE);
                holder.mineMessageContent.setVisibility(View.INVISIBLE);

                Glide.with(AppInit.getInstance()).load(imageUrl).into(holder.mineImage);
            }
        }

        /**
         * 将文字的设定方法提取出来，设定一个方法，便于在处理其他类型的信息时可以绝对干净
         * @param holder
         * @param position
         */
        private void setTextMessage(ChattingItemViewHolder holder, int position){
            //当前文字信息在map中对应的时间
            Long keyTime = mChattingTimeList.get(position);
            //当前文字信息的发起人
            String messageFrom = mChattingUsersMap.get(keyTime);
            //当前文字信息的文字内容
            String messageTextContent = mChattingTXTContentsMap.get(keyTime);
            //如果消息来自于对方，应该将我方消息展示布局隐藏

            //设置聊天内容上面的聊天时间文字提示
            int time = (int) ((System.currentTimeMillis() - keyTime) / 1000);
            time = Math.abs(time);
            holder.messageTime.setText(time + "秒前");
            //将信息内容设置给对方发起人的布局中
            if (messageFrom.equals(mChattingToUserId)){
                //设定界面能显示的空间
                holder.messageTime.setVisibility(View.VISIBLE);
                holder.mineMessageLayout.setVisibility(View.GONE);
                holder.othersImage.setVisibility(View.GONE);
                holder.othersMessageContent.setVisibility(View.VISIBLE);
                holder.othersMessageLayout.setVisibility(View.VISIBLE);
                //设定显示的内容
                holder.othersMessageContent.setText(messageTextContent);
            }else {
                holder.messageTime.setVisibility(View.VISIBLE);
                holder.othersMessageLayout.setVisibility(View.GONE);
                holder.mineImage.setVisibility(View.GONE);
                holder.othersMessageLayout.setVisibility(View.INVISIBLE);
                holder.mineMessageLayout.setVisibility(View.VISIBLE);

                holder.mineMessageContent.setText(messageTextContent);
            }
        }

        @Override
        public int getItemCount() {
            return mChattingTimeList.size();
        }

        class ChattingItemViewHolder extends RecyclerView.ViewHolder{
            //两种消息共有的部分
            TextView messageTime;
            //来自于外部的消息
            LinearLayout othersMessageLayout;//用于设定显示状态
            ImageView othersLogo;
            TextView othersMessageContent;
            //来自于本地的消息
            LinearLayout mineMessageLayout;
            ImageView mineLogo;
            TextView mineMessageContent;
            //聊天中图片显示的布局
            ImageView othersImage;
            ImageView mineImage;
            public ChattingItemViewHolder(View itemView) {
                super(itemView);
                //聊天内容上部的时间提示
                messageTime = itemView.findViewById(R.id.time_chatting_fragment_item);
                //对方消息的消息布局
                othersMessageLayout = itemView.findViewById(R.id.layout_others_chatting_fragment_item);
                othersLogo = itemView.findViewById(R.id.logo_others_chatting_fragment_item);
                othersMessageContent = itemView.findViewById(R.id.chat_content_others_chatting_fragment_item);
                //对方消息的图片 内容
                othersImage = itemView.findViewById(R.id.chat_image_others_chatting_fragment_item);

                mineMessageLayout = itemView.findViewById(R.id.layout_mine_chatting_fragment_item);
                mineLogo = itemView.findViewById(R.id.logo_mine_chatting_fragment_item);
                mineMessageContent = itemView.findViewById(R.id.chat_content_mine_chatting_fragment_item);

                mineImage = itemView.findViewById(R.id.chat_image_mine_chatting_fragment_item);

            }
        }
    }
}
