package com.zhima.opendoor.utils;

import org.xml.sax.XMLReader;

import com.zhima.opendoor.activity.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
public class MxgsaTagHandler implements TagHandler {
    private int sIndex = 0;
    private int eIndex = 0;
    private final Context mContext;

    public MxgsaTagHandler(Context context) {
        mContext = context;
    }

    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // TODO Auto-generated method stub
        if (tag.equals("zhima")) {
            if (opening) {
                sIndex = output.length();
            } else {
                eIndex = output.length();
                output.setSpan(new MxgsaSpan(), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private class MxgsaSpan extends ClickableSpan implements OnClickListener {
        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub
            //閸忚渹缍嬫禒锝囩垳閿涘苯褰叉禒銉︽Ц鐠哄疇娴嗘い鐢告桨閿涘苯褰叉禒銉︽Ц瀵懓鍤�电鐦藉鍡礉娑撳娼伴弰顖濈儲鏉烆剟銆夐棃锟�
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

}