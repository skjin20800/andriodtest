package com.jkb.phoneapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//3번 상속받기
public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.MyViewHolder>{

    private static final String TAG = "PhoneAdapter";
    private Call<Void> deletePost_call;
    private Call<Phone> updatePost;
    private com.jkb.phoneapp.PhoneService phoneService = com.jkb.phoneapp.PhoneService.retrofit.create(com.jkb.phoneapp.PhoneService.class);
    private Dialog diaUpdate;
    private EditText tvDiaId;
    private EditText tvDiaName, tvDiaTel;
    private Button btnInsert, btnClose;


    // 4번 컬렉션 생성
    private List<Phone> phones;

    public PhoneAdapter(List<Phone> phones) {
        this.phones = phones;
    }

    // 5번 addItem.removeItem

    public void changeItem(List<Phone> phones){
        this.phones = phones;
    }


    public void addItem(Phone phone){
        phones.add(phone);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        phones.remove(position);
        notifyDataSetChanged();
    }

    public void setDiaAdd(Dialog dialog){
        diaUpdate = new Dialog(dialog.getContext());
        diaUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        diaUpdate.setContentView(R.layout.update_item);


    }

    public void removeItem2(int position){

        deletePost_call = phoneService.deletePost(position);

        deletePost_call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Log.d(TAG, "onResponse: 삭제실행");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onResponse: 삭제실패");
            }
        });
    }

    public void add_item() {

        Phone phone = new Phone();
        phone.setName(tvDiaName.getText().toString());
        phone.setTel(tvDiaTel.getText().toString());
//        Log.d(TAG, "add_item: 테스트"+ tvDiaId.getText());
//        Long LtvId = Long.parseLong(tvDiaId.getText().toString());
        phone.setId(21L);

        updatePost = phoneService.insertPost(phone);

        updatePost.enqueue(new Callback<Phone>() {
            @Override
            public void onResponse(Call<Phone> call, Response<Phone> response) {


                diaUpdate.dismiss();

//                Phone phone1 = new Phone();
//                phone1.setName(phone.getName());
//                phone1.setTel(phone.getTel());
//                phone1.setId(postViewModel.연락처ID());
//                postViewModel.연락처넣기(phone1);
//                Log.d(TAG, "onResponse: 등록성공"+phone1);
//                phoneAdapter.changeItem(postViewModel.연락처리스트출력());

            }

            @Override
            public void onFailure(Call<Phone> call, Throwable t) {
                Log.d(TAG, "onResponse: 등록실패");
                diaUpdate.dismiss();
            }
        });


    }


    //7번 getView랑 똑같음.
    // 차이점이 있다면 listView는 화면에 3개가 필요하면 최초 로딩시에 3개를 그려야하니깐 getView가 3번 호출됨
    // 그다음에 스크롤을 해서 2개가 추가되야 될때, 다시 getView를 호출함
    // 하지만 recyclerView는 스크롤을 해서 2개가 추가되야 될때 onBindViewHolder를 호출함.
    // onCreateViewHolder는 해당 Activity 실행시에만 호출 됨.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: ");

        // 객체생성
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate(해당리소스를, 사이클러뷰 안에, 아직 붙이지않는다) 리턴시에 연결함
        View view = inflater.inflate(R.layout.phone_item, parent, false);

        return new MyViewHolder(view);//view가 리스트뷰에 하나 그려짐
    }

    // 데이터가 체인지됨
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        holder.setItem(phones.get(position));

    }

    // 6번 컬렉션 크기 알려주기 (화면에 몇개 그려야될지를 알아야 하기 때문)
    @Override
    public int getItemCount() {
        return phones.size();
    }

    // 1번 ViewHolder 만들기
    // ViewHolder란 하나의 View를 가지고 있는 Holder이다.
    public class MyViewHolder extends RecyclerView.ViewHolder {

        // 2번 user_item이 가지고 있는 위젯들을 선언
        private TextView tvTel, tvUsername, tvId, tvDiaId;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTel = itemView.findViewById(R.id.tel);
            tvUsername = itemView.findViewById(R.id.name);
            tvId = itemView.findViewById(R.id.tv_id);

            tvDiaId = diaUpdate.findViewById(R.id.tv_id);
            tvDiaName = diaUpdate.findViewById(R.id.et_name);
            tvDiaTel = diaUpdate.findViewById(R.id.et_tel);
            btnInsert = diaUpdate.findViewById(R.id.btn_insert);
            btnClose = diaUpdate.findViewById(R.id.btn_close);


            itemView.setOnLongClickListener(v -> {

                    Log.d(TAG, "MyViewHolder: 클릭됨"+itemView);
                    Integer tvId1 = Integer.parseInt((String) tvId.getText());


                removeItem2(tvId1);
                Log.d(TAG, "MyViewHolder: tvID "+tvId1);
                removeItem(getAdapterPosition());
                Log.d(TAG, "MyViewHolder: 어댑텅 "+getAdapterPosition());

                return false;
                    }
            );

            itemView.setOnClickListener(v -> {

                tvDiaTel.setText(tvTel.getText());
                tvDiaName.setText(tvUsername.getText());
                tvDiaId.setText(tvId.getText());

                diaUpdate.show();


            });

            btnInsert.setOnClickListener(v -> {
                add_item();
            });

        }

        public void setItem(Phone phone){
            tvTel.setText(phone.getTel());
            tvUsername.setText(phone.getName());
            tvId.setText(phone.getId().toString());

        }

    }
}
