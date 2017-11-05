package com.example.user.dogslight;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


// 강아지 비만도를 사진와 글로 설명해주는 프래그먼트.
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FatFragment newInstance(String param1, String param2) {
        FatFragment fragment = new FatFragment();
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
    private ImageView fatImg;   // 강아지의 비만도를 보여주는 ImageView
    private TextView commentText; // 해당 단계의 강아지의 모습에 대해 설명해주는 textView
    private Button phase1;  // 비만도 1단계 버튼
    private Button phase2;  // 비만도 2단계 버튼
    private Button phase3;  // 비만도 3단계 버튼
    private Button phase4;  // 비만도 4단계 버튼
    private Button phase5;  // 비만도 5단계 버튼

    // Activity가 생성될 때, 아래와 같이 작동한다.
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fatImg = (ImageView) getView().findViewById(R.id.fatImg);
        commentText = (TextView) getView().findViewById(R.id.commentText);
        phase1 = (Button) getView().findViewById(R.id.phase1);
        phase2 = (Button) getView().findViewById(R.id.phase2);
        phase3 = (Button) getView().findViewById(R.id.phase3);
        phase4 = (Button) getView().findViewById(R.id.phase4);
        phase5 = (Button) getView().findViewById(R.id.phase5);

        // 1단계 버튼을 클릭하면 아래와 같은 일이 나타난다.
        phase1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ImageView의 사진이 fat_phase1으로 바뀌고, commmentText가 야윈 상태의 강아지의 모습에 대해 설명해준다.
                fatImg.setImageResource(R.mipmap.fat_phase1);
                commentText.setText("갈비뼈, 요추, 골반 뼈가 쉽게 보이며, 체지방이 만져지지 않습니다. 근육 손실이 심합니다. \n" +
                        "음식 섭취를 늘리고 산책을 많이 다녀주세요!");
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                phase1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                phase2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        // 2단계 버튼을 클릭하면 아래와 같은 일이 나타난다.
        phase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ImageView의 사진이 fat_phase2로 바뀌고, commmentText가 마른 상태의 강아지의 모습에 대해 설명해준다.
                fatImg.setImageResource(R.mipmap.fat_phase2);
                commentText.setText("갈비뼈가 쉽게 만져지고, 만져지는 지방이 적습니다. 요추의 끝이 보이며 골반 뼈 융기가 나타나고 허리와 복부가 홀쭉합니다. \n" +
                        "음식 섭취를 늘려주세요!");
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                phase1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                phase3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        // 3단계 버튼을 클릭하면 아래와 같은 일이 나타난다.
        phase3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ImageView의 사진이 fat_phase3로 바뀌고, commmentText가 이상적인 상태의 강아지의 모습에 대해 설명해준다.
                fatImg.setImageResource(R.mipmap.fat_phase3);
                commentText.setText("지방이 적당히 있어 갈비뼈가 만져지며, 허리를 쉽게 구분할 수 있습니다. \n" +
                        "뒤에서 허리가 보이며 옆에서 봤을 때 배가 들어가 있습니다. \n" +
                        "이상적인 체형입니다!");
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                phase1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                phase4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        // 4단계 버튼을 클릭하면 아래와 같은 일이 나타난다.
        phase4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ImageView의 사진이 fat_phase4로 바뀌고, commmentText가 통통한 상태의 강아지의 모습에 대해 설명해준다.
                fatImg.setImageResource(R.mipmap.fat_phase4);
                commentText.setText("지방이 덥혀 갈비뼈를 만지기 힘들고, 요추와 꼬리 부분에 지방의 축적이 보입니다. \n" +
                        "허리를 구분하기 힘들지만, 배는 여전히 들어가 있습니다. \n" +
                        "산책 시간을 조금 더 늘려주세요!");
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                phase1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                phase5.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        // 5단계 버튼을 클릭하면 아래와 같은 일이 나타난다.
        phase5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ImageView의 사진이 fat_phase5로 바뀌고, commmentText가 비만인 상태의 강아지의 모습에 대해 설명해준다.
                fatImg.setImageResource(R.mipmap.fat_phase5);
                commentText.setText("많은 양의 지방이 몸, 척추, 꼬리에 축적되어 살이 접히며 허리와 배가 구분이 안 됩니다.\n" +
                        "다리에도 지방이 축적되며 복부 팽창이 있습니다. \n" +
                        "급식 양을 줄이고 산책을 늘려주세요!");
                // 선택된 버튼의 색만 다른 색으로 해서 현재 클릭한 버튼을 알 수 있게 한다.
                phase1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                phase5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fat, container, false);
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
}
