package com.example.user.dogslight;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalkNoticeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WalkNoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalkNoticeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WalkNoticeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalkNoticeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalkNoticeFragment newInstance(String param1, String param2) {
        WalkNoticeFragment fragment = new WalkNoticeFragment();
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
    private ListView walkNoticeListView; // 나의 산책 시 유의사항을 관리하는 리스트뷰
    private WalkNoticeListAdapter walkNoticeListAdapter; // 리스트뷰에 연결된 어댑터
    private List<WalkNotice> walkNoticeList; // 유의사항 항목을 담는 리스트

    // Activity가 생성될 때, 아래와 같이 작동한다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 산책 시 유의사항 listView를 불러온다
        walkNoticeListView = (ListView) getView().findViewById(R.id.walkNoticeListView);
        // listView에 담을 내용인 walkNoticeList를 선언한다.
        walkNoticeList = new ArrayList<WalkNotice>();
        // walkNoticeListAdapter 객체를 만들어 walkNoticeList를 담는다.
        walkNoticeListAdapter = new WalkNoticeListAdapter(getActivity(), walkNoticeList);
        // walkNoticeListView에 walkNoticeList를 담은 어댑터를 연결한다.
        walkNoticeListView.setAdapter(walkNoticeListAdapter);

        new BackgroundTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walk_notice, container, false);
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

    // 산책 시 유의사항을 불러온다.
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            // 산책 시 유의사항을 불러오는 WalkNoticeGet을 지정한다.
            target = "http://dms7147.cafe24.com/WalkNoticeGet.php";
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
                // JSONObject의 response값을 얻어온다.
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                // array의 길이와 비교할 변수인 count를 선언한다.
                int count = 0;
                // 읽어온 값을 담을 변수를 선언한다.
                String noticeContent;
                String noticeName;
                String noticeDate;

                // 다 읽어올 때까지 while문을 반복한다.
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // object에서 전해준 값을, BackgroundTask내의 변수에 저장한다.
                    noticeContent = object.getString("noticeContent");
                    noticeName = object.getString("noticeName");
                    noticeDate = object.getString("noticeDate");
                    WalkNotice walkNotice = new WalkNotice(noticeContent, noticeName, noticeDate);
                    // List에 walkNotice 객체를 추가한다.
                    walkNoticeList.add(walkNotice);
                    // count 값을 1증가한다.
                    count++;
                }
                // 어댑터에 데이터의 변경을 알려준다.
                walkNoticeListAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
