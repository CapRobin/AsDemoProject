package com.andbase.library.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;


/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/16 13:27
 * Email 396196516@qq.com
 * Info 进度框fragment
 */
public class AbProgressDialogFragment extends DialogFragment {


	/**
	 * 创建一个新的AbProgressDialogFragment.
	 * @param indeterminateDrawable  进度图片
	 * @param message  消息提示
     * @return
     */
	public static AbProgressDialogFragment newInstance(int indeterminateDrawable,String message) {
		AbProgressDialogFragment f = new AbProgressDialogFragment();
		Bundle args = new Bundle();
		args.putInt("indeterminateDrawable", indeterminateDrawable);
		args.putString("message", message);
		f.setArguments(args);

		return f;
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int indeterminateDrawable = getArguments().getInt("indeterminateDrawable");
		String message = getArguments().getString("message");
		
		ProgressDialog progressDialog = new ProgressDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		if(indeterminateDrawable > 0){
			progressDialog.setIndeterminateDrawable(getActivity().getResources().getDrawable(indeterminateDrawable));
		}
		
		if(message != null){
			progressDialog.setMessage(message);
		}
		
	    return progressDialog;
	}
	
}
