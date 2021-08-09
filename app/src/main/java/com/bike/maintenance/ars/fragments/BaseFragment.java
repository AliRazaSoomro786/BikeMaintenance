package com.konnnect.walletdroid.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.konnnect.walletdroid.R;
import com.konnnect.walletdroid.helper.Logging;


public abstract class BaseFragment extends Fragment {
    private final Logging slogging = new Logging(BaseFragment.class);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        slogging.info("BaseFragment onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        slogging.info(getClassTAg() + " onAttach");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slogging.info(getClassTAg() + " onCreate");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slogging.info(getClassTAg() + " onViewCreated");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        slogging.info(getClassTAg() + " onViewStateRestored");
    }

    @Override
    public void onStart() {
        super.onStart();
        slogging.info(getClassTAg() + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        slogging.info(getClassTAg() + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        slogging.info(getClassTAg() + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        slogging.info(getClassTAg() + " onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        slogging.info(getClassTAg() + " onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        slogging.info(getClassTAg() + " onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        slogging.info(getClassTAg() + " onDetach");
    }


    public final void launchPackage(@NonNull String pkg) {
        if (getActivity() == null) return;

        Intent intent = getIntent(pkg);
        if (intent == null) {
            slogging.error("Null intent for pkg : " + pkg);
        } else {
            startActivity(intent);
            slogging.info("Package lunched : " + pkg);
        }

    }

    public final Intent getIntent(@NonNull String pkg) {
        if (getActivity() == null) return null;
        return getActivity().getPackageManager().getLaunchIntentForPackage(pkg);
    }

    public final void open(@NonNull BaseFragment fragment) {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            fm.beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(this.getClass().getSimpleName()).commit();
        } else {
            slogging.error("Null fragmentManager for fragment : " + fragment.getClass().getSimpleName());
        }

    }

    public final void remove(@NonNull BaseFragment fragment) {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
            fm.popBackStack();
        } else {
            slogging.error("Null fragmentManager for fragment : " + fragment.getClass().getSimpleName());
        }

    }

    public final void startActionActivity(@NonNull String action) {
        startActivity(new Intent(action));
        slogging.info("System app activity started for action : " + action);
    }

    private String getClassTAg() {
        return getClass().getSimpleName();
    }


}
