package com.example.user.dogslight;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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


// 강아지 나이대/ 몸무게 별로 하루에 필요한 열량과,
// 사료의 단백질, 지방, 섬유질 함량을 통해 계산한 하루에 사료 양을 알려주는 프래그먼트.

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
    private ArrayAdapter dogAdapter; // 강아지 이름을 엮어주는 어댑터
    private Spinner dogSpinner;     // 강아지 이름을 보여주는 스피너
    private ArrayAdapter dogFoodAdapter;    // 사료 이름을 엮어주는 어댑터
    private Spinner dogFoodSpinner;         // 사료 이름을 보여주는 스피너

    // xml에 선언된 위젯
    private TextView dogNameText;
    private TextView dogAgesText;
    private TextView dogWeightText;

    private TextView foodNameText;
    private TextView foodProteinText;
    private TextView foodFatText;
    private TextView foodFiberText;

    private TextView derText;
    private TextView feedAmountText;
    private TextView cupOfAmountText;

    private Button calcButton;

    // 강아지 체중유지/감량을 저장할 변수
    private String dogWeightControl;

    // DB에서 읽어온 값을 저장할 변수
    private String dogName = "";
    private String dogAges = "";
    private Double dogWeight = 0.0;

    // DB에서 읽어온 값을 저장할 변수
    // 조회분과 수분함량은 찾기 힘든 경우가 많아 적절한 값으로 지정해놓는다.
    private String foodName = "";
    private Double foodProtein = 0.0;
    private Double foodFat = 0.0;
    private Double foodFiber = 0.0;
    private Double foodCarbs = 0.0;
    private Double foodAsh = 8.0;
    private Double foodMoisture = 9.0;

    // Activity가 생성될 때, 아래와 같이 작동한다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // xml의 위젯을 찾아 변수에 저장한다.
        // radioGroup은 값을 얻어올 때에만 사용하므로, 내부에 선언한다.
        final RadioGroup dogWeightGroup = (RadioGroup) getView().findViewById(R.id.dogWeightGroup);
        dogSpinner = (Spinner) getView().findViewById(R.id.dogSpinner);
        dogFoodSpinner = (Spinner) getView().findViewById(R.id.dogFoodSpinner);

        dogNameText = (TextView) getView().findViewById(R.id.dogNameText);
        dogAgesText = (TextView) getView().findViewById(R.id.dogAgesText);
        dogWeightText = (TextView) getView().findViewById(R.id.dogWeightText);

        foodNameText = (TextView) getView().findViewById(R.id.foodNameText);
        foodProteinText = (TextView) getView().findViewById(R.id.foodProteinText);
        foodFatText = (TextView) getView().findViewById(R.id.foodFatText);
        foodFiberText = (TextView) getView().findViewById(R.id.foodFiberText);

        derText = (TextView) getView().findViewById(R.id.derText);
        feedAmountText = (TextView) getView().findViewById(R.id.feedAmountText);
        cupOfAmountText = (TextView) getView().findViewById(R.id.cupOfAmountText);

        calcButton = (Button) getView().findViewById(R.id.calcButton);

        // radioGroup의 기본 값은 체중유지로 설정한다.
        dogWeightGroup.check(R.id.maintenance);
        dogWeightControl = "체중유지";
        // radioGroup이 체크되면 아래와 같은 일을 한다.
        dogWeightGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                // 체크된 버튼을 얻어온다.
                RadioButton weightControlButton = (RadioButton) getView().findViewById(checkedId);
                // 버튼의 값, 체중유지/감량을 dogWeightControl에 저장한다.
                dogWeightControl = weightControlButton.getText().toString();
            }
        });
        // 각 스피너에 어댑터를 연결한다.
        dogSpinner.setAdapter(dogAdapter);
        dogFoodSpinner.setAdapter(dogFoodAdapter);

        // 강아지 이름을 읽어와서, 스피너에 보인다.
        new BackgroundTaskDog().execute();
        // 사료 이름을 읽어와서, 스피너에 보인다.
        new BackgroundTaskFood().execute();

        // 강아지 이름을 클릭하면 아래와 같은 일을 한다.
        dogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 스피너에서 얻어온 강아지 이름을 dogName에 저장한다.
                dogName = dogSpinner.getSelectedItem().toString();
                // 클릭한 강아지의 모든 정보를 불러서, textView에 보인다.
                new BackgroundTaskSDog().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 사료 이름을 클릭하면 아래와 같은 일을 한다.
        dogFoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 스피너에서 얻어온 사료 이름을 foodName에 저장한다.
                foodName = dogFoodSpinner.getSelectedItem().toString();
                // 클릭한 사료의 모든 정보를 불러서, textView에 보인다.
                new BackgroundTaskSFood().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 계산 버튼을 클릭하면 아래와 같은 일을 한다.
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 강아지와 사료를 모두 선택하지 않았다면, 아래와 같은 창이 뜨게 한다.
                if (dogName.equals("") || foodName.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog dialog = builder.setMessage("모두 선택해 주십시오")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // textView에 적힌 값들을 읽어온다.
                // 계산이 필요한 값은 double로 형변환한다.
                dogAges = dogAgesText.getText().toString();
                String dogWStr = dogWeightText.getText().toString();
                dogWeight = Double.parseDouble(dogWStr);
                String foodProteinStr = foodProteinText.getText().toString();
                foodProtein = Double.parseDouble(foodProteinStr);
                String foodFatStr = foodFatText.getText().toString();
                foodFat = Double.parseDouble(foodFatStr);
                String foodFiberStr = foodFiberText.getText().toString();
                foodFiber = Double.parseDouble(foodFiberStr);

                // 휴식기 에너지 요구량인 RER은 강아지의 체중의 0.75승에 70을 곱한 값과 같다.
                // RER은 DER을 구할 때에 이용된다.
                double rer = 70.0 * (Math.pow(dogWeight, 0.75));
                // DER은 일일 에너지 요구량을 뜻한다.
                double der = 0.0;
                // 사료의 칼로리의 의미한다.
                double kcal = 0.0;
                // 하루의 사료 양을 의미한다.
                double feedAmount = 0.0;
                // 하루 사료 양을 80g 종이컵에 담았을 때를 보여준다.
                double cupofAmount = 0.0;

                // 사료의 열량을 구하기 위해서는 탄수화물 비율이 필요한다.
                // 100%에서 각각의 함량을 모두 빼서 탄수화물의 함량을 구한다.
                foodCarbs = 100 - (foodProtein + foodFat + foodFiber + foodMoisture + foodAsh);
                // 단백질, 탄수화물, 지방은 각각 3.5, 3.5, 8.5kcal로 계산한다.
                kcal = (foodProtein * 3.5 + foodCarbs * 3.5 + foodFat * 8.5) / 100.0;

                // 체중유지가 목적일 때에 값을 계산한다.
                if (dogWeightControl.equals("체중유지")) {
                    // 자견의 경우 der을 아래와 같이 구한다.
                    if (dogAges.equals("자견")) {
                        // 성장기인 자견은 성견보다 많은 에너지가 요구되므로
                        // RER 값에 2.0만큼 곱한다.
                        der = 2.0 * rer;
                        // 하루 사료양(g)은 일일에너지 요구량에서 사료의 열량을 나눈 값과 같다.
                        feedAmount = der / kcal;
                        // 80g의 종이컵의 경우 아래와 같이 계산한다.
                        cupofAmount = feedAmount / 80.0;

                    } else {
                        // 성견은 RER 값에 1.85만큼 곱한다.
                        der = 1.85 * rer;
                        // 하루 사료양(g)은 일일에너지 요구량에서 사료의 열량을 나눈 값과 같다.
                        feedAmount = der / kcal;
                        // 80g의 종이컵의 경우 아래와 같이 계산한다.
                        cupofAmount = feedAmount / 80.0;
                    }
                }
                // 체중감량이 목적일 때에 값을 구한다.
                if (dogWeightControl.equals("체중감량")) {
                    // 자견의 경우 성장기이기 때문에 아래와 같이 알림을 띄운다.
                    if (dogAges.equals("자견")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        AlertDialog dialog = builder.setMessage("자견은 체중감량하기에는 이릅니다.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                    } else {
                        // 체중감량을 목적으로 하는 성견은 RER 값에 1.0만큼 곱한다.
                        der = 1.0 * rer;
                        // 하루 사료양(g)은 일일에너지 요구량에서 사료의 열량을 나눈 값과 같다.
                        feedAmount = der / kcal;
                        // 80g의 종이컵의 경우 아래와 같이 계산한다.
                        cupofAmount = feedAmount / 80.0;
                    }

                }

                // 계산한 값을 textView에 보여준다.
                derText.setText(Math.round(der) + "kCal");
                feedAmountText.setText(Math.round(feedAmount) + "g");
                cupOfAmountText.setText(Math.round(cupofAmount * 100) / 100.0 + "컵");

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
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


    // 사용자의 강아지 이름을 가져와, 스피너에 보여준다.
    class BackgroundTaskDog extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 MyDogList을 지정한다.
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
            List list = new ArrayList();
            try {
                // JSONObject의 response값을 얻어온다.
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                // array의 길이와 비교할 변수인 count를 선언한다.
                int count = 0;
                // 읽어온 값을 담을 변수를 선언한다.
                String myDogName;

                // 다 읽어올 때까지 while문을 반복한다.
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // object에서 전해준 값을, BackgroundTaskDog내의 변수에 저장한다.
                    myDogName = object.getString("myDogName");
                    list.add(myDogName);
                    // count값을 1씩 증가한다.
                    count++;
                }
                // 스피너에 읽어온 list를 담은 어댑터를 지정한다.
                dogSpinner = (Spinner) getView().findViewById(R.id.dogSpinner);
                dogAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
                dogAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dogSpinner.setAdapter(dogAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 사용자의 사료 이름을 가져와, 스피너에 보여준다.
    class BackgroundTaskFood extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 MyDogFoodList 을 지정한다.
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
            List list = new ArrayList();
            try {
                // JSONObject의 response값을 얻어온다.
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                // array의 길이와 비교할 변수인 count를 선언한다.
                int count = 0;
                // 읽어온 값을 담을 변수를 선언한다.
                String myFoodName;

                // 다 읽어올 때까지 while문을 반복한다.
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // object에서 전해준 값을, BackgroundTaskFood내의 변수에 저장한다.
                    myFoodName = object.getString("myFoodName");
                    list.add(myFoodName);
                    // count값을 1씩 증가한다.
                    count++;
                }
                // 스피너에 읽어온 list를 담은 어댑터를 지정한다.
                dogFoodSpinner = (Spinner) getView().findViewById(R.id.dogFoodSpinner);
                dogFoodSpinner = (Spinner) getView().findViewById(R.id.dogFoodSpinner);
                dogFoodAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
                dogFoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dogFoodSpinner.setAdapter(dogFoodAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 선택한 강아지 정보를 가져와 textView에 보여준다.
    class BackgroundTaskSDog extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 DogInfoGet 을 지정한다.
                // 사용자 id와 강아지 이름을 같이 넘겨준다.
                target = "http://dms7147.cafe24.com/DogInfoGet.php?userID=" + URLEncoder.encode(userID, "UTF-8")
                        + "&dogName=" + URLEncoder.encode(dogName, "UTF-8");
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
                    // object에서 전해준 값을, BackgroundTaskSDog내의 변수에 저장한다.
                    myDogName = object.getString("myDogName");
                    myDogAges = object.getString("myDogAges");
                    myDogWeight = object.getDouble("myDogWeight");

                    // textView를 읽어온 값으로 설정한다.
                    dogNameText.setText(myDogName);
                    if (myDogAges.equals("puppy")) {
                        dogAgesText.setText("자견");
                    } else {
                        dogAgesText.setText("성견");
                    }
                    dogWeightText.setText(myDogWeight.toString());

                    // count값을 1씩 증가한다.
                    count++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 선택한 사료 정보를 가져와 textView에 보여준다.
    class BackgroundTaskSFood extends AsyncTask<Void, Void, String> {

        String target;
        // MainActivity에 static으로 선언된 userID를 가져온다.
        String userID = MainActivity.userID;

        @Override
        protected void onPreExecute() {
            try {
                // target에 select 작업을 하는 DogFoodInfoGet 을 지정한다.
                // 사용자 id와 사료 이름을 같이 넘겨준다.
                target = "http://dms7147.cafe24.com/DogFoodInfoGet.php?userID=" + URLEncoder.encode(userID, "UTF-8")
                        + "&foodName=" + URLEncoder.encode(foodName, "UTF-8");
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
                    // object에서 전해준 값을, BackgroundTaskDog내의 변수에 저장한다.
                    myFoodName = object.getString("myFoodName");
                    myFoodProtein = object.getDouble("myFoodProtein");
                    myFoodFat = object.getDouble("myFoodFat");
                    myFoodFiber = object.getDouble("myFoodFiber");

                    // textView를 읽어온 값으로 설정한다.
                    foodNameText.setText(myFoodName);
                    foodProteinText.setText(myFoodProtein.toString());
                    foodFatText.setText(myFoodFat.toString());
                    foodFiberText.setText(myFoodFiber.toString());

                    // count값을 1씩 증가한다.
                    count++;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
