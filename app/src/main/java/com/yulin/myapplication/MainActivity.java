package com.yulin.myapplication;

import android.os.Bundle;
import android.util.Log;

import com.yulin.myapplication.database.ChocoDatabase;
import com.yulin.myapplication.web.ChocoService;
import com.yulin.myapplication.web.DramaResponse;
import com.yulin.myapplication.web.RetrofitServiceManager;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String JSON_URL = "https://static.linetv.tw/interview/dramas-sample.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Completable.fromAction(() -> ChocoDatabase.getInstance(this).getDramaDao().deleteAllDrama())
//                .subscribeOn(Schedulers.io())
//                .andThen(getDramaList());

        getDramaList().subscribe(new SingleObserver<List<DramaBean>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:");
            }

            @Override
            public void onSuccess(List<DramaBean> dramaBeans) {
                if (!dramaBeans.isEmpty()) {
                    for (DramaBean dramaBean : dramaBeans) {
                        Log.d(TAG, "onSuccess: getDrama_id=" + dramaBean.getDrama_id());
                        Log.d(TAG, "onSuccess: getName=" + dramaBean.getName());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError:");
                t.printStackTrace();
            }
        });
    }

    public Single<List<DramaBean>> getDramaList() {
        return getDramaListFromDb()
                .flatMap(dramaList -> {
                    if (dramaList == null || dramaList.size() == 0) {
                        return getDramaListFromWeb();
                    } else {
                        return Single.just(dramaList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<DramaBean>> getDramaListFromWeb() {
        Log.d(TAG, "getDramaListFromWeb");
        return RetrofitServiceManager.getInstance().create(ChocoService.class)
                .getDramas(JSON_URL)
                .map(DramaResponse::getData)
                .flatMap(this::saveDramaList);
    }

    public Single<List<DramaBean>> getDramaListFromDb() {
        Log.d(TAG, "getDramaListFromDb");
        return ChocoDatabase.getInstance(this).getDramaDao().getDramaList();
    }

    public Single<List<DramaBean>> saveDramaList(List<DramaBean> dramaBeans) {
        return Single.fromCallable(() -> {
            Log.d(TAG, "saveDramaList");
            ChocoDatabase.getInstance(this).getDramaDao().deleteAllDrama();
            ChocoDatabase.getInstance(this).getDramaDao().insertDramaList(dramaBeans);
            return dramaBeans;
        });
    }

}
