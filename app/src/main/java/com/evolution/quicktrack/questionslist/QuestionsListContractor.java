package com.evolution.quicktrack.questionslist;

import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.qustionlist.QuestionListResponse;

public interface QuestionsListContractor {
    interface View{
        void onSuccessCheckList(QuestionListResponse questionListResponse);
        void onErrorCheckList(String error);

        void onSuccessSubmitQuest(CommonResponse commonResponse);
        void onErrorSubmitQuest(String error);
    }
    interface Presenter{
        void checkList(QuestionsListRequestEntity questionsListRequestEntity);
        void submitQuestionary(QuestionListSubmitRequestEntity questionListSubmitRequestEntity);
    }
}
