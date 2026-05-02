package com.celauro.SpendWise.utils;

public enum TransactionType {
    EXPENSE,
    INCOME;

    public static boolean findByName(String dtoType){
        for(TransactionType type : values()){
            if(type.name().equalsIgnoreCase(dtoType)){
                return true;
            }
        }
        return false;
    }
}
