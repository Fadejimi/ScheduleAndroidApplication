package app.viewmodels;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import app.model.UserInfo;
import app.rest.APIClient;
import app.rest.TaskMilestoneService;
import app.rest.model.Task;
import app.rest.model.TasksMilestone;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TaskStatisticsViewModel extends Observable {
    public ObservableInt progressBar;
    public ObservableInt taskLabel;
    public ObservableInt imageView;
    public ObservableInt recyclerView;
    public ObservableField<String> messageLabel;

    private Task task;
    private Context context;
    private List<TasksMilestone> tasksMilestones;

    private String token = UserInfo.getInstance().getToken();
    private CompositeDisposable compositeDisposable;

    public TaskStatisticsViewModel(Context context, Task task) {
        this.context = context;
        this.task = task;
        tasksMilestones = new ArrayList<>();
        initBinders();
        fetchTaskMilestones();
    }

    private void initBinders() {
        compositeDisposable = new CompositeDisposable();
        progressBar = new ObservableInt(View.VISIBLE);
        taskLabel = new ObservableInt(View.GONE);
        imageView = new ObservableInt(View.GONE);
        recyclerView = new ObservableInt(View.GONE);
        messageLabel = new ObservableField<>("No data available");
    }

    private void fetchTaskMilestones() {
        TaskMilestoneService service = APIClient.createService(TaskMilestoneService.class, token);

        Disposable disposable = service.getTaskMilestones(task.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TasksMilestone>>() {
                    @Override
                    public void accept(List<TasksMilestone> tasksMilestones) throws Exception {
                        updateTaskMilestones(tasksMilestones);
                        progressBar.set(View.GONE);
                        taskLabel.set(View.GONE);
                        imageView.set(View.GONE);
                        recyclerView.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressBar.set(View.GONE);
                        taskLabel.set(View.VISIBLE);
                        imageView.set(View.VISIBLE);
                        recyclerView.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void updateTaskMilestones(List<TasksMilestone> tasksMilestones) {
        this.tasksMilestones = tasksMilestones;
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

    public List<TasksMilestone> getTasksMilestones() {
        return tasksMilestones;
    }
}
