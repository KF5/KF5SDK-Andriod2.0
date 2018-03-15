package com.kf5.sdk.im.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.SelectAgentGroupItem;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.system.swipeback.BaseSwipeBackActivity;
import com.kf5.sdk.system.widget.RefreshListView;

import java.util.List;

public class AgentGroupChoseActivity extends BaseSwipeBackActivity {

    private ImageView imgBack;

    private RefreshListView mListView;


    public static final String OPTIONS_LIST = "options_list";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.kf5_activity_anim_in, R.anim.kf5_anim_stay);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kf5_activity_agent_group_chose);
        initWidgets();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.kf5_anim_stay, R.anim.kf5_activity_anim_out);
    }

    private void initWidgets() {
        imgBack = (ImageView) findViewById(R.id.kf5_return_img);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = (RefreshListView) findViewById(R.id.kf5_select_group_listview);
        List<SelectAgentGroupItem> list = getIntent().getParcelableArrayListExtra(OPTIONS_LIST);
        final SelectAgentAdapter adapter = new SelectAgentAdapter(this, list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BaseChatActivity.SelectGroupReceiver.ACTION_FILTER);
                try {
                    SelectAgentGroupItem item = adapter.getItem(position - 1);
                    String agentArrayString = item.getAgentIds() != null ? item.getAgentIds() : "[]";
                    intent.putExtra(BaseChatActivity.SelectGroupReceiver.DATA_KEY, agentArrayString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendBroadcast(intent);
                finish();
            }
        });
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
