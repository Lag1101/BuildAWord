package com.android.luckybug.buildaword;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PostGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostGameFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STATUS = "param1";
    private static final String ARG_WINNER = "param2";
    private static final String ARG_COMMENT = "param3";

    // TODO: Rename and change types of parameters
    private String status = "Status";
    private String winner = "Winner";
    private String comment = "Comment";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param status status text.
     * @param winner winner text.
     * @param comment comment.
     * @return A new instance of fragment PostGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostGameFragment newInstance(String status, String winner, String comment) {
        PostGameFragment fragment = new PostGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        args.putString(ARG_WINNER, winner);
        args.putString(ARG_COMMENT, comment);
        fragment.setArguments(args);
        return fragment;
    }

    public PostGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_STATUS);
            winner = getArguments().getString(ARG_WINNER);
            comment = getArguments().getString(ARG_COMMENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_game, container, false);

        ((TextView)view.findViewById(R.id.status)).setText(status);
        ((TextView)view.findViewById(R.id.winner)).setText(winner);
        ((TextView)view.findViewById(R.id.comment)).setText(comment);

        return view;
    }
}
