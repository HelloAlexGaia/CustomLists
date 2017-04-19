package com.example.android.customlist;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_DESCRIPTION;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_DEVELOP;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_LEVEL;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_NAME;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_TIME;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_UUID;
import static com.example.android.customlist.CustomContract.CustomEntry.TABLENAME;

/**
 * Created by 张俊秋 on 2017/4/17.
 */

public class EditorFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EditorFragment";
    private boolean mDevelopedOrNOt;
    private boolean mCanNotEditState =true;
    private MenuItem mMenuEditor;
    private MenuItem mMenuDone;
    private EditText mEtName;
    private EditText mEtDescription;
    private Spinner mSpTime;
    private RatingBar mRbLevel;
    private TextView mTxDeveloped;
    private boolean mUpdateState;
    private String mUUID;
    private BtnState mBtnState;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.editor_fragment_view,container,false);
        mEtName= (EditText) view.findViewById(R.id.et_name);
        mEtDescription= (EditText) view.findViewById(R.id.et_description);
        mSpTime= (Spinner) view.findViewById(R.id.sp_time);
        mRbLevel= (RatingBar) view.findViewById(R.id.rb_level);
        mTxDeveloped= (TextView) view.findViewById(R.id.tx_developed);
        mTxDeveloped.setOnClickListener(this);


        Bundle data =getArguments();
        if (data!=null){
            mUpdateState=true;
            mEtName.setText(data.getString(COLUMN_NAME));
            Log.d(TAG, "onCreateView: "+data.getString(COLUMN_NAME));
            mEtDescription.setText(data.getString(COLUMN_DESCRIPTION));
            mSpTime.setSelection(data.getInt(COLUMN_TIME));
            mRbLevel.setRating(data.getInt(COLUMN_LEVEL));
            mUUID=data.getString(COLUMN_UUID);
            if (data.getInt(COLUMN_DEVELOP)!=1){
                mDevelopedOrNOt=false;
            }else {
                mDevelopedOrNOt=true;
            }
            setDevelopTextView(mDevelopedOrNOt);
        }else {
            mUpdateState=false;
            mCanNotEditState =false;
        }
        mBtnState.setBtnState(false);
        return view;
    }
    private void setDevelopTextView(boolean developedOrNOt){
        if (developedOrNOt){
            mTxDeveloped.setTextColor(getResources().getColor(R.color.accent));
            mTxDeveloped.setText(R.string.developed);
            Drawable drawable=getResources().getDrawable(R.drawable.hook);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            mTxDeveloped.setCompoundDrawables(drawable,null,null,null);
        }else {
            mTxDeveloped.setText(R.string.string_developing);
            mTxDeveloped.setTextColor(getResources().getColor(R.color.divider));
            mTxDeveloped.setCompoundDrawables(null,null,null,null);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        setEditable();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_editor,menu);
        mMenuEditor=menu.findItem(R.id.menu_editor);
        mMenuDone=menu.findItem(R.id.menu_done);
        setMenuItem();
    }
    private void setMenuItem(){
        if (mCanNotEditState){
            mMenuDone.setVisible(false);
        }else {
            mMenuEditor.setVisible(false);
        }
    }
    private void setEditable(){
        if (mCanNotEditState){
            mEtName.setEnabled(!mCanNotEditState);
            mEtDescription.setEnabled(!mCanNotEditState);
            mSpTime.setEnabled(!mCanNotEditState);
            mRbLevel.setEnabled(!mCanNotEditState);
            mTxDeveloped.setEnabled(!mCanNotEditState);
        }else {
            mEtName.setEnabled(!mCanNotEditState);
            mEtDescription.setEnabled(!mCanNotEditState);
            mSpTime.setEnabled(!mCanNotEditState);
            mRbLevel.setEnabled(!mCanNotEditState);
            mTxDeveloped.setEnabled(!mCanNotEditState);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_editor:
                mCanNotEditState=false;
                setEditable();
                getActivity().invalidateOptionsMenu();
                break;
            case R.id.menu_done:
                ContentValues cv=getValues();
                if (cv!=null){
                    CustomDatabase c=new CustomDatabase(getContext());
                    SQLiteDatabase s=c.getReadableDatabase();
                    if (mUpdateState){
                        s.update(TABLENAME,getValues(),"UUID=?",new String[]{mUUID});
                    }else {
                        s.insert(TABLENAME,null,getValues());
                        s.close();
                        c.close();
                        getActivity().onBackPressed();
                    }
                    s.close();
                    c.close();
                    mCanNotEditState=true;
                    setEditable();
                    getActivity().invalidateOptionsMenu();
                }else {
                    Toast.makeText(getContext(),"can't updata data, please check name.",Toast.LENGTH_SHORT).show();
                }

        }
        return super.onOptionsItemSelected(item);
    }
    private ContentValues getValues(){
        String nameChange=mEtName.getText().toString();
        String decriptionChange=mEtDescription.getText().toString();
        int levelChange= (int) mRbLevel.getRating();
        String monthNum=mSpTime.getSelectedItem().toString();
        int timechange=0;
        if (monthNum.equals(getString(R.string.month_1))){
           int timeChange= CustomContract.CustomEntry.MONTH_1;
        }
        if (monthNum.equals(getString(R.string.month_2))){
            timechange= CustomContract.CustomEntry.MONTH_2;
        }
        if (monthNum.equals(getString(R.string.month_3))){
            timechange= CustomContract.CustomEntry.MONTH_3;
        }
        if (monthNum.equals(getString(R.string.month_4))){
            timechange= CustomContract.CustomEntry.MONTH_4;
        }
        if (monthNum.equals(getString(R.string.month_5))){
            timechange= CustomContract.CustomEntry.MONTH_5;
        }
        if(nameChange.equals("")){
            return null;
        }
        return CustomDatabase.getContentValue(nameChange,decriptionChange,levelChange,timechange,mDevelopedOrNOt);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tx_developed:
                mDevelopedOrNOt=!mDevelopedOrNOt;
                setDevelopTextView(mDevelopedOrNOt);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mBtnState= (BtnState) activity;
        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: "+"not implement");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBtnState.setBtnState(true);
    }

    public interface BtnState{
        public void setBtnState(boolean state);
    }
}
