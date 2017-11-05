package com.example.user.dogslight;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// WalkNotice 클래스 타입의 데이터를 가지는 어댑터
public class WalkNoticeListAdapter extends BaseAdapter {
    private Context context;
    // WalkNotice 타입의 List를 선언한다.
    private List<WalkNotice> walkNoticeList;

    public WalkNoticeListAdapter(Context context, List<WalkNotice> walkNoticeList) {
        this.context = context;
        this.walkNoticeList = walkNoticeList;
    }

    @Override
    public int getCount() {
        return walkNoticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return walkNoticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ListView의 한 항목에 들어가는 디자인인 R.layout.walknotice를 전개한다.
        View v = View.inflate(context, R.layout.walknotice, null);
        // walknotice 안의 요소를 변수로 선언한다.
        TextView noticeText = (TextView) v.findViewById(R.id.noticeText);
        TextView nameText = (TextView) v.findViewById(R.id.nameText);
        TextView dateText = (TextView) v.findViewById(R.id.dateText);

        // 각각 textView의 내용을 현재 위치의 유의사항 내용, 이름, 날짜로 설정한다.
        noticeText.setText(walkNoticeList.get(position).getNotice());
        nameText.setText(walkNoticeList.get(position).getName());
        dateText.setText(walkNoticeList.get(position).getDate());

        v.setTag(walkNoticeList.get(position).getNotice());
        return v;

    }
}
