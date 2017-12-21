package com.transsion.sidebar;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {
    String[] text = new String[]{"半仙", "二丫", "狗子", "老四", "老五", "老根", "娃子", "三炮", "幺妹"};
    ContactsAdapter adapter;
    List<String> contactsList;
    List sideTextList;
    Map<String, Integer> indexMap = new HashMap<>();
    ListView contacts_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SideBarView sideBarView = findViewById(R.id.side_bar);
        sideTextList = new ArrayList<>();
        contactsList = new ArrayList<>();
        String name = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张";

        int max = text.length;
        int min = 1;
        Random random = new Random();
        int index = 0;
        for (int i = 0; i < name.length(); i++) {
            sideTextList.add(String.valueOf(name.charAt(i)));
            int size = random.nextInt(max) % (max - min + 1) + min;

            List<Integer> hasAdd = new ArrayList();

            for (int j = 0; j < size; j++) {
                int nameIndex = random.nextInt(text.length);
                while (hasAdd.contains(nameIndex)) {
                    nameIndex = random.nextInt(text.length);
                }
                hasAdd.add(nameIndex);
                contactsList.add(name.charAt(i) + text[nameIndex]);
                if (j == 0) {
                    indexMap.put(String.valueOf(name.charAt(i)), index);
                }
                index++;
            }
        }
        sideBarView.setSideText(sideTextList);
        sideBarView.setOnSideItemSelectListener(new SideBarView.OnSideItemSelectListener() {
            @Override
            public void onSelectItem(int position, String title) {
                if (contacts_list != null) {
                    contacts_list.setSelection(indexMap.get(title));
                }
            }
        });

        contacts_list = findViewById(R.id.contacts_list);
        contacts_list.setAdapter(adapter = new ContactsAdapter(contactsList));
        contacts_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                String name = contactsList.get(firstVisibleItem);
                int index = sideTextList.indexOf(String.valueOf(name.charAt(0)));
                sideBarView.setCurrentIndex(index);
            }
        });
    }

    class ContactsAdapter extends BaseAdapter {
        List<String> contacts;

        public ContactsAdapter(List<String> contacts) {
            this.contacts = contacts;
        }

        @Override
        public int getCount() {
            if (contacts != null) {
                return contacts.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.contact_item, null);
                viewHolder = new ViewHolder();
                viewHolder.contactName = convertView.findViewById(R.id.contact_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.contactName.setText(contacts.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView contactName;
    }
}