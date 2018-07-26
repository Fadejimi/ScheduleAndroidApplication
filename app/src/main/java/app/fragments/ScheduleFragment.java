package app.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scheduler.databinding.FragmentScheduleBinding;

import app.recview.ScheduleAdapter;
import com.scheduler.R;

import java.util.Observable;
import java.util.Observer;

import app.viewmodels.ScheduleViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment implements Observer {
    private ScheduleAdapter listAdapter;

    private final Context context = this.getActivity();
    private FragmentScheduleBinding scheduleBinding;
    private ScheduleViewModel scheduleViewModel;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        scheduleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule,
                container, false);
        View view = scheduleBinding.getRoot();
        initBinding();
        setUpListOfSchedulesView(scheduleBinding.scheduleView);
        setUpObserver(scheduleViewModel);
        return view;
    }

    private void initBinding() {
        scheduleViewModel = new ScheduleViewModel(context);
        scheduleBinding.setScheduleViewModel(scheduleViewModel);
    }

    private void setUpListOfSchedulesView(RecyclerView recView) {
        ScheduleAdapter adapter = new ScheduleAdapter();
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setUpObserver(Observable observable) {
        observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ScheduleViewModel) {
            ScheduleAdapter adapter = (ScheduleAdapter) scheduleBinding.scheduleView.getAdapter();
            ScheduleViewModel viewModel = (ScheduleViewModel) o;
            adapter.setScheduleList(viewModel.getScheduleList());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduleViewModel.reset();
    }
}
