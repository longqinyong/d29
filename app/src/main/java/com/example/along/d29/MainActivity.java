package com.example.along.d29;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //声明控件对象
    private PullToRefreshListView pullToRefreshListView;
    private List<String> data;
    //声明数组适配器对象
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //通过id找到布局文件
        this.pullToRefreshListView= (PullToRefreshListView) this.findViewById(R.id.pullToRefreshListView);
        this.data=getdata();
        this.adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,data);
        this.pullToRefreshListView.setAdapter(adapter);

        //注册当前滑动到控件底部触发的监听器对象
        this.pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(MainActivity.this, "已经滑动到控件底部了!", Toast.LENGTH_SHORT).show();
            }
        });

        //注册滑动松开过程中播放不同的音效
        SoundPullEventListener<ListView> soundPullEventListener=new SoundPullEventListener<>(this);
        soundPullEventListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH,R.raw.pull_event);
        soundPullEventListener.addSoundEvent(PullToRefreshBase.State.REFRESHING,R.raw.refreshing_sound);
        soundPullEventListener.addSoundEvent(PullToRefreshBase.State.RESET,R.raw.reset_sound);
        this.pullToRefreshListView.setOnPullEventListener(soundPullEventListener);

        //首次下拉显示日期时间
        String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        this.pullToRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        //设置模式
        this.pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        //注册上下拉刷新对应的监听器
        this.pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            /**
             * 下拉后在松开手时自动调用的方法
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);


                System.out.println("label="+label);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);


                //下面的代码可以完成将下拉刷新功能缩回去的功能
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        data.add(0,"我是下拉之后显示的数据!");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //刷新界面
                                adapter.notifyDataSetChanged();
                                pullToRefreshListView.onRefreshComplete();
                            }
                        });
                    }
                }).start();
                System.out.println("===onPullDownToRefresh(PullToRefreshBase<ListView> refreshView="+refreshView+")===");
            }

            /**
             * 上拉后松开手时自动调用的方法
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);


                System.out.println("label="+label);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);


                //下面的代码可以完成将下拉刷新功能缩回去的功能
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        data.add("我是上拉之后显示的数据!");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //刷新界面
                                adapter.notifyDataSetChanged();
                                pullToRefreshListView.onRefreshComplete();
                            }
                        });
                    }
                }).start();
                System.out.println("===onPullUpToRefresh(PullToRefreshBase<ListView> refreshView="+refreshView+")==");
            }
        });
    }


    public List<String> getdata() {
        data=new ArrayList<>();
        for (int i=0;i<40;i++){
            data.add("小丽"+i);
        }
        return data;
    }
}
