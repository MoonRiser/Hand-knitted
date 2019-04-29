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
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.hand_knitted.util.MyUtils.IMAGE_PICKER;

public class EditPostActivity extends BaseActivity implements View.OnClickListener , IHKView {

    @BindView(R.id.TB2)
    public Toolbar toolbar;

    @BindView(R.id.ETtitle)
    public EditText ETtitle;

    @BindView(R.id.tool)
    public RadioGroup RGtool;
    @BindView(R.id.group)
    public RadioGroup RGgroup;
    @BindView(R.id.style)
    public RadioGroup RGstyle;

    @BindView(R.id.IMG2)
    public ImageView img;

    @BindView(R.id.ETcontent)
    public EditText ETcontent;


    private IHKPresenter presenter= new HKPresenter(this);
    private Boolean isADD;
    private String picPath;
    private Post myPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        img.setOnClickListener(this);
        Intent intent = getIntent();
        isADD = intent.getBooleanExtra("isADD",false);

        if(!isADD){
            myPost = (Post) intent.getSerializableExtra("post");
            setCurrentConfig(myPost);}


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_epa,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                if(isSomethingNull()){break;}else {
                    getCurrentConfig(isADD);
                }

                break;
            case android.R.id.home:finish();
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IMG2:Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
        }

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
        if("发帖成功".equals(info)){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Glide.with(this).load(images.get(0).path).into(img);
                picPath=images.get(0).path;
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

    }





    private void setCurrentConfig(Post post){


        ETtitle.setText(post.getTitle());
        ETcontent.setText(post.getContent());
        RGgroup.check(MyUtils.groupid[Integer.parseInt(post.getGroup())-1]);
        RGtool.check(MyUtils.toolid[Integer.parseInt(post.getTool())-1]);
        RGstyle.check(MyUtils.styleid[Integer.parseInt(post.getStyle())-1]);
        Glide.with(this).load(post.getImage().getFileUrl()).into(img);


    }

    private void getCurrentConfig(Boolean isADD){

        Post post = new Post();
        String title = ETtitle.getText().toString();
        String content = ETcontent.getText().toString();
        String tool = "1";
        String group = "1";
        String style = "1";
        switch (RGtool.getCheckedRadioButtonId()){
        //    case R.id.RBtool1:tool = "1";break;
            case R.id.RBtool2:tool = "2";break;
            case R.id.RBtool3:tool = "3";break;
        }
        switch (RGgroup.getCheckedRadioButtonId()){
        //    case R.id.RBgroup1:group = "1";break;
            case R.id.RBgroup2:group = "2";break;
            case R.id.RBgroup3:group = "3";break;
            case R.id.RBgroup4:group = "4";break;
            case R.id.RBgroup5:group = "5";break;

        }
        switch (RGstyle.getCheckedRadioButtonId()){
         //   case R.id.RBstyle1:style = "1";break;
            case R.id.RBstyle2:style = "2";break;
            case R.id.RBstyle3:style = "3";break;
            case R.id.RBstyle4:style = "4";break;
            case R.id.RBstyle5:style = "5";break;
        }

        post.setContent(content);
        post.setTitle(title);
        post.setAuthor(BmobUser.getCurrentUser(User.class));
        post.setDate(MyUtils.getCurrentDate());
        post.setGroup(group);
        post.setTool(tool);
        post.setStyle(style);
        post.setSnap(false);
        post.setDate(MyUtils.getCurrentDate());
        if((!isADD)&&(picPath==null)){
            post.setObjectId(myPost.getObjectId());
            presenter.updatePost(post);
            return;
        }


        BmobFile img=new BmobFile(new File(picPath));
        img.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    post.setImage(img);
                    if(isADD){
                        presenter.addPost(post);
                    }else {
                        post.setObjectId(myPost.getObjectId());
                        presenter.updatePost(post);
                    }

                }else {
                    Log.i("图片文件上传失败",e.getErrorCode()+e.getMessage());
                }
            }
    });




    }


    private Boolean isSomethingNull(){

        String title = ETtitle.getText().toString();
        String content = ETcontent.getText().toString();

        if(title.length()==0){
            showToast("请先输入标题，再点击上传");
            return true;
        }
        if(content.length()==0){
            showToast("请先输入内容，再点击上传");
            return true;
        }
        if(picPath==null){

            if(isADD){
                showToast("请先选择图片，再点击上传");
                return true;
            }

        }
        return false;
    }

}
