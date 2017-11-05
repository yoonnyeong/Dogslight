package com.example.user.dogslight;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


// 나의 정보, 강아지, 사료를 관리하는 프래그먼트
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // 사용자 정의 변수
    private AlertDialog dialog;

    private ListView myInfoListView;    // 나의 개인정보를 관리하는 리스트뷰
    private MyInfoListAdapter myInfoListAdapter; // 리스트뷰에 연결된 어댑터
    private List<MyInfo> myInfoList;               // 개인정보 관리 항목을 담는 리스트

    private ListView myDogListView;     // 나의 강아지 정보를 관리하는 리스트뷰
    private MyDogListAdapter myDogListAdapter; // 리스트뷰에 연결된 어댑터
    private List<MyDog> myDogList;              // 강아지 한 마리의 정보를 담는 리스트

    private ListView myDogFoodListView;     // 나의 강아지 사료 정보를 관리하는 리스트뷰
    private MyDogFoodListAdapter myDogFoodListAdapter; // 리스트뷰에 연결된 어댑터
    private List<MyDogFood> myDogFoodList;  // 사료 하나의 정보를 담는 리스트


    // MyFragment가 상단에 오면 데이터를 다시 가져온다.
    @Override
    public void onResume() {
        super.onResume();
        new BackgroundTaskDog().execute();
        new BackgroundTaskFood().execute();

    }

    // Activity가 생성될 때, 아래와 같이 작동한다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 개인정보에 관련된 listView를 불러온다.
        myInfoListView = (ListView) getView().findViewById(R.id.myInfoListView);
        // listView에 담을 내용인 myInfoList를 선언한다.
        myInfoList = new ArrayList<MyInfo>();
        // myInfoList에 들어갈 항목을 추가한다.
        myInfoList.add(new MyInfo("비밀번호 변경"));
        myInfoList.add(new MyInfo("로그아웃"));
        // MyInfoListAdapter 객체를 만들어 myInfoList를 담는다.
        myInfoListAdapter = new MyInfoListAdapter(getContext().getApplicationContext(), myInfoList);
        // myInfoListView에 myInfoList를 담은 어댑터를 연결한다.
        myInfoListView.setAdapter(myInfoListAdapter);
        // 어댑터에 리스트가 추가됐음을 알려서 myInfoListView에 반영되도록 한다.
        myInfoListAdapter.notifyDataSetChanged();

        // myInfoListView의 항목을 클릭하면 다음과 같은 일을 한다.
        myInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;

                // 비밀번호 변경을 클릭하면 다음과 같은 일을 한다.
                if(position == 0) {
                    // 자신의 비밀번호를 알고 있는지 확인하는 ValidatePwdActivity로 이동한다.
                    Intent intent = new Intent(getActivity(), ValidatePwdActivity.class);
                    intent.putExtra("userID", userID);
                    getActivity().startActivity(intent);
                }
                // 로그아웃을 클릭하면 다음과 같은 일을 한다.
                if(position == 1) {
                    // 알림창을 띄워 로그아웃을 할 것인지 확인한다.
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("로그아웃 하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 확인 버튼을 누르면, LoginActivity로 가게 된다.
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("취소", null)
                            .create();
                    dialog.show();
                }
            }
        });


        // 강아지 정보 listView를 불러온다
        myDogListView = (ListView) getView().findViewById(R.id.myDogListView);
        // listView에 담을 내용인 myDogList를 선언한다.
        myDogList = new ArrayList<MyDog>();
        // myDogListAdapter 객체를 만들어 myDogList를 담는다.
        myDogListAdapter = new MyDogListAdapter(getContext().getApplicationContext(), myDogList, this);
        // myDogListView에 myDogList를 담은 어댑터를 연결한다.
        myDogListView.setAdapter(myDogListAdapter);
        // 리스트뷰에 포커싱되어 삭제버튼이 눌리지 않는 일을 막기 위해 focus를 false로 한다.
        myDogListView.setFocusable(false);

        // myDogListView의 항목을 클릭하면 다음과 같은 일을 한다.
        myDogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String dogName;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 현재 클릭된 항목의 값을 변수에 저장한다.
                dogName = myDogList.get(position).getMyDogName();
                // 선택된 강아지의 정보를 수정하는 EditDogActivity로
                // 강아지 이름을 담아 이동한다.
                Intent intent = new Intent(getActivity(), EditDogActivity.class);
                intent.putExtra("dogName", dogName);
                getActivity().startActivity(intent);
            }
        });


        // 사료 정보 listView를 불러온다
        myDogFoodListView = (ListView) getView().findViewById(R.id.dogFoodListView);
        // listView에 담을 내용인 myDogFoodList를 선언한다.
        myDogFoodList = new ArrayList<MyDogFood>();
        // myDogFoodListAdapter 객체를 만들어 myDogFoodList를 담는다.
        myDogFoodListAdapter = new MyDogFoodListAdapter(getContext().getApplicationContext(), myDogFoodList, this);
        // myDogFoodListView에 myDogFoodList를 담은 어댑터를 연결한다.
        myDogFoodListView.setAdapter(myDogFoodListAdapter);
        // 리스트뷰에 포커싱되어 삭제버튼이 눌리지 않는 일을 막기 위해 focus를 false로 한다.
        myDogFoodListView.setFocusable(false);

        // myDogFoodListView의 항목을 클릭하면 다음과 같은 일을 한다.
        myDogFoodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String foodName;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 현재 클릭된 항목의 값을 변수에 저장한다.
                foodName = myDogFoodList.get(position).getMyFoodName();
                // 선택된 사료의 정보를 수정하는 EditDogFoodActivity로
                // 사료 이름을 담아 이동한다.
                Intent intent = new Intent(getActivity(), EditDogFoodActivity.class);
                intent.putExtra("foodName", foodName);
                getActivity().startActivity(intent);
            }
        });

        // 강아지를 추가하는 버튼을 클릭하면 다음과 같은 일을 한다.
        Button addDogButton = (Button) getView().findViewById(R.id.addDogButton);
        addDogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;
                // 강아지를 추가하는 액티비티인 AddDogActivity로 이동한다.
                Intent intent = new Intent(getActivity(), AddDogActivity.class);
                getActivity().startActivity(intent);

            }

        });

        // 사료를 추가하는 버튼을 클릭하면 다음과 같은 일을 한다.
        Button addFoodButton = (Button) getView().findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity에 static으로 선언된 userID를 가져온다.
                String userID = MainActivity.userID;
                // 사료를 추가하는 액티비티인 AddDogActivity로 이동한다.
                Intent intent = new Intent(getActivity(), AddDogFoodActivity.class);
                getActivity().startActivity(intent);

            }
        });

        // 나의 강아지 목록을 가져온다.
        new BackgroundTaskDog().execute();
        // 나의 강아지 사료 목록을 가져온다.
        new BackgroundTaskFood().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;

       // return inflater.inflate(R.layout.fragment_my, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // 나의 강아지 목록을 가져온다.
    class BackgroundTaskDog extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 MyDogList 를 지정한다.
                // 사용자 id를 같이 넘겨준다.
                target = "http://dms7147.cafe24.com/MyDogList.php?userID=" + URLEncoder.encode(userID, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // 위에서 지정한 target을 url로 만든다.
                URL url = new URL(target);
                // url을 연결하고 값을 읽어온다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                // 끝이 아닐 때까지 값을 읽어온다.
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                // 작업을 종료한 것들은 닫는다.
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                myDogList.clear();
                // JSONObject의 response값을 얻어온다.
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                // array의 길이와 비교할 변수인 count를 선언한다.
                int count = 0;
                // 읽어온 값을 담을 변수를 선언한다.
                String myDogName;
                String myDogAges;
                Double myDogWeight;
                // 다 읽어올 때까지 while문을 반복한다.
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // object에서 전해준 값을, BackgroundTaskDog내의 변수에 저장한다.
                    myDogName = object.getString("myDogName");
                    myDogAges = object.getString("myDogAges");
                    myDogWeight = object.getDouble("myDogWeight");
                    MyDog myDog = new MyDog(myDogName, myDogAges, myDogWeight);
                    // List에 MyDog 객체를 추가한다.
                    myDogList.add(myDog);
                    count++;
                }
                // 어댑터에 데이터의 변경을 알려준다.
                myDogListAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 나의 사료 목록을 가져온다.
    class BackgroundTaskFood extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 MyDogFoodList 를 지정한다.
                // 사용자 id를 같이 넘겨준다.
                target = "http://dms7147.cafe24.com/MyDogFoodList.php?userID=" + URLEncoder.encode(userID, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // 위에서 지정한 target을 url로 만든다.
                URL url = new URL(target);
                // url을 연결하고 값을 읽어온다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                // 끝이 아닐 때까지 값을 읽어온다.
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                // 작업을 종료한 것들은 닫는다.
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                myDogFoodList.clear();
                // JSONObject의 response값을 얻어온다.
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                // array의 길이와 비교할 변수인 count를 선언한다.
                int count = 0;
                // 읽어온 값을 담을 변수를 선언한다.
                String myFoodName;
                Double myFoodProtein;
                Double myFoodFat;
                Double myFoodFiber;
                // 다 읽어올 때까지 while문을 반복한다.
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // object에서 전해준 값을, BackgroundTask내의 변수에 저장한다.
                    myFoodName = object.getString("myFoodName");
                    myFoodProtein = object.getDouble("myFoodProtein");
                    myFoodFat = object.getDouble("myFoodFat");
                    myFoodFiber = object.getDouble("myFoodFiber");
                    MyDogFood myDogFood = new MyDogFood(myFoodName, myFoodProtein, myFoodFat, myFoodFiber);
                    // List에 MyDogFood 객체를 추가한다
                    myDogFoodList.add(myDogFood);
                    count++;
                }
                // 어댑터에 데이터의 변경을 알려준다.
                myDogFoodListAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
