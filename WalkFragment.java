package com.example.user.dogslight;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Spinner;

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

// 산책 시간을 기록하고,
// 산책 기록을 개월별로 추가하고,
// 아두이노의 불빛을 이용해 야간산책 모드를 사용한다.
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WalkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public WalkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalkFragment newInstance(String param1, String param2) {
        WalkFragment fragment = new WalkFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_walk, container, false); // 이 부분에 내가 원하는 곳 // false 작동될 때 그 때 실행.
        return root;

    }


    // 사용자 정의 변수
    private Chronometer mChronometer;  // 타이머
    private Button startButton, resetButton, recButton;  // 타이머 시작, 리셋, 기록 버튼
    private Button lightButton;  // 야간모드 버튼

    private ListView walkListView;  // 나의 산책 기록을 관리하는 리스트뷰
    private WalkListAdapter walkListAdapter; // 리스트뷰에 연결된 어댑터
    private List<Walk> walkList;    // 산책 기록 항목을 담는 리스트


    private boolean chonometerOn = false;  // 타이머가 켜져있는지 확인하는 변수

    private String recordTime ="";  // 산책 시간을 기록하기 위한 변수
    private String month = "";  // 리스트뷰를 추가로 보여주기 위한 개월 변수
    private Spinner walkSpinner;    // 리스트뷰를 추가로 보여주기 위한 개월 스피너


    @Override
    public void onResume() {
        super.onResume();
        new BackgroundTaskSchedule().execute();
    }

    // Activity가 생성될 때, 아래와 같이 작동한다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) { // 날짜 받아 오는곳
        super.onActivityCreated(savedInstanceState);


        // 산책 기록에 관련된 listView를 불러온다.
        walkListView = (ListView) getView().findViewById(R.id.walkListView);
        // listView에 담을 내용인 walkList를 선언한다.
        walkList = new ArrayList<Walk>();
        // WalkListAdapter 객체를 만들어 walkList를 담는다.
        walkListAdapter = new WalkListAdapter(getContext(), walkList);
        // walkListView에 walkList를 담은 어댑터를 연결한다.
        walkListView.setAdapter(walkListAdapter);
        // 산책 기록을 보여준다.
        new BackgroundTaskSchedule().execute();

        // 산책 기록을 월 단위로 보여주도록 관리할 스피너를 변수에 저장한다.
        walkSpinner =(Spinner)getView().findViewById(R.id.walkSpinner);
        // R,array,date를 담은 어댑터를 만든다.
        ArrayAdapter walkAdapter = ArrayAdapter.createFromResource(getContext(), R.array.date, android.R.layout.simple_spinner_item);
        // 스피너에 어댑터를 연결한다.
        walkSpinner.setAdapter(walkAdapter);
        // 스피너를 클릭하면 다음과 같은 일을 한다.
        walkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 스피너에서 선택한 개월 수를 month에 저장한다.
                month = walkSpinner.getSelectedItem().toString();
                // 해당 월 수의 산책 기록을 가져와 리스트뷰에 보여준다.
                new BackgroundTaskSchedule().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // xml의 변수들을 가져온다.
        mChronometer = (Chronometer) getView().findViewById(R.id.chronometer);
        startButton = (Button) getView().findViewById(R.id.startButton);
        resetButton = (Button) getView().findViewById(R.id.resetButton);
        recButton = (Button) getView().findViewById(R.id.recButton);
        lightButton = (Button) getView().findViewById(R.id.lightButton);

        // 시작 버튼.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 시간을 초기화한다.
                mChronometer.setBase(SystemClock.elapsedRealtime());
                // 타이머를 시작한다.
                mChronometer.start();
                // 타이머 시작을 확인하는 변수를 true로 바꿔준다.
                chonometerOn = true;
            }
        });

        // 리셋 버튼.
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 타이머를 멈춘다.
                mChronometer.stop();
                // 시간을 초기화한다.
                mChronometer.setBase(SystemClock.elapsedRealtime());
                // 타이머 시작을 확인하는 변수를 false로 바꿔준다.
                chonometerOn = false;
            }
        });

        // 기록 버튼.
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 타이머가 시작되어 있다면 아래 문장을 실행한다.
                if(chonometerOn) {
                    // 타이머를 멈춘다.
                    mChronometer.stop();
                    // 타이머 시작을 확인하는 변수를 false로 바꿔준다.
                    chonometerOn = false;

                    // 현재 시간에서 타이머를 시작할 때의 시간을 뺀다.
                    long time = (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000;
                    // time이 1분이 넘으면 아래와 같이 한다.
                    if (time >= 60) {
                        // 분과 초로 나눈 후, recordTime에 저장한다.
                        int sec = (int) time % 60;
                        int min = (int) time / 60;
                        recordTime = min + "분" + sec + "초";

                        // time이 1시간이 넘는다면 아래와 같이 한다.
                        if (time >= 3600) {
                            // 시간, 분, 초로 만들어 recordTime에 저장한다.
                            sec = (int) time % 60;
                            min = (int) time / 60;
                            int hour = (int) time / 3600;
                            recordTime = hour + "시간" + min + "분" + sec + "초";
                        }
                    }
                    // 1분이 넘지 않으면 recordTime에 초로 저장한다.
                    else {
                        recordTime = time + "초";
                    }
                    // 알림창을 띄워 기록저장 여부를 묻는다.
                    AlertDialog.Builder add = new AlertDialog.Builder(getContext());
                    add.setTitle("산책 시간을 기록하시겠습니까? \n산책시간은 " + recordTime + "입니다");
                    add.setPositiveButton("닫기", null);
                    add.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 기록을 저장하겠다고 하면, AddScheduleActivity로 넘어가 추가를 도와준다.
                            Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                            intent.putExtra("walk", recordTime);
                            getActivity().startActivity(intent);
                            walkListAdapter.notifyDataSetChanged();
                        }
                    }).show();
                }
                // 타이머가 꺼져있었다면 알림창을 띄운다.
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    AlertDialog dialog = builder.setMessage("시작 버튼을 먼저 눌러주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
            }
        });

        // 야간모드 버튼을 누르면 LightModeActivity로 이동한다.
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder add = new AlertDialog.Builder(getContext());
                add.setTitle("블루투스를 켜주세요.");
                add.setPositiveButton("닫기", null);
                add.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), LightModeActivity.class);
                        getActivity().startActivity(intent);
                    }
                }).show();

            }
        });

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

    // 사용자의 산책 기록을 가져온다.
    class BackgroundTaskSchedule extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 WalkListGet 을 지정한다.
                // 사용자 id와 개월을 같이 넘겨준다.
                target = "http://dms7147.cafe24.com/WalkListGet.php?userID=" + URLEncoder.encode(userID, "UTF-8")
                        + "&month=" + URLEncoder.encode(month, "UTF-8");
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
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String result) { //해당 결과를 처리
            try {
                // List를 비운다.
                walkList.clear();
                // JSONObject의 response값을 얻어온다.
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                // array의 길이와 비교할 변수인 count를 선언한다.
                int count = 0;
                // 읽어온 값을 담을 변수를 선언한다.
                String walkContent;
                String walkName;
                String walkDate;
                // 다 읽어올 때까지 while문을 반복한다.
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // object에서 전해준 값을, BackgroundTaskSchedule내의 변수에 저장한다.
                    walkContent = object.getString("walkContent");
                    walkName = object.getString("walkName");
                    walkDate = object.getString("walkDate");
                    Walk walk = new Walk(walkContent, walkName, walkDate);
                    // 산책기록 객체를 생성해 리스트에 추가한다.
                    walkList.add(walk);
                    // count값을 1만큼 늘려준다.
                    count++;
                }
                // 어댑터에 데이터의 변경을 알려준다.
                walkListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
