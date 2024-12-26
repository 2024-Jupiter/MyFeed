package com.myfeed.model.report;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportType {
    SPAM("스팸"),
    ABUSE("욕설 또는 비방"),
    INAPPROPRIATE("부적절한 콘텐츠"),
    FRAUD("사기 또는 피싱"),
    COPYRIGHT("저작권 침해"),
    PRIVACY_VIOLATION("개인정보 침해"),
    SEXUAL_CONTENT("성적 콘텐츠"),
    VIOLENT_CONTENT("폭력적인 콘텐츠"),
    OTHER("기타");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static ReportType from(String value) {
        for (ReportType type : values()) {
            if (type.name().equalsIgnoreCase(value) || type.description.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ReportType: " + value);
    }
}