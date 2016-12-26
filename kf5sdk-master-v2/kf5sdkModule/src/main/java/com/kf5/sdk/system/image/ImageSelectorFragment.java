package com.kf5.sdk.system.image;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kf5.sdk.R;
import com.kf5.sdk.system.image.adapter.FolderAdapter;
import com.kf5.sdk.system.image.adapter.ImageGridAdapter;
import com.kf5.sdk.system.image.bean.Folder;
import com.kf5.sdk.system.image.bean.Image;
import com.kf5.sdk.system.image.utils.FileUtils;
import com.kf5.sdk.system.image.view.ListFilePopWindow;
import com.kf5.sdk.system.utils.FileSizeUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/18 10:26
 * email:812219713@qq.com
 */

public class ImageSelectorFragment extends Fragment {

    public static final String TAG = "me.nereo.multi_image_selector.MultiImageSelectorFragment";

    private static final String KEY_TEMP_FILE = "key_temp_file";

    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;


    // 结果数据
    private ArrayList<String> resultList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    // 图片Grid
    private GridView mGridView;
    private Callback mCallback;

    private ImageGridAdapter mImageAdapter;

    private FolderAdapter mFolderAdapter;

    // 类别
    private TextView mCategoryText;
    // 预览按钮
    //	private Button mPreviewBtn;

    private int mDesireImageCount;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;

    private File mTmpFile;

    private ListFilePopWindow listFilePopWindow;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kf5_fragment_multi_image, null, false);
        mCategoryText = (TextView) view.findViewById(R.id.kf5_category_btn);
        mGridView = (GridView) view.findViewById(R.id.kf5_selector_gridview);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 选择图片数量
        mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);
        // 图片选择模式
        final int mode = getArguments().getInt(EXTRA_SELECT_MODE);

        // 默认选择
        if (mode == MODE_MULTI) {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
        }

        // 是否显示照相机
        mIsShowCamera = getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
        mImageAdapter = new ImageGridAdapter(getActivity(), mIsShowCamera, 3);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);


        // 初始化，加载所有图片
        mCategoryText.setText(R.string.kf5_photo_all);
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listFilePopWindow == null) {
                    listFilePopWindow = new ListFilePopWindow(getActivity(), mCategoryText);
                    listFilePopWindow.setListAdapter(mFolderAdapter);
                    listFilePopWindow.setPopwindowItemClickListener(new ListFilePopWindow.OnFileListPopWindowItemClickListener() {

                        @Override
                        public void onFileListItemCilck(AdapterView<?> adapterView, View view, int i, long l) {
                            // TODO Auto-generated method stub
                            mFolderAdapter.setSelectIndex(i);

                            final int index = i;
                            final AdapterView v = adapterView;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //									mFolderPopupWindow.dismiss();
                                    if (index == 0) {
                                        getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                                        mCategoryText.setText(R.string.kf5_photo_all);
                                        if (mIsShowCamera) {
                                            mImageAdapter.setShowCamera(true);
                                        } else {
                                            mImageAdapter.setShowCamera(false);
                                        }
                                    } else {
                                        Folder folder = (Folder) v.getAdapter().getItem(index);
                                        if (null != folder) {
                                            mImageAdapter.setData(folder.images);
                                            mCategoryText.setText(folder.name);
                                            // 设定默认选择
                                            if (resultList != null && resultList.size() > 0) {
                                                mImageAdapter.setDefaultSelected(resultList);
                                            }
                                        }
                                        mImageAdapter.setShowCamera(false);
                                    }

                                    // 滑动到最初始位置
                                    mGridView.smoothScrollToPosition(0);
                                }
                            }, 100);
                        }
                    });
                }
                listFilePopWindow.show();
            }
        });


        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if (i == 0) {
                        showCameraAction();
                    } else {
                        // 正常操作
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image, mode);
                    }
                } else {
                    // 正常操作
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == SCROLL_STATE_FLING) {
                    Glide.with(getActivity()).pauseRequests();
                } else {
                    Glide.with(getActivity()).resumeRequests();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mFolderAdapter = new FolderAdapter(getActivity());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_TEMP_FILE, mTmpFile);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mTmpFile = (File) savedInstanceState.getSerializable(KEY_TEMP_FILE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        //new LoadImageTask().execute();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            } else {
                while (mTmpFile != null && mTmpFile.exists()) {
                    boolean success = mTmpFile.delete();
                    if (success) {
                        mTmpFile = null;
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (listFilePopWindow != null) {
            if (listFilePopWindow.isShowing()) {
                listFilePopWindow.disMiss();
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            try {
                mTmpFile = FileUtils.createTmpFile(getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mTmpFile != null && mTmpFile.exists()) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            } else {
                Toast.makeText(getActivity(), getString(R.string.kf5_image_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.kf5_no_camera_hint), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            // 多选模式
            if (mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.path);
                    }
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.kf5_photo_amount_limit_hint, String.valueOf(resultList.size())), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    resultList.add(image.path);
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.path);
                    }
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE) {
                // 单选模式
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image.path);
                }
            }
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback;

    {
        mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media._ID};

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == LOADER_ALL) {
                    return new CursorLoader(getActivity(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                            new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                } else if (id == LOADER_CATEGORY) {
                    return new CursorLoader(getActivity(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
                            null, IMAGE_PROJECTION[2] + " DESC");
                }
                return null;
            }

            private boolean fileExist(String path) {
                if (!TextUtils.isEmpty(path)) {
                    return new File(path).exists();
                }
                return false;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null) {
                    if (data.getCount() > 0) {
                        List<Image> images = new ArrayList<>();
                        data.moveToFirst();
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                            Image image = null;
                            if (fileExist(path) && FileSizeUtil.getFileOrFilesSize(path, FileSizeUtil.SIZETYPE_B) > 0) {
                                image = new Image(path, name, dateTime);
                                images.add(image);
                            }
                            if (!hasFolderGened) {
                                // 获取文件夹名称
                                File folderFile = new File(path).getParentFile();
                                if (folderFile != null && folderFile.exists()) {
                                    String fp = folderFile.getAbsolutePath();
                                    Folder f = getFolderByPath(fp);
                                    if (f == null) {
                                        Folder folder = new Folder();
                                        folder.name = folderFile.getName();
                                        folder.path = fp;
                                        folder.cover = image;
                                        List<Image> imageList = new ArrayList<>();
                                        imageList.add(image);
                                        folder.images = imageList;
                                        mResultFolder.add(folder);
                                    } else {
                                        f.images.add(image);
                                    }
                                }
                            }

                        } while (data.moveToNext());

                        mImageAdapter.setData(images);
                        // 设定默认选择
                        if (resultList != null && resultList.size() > 0) {
                            mImageAdapter.setDefaultSelected(resultList);
                        }

                        if (!hasFolderGened) {
                            mFolderAdapter.setData(mResultFolder);
                            hasFolderGened = true;
                        }

                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }

    private Folder getFolderByPath(String path) {
        if (mResultFolder != null) {
            for (Folder folder : mResultFolder) {
                if (TextUtils.equals(folder.path, path)) {
                    return folder;
                }
            }
        }
        return null;
    }

    /**
     * 回调接口
     */
    public interface Callback {
        void onSingleImageSelected(String path);

        void onImageSelected(String path);

        void onImageUnselected(String path);

        void onCameraShot(File imageFile);
    }

}
