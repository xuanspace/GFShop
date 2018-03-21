package com.topway.fine.adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Company;
import com.topway.fine.model.PhoneContact;
import com.topway.fine.model.PhoneData;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.utils.PinyinUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 手机联系人列表数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhoneContactAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private ListView listView;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    PinyinUtil pinyin = PinyinUtil.getInstance();
    private String search;
    private boolean makeAlpha;
    private static HashMap<Integer,PhoneContact> cache;
    private static SparseArray<IndexItem> indexs;
    private static boolean isLoaded = false;

    public PhoneContactAdapter() {
        this.search = "";
        this.makeAlpha = false;
    }

    public PhoneContactAdapter(Context context) {
        this.context = context;
        this.search = "";
        this.makeAlpha = false;
    }

    public PhoneContactAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        this.search = "";
        this.makeAlpha = false;
    }

    class IndexItem {
        public char alpha;
        public ArrayList<PhoneContact> items;

        public IndexItem() {
            items = new ArrayList<PhoneContact>();
        }

        public void clean() {
            if (items != null)
                items.clear();
        }
    }

    public void init() {
        // 在此初始化index,防止多线程问题
        isLoaded = false;
        cleanIndex();
        initIndex();

        // 用线程异步执行获取联系人信息
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                notifyDataSetChanged();
            }
        };
        new Thread() {
            @Override
            public void run() {
                getContacts();
                Message message = Message.obtain();
                message.obj = null;
                handler.sendMessage(message);
                isLoaded = true;
            }
        }.start();
    }

    public void getContacts() {
        if (cache == null)
            cache = new HashMap<Integer,PhoneContact>();
        if (cache.size() > 0 ) {
            buildIndex();
            return;
        }

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI,
                new String[] { "_id", "contact_id", ContactsContract.Contacts.DISPLAY_NAME, "phonebook_label" },
                null, null, "phonebook_label + \" ASC\"");

        while (cursor.moveToNext()) {
            PhoneContact contact = new PhoneContact();
            String id = cursor.getString(0);
            contact.contact_id = cursor.getString(1);
            contact.name = cursor.getString(2);
            contact.alpha = cursor.getString(3);

            Integer key = Integer.parseInt(id);
            contact.id = key.intValue();
            cache.put(key,contact);

            char letter = contact.alpha.charAt(0);
            IndexItem index = indexs.get(letter);
            if (index == null) {
                index = new IndexItem();
                index.alpha = letter;
                indexs.put(letter,index);
            }
            index.items.add(contact);
        }
    }

    public PhoneContact getContactData(int raw_contact_id) {
        PhoneContact contact = cache.get(raw_contact_id);
        if (contact == null) {
            contact = new PhoneContact();
            contact.id = raw_contact_id;
            cache.put(contact.id, contact);
        }

        // 是否查询过data,有则跳过
        if (contact.data.size() > 0)
            return contact;

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(uri,
                new String[] { "_id", "mimetype", "data1", "data2" },
                "raw_contact_id=" + raw_contact_id , null, "mimetype + \" ASC\"");

        while (cursor.moveToNext()) {
            PhoneData item = new PhoneData();
            item.id = cursor.getString(0);
            item.type = cursor.getString(1);
            item.data1 = cursor.getString(2);
            item.data2 = cursor.getString(3);

            // 跳过联系人名，
            if (item.type.lastIndexOf("/name") != -1)
                continue;
            contact.data.add(item);
        }
        return contact;
    }

    public void deletePhoneData(PhoneContact contact, PhoneData data) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/data");
        resolver.delete(uri,"_id=" + data.id, null);
        contact.data.remove(data);
    }

    public String getPhoneLastTime(String number) {
        String lasttime = "";
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                new String[]{"date"}, "number=" + number, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            lasttime = cursor.getString(0);
            Long value = Long.parseLong(lasttime);
            SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
            Date date= new Date(value);
            lasttime = sf.format(date);
        }
        return lasttime;
    }

    public void search(String keyword) {
        cleanIndex();
        initIndex();

        this.search = keyword.trim();
        Iterator iter = cache.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            PhoneContact contact = (PhoneContact)entry.getValue();
            if (keyword.isEmpty()) {
                addIndex(contact);
            }
            else if (contact.name.indexOf(keyword) != -1) {
                addIndex(contact);
            }
        }
        notifyDataSetChanged();
    }

    public void initIndex() {
        if (indexs == null)
            indexs = new SparseArray<IndexItem>();
        IndexItem index = new IndexItem();
        index.alpha = '?';
        indexs.put('?',index);
    }

    public void addIndex(PhoneContact contact) {
        char letter = contact.alpha.charAt(0);
        IndexItem index = indexs.get(letter);
        if (index == null) {
            index = new IndexItem();
            index.alpha = letter;
            indexs.put(letter,index);
        }
        index.items.add(contact);
    }

    public void cleanIndex() {
        if (indexs != null) {
            for (int i = 0; i < indexs.size(); i++)
                indexs.valueAt(i).clean();
            indexs.clear();
        }
    }

    public void buildIndex() {
        Iterator iter = cache.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            PhoneContact contact = (PhoneContact)entry.getValue();
            addIndex(contact);
        }
    }

    public void initData() {
        getContacts();
        notifyDataSetChanged();
    }

    public void loadData() {

    }

    public PhoneContact getContactDetail(int position) {
        PhoneContact contact = (PhoneContact)getItem(position);
        getContactData(contact.getId());
        return contact;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (isLoaded) {
            for (int i = 0; i < indexs.size(); i++) {
                IndexItem index = indexs.valueAt(i);
                count += index.items.size();
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        PhoneContact item = null;
        if (isLoaded) {
            int length = 0;
            for (int i = 0; i < indexs.size(); i++) {
                IndexItem index = indexs.valueAt(i);
                length += index.items.size();
                if (position < length) {
                    int at = length - position - 1;
                    item = index.items.get(at);
                    break;
                }
            }
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //通过该项的位置，获得所在分类组的索引号
    public int getPositionForSection(int section) {
        int position = 0;
        if (isLoaded) {
            for (int i = 0; i < indexs.size(); i++) {
                IndexItem index = indexs.valueAt(i);
                position += index.items.size();
                if (i == (section - 1))
                    break;
                ;
            }
        }
        return position;
    }

    //根据分类列的索引号获得该序列的首个位置
    public int getSectionForPosition(int position) {
        int section = 0;
        if (isLoaded) {
            int length = 0;
            for (int i = 0; i < indexs.size(); i++) {
                IndexItem index = indexs.valueAt(i);
                length += index.items.size();
                if (position < length) {
                    section = i;
                    break;
                }
            }
        }
        return section-1;
    }

    public String getAlphaForPosition(int position) {
        char alpha = '?';
        if (isLoaded) {
            int section = 0, length = 0;
            for (int i = 0; i < indexs.size(); i++) {
                IndexItem index = indexs.valueAt(i);
                length += index.items.size();
                if (position < length) {
                    alpha = index.alpha;
                    break;
                }
            }
        }
        return String.format("%c",alpha);
    }

    //计算item->position,position->item
    @Override
    public Object[] getSections() {
        String[] sections = null;
        if (isLoaded) {
            sections = new String[indexs.size()];
            for (int i = 0; i < indexs.size(); i++) {
                IndexItem index = indexs.valueAt(i);
                sections[i] = String.format("%c", index.alpha);
            }
        }
        return sections;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_company_item2, null);
            holder = new ViewHolder();
            holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.tv_alpha = (TextView) convertView.findViewById(R.id.tv_alpha);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
            holder.view_temp = (View) convertView.findViewById(R.id.view_temp);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setValue(position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_logo;
        TextView tv_alpha;
        TextView tv_name;
        TextView tv_detail;
        View view_temp;

        public void setValue(int position) {
            PhoneContact item = (PhoneContact) getItem(position);
            if (item == null) return;

            tv_name.setText(item.getName());
            //tv_detail.setText(item.getName());

            // 设置A-Z字母分割条,通过首字母判断
            String header = getAlphaForPosition(position);
            if (position == 0 || header != null
                    && !header.equals(getAlphaForPosition(position-1))) {
                // 如果第当前用户标记了A-Z头显示带字母的分隔条
                if ("".equals(header)) {
                    tv_alpha.setVisibility(View.GONE);
                    view_temp.setVisibility(View.VISIBLE);
                } else {
                    tv_alpha.setVisibility(View.VISIBLE);
                    tv_alpha.setText(header);
                    view_temp.setVisibility(View.GONE);
                }
            } else {
                tv_alpha.setVisibility(View.GONE);
                view_temp.setVisibility(View.VISIBLE);
            }

        }
    }

}

