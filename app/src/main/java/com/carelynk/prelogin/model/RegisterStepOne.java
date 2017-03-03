package com.carelynk.prelogin.model;

/**
 * Created by Admin on 27-Feb-17.
 */

public class RegisterStepOne {

    public String Email;
    public String PasswordHash;
    public int UserProfileId;
    public String AboutMe;
    public String FirstName;
    public String LastName;
    public String DateOfBirth;
    public int Gender;
    public double ContactNo;

    public RegisterStepOne(double contactNo, String email, String passwordHash, int userProfileId, String aboutMe, String firstName, String lastName, String dateOfBirth, int gender) {
        ContactNo = contactNo;
        Email = email;
        PasswordHash = passwordHash;
        UserProfileId = userProfileId;
        AboutMe = aboutMe;
        FirstName = firstName;
        LastName = lastName;
        DateOfBirth = dateOfBirth;
        Gender = gender;
    }

    @Override
    public String toString() {
        return "RegisterStepOne{" +
                "Email='" + Email + '\'' +
                ", PasswordHash='" + PasswordHash + '\'' +
                ", UserProfileId=" + UserProfileId +
                ", AboutMe='" + AboutMe + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", DateOfBirth='" + DateOfBirth + '\'' +
                ", Gender=" + Gender +
                ", ContactNo=" + ContactNo +
                '}';
    }
}
