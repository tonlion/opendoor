package com.zhima.opendoor.view;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhima.opendoor.R;
 

 
/**
 * ���ڹ���Dialog
 * 
 * 
 * 
 */
public class DialogManager {
 
    private AlertDialog.Builder builder;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLable;
 
    private Context mContext;
 
     private AlertDialog dialog;//����ȡ��AlertDialog.Builder
 
    /**
     * ���췽�� ����������
     */
    public DialogManager(Context context) {
        this.mContext = context;
    }
 
    // ��ʾ¼���ĶԻ���
    public void showRecordingDialog() {
 
        builder = new AlertDialog.Builder(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_recorder,null);
 
        mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);
        mLable = (TextView) view.findViewById(R.id.id_recorder_dialog_label);
 
        builder.setView(view);
        builder.create();
        dialog = builder.show();
    }
 
    public void recording(){
        if(dialog != null && dialog.isShowing()){ //��ʾ״̬
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);
 
            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText("��ָ�ϻ���ȡ������");
        }
    }
 
    // ��ʾ��ȡ���ĶԻ���
    public void wantToCancel() {
        if(dialog != null && dialog.isShowing()){ //��ʾ״̬
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
 
            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText("�ɿ���ָ��ȡ������");
        }
    }
 
    // ��ʾʱ����̵ĶԻ���
    public void tooShort() {
        if(dialog != null && dialog.isShowing()){ //��ʾ״̬
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
 
            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText("¼��ʱ�����");
        }
    }
 
    // ��ʾȡ���ĶԻ���
    public void dimissDialog() {
        if(dialog != null && dialog.isShowing()){ //��ʾ״̬
            dialog.dismiss();
            dialog = null;
        }
    }
 
    // ��ʾ������������ĶԻ���
    public void updateVoiceLevel(int level) {
        if(dialog != null && dialog.isShowing()){ //��ʾ״̬
//          mIcon.setVisibility(View.VISIBLE);
//          mVoice.setVisibility(View.VISIBLE);
//          mLable.setVisibility(View.VISIBLE);
 
            //����ͼƬ��id
            int resId = mContext.getResources().getIdentifier("v"+level, "drawable", mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
 
}