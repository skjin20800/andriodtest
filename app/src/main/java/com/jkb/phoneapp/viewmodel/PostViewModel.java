package com.jkb.phoneapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jkb.phoneapp.Phone;

import java.util.ArrayList;
import java.util.List;


public class PostViewModel extends ViewModel {

    private static final String TAG = "PostViewModel";
    // LiveData 변경법
    // 1. 서버의 DB에 Data 변경요청, 2. 변경된 데이터 다운, 3. UI 적용
    // MutableLiveData 변경법
    // 1. 데이터 변경, 2 UI 적용  -> 서버의 DB의 Data값은 변동없다
    // 공통점 : 데이터변경시 UI 자동변경


    private MutableLiveData<List<Phone>> mtPhone = new MutableLiveData<>();

    public MutableLiveData<List<Phone>> 구독(){
        return mtPhone;
    }


    public void 연락처리스트넣기(List<Phone> phones){
        mtPhone.setValue(phones);
    }

    public void 연락처넣기(Phone phone){
        List<Phone> phones = new ArrayList<>();
        phones = mtPhone.getValue();
        phones.add(phone);
        mtPhone.setValue(phones);
    }

    public List<Phone> 연락처리스트출력(){
        return mtPhone.getValue();
    }

    public Long 연락처ID(){

        int size = mtPhone.getValue().size();

        Long phoneId = mtPhone.getValue().get(size-1).getId();
        Log.d(TAG, "연락처ID: "+(phoneId+1));
        return phoneId+1;
    }

    public void 데이터초기화(){
        List<Phone> phones = new ArrayList<>();
        mtPhone.setValue(phones);
    }
}
