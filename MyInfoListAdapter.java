package com.example.user.dogslight;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

// MyInfo 클래스 타입의 데이터를 가지는 어댑터
public class MyInfoListAdapter extends BaseAdapter {
    private Context context;
    // MyInfo 타입의 List를 선언한다.
    private List<MyInfo> myInfoList;

    public MyInfoListAdapter(Context context, List<MyInfo> myInfoList) {
        this.context = context;
        this.myInfoList = myInfoList;
    }

    @Override
    public int getCount() {
        return myInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return myInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // ListView의 한 항목에 들어가는 디자인인 R.layout.myinfo를 전개한다.
        View v = View.inflate(context, R.layout.myinfo, null);
        // myinfo 파일 안의 요소를 변수로 선언한다.
        TextView myInfoText = (TextView) v.findViewById(R.id.myInfoText);
        ImageView imageView = (ImageView) v.findViewById(R.id.image);

        // 텍스트뷰의 내용은 현재 위치의 infoText의 내용으로 설정한다.
        myInfoText.setText(myInfoList.get(position).getInfoText());

        v.setTag(myInfoList.get(position).getInfoText());
        return v;
    }
}