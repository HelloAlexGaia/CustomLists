package com.example.android.customlist;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_DESCRIPTION;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_DEVELOP;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_LEVEL;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_NAME;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_TIME;
import static com.example.android.customlist.CustomContract.CustomEntry.COLUMN_UUID;
import static com.example.android.customlist.CustomContract.CustomEntry.TABLENAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListCustomFragment extends Fragment {
    private static final String TAG = "ListCustomFragment";
    private RecyclerView mRecyclerView;
    private SQLiteDatabase mCustomDb;
    private Cursor mCustomCursor;
    private int mCustomindexName;
    private int mCustomindexRating;
    private int mCustomindexDevelop;
    private int mCustomIndexTime;
    private int mCustomIndexDes;
    private int mCustomIndexUUID;
    private TurnToDetail mTurnToDetail;

    public ListCustomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_list, container, false);
        mCustomDb=new CustomDatabase(getContext()).getReadableDatabase();
        mCustomCursor=mCustomDb.query(TABLENAME,null,null,null,null,null,null);
        mCustomindexName=mCustomCursor.getColumnIndex(COLUMN_NAME);
        mCustomindexRating=mCustomCursor.getColumnIndex(COLUMN_LEVEL);
        mCustomindexDevelop=mCustomCursor.getColumnIndex(COLUMN_DEVELOP);
        mCustomIndexTime=mCustomCursor.getColumnIndex(COLUMN_TIME);
        mCustomIndexDes=mCustomCursor.getColumnIndex(COLUMN_DESCRIPTION);
        mCustomIndexUUID=mCustomCursor.getColumnIndex(COLUMN_UUID);

        mRecyclerView= (RecyclerView) view.findViewById(R.id.rv_custom_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new MyAdapter());
        return view;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTxItemTitle;
        private RatingBar mRbLevel;
        private ImageView mIvDevelop;
        private Bundle mData;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTxItemTitle= (TextView) itemView.findViewById(R.id.tx_item_title);
            mRbLevel= (RatingBar) itemView.findViewById(R.id.rb_item_level);
            mIvDevelop= (ImageView) itemView.findViewById(R.id.iv_item_develop);
            mData=new Bundle();
            itemView.setOnClickListener(this);
        }
        public void bindData(int position){
            mCustomCursor.moveToPosition(position);
            String name=mCustomCursor.getString(mCustomindexName);
            mData.putString(COLUMN_NAME,name);
            int level=mCustomCursor.getInt(mCustomindexRating);
            mData.putInt(COLUMN_LEVEL,level);
            int develop=mCustomCursor.getInt(mCustomindexDevelop);
            mData.putInt(COLUMN_DEVELOP,develop);
            mData.putInt(COLUMN_TIME,mCustomCursor.getInt(mCustomIndexTime));
            mData.putString(COLUMN_DESCRIPTION,mCustomCursor.getString(mCustomIndexDes));
            mData.putString(COLUMN_UUID,mCustomCursor.getString(mCustomIndexUUID));

            mTxItemTitle.setText(name);
            mRbLevel.setRating(level);
            if (develop==1){
                mIvDevelop.setVisibility(View.VISIBLE);
            }else {
                mIvDevelop.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            mTurnToDetail.turnToEditor(mData);
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.item_view,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return mCustomCursor.getCount();
        }
    }

    public interface TurnToDetail{
        public void turnToEditor(Bundle data);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mTurnToDetail= (TurnToDetail) activity;
        }catch (ClassCastException c){
            Log.d(TAG, "onAttach: not implement");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mCustomCursor.close();
        mCustomDb.close();
    }
}
