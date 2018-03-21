package com.topway.fine.model;

import android.provider.ContactsContract;

import com.topway.fine.R;

/**
 * andriod系统联系人data表字段数据
 * @version 1.0
 * @created 2016-3-1
 */
public class PhoneData {
    public String id;
    public String type;
    public String data1;
    public String data2;

    public boolean isPhone() {
        if (type.contains("/phone_v2"))
            return true;
        return false;
    }

    public boolean isAddress() {
        if (type.contains("/postal-address_v2"))
            return true;
        return false;
    }

    public int getImage() {
        int url = R.drawable.detail_icon_phone;
        // 电话类型
        if (type.contains("/phone_v2")) {
            url = R.drawable.detail_icon_phone;
        }

        // 网络电话
        else if (type.contains("/sip_address")) {
            url = R.drawable.detail_icon_phone;
        }

        // 电子邮件
        else if (type.contains("/email_v2")) {
            url = R.drawable.detail_icon_email;
        }

        // 即时通讯
        else if (type.contains("/im")) {
            url = R.drawable.default_icon_chat;
        }

        // 昵称
        else if (type.contains("/nickname")) {
            //url = ;
        }

        // 单位
        else if (type.contains("/organization")) {
            url = R.drawable.default_icon_org;
        }

        // 名称
        else if (type.contains("/name")) {
            //url = "";
        }

        // 地址
        else if (type.contains("/postal-address_v2")) {
            url = R.drawable.detail_icon_locate;
        }

        // 身份ID
        else if (type.contains("/identity")) {
            url = R.drawable.icon_account;
        }

        // 图片
        else if (type.contains("/photo")) {
            url = R.drawable.icon_account;
        }

        // 备注
        else if (type.contains("/note")) {
            url = R.drawable.detail_icon_note;
        }

        // 成员组
        else if (type.contains("/group_membership")) {
            url = R.drawable.detail_icon_group;
        }

        // 生日
        else if (type.contains("/contact_event")) {
            url = R.drawable.detail_icon_event;
        }

        // 网址
        else if (type.contains("/website")) {
            url = R.drawable.detail_icon_web;
        }
        return url;
    }

    /*
     * 判断数据类型的minetype
     *  prefiex vnd.android.cursor.item
     */
    public void transData2() {

        int data2type = 0;
        try {
            data2type = Integer.parseInt(data2);
        } catch(NumberFormatException ex) {
            return;
        }

        // 电话类型
        if (type.contains("/phone_v2")) {
            switch (data2type) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                    data2 = "助理电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                    data2 = "回拨电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                    data2 = "电话卡";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                    data2 = "公司电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                    data2 = "传真";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                    data2 = "传真";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                    data2 = "ISDN";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                    data2 = "MMS";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                    data2 = "传真";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    data2 = "电话";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                    data2 = "手机";
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                    data2 = "电话";
                    break;
            }
        }

        // 网络电话
        else if (type.contains("/sip_address")) {
            switch (data2type) {
                case ContactsContract.CommonDataKinds.SipAddress.TYPE_HOME:
                    data2 = "网络电话";
                    break;
                case ContactsContract.CommonDataKinds.SipAddress.TYPE_OTHER:
                    data2 = "网络电话";
                    break;
                case ContactsContract.CommonDataKinds.SipAddress.TYPE_WORK:
                    data2 = "网络电话";
                    break;
            }
        }

        // 电子邮件
        else if (type.contains("/email_v2")) {
            switch (data2type) {
                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                    data2 = "邮箱";
                    break;
                case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                    data2 = "邮箱";
                    break;
                case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                    data2 = "邮箱";
                    break;
                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                    data2 = "邮箱";
                    break;
            }
        }

        // 即时通讯
        else if (type.contains("/im")) {
            switch (data2type) {
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM:
                    data2 = "AIM";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM:
                    data2 = "其他即IM";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK:
                    data2 = "GoogleTalk";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ:
                    data2 = "ICQ";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER:
                    data2 = "ICQ";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN:
                    data2 = "MSN";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_NETMEETING:
                    data2 = "网络会议";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ:
                    data2 = "QQ";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE:
                    data2 = "Skype";
                    break;
                case ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO:
                    data2 = "Yahoo";
                    break;
            }

            // IM分类
            switch (data2type) {
                case ContactsContract.CommonDataKinds.Im.TYPE_HOME:
                    break;
                case ContactsContract.CommonDataKinds.Im.TYPE_OTHER:
                    break;
                case ContactsContract.CommonDataKinds.Im.TYPE_WORK:
                    break;
            }
        }

        // 昵称
        else if (type.contains("/nickname")) {
            data2 = "昵称";
        }

        // 单位
        else if (type.contains("/organization")) {
            data2 = "单位";
        }

        // 名称
        else if (type.contains("/name")) {
            data2 = "名称";
        }

        // 地址
        else if (type.contains("/postal-address_v2")) {
            switch (data2type) {
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME:
                    data2 = "地址";
                    break;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER:
                    data2 = "地址";
                    break;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK:
                    data2 = "地址";
                    break;
            }
        }

        // 身份ID
        else if (type.contains("/identity")) {
            data2 = "身份";
        }

        // 图片
        else if (type.contains("/photo")) {
            data2 = "图片";
        }

        // 备注
        else if (type.contains("/note")) {
            data2 = "备注";
        }

        // 成员组
        else if (type.contains("/group_membership")) {
            data2 = "分组";
        }

        // 生日
        else if (type.contains("/contact_event")) {
            switch (data2type) {
                case ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY:
                    data2 = "纪念日";
                    break;
                case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY:
                    data2 = "生日";
                    break;
                case ContactsContract.CommonDataKinds.Event.TYPE_OTHER:
                    data2 = "生日";
                    break;
            }
        }

        // 网址
        else if (type.contains("/website")) {
            data2 = "网址";
        }
    }
}
