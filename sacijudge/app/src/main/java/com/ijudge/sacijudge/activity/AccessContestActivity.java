package com.ijudge.sacijudge.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ijudge.sacijudge.mapmodels.JudgesMapModel;
import com.ijudge.sacijudge.R;
import com.ijudge.sacijudge.Utils;
import com.ijudge.sacijudge.models.JudgeModel;
import com.wang.avi.AVLoadingIndicatorView;


public class AccessContestActivity extends AppCompatActivity{
    private static final int ZXING_CAMERA_PERMISSION = 1;

    private Class<?> mClss = AccessContestActivity.class;
    AVLoadingIndicatorView avi;
    Button btnScanQRCode;
    Button btnIptAccesCode;
    TextView loadingMessage;
    EditText inptAccessCode;
    TextView modalLabel;
    ConstraintLayout container;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main2);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        container = (ConstraintLayout) findViewById(R.id.container);
        btnScanQRCode =(Button) findViewById(R.id.btnQrCode);
        btnIptAccesCode = (Button) findViewById(R.id.btnInputAccessCode);
        loadingMessage = (TextView) findViewById(R.id.loadingMessage);
        inptAccessCode = (EditText) findViewById(R.id.inptAccessCode);
        modalLabel = (TextView) findViewById(R.id.modalLabel);
        btnScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });
        btnIptAccesCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(AccessContestActivity.this);
              if (!inptAccessCode.getText().toString().trim().equals("") && inptAccessCode.getText().toString().trim() != null ){
                  startAnim();
                 try {
                     FirebaseDatabase.getInstance().getReference().child("routeJudges").child(inptAccessCode.getText().toString()).child("eventid").addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             try {
                                 final String eventID = dataSnapshot.getValue().toString();
                                 FirebaseDatabase.getInstance().getReference().child("judges").child(dataSnapshot.getValue().toString()).child(inptAccessCode.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         JudgeModel judgeModel = new JudgeModel();
                                         System.out.println(dataSnapshot.toString());
                                         JudgesMapModel judgesMapModel = dataSnapshot.getValue(JudgesMapModel.class);
                                         System.out.println(judgesMapModel.judgeName);
                                         judgeModal(judgesMapModel.judgeName,judgesMapModel.judgeDescription,eventID,judgesMapModel.judgeId);
                                         stopAnim();
                                     }
                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                     }
                                 });
                             }catch (NullPointerException e){
                                 System.out.println(e);
                                 stopAnim();
                                 Utils.popup(container,"Check Access Code");
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });
                 }catch (DatabaseException e){
                     System.out.println(e);
                     stopAnim();
                     Utils.popup(container,"check Access Code");
                 }
              }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                startAnim();
                Log.d("MainActivity", "Scanned");

                System.out.print(FirebaseDatabase.getInstance().getReference().child("judges"));
                FirebaseDatabase.getInstance().getReference().child("routeJudges").child(result.getContents()).child("eventid").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String eventID = dataSnapshot.getValue().toString();
                        FirebaseDatabase.getInstance().getReference().child("judges").child(dataSnapshot.getValue().toString()).child(result.getContents()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                JudgeModel judgeModel = new JudgeModel();
                                System.out.println(dataSnapshot.toString());
                                JudgesMapModel judgesMapModel = dataSnapshot.getValue(JudgesMapModel.class);
                                System.out.println(judgesMapModel.judgeName);
                                judgeModal(judgesMapModel.judgeName,judgesMapModel.judgeDescription,eventID,judgesMapModel.judgeId);
                                stopAnim();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private void startScanning(){
        IntentIntegrator integrator = new IntentIntegrator(AccessContestActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }
    void startAnim(){
        avi.show();
        loadingMessage.setVisibility(View.VISIBLE);
        // or avi.smoothToShow();

        //hide btn
        btnIptAccesCode.setVisibility(View.INVISIBLE);
        btnScanQRCode.setVisibility(View.INVISIBLE);
        inptAccessCode.setVisibility(View.INVISIBLE);
        modalLabel.setVisibility(View.INVISIBLE);
    }

    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
        btnIptAccesCode.setVisibility(View.VISIBLE);
        btnScanQRCode.setVisibility(View.VISIBLE);
        inptAccessCode.setVisibility(View.VISIBLE);
        modalLabel.setVisibility(View.VISIBLE);
    }

    private void judgeModal(String judgeName, String judgeDes, final String eventid, final String judgeID){
        final Dialog dialog = new Dialog(AccessContestActivity.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.welcom_modal);

        final TextView lblJudgeName = (TextView) dialog.findViewById(R.id.judgeName);
        final TextView lblJudgeDes = (TextView) dialog.findViewById(R.id.judgeDes);
        final Button btnInputAccessCode = (Button) dialog.findViewById(R.id.btnInputAccessCode);
        lblJudgeName.setText(judgeName);
        lblJudgeDes.setText(judgeDes);
        btnInputAccessCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AccessContestActivity.this,TabulationActivity.class);
                i.putExtra("eventId",eventid);
                i.putExtra("judgeID",judgeID);
                startActivity(i);
                finish();
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}



