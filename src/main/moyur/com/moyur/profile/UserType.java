package com.moyur.profile;

public enum UserType {
    NORMAL, INFLUENCER;
    
    public static UserType fromString(String userType) {
        for (UserType type : UserType.values()) {
            if (type.name().equalsIgnoreCase(userType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No UserType with text " + userType + " found");
    }
}