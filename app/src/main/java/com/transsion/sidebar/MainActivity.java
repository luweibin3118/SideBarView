package com.transsion.sidebarview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.transsion.sidebar.SideBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SideBarView sideBarView = findViewById(R.id.side_bar);
        List sideTextList = new ArrayList<>();
        String name = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张";
        for (int i = 0; i < name.length(); i++) {
            sideTextList.add(String.valueOf(name.charAt(i)));
        }
        sideBarView.setSideText(sideTextList);
        sideBarView.setOnSideItemSelectListener(new SideBarView.OnSideItemSelectListener() {
            @Override
            public void onSelectItem(int position, String title) {
                Log.i("TTT", "on select item: " + position + "  name: " + title);
            }
        });
    }
}
