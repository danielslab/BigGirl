package com.yibao.biggirl.mvp.video;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yibao.biggirl.R;
import com.yibao.biggirl.factory.RecyclerViewFactory;
import com.yibao.biggirl.model.video.VideoResultsBean;
import com.yibao.biggirl.mvp.girls.GirlsContract;
import com.yibao.biggirl.mvp.girls.GirlsPresenter;
import com.yibao.biggirl.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Author：Sid
 * Des：${TODO}
 * Time:2017/5/9 06:53
 */
public class VideoFragmnet
        extends Fragment
        implements GirlsContract.View<VideoResultsBean>, SwipeRefreshLayout.OnRefreshListener
{


    @BindView(R.id.fag_content)
    LinearLayout       mFagContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    Unbinder unbinder;
    private VideoAdapter            mAdapter;
    private int page = 1;
    private int size = 20;
    private FloatingActionButton        mFab;
    private ArrayList<VideoResultsBean> mList;
    private GirlsContract.Presenter mPresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new GirlsPresenter(this);
        mPresenter.start(Constants.FRAGMENT_VIDEO);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = View.inflate(getActivity(), R.layout.girls_frag, null);

        initView();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        mList = new ArrayList<>();
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setVisibility(View.VISIBLE);
        //        mFab.setOnClickListener(this);


    }


    private void initRecyclerView(List<VideoResultsBean> list, int type, String dataType) {
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.setOnRefreshListener(this);
        mAdapter = new VideoAdapter(getActivity(), list);
        RecyclerView recyclerView = RecyclerViewFactory.creatRecyclerView(
                                                                          type,

                                                                          mAdapter);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        int lastPosition = -1;
                        switch (newState) {
                            //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                            case RecyclerView.SCROLL_STATE_IDLE:
                                mFab.setVisibility(View.VISIBLE);
                                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                                if (layoutManager instanceof GridLayoutManager) {
                                    //通过LayoutManager找到当前显示的最后的item的position
                                    lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                                } else if (layoutManager instanceof LinearLayoutManager) {
                                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                                    //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                                    //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(
                                            lastPositions);
                                    lastPosition = findMax(lastPositions);
                                }

                                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                                //如果相等则说明已经滑动到最后了
                                if (lastPosition == recyclerView.getLayoutManager()
                                                                .getItemCount() - 1)
                                {
                                    page++;

                                    mPresenter.loadData(size,
                                                        page,
                                                        Constants.LOAD_MORE_DATA,Constants.FRAGMENT_VIDEO);
                                    //                        mProgressBar.setVisibility(View.VISIBLE);
                                }
                                break;
                            case RecyclerView.SCROLL_STATE_DRAGGING:
                                mFab.setVisibility(View.INVISIBLE);
                                break;
                            case RecyclerView.SCROLL_STATE_SETTLING:
                                mFab.setVisibility(View.INVISIBLE);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        //得到当前显示的最后一个item的view
                        View lastChildView = recyclerView.getLayoutManager()
                                                         .getChildAt(recyclerView.getLayoutManager()
                                                                                 .getChildCount() - 1);
                        //得到lastChildView的bottom坐标值
                        int lastChildBottom = lastChildView.getBottom();
                        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                        int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                        //通过这个lastChildView得到这个view当前的position值
                        int lastPosition = recyclerView.getLayoutManager()
                                                       .getPosition(lastChildView);

                        //判断lastChildView的bottom值跟recyclerBottom
                        //判断lastPosition是不是最后一个position
                        //如果两个条件都满足则说明是真正的滑动到了底部
                        if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager()
                                                                                             .getItemCount() - 1)
                        {
                            //                    mProgressBar.setVisibility(View.VISIBLE);
                        }
                    }

                });
        mFagContent.addView(recyclerView);
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onRefresh() {
        Observable.timer(1, TimeUnit.SECONDS)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(aLong -> {
                      mPresenter.loadData(size,
                                          page,
                                          Constants.REFRESH_DATA,Constants.FRAGMENT_VIDEO);

                      mSwipeRefresh.setRefreshing(true);
                      page = 1;
                  });
    }

    @Override
    public void loadData(List<VideoResultsBean> list) {
        mList.addAll(list);
        initRecyclerView(mList, 1, Constants.FRAGMENT_VIDEO);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void refresh(List<VideoResultsBean> list) {
        mList.clear();
        mAdapter.clear();
        mList.addAll(list);
        mAdapter.AddHeader(list);
        mAdapter.notifyDataSetChanged();
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void loadMore(List<VideoResultsBean> list) {
        mList.addAll(list);
        mAdapter.AddFooter(mList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    @Override
    public void showNormal() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public VideoFragmnet newInstance() {
        return new VideoFragmnet();
    }

    @Override
    public void setPrenter(GirlsContract.Presenter prenter) {

    }


    //    @Override
    //    public void onClick(View view) {
    //        page=1;
    //        mPresenter.loadData(size, page, Constants.REFRESH_DATA);
    //        mSwipeRefresh.setRefreshing(true);
    //
    //    }
}
