package com.example.dialogspinner;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private Button btn;
	private List<String> list = new ArrayList<String>();
	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		list = initData();

		btn = (Button) findViewById(R.id.btn_list);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowDialog();
			}
		});
	}
	private ArrayList<String> initData() {
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0;i < 10;i++){
			String name = "Item"+i;
			list.add(name);
		}
		return list;
	}
	public void ShowDialog() {
		Context context = MainActivity.this;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.formcommonlist, null);
		ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);
		MyAdapter adapter = new MyAdapter(context, list);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
				//在这里面就是执行点击后要进行的操作,这里只是做一个显示
				Toast.makeText(MainActivity.this, "您点击的是"+list.get(positon).toString(), Toast.LENGTH_SHORT).show();
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
			}
		});
		builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.show();
	}

	class MyAdapter extends BaseAdapter {
		private List<String> mlist;
		private Context mContext;

		public MyAdapter(Context context, List<String> list) {
			this.mContext = context;
			mlist = new ArrayList<String>();
			this.mlist = list;
		}

		@Override
		public int getCount() {
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {

			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Person person = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				convertView = inflater.inflate(R.layout.rtu_item,null);
				person = new Person();
				person.name = (TextView)convertView.findViewById(R.id.tv_name);
				convertView.setTag(person);
			}else{
				person = (Person)convertView.getTag();
			}
			person.name.setText(list.get(position).toString());
			return convertView;
		}
		class Person{
			TextView name;
		}
	}
}
