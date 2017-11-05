package com.example.user.dogslight;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

// MyDog 클래스 타입의 데이터를 가지는 어댑터
public class MyDogListAdapter extends BaseAdapter {
    private Context context;
    // MyDog 타입의 List를 선언한다.
    private List<MyDog> myDogList;
    // 어댑터를 부르는 프래그먼트를 선언한다.
    private Fragment fragment;

    // 생성자에 어댑터를 부르는 프래그먼트를 포함한다.
    public MyDogListAdapter(Context context, List<MyDog> myDogList, Fragment fragment) {
        this.context = context;
        this.myDogList = myDogList;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return myDogList.size();
    }

    @Override
    public Object getItem(int position) {
        return myDogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // ListView의 한 항목에 들어가는 디자인인 R.layout.mydog를 전개한다.
        View v = View.inflate(context, R.layout.mydog, null);
        // mydog 안에 요소를 변수로 선언한다.
        TextView myDogNameText = (TextView) v.findViewById(R.id.myDogNameText);
        TextView myDogAgesText = (TextView) v.findViewById(R.id.myDogAgesText);
        TextView myDogWeightText = (TextView) v.findViewById(R.id.myDogWeightText);

        // 각각 textView의 내용을 현재 위치의 강아지 이름, 나이대, 몸무게로 설정한다.
        myDogNameText.setText(myDogList.get(position).getMyDogName());
        myDogWeightText.setText(myDogList.get(position).getMyDogWeight().toString() + "kg");

        if (myDogList.get(position).getMyDogAges().equals("puppy")) {
            myDogAgesText.setText("자견");
        } else {
            myDogAgesText.setText("성견");
        }

        v.setTag(myDogList.get(position).getMyDogName());

        // mydog 안의 삭제 버튼을 변수로 선언한다.
        Button delDogButton = (Button) v.findViewById(R.id.delDogButton);
        // listView안에서 클릭되지 않는 이상, 삭제 버튼이 포커스를 갖지 않도록 한다.
        delDogButton.setFocusable(false);
        // 삭제 버튼이 눌리면 아래와 같은 일을 한다.
        delDogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;

                // 삭제 작업을 하는 리스너를 만든다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    //응답받도록
                    public void onResponse(String response) {
                        try {
                            // JSONObject의 response값을 얻어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            // response의 success값이 true, 즉 삭제가 성공적으로 이루어졌다면 아래와 같은 일을 한다.
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                                AlertDialog dialog = builder.setMessage("삭제되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                // list에서 삭제한 후, 데이터가 변경됐음을 어댑터 자신에게 알려 인지하도록 한다.
                                myDogList.remove(position);
                                notifyDataSetChanged();
                            }
                            // 삭제를 실패했다면 아래와 같이 알림창이 뜨게 한다.
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                                AlertDialog dialog = builder.setMessage("삭제에 실패했습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // DeleteDogRequest에 아이디와 리스트 중 현재 클릭한 강아지 이름, 리스너를 넘긴다.
                DeleteDogRequest deleteDogRequest = new DeleteDogRequest(userID, myDogList.get(position).getMyDogName(), responseListener);
                // 큐를 만들어 리퀘스트를 실제로 보낼 수 있도록 한다.
                RequestQueue queue = Volley.newRequestQueue(fragment.getActivity());
                queue.add(deleteDogRequest);
            }
        });

        return v;
    }
}
