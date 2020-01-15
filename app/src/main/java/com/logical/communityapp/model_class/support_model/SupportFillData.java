package com.logical.communityapp.model_class.support_model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.logical.communityapp.BR;

public class SupportFillData extends BaseObservable {
    private String title_query;
    private String query_desc;
     Context context;

    public SupportFillData(Context context) {
        this.context = context;
    }

    public final ObservableField<String> errorPassword = new ObservableField<>();
    public final ObservableField<String> errorEmail = new ObservableField<>();

    private MutableLiveData<SupportFillData> userMutableLiveData;

    LiveData<SupportFillData> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }


    @Bindable
    public String getTitle_query() {
        if (title_query == null) {
            return "";
        }
        return title_query;
    }

    public void setTitle_query( String title_query) {
        this.title_query = title_query;
        notifyPropertyChanged(BR.supportdata);
    }

    @Bindable({"title_query"})
    public String getTitleError() {
        if (getTitle_query().isEmpty()) {
            return "Please enter title";
        }
        return "";
    }

    @Bindable
    public String getQuery_desc() {
        if (query_desc == null) {
            return "";
        }
        return query_desc;
    }

    public void setQuery_desc(String query_desc) {
        this.query_desc = query_desc;
        notifyPropertyChanged(BR.supportdata);
    }

    @Bindable({"query_desc"})
    public String getDescError() {
        if (getQuery_desc().isEmpty()) {
            return "Please enter description";
        } else {
            return "";
        }
    }
}
