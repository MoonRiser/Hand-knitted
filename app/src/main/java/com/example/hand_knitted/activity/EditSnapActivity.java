package com.example.hand_knitted.activity;

import android.content.Intent;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.hand_knitted.R;
import com.example.hand_knitted.bean.Post;
import com.example.hand_knitted.bean.User;
import com.example.hand_knitted.bean.Work;
import com.example.hand_knitted.presenter.HKPresenter;
import com.example.hand_knitted.presenter.IHKPresenter;
import com.example.hand_knitted.util.MyUtils;
import com.example.hand_knitted.view.IHKView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.hand_knitted.util.MyUtils.IMAGE_PICKER;

public class EditSnapActivity extends BaseActivity implements View.OnClickListener, IHKView {

    @BindView(R.id.TB2s)
    public Toolbar toolbar;

    @BindView(R.id.ETtitles)
    public EditText ETtitle;

    @BindView(R.id.IMG2s)
    public ImageView img;

    @BindView(R.id.ETcontents)
    public EditText ETcontent;
    @BindView(R.id.PBhs)
    public ContentLoadingProgressBar progressBar;


    private IHKPresenter presenter = new HKPresenter(this);
    private Boolean isADD;
    private String picPath;
    private Post myPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_snap);


        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setSelectLimit(1);                        //选中数量限制

        progressBar.hide();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        img.setOnClickListener(this);
        Intent intent = getIntent();
        isADD = intent.getBooleanExtra("isADD", false);

        if (!isADD) {
            myPost = (Post) intent.getSerializableExtra("post");
            setCurrentConfig(myPost);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_epa, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (isSomethingNull()) {
                    break;
                } else {
                    getCurrentConfig(isADD);
                }

                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }


    @Override
    public void showWorkData(List<Work> list) {

    }

    @Override
    public void showPostData(List<Post> posts) {

    }

    @Override
    public void showProgress(Boolean show) {

    }

    @Override
    public void showResultToast(String info) {
        showToastLong(info);
        if ("发帖成功".equals(info) || "帖子更新成功".equals(info)) {
            //finish();
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                picPath = images.get(0).path;
                Glide.with(this).load(picPath).into(img);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void setCurrentConfig(Post post) {


        ETtitle.setText(post.getTitle());
        ETcontent.setText(post.getContent());
        Glide.with(this).load(post.getImage().getFileUrl()).into(img);


    }

    private void getCurrentConfig(Boolean isADD) {

        Post post = new Post();
        String title = ETtitle.getText().toString();
        String content = ETcontent.getText().toString();

        post.setContent(content);
        post.setTitle(title);
        post.setAuthor(BmobUser.getCurrentUser(User.class));
        post.setDate(MyUtils.getCurrentDate());
        post.setSnap(true);
        if ((!isADD) && (picPath == null)) {
            post.setObjectId(myPost.getObjectId());
            presenter.updatePost(post);
            return;
        }



        final String[] filePaths = new String[1];
        filePaths[0] = picPath;
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {

                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    post.setImage(files.get(0));
                    if (isADD) {
                        presenter.addPost(post);
                        Log.i("三张图片均已上传完成", "");
                    } else {
                        post.setObjectId(myPost.getObjectId());
                        presenter.updatePost(post);
                    }
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                showToastLong("错误码" + statuscode + ",错误描述：" + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {

                if (progressBar != null) {
                    progressBar.show();
                    progressBar.setProgress(totalPercent);
                    if (totalPercent == 100)
                        progressBar.hide();
                }
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });


    }


    private Boolean isSomethingNull() {

        String title = ETtitle.getText().toString();
        String content = ETcontent.getText().toString();

        if (title.length() == 0) {
            showToast("请先输入标题，再点击上传");
            return true;
        }
        if (content.length() == 0) {
            showToast("请先输入内容，再点击上传");
            return true;
        }
        if (picPath == null) {

            if (isADD) {
                showToast("请先选择图片，再点击上传");
                return true;
            }

        }
        return false;
    }

}
