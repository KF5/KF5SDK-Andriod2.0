package com.chosen.album.internal.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chosen.album.internal.entity.Item;
import com.chosen.album.internal.entity.SelectionSpec;
import com.chosen.album.internal.model.SelectedItemCollection;

import java.util.List;

/**
 * @author Chosen
 * @create 2019/1/9 16:33
 * @email 812219713@qq.com
 */
public class SelectedPreviewActivity extends BasePreviewActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SelectionSpec.getInstance().hasInited) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Bundle bundle = getIntent().getBundleExtra(EXTRA_DEFAULT_BUNDLE);
        List<Item> selected = bundle.getParcelableArrayList(SelectedItemCollection.STATE_SELECTION);
        mAdapter.addAll(selected);
        mAdapter.notifyDataSetChanged();
        if (mSpec.countable) {
            mCheckView.setCheckedNum(1);
        } else {
            mCheckView.setChecked(true);
        }
        mPreviousPos = 0;
        updateSize(selected.get(0));
    }

}
