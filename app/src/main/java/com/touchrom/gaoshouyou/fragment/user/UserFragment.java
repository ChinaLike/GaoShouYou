package com.touchrom.gaoshouyou.fragment.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.DensityUtils;
import com.arialyy.frame.util.FileUtil;
import com.bumptech.glide.Glide;
import com.touchrom.gaoshouyou.base.adapter.SimpleAdapter;
import com.touchrom.gaoshouyou.base.adapter.SimpleViewHolder;
import com.lyy.ui.widget.NoScrollGridView;
import com.touchrom.gaoshouyou.R;
import com.touchrom.gaoshouyou.activity.AppManagerActivity;
import com.touchrom.gaoshouyou.activity.MainActivity;
import com.touchrom.gaoshouyou.activity.RegActivity;
import com.touchrom.gaoshouyou.activity.user.CollectActivity;
import com.touchrom.gaoshouyou.activity.user.FollowActivity;
import com.touchrom.gaoshouyou.activity.user.GiftManagerActivity;
import com.touchrom.gaoshouyou.activity.user.HonorActivity;
import com.touchrom.gaoshouyou.activity.user.MsgActivity;
import com.touchrom.gaoshouyou.activity.user.UserCommentActivity;
import com.touchrom.gaoshouyou.base.AppManager;
import com.touchrom.gaoshouyou.base.BaseFragment;
import com.touchrom.gaoshouyou.config.Constance;
import com.touchrom.gaoshouyou.config.ResultCode;
import com.touchrom.gaoshouyou.databinding.FragmentSimpleContentBinding;
import com.touchrom.gaoshouyou.dialog.ImgChooseDialog;
import com.touchrom.gaoshouyou.entity.AccountEntity;
import com.touchrom.gaoshouyou.entity.SimpleAdapterEntity;
import com.touchrom.gaoshouyou.entity.UserEntity;
import com.touchrom.gaoshouyou.entity.WebEntity;
import com.touchrom.gaoshouyou.help.turn.TurnHelp;
import com.touchrom.gaoshouyou.module.UserModule;
import com.touchrom.gaoshouyou.util.S;
import com.touchrom.gaoshouyou.widget.IconTextItem;
import com.touchrom.gaoshouyou.widget.LoginView;
import com.touchrom.gaoshouyou.widget.RedDotLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lk on 2015/12/4.
 * 个人中心界面
 */
@SuppressLint("ValidFragment")
public class UserFragment extends BaseFragment<FragmentSimpleContentBinding> implements View.OnClickListener, NotifyHelp.OnNotifyCallback {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int IMG_LOAD = 2;
    static final int IMG_CROPPER = 3;
    private static final Object LOCK = new Object();
    private static volatile UserFragment mFragment = null;
    @InjectView(R.id.nike_name)
    TextView mNikeName;
    @InjectView(R.id.exp)
    TextView mExp;
    @InjectView(R.id.grid)
    NoScrollGridView mGrid;
    @InjectView(R.id.my_app_mg)
    IconTextItem mAm;
    @InjectView(R.id.user_setting)
    IconTextItem mUserSetting;
    @InjectView(R.id.head_img)
    ImageView mUserIcon;
    @InjectView(R.id.root_rl)
    RelativeLayout mRoot;
    @InjectView(R.id.temp)
    View mTemp;
    @InjectView(R.id.content)
    ScrollView mContent;
    @InjectView(R.id.login_out)
    TextView mLoginOut;
    @InjectView(R.id.sign_bt)
    Button mSingBt;
    @InjectView(R.id.sign_rule)
    TextView mSignRule;
    LoginView mLogin;
    private File mCacheFile;
    MyAdapter mAdapter;

    private UserFragment() {
    }

    public static UserFragment getInstance() {
        if (mFragment == null) {
            synchronized (LOCK) {
                mFragment = new UserFragment();
            }
        }
        return mFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_my_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        NotifyHelp.getInstance().addObj(Constance.NOTIFY_KEY.USER_FRAGMENT, this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotifyHelp.getInstance().removeObj(Constance.NOTIFY_KEY.USER_FRAGMENT);
    }

    private void initView() {
        if (AppManager.getInstance().isLogin()) {
            mContent.setVisibility(View.VISIBLE);
            mTemp.setVisibility(View.VISIBLE);
            if (mLogin != null){
                mRoot.removeView(mLogin);
            }
            ((MainActivity) mActivity).setLeaveVisibility(View.VISIBLE);
            AccountEntity entity = AppManager.getInstance().getAccount();
            getModule(UserModule.class).getUserInfo(entity.getAccount(), entity.getPassword());
        } else {
            mContent.setVisibility(View.GONE);
            mTemp.setVisibility(View.GONE);
            ((MainActivity) mActivity).setLeaveVisibility(View.GONE);
            mLogin = new LoginView(getContext());
            mLogin.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mLogin.setLoginCallback(new LoginView.LoginCallback() {
                @Override
                public void onLogin(boolean success, UserEntity entity) {
                    if (success) {
                        initWidget(entity);
                        mRoot.removeView(mLogin);
                    } else {
                        S.i(mRootView, entity.getRegMsg());
                    }
                }

                @Override
                public void onReg() {
                    Intent intent = new Intent(getContext(), RegActivity.class);
                    startActivityForResult(intent, 0xdc1);
                }
            });
            mRoot.addView(mLogin);
        }
    }

    private void initWidget(final UserEntity entity) {
        mContent.setVisibility(View.VISIBLE);
        mTemp.setVisibility(View.VISIBLE);
        mAm.setOnClickListener(this);
        mUserSetting.setOnClickListener(this);
        mSingBt.setOnClickListener(this);
        mSignRule.setOnClickListener(this);
        mAdapter = new MyAdapter(getContext(), setData(entity.isHasNewMsg()), R.layout.item_share);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getContext(), FollowActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getContext(), UserCommentActivity.class);
                        break;
                    case 2:
                        intent = new Intent(getContext(), CollectActivity.class);
                        break;
                    case 3:
                        intent = new Intent(getContext(), HonorActivity.class);
                        break;
                    case 4:
                        intent = new Intent(getContext(), GiftManagerActivity.class);
                        break;
                    case 5:
                        intent = new Intent(getContext(), MsgActivity.class);
                        break;
                }
                if (intent != null) {
                    getContext().startActivity(intent);
                }
                if (position == 5) {
                    mAdapter.consumeRedDot(position);
                    entity.setHasNewMsg(false);
                    AppManager.getInstance().saveUser(entity);
                    getModule(UserModule.class).msgReadState();
                }
            }
        });
        mUserIcon.setOnClickListener(this);
        ((MainActivity) mActivity).setLeave(entity.getLevel(), entity.getLevelTag());
        mNikeName.setText(entity.getNikeName());
        Glide.with(getContext()).load(entity.getHeadImg()).into(mUserIcon);
        mExp.setText("经验：" + entity.getExp() + "  积分：" + entity.getIntegral());
        mLoginOut.setOnClickListener(this);
    }

    private List<SimpleAdapterEntity> setData(boolean hasNewMsg) {
        List<SimpleAdapterEntity> list = new ArrayList<>();
        int[] imgs = new int[]{
                R.mipmap.icon_share_wx,
                R.mipmap.icon_share_wx_moment,
                R.mipmap.icon_share_weibo,
                R.mipmap.icon_share_qq,
                R.mipmap.icon_share_qzone,
                R.mipmap.icon_share_more
        };
        String[] texts = new String[]{
                "关注", "评论", "收藏", "荣誉", "礼包", "消息"
        };
        boolean[] news = new boolean[]{false, false, false, false, false, hasNewMsg};

        int i = 0;
        for (int img : imgs) {
            SimpleAdapterEntity entity = new SimpleAdapterEntity();
            entity.setMsg(texts[i]);
            entity.setArg(img);
            entity.setB(news[i]);
            list.add(entity);
            i++;
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_app_mg:
                startActivity(new Intent(getContext(), AppManagerActivity.class));
                break;
            case R.id.head_img:
                ImgChooseDialog dialog = new ImgChooseDialog(this);
                dialog.show(getChildFragmentManager(), "imgChooseDialog");
                break;
            case R.id.login_out:
                showLoadingDialog();
                AppManager.getInstance().loginOut();
                dismissLoadingDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                    }
                }, 500);
                break;
            case R.id.user_setting:
                WebEntity entity = new WebEntity();
                entity.setTitle("用户设置");
                entity.setContentUrl(Constance.URL.USER_SETTING);
                TurnHelp.turnNormalWeb(getContext(), entity);
                break;
            case R.id.sign_bt:
                getModule(UserModule.class).signIn();
                break;
            case R.id.sign_rule:
                WebEntity entity1 = new WebEntity();
                entity1.setTitle("签到规则");
                entity1.setContentUrl(Constance.URL.SIGN_RULE);
                TurnHelp.turn(getContext(), entity1);
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        String cache = AndroidUtils.getDiskCacheDir(getContext()) + File.separator + "cache_" + System.currentTimeMillis() + ".jpg";
        mCacheFile = new File(cache);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCacheFile));
        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.DIALOG.IMG_CHOOSE) {
            if ((int) obj == 1) {
                dispatchTakePictureIntent();
            } else {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMG_LOAD);
            }
        } else if (result == ResultCode.SERVER.LOGIN) {
            UserEntity entity = (UserEntity) obj;
            if (entity.isSuccess()) {
                AppManager.getInstance().saveLoginState(true);
                AppManager.getInstance().saveUser(entity);
                initWidget(entity);
            } else {
                S.i(mRootView, entity.getRegMsg());
                AppManager.getInstance().saveLoginState(false);
                initView();
            }
        } else if (result == ResultCode.SERVER.SIGN_IN) {
            AccountEntity entity = AppManager.getInstance().getAccount();
            getModule(UserModule.class).getUserInfo(entity.getAccount(), entity.getPassword());
        }
    }


    public void cropImg(String imgUrl) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imgUrl)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        intent.putExtra("outputX", DensityUtils.dp2px(100));
        intent.putExtra("outputY", DensityUtils.dp2px(100));
        startActivityForResult(intent, IMG_CROPPER);
    }

    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            if (mCacheFile != null && mCacheFile.exists()) {
                mCacheFile.delete();
            }
            Glide.clear(mUserIcon);
            if (photo != null) {
                mUserIcon.setImageBitmap(photo);
                FileUtil.saveBitmap(Constance.PATH.TEMP_HEAD_IMG_PATH, photo);
                getModule(UserModule.class).uploadHeadImg(Constance.PATH.TEMP_HEAD_IMG_PATH);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMG_LOAD && resultCode == Activity.RESULT_OK && data != null) {
            Uri img = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(img, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgPath = cursor.getString(columnIndex);
                cursor.close();
                cropImg(imgPath);
            }
        }
        if (requestCode == IMG_CROPPER && resultCode == Activity.RESULT_OK && data != null) {
            setPicToView(data);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            cropImg(mCacheFile.getPath());
        }
        if (requestCode == 0xdc1 && resultCode == Activity.RESULT_OK && data != null) {
            UserEntity userEntity = data.getParcelableExtra("user");
            if (mLogin != null) {
                mRoot.removeView(mLogin);
            }
            initWidget(userEntity);
        }
    }

    @Override
    public void onNotify(int action, Object obj) {
        initView();
    }

    class MyAdapter extends SimpleAdapter<SimpleAdapterEntity> {

        public MyAdapter(Context context, List<SimpleAdapterEntity> mData, int itemLayoutId) {
            super(context, mData, itemLayoutId);
        }

        @Override
        public void convert(SimpleViewHolder helper, SimpleAdapterEntity item) {
            helper.setImageResource(R.id.img, item.getArg());
            helper.setText(R.id.text, item.getMsg());
            RedDotLayout rdy = helper.getView(R.id.red_dot_1);
            rdy.setVisibility(item.isB() ? View.VISIBLE : View.GONE);
        }

        public void consumeRedDot(int position) {
            mData.get(position).setB(false);
            notifyDataSetChanged();
        }
    }
}
