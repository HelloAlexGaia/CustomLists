package com.example.android.customlist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ListActivity extends AppCompatActivity implements ListCustomFragment.TurnToDetail, View.OnClickListener,EditorFragment.BtnState {
    private FragmentManager mFragmentManager;
    private FloatingActionButton mAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mAdd= (FloatingActionButton) findViewById(R.id.btn_add);
        mAdd.setOnClickListener(this);
        mFragmentManager=getSupportFragmentManager();
        Fragment fragment=mFragmentManager.findFragmentById(R.id.container_view);
        if (fragment==null){
            fragment=new ListCustomFragment();
            mFragmentManager.beginTransaction().add(R.id.container_view,fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

    }
    private void setFloatButtonState(boolean state){
        if (state){
            mAdd.setVisibility(View.VISIBLE);
        }else {
            mAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void turnToEditor(Bundle bundle) {
        Fragment fragment=new EditorFragment();
        fragment.setArguments(bundle);
        mFragmentManager.beginTransaction().replace(R.id.container_view,fragment).addToBackStack(null).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                mFragmentManager.beginTransaction().replace(R.id.container_view,new EditorFragment()).addToBackStack(null).
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
    }

    @Override
    public void setBtnState(boolean state) {
        setFloatButtonState(state);
    }
}
