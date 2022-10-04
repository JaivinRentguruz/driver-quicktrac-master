package com.evolution.quicktrack.questionslist;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.qustionlist.QuestionListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsListPresenter implements QuestionsListContractor.Presenter, BaseContractor {

    private QuestionsListContractor.View view;

    public QuestionsListPresenter(QuestionsListContractor.View view) {
        this.view = view;
    }

    @Override
    public void checkList(QuestionsListRequestEntity questionsListRequestEntity) {
        Call<QuestionListResponse> call = apiService.getQuestion(questionsListRequestEntity.getLogin_token(), questionsListRequestEntity.getApikey(), questionsListRequestEntity.getTruckid(), questionsListRequestEntity.getDriverid());
        call.enqueue(new Callback<QuestionListResponse>() {
            @Override
            public void onResponse(Call<QuestionListResponse> call, Response<QuestionListResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    view.onSuccessCheckList(response.body());
                } else {
                    view.onErrorCheckList(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<QuestionListResponse> call, Throwable t) {
                view.onErrorCheckList(t.toString());
            }
        });
    }

    @Override
    public void submitQuestionary(QuestionListSubmitRequestEntity questionListSubmitRequestEntity) {
        Call<CommonResponse> call = apiService.uploadData3(questionListSubmitRequestEntity.getLogin_token(), questionListSubmitRequestEntity.getData(), questionListSubmitRequestEntity.getDriverid());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful() && response.body().getStatus().equals(Constants.API_STATUS)) {

                    view.onSuccessSubmitQuest(response.body());

                } else {
                        view.onErrorSubmitQuest(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                view.onErrorSubmitQuest(t.toString());
            }
        });
    }
}
