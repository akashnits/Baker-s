package com.example.android.bakers.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeStepDetailsFragment extends Fragment {
    @Nullable
    @BindView(R.id.mediaPlayerView)
    SimpleExoPlayerView mediaPlayerView;
    @Nullable
    @BindView(R.id.tvRecipeStepDescription)
    TextView tvRecipeStepDescription;
    @Nullable
    @BindView(R.id.btPrevious)
    Button btPrevious;
    @Nullable
    @BindView(R.id.btNext)
    Button btNext;
    Unbinder unbinder;

    private SimpleExoPlayer mExoPlayer;
    private int mStepId;
    private ArrayList<Step> mStepArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_step_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExoPlayer != null)
                    mExoPlayer.stop();
                if(mStepId != 0)
                {
                    mStepId--;

                    ((MainActivity) getActivity()).showRecipeStepDetailsFragment(getBundle());
                }

            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExoPlayer != null)
                    mExoPlayer.stop();
                if(mStepArrayList != null && mStepId != mStepArrayList.size()-1){
                    mStepId++;

                    ((MainActivity) getActivity()).showRecipeStepDetailsFragment(getBundle());
                }
            }
        });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null) {
            Bundle args = getArguments();
            mStepId= args.getInt("stepPosition");
            Step step= null;
            try {
                mStepArrayList= args.getParcelableArrayList("stepArrayList");
                if(mStepArrayList != null)
                    step = mStepArrayList.get(mStepId);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }


            if (step != null) {
                if (step.getVideoURL().length() > 0) {
                    initializePlayer(Uri.parse(step.getVideoURL()));
                } else if (step.getVideoURL().length() > 0) {
                    initializePlayer(Uri.parse(step.getThumbnailURL()));
                } else {
                    initializePlayer(null);
                }

                String description = step.getDescription();
                if (description.length() > 0)
                    tvRecipeStepDescription.setText(description);
            }
        }

        if(savedInstanceState != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setInitializePlayer(savedInstanceState);

        }
        if(savedInstanceState != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Step step = setInitializePlayer(savedInstanceState);


            if(step != null) {
                String description = step.getDescription();
                if (description != null && description.length() > 0)
                    tvRecipeStepDescription.setText(description);

                mStepId = step.getId();
            }
        }
    }

    private void initializePlayer(Uri mediaUri){
        if(mediaUri != null){
            if(mediaPlayerView != null){
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                mediaPlayerView.setPlayer(mExoPlayer);

                String userAgent = Util.getUserAgent(getContext(), "Baker's");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                                            getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource, 5);
                mExoPlayer.prepare(loopingSource);
                mExoPlayer.setPlayWhenReady(true);
            }
        }else{
            Toast.makeText(getContext(), "No Video available", Toast.LENGTH_SHORT).show();
        }
    }

    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
            }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("stepPosition", mStepId);
        outState.putParcelableArrayList("stepArrayList", mStepArrayList);
    }

    private Step setInitializePlayer(Bundle savedInstanceState){
        Step step= null;
        mStepArrayList= savedInstanceState.getParcelableArrayList("stepArrayList");
        mStepId= savedInstanceState.getInt("stepPosition");
        step= mStepArrayList.get(mStepId);
        if (step != null) {
            if (step.getVideoURL().length() > 0) {
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else if (step.getVideoURL().length() > 0) {
                initializePlayer(Uri.parse(step.getThumbnailURL()));
            } else {
                initializePlayer(null);
            }
            return step;
        }
        return null;
    }

    private Bundle getBundle(){
        Bundle bundle= new Bundle();
        bundle.putParcelableArrayList("stepArrayList", mStepArrayList);
        bundle.putInt("stepPosition", mStepId);
        return bundle;
    }
}