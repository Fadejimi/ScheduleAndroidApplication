package app.viewmodels;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import app.dialogs.DialogInterface;
import app.dialogs.ScheduleDialog;
import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.ScheduleService;
import app.rest.model.Schedule;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ScheduleViewModel extends Observable {

    public ObservableInt progressBar;
    public ObservableInt scheduleRecycler;
    public ObservableInt scheduleLabel;
    public ObservableInt imageView;
    public ObservableField<String> messageLabel;
    private String token = UserInfo.getInstance().getToken();

    private List<Schedule> scheduleList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ScheduleViewModel(@NonNull Context context) {
        this.context = context;
        this.scheduleList = new ArrayList<>();
        progressBar = new ObservableInt(View.GONE);
        scheduleRecycler = new ObservableInt(View.GONE);
        scheduleLabel = new ObservableInt(View.GONE);
        imageView = new ObservableInt(View.GONE);
        messageLabel = new ObservableField<>("Press the button to load schedules");
        initializeViews();
        fetchUsersList();
    }

    public void onClickNewSchedule(View view) {
        DialogInterface scheduleDialog = new ScheduleDialog(context);
        scheduleDialog.showDialog();
        fetchUsersList();
    }

    public void initializeViews() {
        scheduleLabel.set(View.GONE);
        scheduleRecycler.set(View.GONE);
        progressBar.set(View.VISIBLE);
    }

    private void fetchUsersList() {
        ScheduleService service = APIClient.createService(ScheduleService.class, token);

        Disposable disposable = service.getSchedules()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Schedule>>() {
                    @Override
                    public void accept(List<Schedule> schedules) throws Exception {
                        updateScheduleDataList(schedules);
                        progressBar.set(View.GONE);
                        scheduleLabel.set(View.GONE);
                        scheduleRecycler.set(View.VISIBLE);
                        imageView.set(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        messageLabel.set("There seems to have been some errors.");
                        progressBar.set(View.GONE);
                        scheduleLabel.set(View.VISIBLE);
                        imageView.set(View.VISIBLE);
                        scheduleRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updateScheduleDataList(List<Schedule> schedules) {
        scheduleList = schedules;
        setChanged();
        notifyObservers();
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }
}
