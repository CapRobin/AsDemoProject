package com.andbase.demo.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

import com.andbase.demo.R;
import com.andbase.demo.adapter.ImageUploadAdapter;
import com.andbase.demo.global.MyApplication;
import com.andbase.demo.model.ImageUploadInfo;
import com.andbase.library.app.base.AbBaseActivity;
import com.andbase.library.image.AbImageLoader;
import com.andbase.library.util.AbAppUtil;
import com.andbase.library.util.AbDialogUtil;
import com.andbase.library.util.AbFileUtil;
import com.andbase.library.util.AbLogUtil;
import com.andbase.library.util.AbStrUtil;
import com.andbase.library.util.AbToastUtil;
import com.andbase.library.view.sample.AbSampleGridView;


public class AddPhotoActivity extends AbBaseActivity {
	
	private MyApplication application;
    private AbSampleGridView girdView = null;
	private ImageUploadAdapter imageListAdapter = null;
	private ArrayList<ImageUploadInfo> photoList = null;
	private ArrayList<String> photoListString = null;
	private ArrayList<String> photoServerList = null;

	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	
	// 照相机拍照得到的图片
	private File currentPhotoFile;
	private String fileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_add_photo);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

		application = (MyApplication) this.getApplication();
		photoList = new ArrayList<ImageUploadInfo>();
		photoServerList = new ArrayList<String>();
		photoListString = new ArrayList<String>();
		//默认
		photoList.add(new ImageUploadInfo(String.valueOf(R.drawable.cam_photo)));

		DisplayMetrics dm = AbAppUtil.getDisplayMetrics(this);
		int width = ((dm.widthPixels-80)/3);

		girdView = (AbSampleGridView)findViewById(R.id.grid_view);
		girdView.setPadding(10,10);
		girdView.setColumn(3);
		imageListAdapter = new ImageUploadAdapter(this, photoList,width,width,imageLoader);
		girdView.setAdapter(imageListAdapter);
	   
		Button addBtn = (Button)findViewById(R.id.addBtn);

        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= photoList.size()-1){
                    View avatarView = View.inflate(AddPhotoActivity.this,R.layout.view_choose_avatar,null);
                    Button albumButton = (Button)avatarView.findViewById(R.id.choose_album);
                    Button camButton = (Button)avatarView.findViewById(R.id.choose_cam);
                    Button cancelButton = (Button)avatarView.findViewById(R.id.choose_cancel);
                    albumButton.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            AbDialogUtil.removeDialog(AddPhotoActivity.this);
                            // 从相册中去获取
                            try {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
                            } catch (Exception e) {
                                AbToastUtil.showToast(AddPhotoActivity.this,"没有找到照片");
                            }
                        }

                    });

                    camButton.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            AbDialogUtil.removeDialog(AddPhotoActivity.this);
                            doPickPhotoAction();
                        }

                    });

                    cancelButton.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            AbDialogUtil.removeDialog(AddPhotoActivity.this);
                        }

                    });
                    AbDialogUtil.showDialog(avatarView, Gravity.BOTTOM);
                }else{
                    //点击了 其他的
                    //AbToastUtil.showToast(AddPhotoActivity.this,"1点击了：" + position);

                    Intent intent = new Intent(AddPhotoActivity.this, ImageViewerActivity.class);
					intent.putStringArrayListExtra("PATH",photoListString);
                    intent.putExtra("POSITION",position);
                    startActivity(intent);
                }
            }

        });
		
		addBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				uploadFile();
			}
		});

	}
	
	/**
	 * 从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		//判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			AbToastUtil.showToast(AddPhotoActivity.this,"没有可用的存储卡");
		}
	}

	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			fileName = "camera_"+new Random().nextInt(1000) + "-" + System.currentTimeMillis() + ".png";
            String photo_dir = AbFileUtil.getImageDownloadDir(this);
			currentPhotoFile = new File(photo_dir, fileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(AddPhotoActivity.this,"未找到系统相机程序");
		}
	}

	/**
	 * 因为调用了Camera和Gally所以要判断他们各自的返回情况,
	 * 他们启动时是这样的startActivityForResult
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode != RESULT_OK){
			return;
		}
		String currentFilePath = null;
		switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA:
				Uri uri = intent.getData();
				currentFilePath = getPath(uri);

				AbLogUtil.d(this, "从相册获取的图片的路径是 = " + currentFilePath);
				photoListString.add(currentFilePath);
				imageListAdapter.addItem(imageListAdapter.getCount()-1,new ImageUploadInfo(currentFilePath));

                /*if(!AbStrUtil.isEmpty(currentFilePath)){
                    Intent intent1 = new Intent(activity, CropImageActivity.class);
                    intent1.putExtra("PATH", currentFilePath);
                    startActivityForResult(intent1, CAMERA_CROP_DATA);
                }else{
                    AbToastUtil.showToast(activity,"未在存储卡中找到这个文件");
                }*/
				break;
			case CAMERA_WITH_DATA:
				AbLogUtil.d(this, "从拍照获取的图片的路径是 = " + currentPhotoFile.getPath());
				currentFilePath = currentPhotoFile.getPath();
				photoListString.add(currentFilePath);
				imageListAdapter.addItem(imageListAdapter.getCount()-1,new ImageUploadInfo(currentFilePath));

                /*if(!AbStrUtil.isEmpty(currentFilePath)){
                    Intent intent2 = new Intent(activity, CropImageActivity.class);
                    intent2.putExtra("PATH",currentFilePath);
                    startActivityForResult(intent2,CAMERA_CROP_DATA);
                }else{
                    AbToastUtil.showToast(activity,"未在存储卡中找到这个文件");
                }*/

				break;
			case CAMERA_CROP_DATA:
				String path = intent.getStringExtra("PATH");
				AbLogUtil.e(this, "裁剪后得到的图片的路径是 = " + path);
				imageListAdapter.addItem(imageListAdapter.getCount()-1,new ImageUploadInfo(path));
				break;
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if(AbStrUtil.isEmpty(uri.getAuthority())){
			return null;
		}

        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
	}

	public void uploadFile(){
		//
	}

}
