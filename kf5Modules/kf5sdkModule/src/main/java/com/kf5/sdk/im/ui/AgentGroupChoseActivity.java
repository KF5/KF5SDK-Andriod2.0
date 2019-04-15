package com.kf5.sdk.im.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.SelectAgentGroupItem;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.system.entity.RefreshLayoutConfig;
import com.kf5.sdk.system.entity.TitleBarProperty;
import com.kf5.sdk.system.utils.ClickUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

public class AgentGroupChoseActivity extends BaseActivity {

    public static final String OPTIONS_LIST = "options_list";

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_layout_refresh_listview;
    }

    @Override
    protected TitleBarProperty getTitleBarProperty() {
        return new TitleBarProperty.Builder()
                .setTitleContent(getString(R.string.kf5_select_question))
                .setRightViewVisible(false)
                .build();
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        List<SelectAgentGroupItem> list = getIntent().getParcelableArrayListExtra(OPTIONS_LIST);
        final SelectAgentAdapter adapter = new SelectAgentAdapter(this, list);
        RefreshLayout refreshLayout = ((RefreshLayout) findViewById(R.id.kf5_refreshLayout));
        ListView mListView = (ListView) findViewById(R.id.kf5_listView);
        RefreshLayoutConfig.start()
                .with(this)
                .withListView(mListView)
                .listViewDivider(getResources().getDrawable(R.drawable.kf5_divider_inset_left_16))
                .listViewDividerHeight(1)
                .listViewItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (ClickUtils.isInvalidClick(view)) {
                            return;
                        }
                        Intent intent = new Intent(BaseChatActivity.SelectGroupReceiver.ACTION_FILTER);
                        try {
                            SelectAgentGroupItem item = adapter.getItem(position - 1);
                            intent.putExtra(BaseChatActivity.SelectGroupReceiver.DATA_KEY, item.getKey());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        sendBroadcast(intent);
                        finish();
                    }
                })
                .withRefreshLayout(refreshLayout)
                .refreshLayoutEnableRefreshAndLoadMore(false, false)
                .commitWithSetAdapter(adapter);
    }


    private class SelectAgentAdapter extends CommonAdapter<SelectAgentGroupItem> {

        public SelectAgentAdapter(Context context, List<SelectAgentGroupItem> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflateLayout(R.layout.kf5_select_agent_list_item, viewGroup);
                holder.tvTitle = (TextView) view.findViewById(R.id.kf5_select_group_list_item_title);
                holder.tvDescription = (TextView) view.findViewById(R.id.kf5_select_group_list_item_description);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            SelectAgentGroupItem item = getItem(position);
            if (item != null) {
                holder.tvDescription.setText(item.getDescription());
                holder.tvTitle.setText(item.getTitle());
            }

            return view;
        }

        private class ViewHolder {
            TextView tvTitle, tvDescription;
        }
    }
}
