package com.example.soda.soda.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.soda.soda.R;
import com.example.soda.soda.R2;
import com.example.soda.soda.data.DataSourceResponse;
import com.example.soda.soda.main.chat_list_fragment.ChatListFragment;
import com.example.soda.soda.main.chat_list_fragment.ChatListPresenter;
import com.example.soda.soda.main.friends_list_fragment.FriendsListFragment;
import com.example.soda.soda.main.friends_list_fragment.FriendsListPresnter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R2.id.pager_view_pager_main)
    ViewPager pagerViewPagerMain;

    List<Fragment> pagerList;

    private DataSourceResponse dataSourceResponse;

    private FriendsListPresnter mFriendsPresenter;
    private FriendsListFragment mFriendsView;

    private ChatListPresenter mChatListPresenter;
    private ChatListFragment mChatListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter
                (getSupportFragmentManager());

        dataSourceResponse = DataSourceResponse.getInstance();

        mFriendsView = new FriendsListFragment();
        mFriendsPresenter = new FriendsListPresnter(dataSourceResponse,mFriendsView);
        mFriendsView.setPresenter(mFriendsPresenter);

        mChatListView = new ChatListFragment();
        mChatListPresenter = new ChatListPresenter(dataSourceResponse,mChatListView);
        mChatListView.setPresenter(mChatListPresenter);

        pagerList = new ArrayList<>();
        pagerList.add(mChatListView);//显示顺序
        pagerList.add(mFriendsView);
        pagerList.add(new MineListFragment());
        pagerViewPagerMain.setAdapter(adapter);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    class MainFragmentPagerAdapter extends FragmentStatePagerAdapter{
        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return pagerList.get(position);
        }

        @Override
        public int getCount() {
            return pagerList.size();
        }
    }
}
