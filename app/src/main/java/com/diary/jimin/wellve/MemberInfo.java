package com.diary.jimin.wellve;

public class MemberInfo {

    private String name;
    private String phone;
    private String address;
    private String date;

    public MemberInfo(String name, String phone, String address, String date) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.date = date;
    }


//    public MemberInfo(String nikname, String phone, String address, String date) {
//        this.name = name;
//        this.phone = phone;
//        this.address = address;
//        this.date = date;
//    }



    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
