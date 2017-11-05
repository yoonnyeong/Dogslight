package com.example.user.dogslight;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// Walk 클래스 타입의 데이터를 가지는 어댑터
public class WalkListAdapter extends BaseAdapter{
    private Context context;
    // Walk 타입의 List를 선언한다.
    private List<Walk> walkList;

    public WalkListAdapter(Context context, List<Walk> walkList) { // 생성자
        this.context = context;
        this.walkList = walkList;
    }

    @Override
    public int getCount() {
        return walkList.size();
    }

    @Override
    public Object getItem(int i) {
        return walkList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // ListView의 한 항목에 들어가는 디자인인 R.layout.walk를 전개한다.
        View v = View.inflate(context, R.layout.walk, null);
        // walk 안의 요소를 변수로 선언한다.
        TextView walkText = (TextView) v.findViewById(R.id.walkText);
        TextView nameText = (TextView) v.findViewById(R.id.nameText);
        TextView dateText = (TextView) v.findViewById(R.id.dateText);

        // 각각 textView의 내용을 산책시간, 강아지 이름, 날짜로 설정한다.
        walkText.setText(walkList.get(i).getWalk());
        nameText.setText(walkList.get(i).getName());
        dateText.setText(walkList.get(i).getDate());

        v.setTag(walkList.get(i).getWalk());
        return v;
    }


}
