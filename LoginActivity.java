package com.project.make;
/*
 * Login 부분
 * 로그인시 캐시저장으로 자동 Activity 전환
 * -Yun Joong Hyun
*/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.project.make.format.FormatActivity;
import com.project.make.http.Client_Jsp_id;
import com.project.make.http.Client_Jsp_mail;
import com.project.make.toaststyle.CommonToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.project.make.R;

@SuppressLint("SdCardPath")
public class LoginActivity extends Activity {
	
	Context mContext;
	RelativeLayout re;
	int cnt=0;
	String check="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);
		
		mContext = this;
		
		check=getIntent().getStringExtra("check");
		
		// 버튼을 뷰와 연결한다.
		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		Button button3 = (Button) findViewById(R.id.button3);
		Button button4 = (Button) findViewById(R.id.button4);
		// update 부분 
		if("update".equals(check)){
			button4.setVisibility(View.VISIBLE);
			button4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					Uri u = Uri.parse("http://localhost:8080/web/application/Mobile/Apk/Album.apk");
					i.setData(u);
					startActivity(i);
				}
			});
		}else{
			button4.setVisibility(View.INVISIBLE); //공간차지x
		}
			
		// 버튼에 클릭 리스너를 등록.
		// 등록된 클릭 리스너는 버튼에 클릭 이벤트가 발생했을때 처리.
	 final CommonToast toast = new CommonToast(mContext);
	 button1.setOnClickListener(new OnClickListener()
		{
		//클릭 이벤트 받는곳
		public void onClick(View v)
		{
			//무엇을 할지 정의 인텐트 생성
				EditText id_edit=(EditText) findViewById(R.id.idText);
				
				String str = id_edit.getText().toString();
				
				if(str.isEmpty()){
				
			try {
				Client_Jsp_id load = new Client_Jsp_id();
				load.Setting("http://localhost:8080/web/application/Mobile/mobile_select.jsp", str); //test myPC
				load.start();
				load.join();
				str = load.getResult();
				str = str.trim();
				
				toast.showToast(str, Toast.LENGTH_SHORT);
				//파일 생성
				if(str!=null){
					toast.showToast("인증성공",Toast.LENGTH_SHORT);
					writeFile(str);	
				}else{
					toast.showToast("인증실패",Toast.LENGTH_SHORT);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
				Intent intent = new Intent(LoginActivity.this, FormatActivity.class);
			// 인텐트에 있는 정보대로 액티비티를 시작한다. 
				intent.putExtra("Name", str); // 앞에 URL은 구분하기위한 변수명, 뒤에 인자는 실제 데이타 값
				startActivity(intent);
				finish();
			}else{
				toast.showToast("ID 입력하세요",Toast.LENGTH_SHORT);
			}
		}
	});
		button2.setOnClickListener(new OnClickListener() //login 없이 입장
		{
			//클릭 이벤트 받는곳
			public void onClick(View v)
			{
				
				//무엇을 할지 정의 인텐트 생성
				String t = "bin";
				Intent intent = new Intent(LoginActivity.this, FormatActivity.class);
				// 인텐트에 있는 정보대로 액티비티를 시작한다. 
				intent.putExtra("Name", t);
				startActivity(intent);
				finish();
			}
		});
		
		mContext = getApplicationContext();
		final AlertDialog.Builder join = new AlertDialog.Builder(this);
		final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
        		View layout = inflater.inflate(R.layout.join_dialog,
	                        (ViewGroup) findViewById(R.id.layout_root));
        		final EditText mail_text=(EditText) layout.findViewById(R.id.mail_text);
        		final EditText pwd_text=(EditText) layout.findViewById(R.id.pwd_text);
        		final EditText number_text=(EditText) layout.findViewById(R.id.number_text);
        		String number=givePhoneNumber();
        		number_text.setText(number);
				join.setIcon(R.drawable.ic_launcher1);
				join.setTitle("임시회원");
				join.setView(layout);
				
				join.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener(){
	                	@Override
						public void onClick(DialogInterface dialog, int which) {
	                		
	                		String mail = mail_text.getText().toString();
	                		String pwd = pwd_text.getText().toString();
	                		String number= number_text.getText().toString();
	                		String str="",t="";
		               
	                		if(mail.isEmpty()||pwd.isEmpty()){	
	                		
	                			toast.showToast("모두 입력해주세요.",Toast.LENGTH_SHORT);
		                	
	                		}else{
		                						
		                			try {
						       			Client_Jsp_mail load = new Client_Jsp_mail();
						       			load.Setting("http://loacalhost:8080/web/application/Mobile/enroll_insert.jsp",mail,number,pwd); //test myPC
						       			load.start();
						       			load.join();
						           		str = load.getResult();
						           		str = str.trim();
						            				
						            				
						            	} catch (Exception e) {
						            		e.printStackTrace();
						            		Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
						            	}
						                	
						              		if(str.equals("fail")){
						              			toast.showToast(str, Toast.LENGTH_LONG);
							                		
						               		}else{
						               			 t = "인증완료.";
								           		toast.showToast(t, Toast.LENGTH_LONG);
								           		NextPage(str);
								           		//NextCitation(pwd);
						               		}
						                    	
						               		dialog.dismiss();
		                	}
	                    	
	                    }
	                });
	                
				join.setNegativeButton(R.string.canceler, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
	        				String t = "감사합니다.";
	                		toast.showToast(t, Toast.LENGTH_SHORT);
							dialog.cancel();
							
						}
					});

				join.create();
				join.show(); 
				join.setCancelable(false);
			    				
			}
		});
		
	}
	

	private void writeFile(String str){
		
		FileOutputStream fos;
			 
			 String dirPath="/data/data/com.project.make/files";
			 File dir =new File(dirPath);
				 if(!dir.exists()){
					 dir.mkdirs();
				 }
			 String Path = "/data/data/com.project.make/files/cash.properties";	
			 File path = new File(Path);
		
			// String src = new String("admin");
			 try {
				path.createNewFile(); //생성
				
				fos= new FileOutputStream(path);//안에 다시 쓰기
				fos.write(str.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
//	public void NextCitation(String pwd){
//		
//		//인증코드 입력 Activity 구현
//		Intent i = new Intent(LoginActivity.this,CitationActivity.class);
//		i.putExtra("Pwd", pwd);
//		startActivity(i);
//		
//	}
	
  public void NextPage(String Name){
		
	  	writeFile(Name);
		//인증코드 입력 Activity 구현
		Intent i = new Intent(LoginActivity.this,FormatActivity.class);
		i.putExtra("Name", Name);
		startActivity(i);
		
	}
	
	private String givePhoneNumber(){
		
		TelephonyManager systemService = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	       
        String PhoneNumber = systemService.getLine1Number();             
		
		return PhoneNumber;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			
			if(cnt==1){
				moveTaskToBack(true);
				finish();
				System.exit(0);
				return true;
			}else{
				cnt++;
				Toast.makeText(getApplicationContext(), "빠르게 2번 클릭시 종료됩니다.", Toast.LENGTH_SHORT).show();
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
