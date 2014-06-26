package com.example.ulikeitweather.app.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.adapter.ForecastArrayAdapter;
import com.example.ulikeitweather.app.client.APICallListener;
import com.example.ulikeitweather.app.client.APICallManager;
import com.example.ulikeitweather.app.client.APICallTask;
import com.example.ulikeitweather.app.client.ResponseStatus;
import com.example.ulikeitweather.app.client.request.WeatherRequest;
import com.example.ulikeitweather.app.client.response.Response;
import com.example.ulikeitweather.app.entity.MyLocation;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.task.TaskListFragment;
import com.example.ulikeitweather.app.utility.Logcat;
import com.example.ulikeitweather.app.utility.NetworkManager;
import com.example.ulikeitweather.app.view.ViewState;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Asus on 26.6.2014.
 */
public abstract class WeatherParentFragment extends TaskListFragment implements APICallListener {

    private static final String META_REFRESH = "refresh";

    private static final int LAZY_LOADING_TAKE = 16;
    private static final int LAZY_LOADING_OFFSET = 4;
    private static final int LAZY_LOADING_MAX = LAZY_LOADING_TAKE * 10;

    private boolean mLazyLoading = false;
    private boolean mActionBarProgress = false;
    private ViewState mViewState = null;
    protected View mRootView;
    private ForecastArrayAdapter mAdapter;
    private APICallManager mAPICallManager = new APICallManager();

    private ArrayList<Weather> mWeatherList = new ArrayList<Weather>();
    private MyLocation mLocation;


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // load and show data
        if(mViewState==null || mViewState==ViewState.OFFLINE)
        {
            loadData();
        }
        else if(mViewState==ViewState.CONTENT)
        {
            if(mWeatherList!=null) renderView();
            showList();
        }
        else if(mViewState==ViewState.PROGRESS)
        {
            showProgress();
        }

        // progress in action bar
        showActionBarProgress(mActionBarProgress);

        // lazy loading progress
        if(mLazyLoading) showLazyLoadingProgress(true);
    }


    @Override
    public void onPause()
    {
        super.onPause();

        // stop adapter
        if(mAdapter!=null) mAdapter.stop();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // cancel async tasks
        mAPICallManager.cancelAllTasks();
    }


    @Override
    public void onAPICallRespond(final APICallTask task, final ResponseStatus status, final Response<?> response)
    {
        runTaskCallback(new Runnable()
        {
            public void run()
            {
                if(mRootView==null) return; // view was destroyed

                if(task.getRequest().getClass().equals(WeatherRequest.class))
                {
                    Response<List<Weather>> weatherResponse = (Response<List<Weather>>) response;

                    // error
                    if(weatherResponse.isError())
                    {
                        Logcat.d("Fragment.onAPICallRespond(ExampleRequest): " + status.getStatusCode() + " " + status.getStatusMessage() +
                                " / error / " + weatherResponse.getErrorType() + " / " + weatherResponse.getErrorMessage());

                        // hide progress
                        showLazyLoadingProgress(false);
                        showList();

                        // handle error
                        handleError(weatherResponse.getErrorType(), weatherResponse.getErrorMessage());
                    }

                    // response
                    else
                    {
                        Logcat.d("Fragment.onAPICallRespond(ExampleRequest): " + status.getStatusCode() + " " + status.getStatusMessage());

                        // check meta data
                        if(task.getRequest().getMetaData()!=null && task.getRequest().getMetaData().getBoolean(META_REFRESH, false))
                        {
                            // refresh
                            mWeatherList.clear();
                        }

                        // get data
                        List<Weather> weatherList = weatherResponse.getResponseObject();
                        Iterator<Weather> iterator = weatherList.iterator();
                        while(iterator.hasNext())
                        {
                            Weather weather = iterator.next();
                            mWeatherList.add(new Weather(weather));
                        }

                        // render view
                        if(mLazyLoading && mViewState==ViewState.CONTENT && mAdapter!=null)
                        {
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            if(mWeatherList!=null) renderView();
                        }

                        // hide progress
                        showLazyLoadingProgress(false);
                        showList();
                    }
                }

                // finish request
                mAPICallManager.finishTask(task);

                // hide progress in action bar
                if(mAPICallManager.getTasksCount()==0) showActionBarProgress(false);
            }
        });
    }


    @Override
    public void onAPICallFail(final APICallTask task, final ResponseStatus status, final Exception exception)
    {
        runTaskCallback(new Runnable()
        {
            public void run()
            {
                if(mRootView==null) return; // view was destroyed

                if(task.getRequest().getClass().equals(WeatherRequest.class))
                {
                    Logcat.d("Fragment.onAPICallFail(ExampleRequest): " + status.getStatusCode() + " " + status.getStatusMessage() +
                            " / " + exception.getClass().getSimpleName() + " / " + exception.getMessage());

                    // hide progress
                    showLazyLoadingProgress(false);
                    showList();

                    // handle fail
                    handleFail(exception);
                }

                // finish request
                mAPICallManager.finishTask(task);

                // hide progress in action bar
                if(mAPICallManager.getTasksCount()==0) showActionBarProgress(false);
            }
        });
    }


    private void handleError(String errorType, String errorMessage)
    {
        // TODO: show dialog
    }


    private void handleFail(Exception exception)
    {
        int messageId;
        if(exception!=null && exception.getClass().equals(UnknownHostException.class)) messageId = R.string.global_apicall_unknown_host_toast;
        else if(exception!=null && exception.getClass().equals(FileNotFoundException.class)) messageId = R.string.global_apicall_not_found_toast;
        else if(exception!=null && exception.getClass().equals(SocketTimeoutException.class)) messageId = R.string.global_apicall_timeout_toast;
        else if(exception!=null && exception.getClass().equals(JsonParseException.class)) messageId = R.string.global_apicall_parse_fail_toast;
        else if(exception!=null && exception.getClass().equals(ParseException.class)) messageId = R.string.global_apicall_parse_fail_toast;
        else if(exception!=null && exception.getClass().equals(NumberFormatException.class)) messageId = R.string.global_apicall_parse_fail_toast;
        else if(exception!=null && exception.getClass().equals(ClassCastException.class)) messageId = R.string.global_apicall_parse_fail_toast;
        else messageId = R.string.global_apicall_fail_toast;
        Toast.makeText(getActivity(), messageId, Toast.LENGTH_LONG).show();
    }


    private void loadData()
    {
        if(NetworkManager.isOnline(getActivity()))
        {
            if(!mAPICallManager.hasRunningTask(WeatherRequest.class))
            {
                // show progress
                showProgress();

                // show progress in action bar
                showActionBarProgress(true);

                // execute request
                WeatherRequest request = new WeatherRequest(mLocation);
                mAPICallManager.executeTask(request, this);
            }
        }
        else
        {
            showOffline();
        }
    }


    public void refreshData()
    {
        if(NetworkManager.isOnline(getActivity()))
        {
            if(!mAPICallManager.hasRunningTask(WeatherRequest.class))
            {
                // show progress in action bar
                showActionBarProgress(true);

                // meta data
                Bundle bundle = new Bundle();
                bundle.putBoolean(META_REFRESH, true);

                // execute request
                int take = (mWeatherList.size() <= LAZY_LOADING_MAX && mWeatherList.size() > 0) ? mWeatherList.size() : LAZY_LOADING_TAKE;
                WeatherRequest request = new WeatherRequest(mLocation);
                request.setMetaData(bundle);
                mAPICallManager.executeTask(request, this);
            }
        }
        else
        {
            Toast.makeText(getActivity(), R.string.global_offline_toast, Toast.LENGTH_LONG).show();
        }
    }


    private void lazyLoadData()
    {
        if(NetworkManager.isOnline(getActivity()))
        {
            // show lazy loading progress
            showLazyLoadingProgress(true);

            // execute request
            WeatherRequest request = new WeatherRequest(mLocation);
            mAPICallManager.executeTask(request, this);
        }
    }


    private void showLazyLoadingProgress(boolean visible)
    {
        if(visible)
        {
            mLazyLoading = true;
        }
        else
        {
            mLazyLoading = false;
        }
    }


    private void showActionBarProgress(boolean visible)
    {
        // show progress in action bar
        ((ActionBarActivity) getActivity()).setSupportProgressBarIndeterminateVisibility(visible);
        mActionBarProgress = visible;
    }


    private void showList()
    {
        // show list container
        ViewGroup containerList = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        containerList.setVisibility(View.VISIBLE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        mViewState = ViewState.CONTENT;
    }


    private void showProgress()
    {
        // show progress container
        ViewGroup containerList = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        containerList.setVisibility(View.GONE);
        containerProgress.setVisibility(View.VISIBLE);
        containerOffline.setVisibility(View.GONE);
        mViewState = ViewState.PROGRESS;
    }


    private void showOffline()
    {
        // show offline container
        ViewGroup containerList = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        containerList.setVisibility(View.GONE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.VISIBLE);
        mViewState = ViewState.OFFLINE;
    }

    protected abstract void renderView();

}
