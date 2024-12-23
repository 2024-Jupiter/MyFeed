package com.myfeed.model.board;

public enum Tag {
    POLITICS("정치"),
    ENTERTAINMENT("연예"),
    SPORTS("스포츠"),
    TECHNOLOGY("기술"),
    ECONOMY("경제"),
    SOCIETY("사회"),
    CULTURE("문화"),
    SCIENCE("과학"),
    ENVIRONMENT("환경"),
    HEALTH("건강"),
    EDUCATION("교육"),
    HISTORY("역사"),
    MUSIC("음악"),
    TRAVEL("여행"),
    GAME("게임"),
    FASHION("패션");

    private final String koreanName;

    Tag(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}