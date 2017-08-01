package com.example.soda.soda.main.chat_list_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soda.soda.R;
import com.example.soda.soda.chatting.ChattingActivity;
import com.example.soda.soda.fragment.BaseFragment;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by soda on 2017/7/27.
 */

public class ChatListFragment extends BaseFragment implements ChatListContract.View{
    public ChatListContract.Presenter mPresenter;
    @Override
    public void setPresenter(ChatListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private View root;
    @BindView(R.id.chat_list_recycler)
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    ChatListAdapter mAdapter;
    //页面所关系的联系人列表  此页面因为联系人名为单键 所以以此为key
    List<String> mNameList;
    Map<String,String> mLastMessageContent;
    Map<String,EMMessage.Type> mLastMessageType;
    Map<String,Integer> mUnreadMessageCount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_chat_list,container,false);
        ButterKnife.bind(this,root);

        //初始化recyclerview
        initRecyclerView();


        return root;
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ChatListAdapter();

        mNameList = new ArrayList<>();
        mLastMessageContent = new HashMap<>();
        mLastMessageType = new HashMap<>();
        mUnreadMessageCount = new HashMap<>();

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }



    @Override
    public void startChattingActivity(String toUserId) {
        ChattingActivity.startActivity(getContext(),toUserId);
    }

    /**
     * 获取联系人的回调接口，重要
     * @param conversationMap
     */
    @Override
    public void upDateChatList(Map<String, EMConversation> conversationMap) {
        mNameList.clear();
        Set<String> keySet = conversationMap.keySet();
        for (String fromUserId:
             keySet) {
            EMConversation conversation = conversationMap.get(fromUserId);
            String conversationId = conversation.conversationId();
            EMMessage messageFromOthers = conversation.getLatestMessageFromOthers();
            if (messageFromOthers == null)continue;

            int unreadMsgCount = conversation.getUnreadMsgCount();
            EMMessage lastMessage = conversation.getLastMessage();
            EMMessage.Type lastMessageType = lastMessage.getType();

            mLastMessageType.put(conversationId,lastMessageType);
            mNameList.add(conversationId);
            if (EMMessage.Type.TXT == messageFromOthers.getType()){
                mLastMessageContent.put(conversationId,((EMTextMessageBody)messageFromOthers.getBody()).getMessage());
            }
            mUnreadMessageCount.put(conversationId,unreadMsgCount);

            //此方法不清除数据库消息
            conversation.clear();
        }
        notifyAdapter();
    }

    private void notifyAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void OnGetTxtMessage(String fromUserId, String txtContent, long msgTime) {
        if (mNameList.contains(fromUserId)){
        }else {
            mNameList.add(fromUserId);
        }
        mLastMessageContent.put(fromUserId,txtContent);
        notifyAdapter();
    }

    @Override
    public void OnGetMediaMessage(String fromUserId, long thumbnailUrl, EMMessage.Type type) {
        if (mNameList.contains(fromUserId)){
        }else {
            mNameList.add(fromUserId);
        }
        if (type == EMMessage.Type.IMAGE){
            mLastMessageContent.put(fromUserId,"[图片消息]");
        }
    }


    class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatItemViewHolder>{
        @Override
        public ChatItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.item_chat,parent,false);

            ChatItemViewHolder holder = new ChatItemViewHolder(root);
            return holder;
        }

        @Override
        public void onBindViewHolder(ChatItemViewHolder holder, int position) {
            final String toUserId = mNameList.get(position);
            EMMessage.Type type = mLastMessageType.get(toUserId);
            String chatContent = mLastMessageContent.get(toUserId);
            holder.chatNickName.setText(toUserId);
            if (type == EMMessage.Type.TXT){
                if (chatContent != null){
                holder.chatContent.setText(chatContent);
                }
            }else if (type == EMMessage.Type.IMAGE){
                holder.chatContent.setText("[图片消息]");
            }





            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.onUserClickItem(toUserId);
                }
            });

        }
        //我们关心的联系人数量便是条目的数量
        @Override
        public int getItemCount() {
            return mNameList.size();
        }

        class ChatItemViewHolder extends RecyclerView.ViewHolder{
            View root;
            ImageView chatLogo;
            TextView chatNickName;
            TextView chatContent;
            public ChatItemViewHolder(View itemView) {
                super(itemView);
                root = itemView;
                chatLogo = itemView.findViewById(R.id.nick_name_text_chat_fragment);
                chatNickName = itemView.findViewById(R.id.chat_nickname_text_chat_fragment);
                chatContent = itemView.findViewById(R.id.chat_content_text_chat_fragment);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("fragmentLife","onResume");
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("fragmentLife","onStop");
        mPresenter.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("fragmentLife","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("fragmentLife","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("fragmentLife","onDetach");
    }
}
