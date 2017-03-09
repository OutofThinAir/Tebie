package com.example.administrator.chenyipengxutil3work.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chenyipengxutil3work.R;
import com.example.administrator.chenyipengxutil3work.bean.Bean;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PullToRefreshExpandableListView expan_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        expan_list = (PullToRefreshExpandableListView) findViewById(R.id.expand_list);
        //请求数据
        getData("https://mock.eolinker.com/success/rq7m6GNqurs93zYkEANkY8Z4358Aihf1");
       // getDataHuancuan("https://mock.eolinker.com/success/rq7m6GNqurs93zYkEANkY8Z4358Aihf1");

        //下拉刷新上啦加载
        //设置刷新模式
        expan_list.setMode(PullToRefreshBase.Mode.BOTH);
        //刷新监听
        expan_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {

            //下拉刷新时调用
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                getData("https://mock.eolinker.com/success/rq7m6GNqurs93zYkEANkY8Z4358Aihf1");
                expan_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       expan_list.onRefreshComplete();
                    }
                },2000);

            }

            //上拉加载时调用
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                expan_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        expan_list.onRefreshComplete();
                    }
                },2000);

            }
        });

    }

    //自定义适配器
    private class MyExpandAdapter extends BaseExpandableListAdapter{
        private Context context;
        ArrayList<Bean.DataBean> fuList;
        ArrayList<Bean.DataBean.DatasBean> ziList;

        public MyExpandAdapter(Context context, ArrayList<Bean.DataBean> fuList, ArrayList<Bean.DataBean.DatasBean> ziList) {
            this.context = context;
            this.fuList = fuList;
            this.ziList = ziList;
        }

        //父条目的数量
        @Override
        public int getGroupCount() {
            return fuList.size();
        }

        //子条目的数量
        @Override
        public int getChildrenCount(int groupPosition) {
            return fuList.get(groupPosition).getDatas().size();
        }

        //获得父条目
        @Override
        public Object getGroup(int groupPosition) {
            return fuList.get(groupPosition);
        }

        //获得子条目
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return fuList.get(groupPosition).getDatas().get(childPosition);
        }

        //获得父条目的ID
        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        //获得子条目的id
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        //是否具有稳定的id
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //返回父条目的视图
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder1 v1;
            if (convertView == null) {
                convertView=View.inflate(context,R.layout.list_fu_item,null);
                v1=new ViewHolder1();
                v1.checBox= (CheckBox) convertView.findViewById(R.id.checkbox);
                v1.textView = (TextView) convertView.findViewById(R.id.fu_item_text);
                convertView.setTag(v1);
            }else {
                v1= (ViewHolder1) convertView.getTag();
            }
            v1.textView.setText(fuList.get(groupPosition).getTitle());
            return convertView;

        }

        //返回子条目的视图
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder2 v2 ;
            if (convertView == null) {
                convertView=View.inflate(context,R.layout.list_zi_item,null);
                v2=new ViewHolder2();
                v2.checBox2= (CheckBox) convertView.findViewById(R.id.zi_check);
                v2.textView2= (TextView) convertView.findViewById(R.id.zi_item_title);
                v2.textView3= (TextView) convertView.findViewById(R.id.zi_item_info);
                v2.price= (TextView) convertView.findViewById(R.id.zi_item_price);
                convertView.setTag(v2);
            }else {
                v2= (ViewHolder2) convertView.getTag();

            }
            v2.textView2.setText(fuList.get(groupPosition).getDatas().get(childPosition).getType_name());
            v2.textView3.setText(fuList.get(groupPosition).getDatas().get(childPosition).getMsg());
            v2.price.setText("￥"+fuList.get(groupPosition).getDatas().get(childPosition).getPrice());
            return convertView;
        }

        //子条目是否可点击
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        class ViewHolder1{
            CheckBox checBox;
            TextView textView;
        }

        class ViewHolder2{
            CheckBox checBox2;
            TextView textView2;
            TextView textView3;
            TextView price;

        }
    }

    //网络请求数据
   private void getData(String url){

       //用请求参数这个类封装url
       RequestParams params = new RequestParams(url);
       //Xutil请求数据
       //参数2：普通接口回调，获得请求的数据
       //若是设置缓存,接口是另一个

       x.http().get(params, new Callback.CommonCallback<String>() {

           //请求成功后调用
           @Override
           public void onSuccess(String result) {
               //获得数据Gson解析
               Gson g = new Gson();
               Bean bean = g.fromJson(result,Bean.class);
               //获得外层集合
               ArrayList<Bean.DataBean> fuList = (ArrayList<Bean.DataBean>) bean.getData();
               Log.d("onSuccess",fuList.toString());
               //获得内层集合
               ArrayList<Bean.DataBean.DatasBean> ziList =new ArrayList<Bean.DataBean.DatasBean>();

               //遍历父集合
               for (int i = 0; i <fuList.size() ; i++) {
                   for (int j = 0; j <bean.getData().get(i).getDatas().size() ; j++) {
                       Bean.DataBean.DatasBean d = fuList.get(i).getDatas().get(j);
                       ziList.add(d);
                   }

             }
               //expandList设置适配器
               MyExpandAdapter adapter = new MyExpandAdapter(MainActivity.this,fuList,ziList);
              // 通过PullToRefreshExpandableListView.getRefreshableView();
               //来获得ExpandableListView的实例，然后用ExpandableListView的实例来进行数据的加载即可
               expan_list.getRefreshableView().setAdapter(adapter);
             // expan_list.setAdapter((ListAdapter) adapter);
              //expan_list.g
               adapter.notifyDataSetChanged();




           }

           //请求错误时调用
           @Override
           public void onError(Throwable ex, boolean isOnCallback) {
               Log.d("onError------","onError");
           }

           //请求取消时调用
           @Override
           public void onCancelled(CancelledException cex) {
               Log.d("onCancelled------","onCancelled");
           }

           //请求完成调用
           @Override
           public void onFinished() {
               Log.d("onFinished------","onFinished");
           }
       });
   }

    //网络请求数据+缓存
    private void getDataHuancuan(String url){
        //用请求参数这个类封装url
        RequestParams params = new RequestParams(url);
        //Xutil请求数据
        //参数2：普通接口回调，获得请求的数据
        //若是设置缓存,接口是另一个
        //缓存
        //设置缓存时间
        params.setCacheMaxAge(1000*60*60);

        x.http().get(params, new Callback.CacheCallback<String>() {
            //定义一个string接受缓存
            private String result;

            @Override
            public void onSuccess(String result) {
                //当第二次进入的时候将缓存的数据显示出来
                if (result==null){
                    this.result=result;
                }

                //获得数据Gson解析
                Gson g = new Gson();
                Bean bean = g.fromJson(result,Bean.class);
                //获得外层集合
                ArrayList<Bean.DataBean> fuList = (ArrayList<Bean.DataBean>) bean.getData();
                Log.d("onSuccess",fuList.toString());
                //获得内层集合
                ArrayList<Bean.DataBean.DatasBean> ziList =new ArrayList<Bean.DataBean.DatasBean>();

                //遍历父集合
                for (int i = 0; i <fuList.size() ; i++) {
                    for (int j = 0; j <bean.getData().get(i).getDatas().size() ; j++) {
                        Bean.DataBean.DatasBean d = fuList.get(i).getDatas().get(j);
                        ziList.add(d);
                    }

                }
                //expandList设置适配器
                MyExpandAdapter adapter = new MyExpandAdapter(MainActivity.this,fuList,ziList);
                expan_list.setAdapter((ListAdapter) adapter);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            //缓存时调用
            @Override
            public boolean onCache(String result) {
                //默认是不缓存,改为true
                //将缓存的数据存储到定义的变量中
                this.result=result;
                Toast.makeText(MainActivity.this, "onCache", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

}
