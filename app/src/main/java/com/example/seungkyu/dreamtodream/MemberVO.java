package com.example.seungkyu.dreamtodream;

/**
 * Created by seungkyu on 2016-10-04.
 */

public class MemberVO {
    private String m_email;
    private String m_password;
    private String m_name;
    private String m_gender;
    private String m_phoneNumber;
    private String m_birthday;

    public MemberVO(String m_email, String m_password, String m_name, String m_gender, String m_phoneNumber, String m_birthday) {
        this.m_email = m_email;
        this.m_password = m_password;
        this.m_name = m_name;
        this.m_gender = m_gender;
        this.m_phoneNumber = m_phoneNumber;
        this.m_birthday = m_birthday;
    }

    public String getM_email() {
        return m_email;
    }

    public void setM_email(String m_email) {
        this.m_email = m_email;
    }

    public String getM_password() {
        return m_password;
    }

    public void setM_password(String m_password) {
        this.m_password = m_password;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_gender() {
        return m_gender;
    }

    public void setM_gender(String m_gender) {
        this.m_gender = m_gender;
    }

    public String getM_phoneNumber() {
        return m_phoneNumber;
    }

    public void setM_phoneNumber(String m_phoneNumber) {
        this.m_phoneNumber = m_phoneNumber;
    }

    public String getM_birthday() {
        return m_birthday;
    }

    public void setM_birthday(String m_birthday) {
        this.m_birthday = m_birthday;
    }

    @Override
    public String toString() {
        return "MemberVO{" +
                "m_email='" + m_email + '\'' +
                ", m_password='" + m_password + '\'' +
                ", m_name='" + m_name + '\'' +
                ", m_gender='" + m_gender + '\'' +
                ", m_phoneNumber='" + m_phoneNumber + '\'' +
                ", m_birthday='" + m_birthday + '\'' +
                '}';
    }
}
