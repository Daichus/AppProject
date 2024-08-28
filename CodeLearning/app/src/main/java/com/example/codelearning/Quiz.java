package com.example.codelearning;

//管理自定義AdaptorView中View需用資料的類別
public class Quiz {
    private String quizContent;
    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private String answer;

    public Quiz(String quizContent, String opt1, String opt21, String opt3, String opt4, String answer) {
        this.quizContent = quizContent;
        this.opt1 = opt1;
        this.opt2 = opt21;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.answer = answer;
    }

    public String getQuizContent() {
        return quizContent;
    }

    public void setQuizContent(String quizContent) {
        this.quizContent = quizContent;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt21) {
        this.opt2 = opt21;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public void setOpt4(String opt4) {
        this.opt4 = opt4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
