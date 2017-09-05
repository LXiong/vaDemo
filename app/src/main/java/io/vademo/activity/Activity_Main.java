package io.vademo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.InstallResult;

import java.io.IOException;

import io.vademo.R;

public class Activity_Main extends Activity { //717219917@qq.com
     EditText path,pkgName;
     Button install,launch,uninstall;
     TextView hello;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path=(EditText)findViewById(R.id.path);
        pkgName=(EditText)findViewById(R.id.pkgName);
        install=(Button) findViewById(R.id.install);
        uninstall=(Button) findViewById(R.id.uninstall);
        launch=(Button)findViewById(R.id.launch);
        hello=(TextView) findViewById(R.id.hello);

        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(path.getText().toString())){
                    Toast.makeText(Activity_Main.this,"path can't empty",Toast.LENGTH_SHORT).show();
                  return;
                }
                install(path.getText().toString());
            }
        });
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(pkgName.getText().toString())){
                    Toast.makeText(Activity_Main.this,"pkgName can't empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                int result =launch(pkgName.getText().toString());
                Toast.makeText(Activity_Main.this,"launch result:"+result,Toast.LENGTH_SHORT).show();

            }
        });
        uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(pkgName.getText().toString())){
                    Toast.makeText(Activity_Main.this,"pkgName can't empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                uninstall(pkgName.getText().toString());
            }
        });

    }

   private  void install(String path){
//    VirtualCore.get().installPackage(path, InstallStrategy.COMPARE_VERSION);
       InstallResult res = VirtualCore.get().installPackage(path, InstallStrategy.UPDATE_IF_EXIST);
       if (res.isSuccess) {
           try { VirtualCore.get().preOpt(res.packageName); } catch (IOException e) { e.printStackTrace(); }
           if (res.isUpdate) {
               Toast.makeText(Activity_Main.this, "Update: " + res.packageName + " success!", Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(Activity_Main.this, "Install: " + res.packageName + " success!", Toast.LENGTH_SHORT).show();
           }

           if (TextUtils.isEmpty(pkgName.getText().toString())){
               Toast.makeText(Activity_Main.this,"pkgName can't empty",Toast.LENGTH_SHORT).show();
               return;
           }
           Toast.makeText(Activity_Main.this,"launch result:"+launch(pkgName.getText().toString()),Toast.LENGTH_SHORT).show();
       } else {
           Toast.makeText(Activity_Main.this, "Install failed: " + res.error, Toast.LENGTH_SHORT).show();
       }


   }

   private int launch(String pkgName){
     if (VirtualCore.get().isAppInstalled(pkgName)){
         Intent intent = VirtualCore.get().getLaunchIntent(pkgName, 0);
         return VActivityManager.get().startActivity(intent, 0);
     }else{
         return -1;
     }
   }

   private void uninstall(String pkgName){
     boolean result=VirtualCore.get().uninstallPackage(pkgName);
       if(result){
           Toast.makeText(Activity_Main.this,"uninstall "+pkgName+"success!",Toast.LENGTH_SHORT).show();
       }else {
           Toast.makeText(Activity_Main.this,"uninstall "+pkgName+"failed!",Toast.LENGTH_SHORT).show();
       }

   }





}
