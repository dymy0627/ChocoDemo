package com.yulin.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.yulin.myapplication.database.ChocoDatabase;
import com.yulin.myapplication.web.ChocoService;
import com.yulin.myapplication.web.DramaResponse;
import com.yulin.myapplication.web.RetrofitServiceManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerViewAdapter mViewAdapter;
    private List<DramaBean> mDramaBeanList = new ArrayList<>();
    private List<String> mDramaNameList = new ArrayList<>();

    private AutoCompleteTextView mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences("Choco", MODE_PRIVATE);

        RecyclerView recyclerView = findViewById(R.id.drama_listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.requestFocus();

        mViewAdapter = new RecyclerViewAdapter(this, mDramaBeanList);
        mViewAdapter.setHasStableIds(true);
        mViewAdapter.setItemClickListener(view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra("Drama", mViewAdapter.getItem(viewHolder.getAdapterPosition()));
            startActivity(intent);
        });
        recyclerView.setAdapter(mViewAdapter);

        mSearchEditText = findViewById(R.id.search_editText);
        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged:" + s);
                mViewAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mSearchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                new Handler(getMainLooper()).postDelayed(recyclerView::requestFocus, 100);
            }
            return false;
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDramaNameList);
        mSearchEditText.setAdapter(adapter);
        mSearchEditText.setThreshold(1);

        final SwipeRefreshLayout refreshView = findViewById(R.id.refreshView);
        refreshView.setOnRefreshListener(() -> {
            Log.d(TAG, "onRefresh");
            getDramaListFromWeb()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<DramaBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onRefresh: onSubscribe:");
                        }

                        @Override
                        public void onSuccess(List<DramaBean> dramaBeans) {
                            Log.d(TAG, "onRefresh: onSuccess:");
                            updateDramaList(dramaBeans);
                            updateAutoCompleteData(dramaBeans);
                            Toast.makeText(MainActivity.this, "列表更新完成", Toast.LENGTH_SHORT).show();
                            refreshView.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onRefresh: onError:");
                            e.printStackTrace();
                            if (e instanceof UnknownHostException) {
                                Toast.makeText(MainActivity.this, "請確認網路連線", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "列表更新異常", Toast.LENGTH_SHORT).show();
                            }
                            refreshView.setRefreshing(false);
                        }
                    });
        });
    }

    private SharedPreferences mSharedPreferences;
    private static final String LAST_SEARCH = "last_search";

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        getDramaList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<DramaBean>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getDramaList: onSubscribe:");
                    }

                    @Override
                    public void onSuccess(List<DramaBean> dramaBeans) {
                        Log.d(TAG, "getDramaList: onSuccess:");
                        if (!dramaBeans.isEmpty()) {
                            updateDramaList(dramaBeans);
                            updateAutoCompleteData(dramaBeans);

                            // restore last search state.
                            String lastSearchText = mSharedPreferences.getString(LAST_SEARCH, null);
                            if (lastSearchText != null && !lastSearchText.isEmpty()) {
                                mSearchEditText.setText(lastSearchText);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "getDramaList: onError:");
                        if (t instanceof UnknownHostException) {
                            Toast.makeText(MainActivity.this, "請確認網路連線", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "獲取列表異常", Toast.LENGTH_SHORT).show();
                        }
                        t.printStackTrace();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        mSharedPreferences.edit().putString(LAST_SEARCH, mSearchEditText.getText().toString()).apply();
    }

    private void updateDramaList(List<DramaBean> dramaBeans) {
        mDramaBeanList.clear();
        mDramaBeanList.addAll(dramaBeans);
        mViewAdapter.notifyDataSetChanged();
    }

    // update data for AutoCompleteTextView.
    private void updateAutoCompleteData(List<DramaBean> dramaBeans) {
        mDramaNameList.clear();
        for (DramaBean dramaBean : dramaBeans) {
            mDramaNameList.add(dramaBean.getName());
        }
    }

    public Single<List<DramaBean>> getDramaList() {
        return getDramaListFromDb()
                .flatMap(dramaList -> {
                    if (dramaList == null || dramaList.size() == 0) {
                        return getDramaListFromWeb();
                    } else {
                        return Single.just(dramaList);
                    }
                });
    }

    public Single<List<DramaBean>> getDramaListFromWeb() {
        Log.d(TAG, "getDramaListFromWeb");
        return RetrofitServiceManager.getInstance().create(ChocoService.class)
                .getDramas()
                .map(DramaResponse::getData)
                .doOnSuccess(this::saveDramaList);
    }

    public Single<List<DramaBean>> getDramaListFromDb() {
        Log.d(TAG, "getDramaListFromDb");
        return ChocoDatabase.getInstance(this).getDramaDao().getDramaList();
    }

    public void saveDramaList(List<DramaBean> dramaBeans) {
        Completable.fromAction(() -> {
            Log.d(TAG, "saveDramaList");
            ChocoDatabase.getInstance(getApplicationContext()).getDramaDao().deleteAllDrama();
            ChocoDatabase.getInstance(getApplicationContext()).getDramaDao().insertDramaList(dramaBeans);
            Thread.sleep(5000); // simulate long save.
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "saveDramaList: onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "saveDramaList: onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "saveDramaList: onError");
                    }
                });
    }

}
