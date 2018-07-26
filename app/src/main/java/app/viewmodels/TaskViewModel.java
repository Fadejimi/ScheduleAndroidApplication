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
import app.dialogs.TaskDialog;
import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.TaskService;
import app.rest.model.Schedule;
import app.rest.model.Task;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TaskViewModel extends Observable {

    public ObservableInt progressBar;
    public ObservableInt taskRecycler;
    public ObservableField<String> messageLabel;
    public ObservableInt taskLabel;
    public ObservableInt imageView;
    public String token = UserInfo.getInstance().getToken();

    private Schedule schedule;
    private List<Task> taskList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TaskViewModel(@NonNull Context context, Schedule schedule) {
        this.context = context;
        taskList = new ArrayList<>();
        progressBar = new ObservableInt(View.GONE);
        taskRecycler = new ObservableInt(View.GONE);
        messageLabel = new ObservableField<>("No tasks");
        taskLabel = new ObservableInt(View.GONE);
        imageView = new ObservableInt(View.GONE);
        this.schedule = schedule;
        initBinders();
        fetchTasks();
    }

    public void onClickNewTask(View view) {
        DialogInterface dialogInterface = new TaskDialog(context, schedule);
        dialogInterface.showDialog();
        fetchTasks();
    }

    private void initBinders() {
        progressBar.set(View.VISIBLE);
        taskRecycler.set(View.GONE);
        taskLabel.set(View.GONE);
        imageView.set(View.GONE);
    }

    private void fetchTasks() {
        TaskService service = APIClient.createService(TaskService.class, token);

        Disposable disposable = service.getTasks(schedule.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Task>>() {
                    @Override
                    public void accept(List<Task> tasks) throws Exception {
                        updateTaskDataList(tasks);
                        progressBar.set(View.GONE);
                        taskRecycler.set(View.VISIBLE);
                        taskLabel.set(View.GONE);
                        imageView.set(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        messageLabel.set("There seems to be some errors");
                        imageView.set(View.VISIBLE);
                        progressBar.set(View.GONE);
                        taskRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updateTaskDataList(List<Task> tasks) {
        this.taskList = tasks;
        setChanged();
        notifyObservers();
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
