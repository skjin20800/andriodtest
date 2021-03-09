package com.jkb.phoneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jkb.phoneapp.viewmodel.PostViewModel;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    private com.jkb.phoneapp.PhoneService phoneService;
    private Call<CMRespDto<List<Phone>>> call;
    private Call<Phone> insert_call;

    private RecyclerView rvPhoneList;
    private PhoneAdapter phoneAdapter;
    private List<Phone> phones = new ArrayList<>();
    private FloatingActionButton fabtn;
    private Dialog diaAdd;
    private PostViewModel postViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phones.add(new Phone(3L, "aa", "bb"));

        init();
        phoneView();
        adapter();
        download();
        btn_add();




    }

    public void init() {
        phoneService = com.jkb.phoneapp.PhoneService.retrofit.create(com.jkb.phoneapp.PhoneService.class);
        diaAdd = new Dialog(MainActivity.this);
        fabtn = findViewById(R.id.fa_btn);
        diaAdd.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        diaAdd.setContentView(R.layout.add_item);


    }

    public void download() {
        call = phoneService.findAll();
        call.enqueue(new Callback<CMRespDto<List<Phone>>>() {
            @Override
            public void onResponse(Call<CMRespDto<List<Phone>>> call, Response<CMRespDto<List<Phone>>> response) {
                CMRespDto<List<Phone>> cmRespDto = response.body();
                phones = cmRespDto.getData();
                // 어댑터에게 넘기기
                Log.d(TAG, "onResponse: callback data: " + phones);


                postViewModel.연락처리스트넣기(phones);
                phoneAdapter.changeItem(postViewModel.연락처리스트출력());
//                phoneAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CMRespDto<List<Phone>>> call, Throwable t) {
                Log.d(TAG, "onFailure: findAll() 실패" + t.getMessage());
            }
        });

    }

    public void adapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvPhoneList = findViewById(R.id.rv_phone_list);
        rvPhoneList.setLayoutManager(manager);


        phoneAdapter = new PhoneAdapter(phones);
        rvPhoneList.setAdapter(phoneAdapter);
        phoneAdapter.setDiaAdd(diaAdd);
    }

    public void btn_add() {
        fabtn.setOnClickListener(v -> {

            diaAdd.show();

            Button closebtn = diaAdd.findViewById(R.id.btn_close);
            closebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 원하는 기능 구현
                    diaAdd.dismiss(); // 다이얼로그 닫기
                }
            });
            // 네 버튼
            Button insertbtn = diaAdd.findViewById(R.id.btn_insert);
            insertbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 원하는 기능 구현
                    add_item();
                }
            });

        });


    }
        public void add_item() {
            EditText etname = diaAdd.findViewById(R.id.et_name);
            EditText etemail = diaAdd.findViewById(R.id.et_email);

        Phone phone = new Phone();
            phone.setName(etname.getText().toString());
            phone.setTel(etemail.getText().toString());



            insert_call = phoneService.insertPost(phone);


            insert_call.enqueue(new Callback<Phone>() {
                @Override
                public void onResponse(Call<Phone> call, Response<Phone> response) {


                    diaAdd.dismiss();

                    Phone phone1 = new Phone();
                    phone1.setName(phone.getName());
                    phone1.setTel(phone.getTel());
                    phone1.setId(postViewModel.연락처ID());
                    postViewModel.연락처넣기(phone1);
                    Log.d(TAG, "onResponse: 등록성공"+phone1);
                    phoneAdapter.changeItem(postViewModel.연락처리스트출력());

                }

                @Override
                public void onFailure(Call<Phone> call, Throwable t) {
                    Log.d(TAG, "onResponse: 등록실패");
                    diaAdd.dismiss();
                }
            });


        }


public void phoneView(){
    postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
    postViewModel.데이터초기화();

    postViewModel.구독().observe(this, phones -> {
        phoneAdapter.notifyDataSetChanged();
        Log.d(TAG, "onCreate: 데이터 변경됨");
    });

}



}