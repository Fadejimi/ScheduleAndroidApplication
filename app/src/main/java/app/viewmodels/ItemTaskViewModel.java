package app.viewmodels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;

import com.scheduler.R;

import app.activities.MainActivity;
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


public class ItemTaskViewModel extends BaseObservable {
    private Task task;
    private Context context;
    private String token = UserInfo.getInstance().getToken();
    private ObservableInt progressBar;
    private CompositeDisposable compositeDisposable;
    private Schedule schedule;

    public ItemTaskViewModel(Schedule schedule, Task task, Context context) {
        this.task = task;
        this.context = context;
        this.schedule = schedule;
        progressBar = new ObservableInt(View.GONE);
        compositeDisposable = new CompositeDisposable();
    }

    public String getName() {
        return task.getName();
    }

    public float getRating() {
        return (float) task.getPercentage() / 100 * 5;
    }

    public void onClickView(View v) {
        MainActivity.showTaskStatistics(task, schedule);
    }

    public void onClickDelete(View v) {
        showDelete(v);
    }

    private void showDelete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure you want to delete the schedule")
                .setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /** TODO - ADD THE PROGRESS BAR BUTTON **/
                        dialogInterface.dismiss();
                        progressBar.set(View.VISIBLE);
                        deleteTask();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();


    }

    private void deleteTask() {
        TaskService service = APIClient.createService(TaskService.class, token);

        Disposable disposable = service.deleteTask(schedule.getId(), task.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Task>() {
                    @Override
                    public void accept(Task task) throws Exception {
                        progressBar.set(View.GONE);
                        Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                        updateTask();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressBar.set(View.GONE);
                        Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void updateTask() {
        notifyChange();
    }

    public void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }

    public Task getTask() {
        return this.task;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
