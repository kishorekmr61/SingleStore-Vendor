package com.project.vendorsapp.Classes;

public class Staff {
    String Staffname;
    String Staffemail;
    String Staffmobileno;
    String Staffrole;
    String Staffaddress;
    String Staffreportingto;


    public Staff(String staffname, String staffemail, String staffmobileno, String staffrole, String staffaddress,String staffreportingto) {
        Staffname = staffname;
        Staffemail = staffemail;
        Staffmobileno = staffmobileno;
        Staffrole = staffrole;
        Staffaddress = staffaddress;
        Staffreportingto=staffreportingto;

    }


    public String getStaffname() {
        return Staffname;
    }

    public void setStaffname(String staffname) {
        Staffname = staffname;
    }

    public String getStaffemail() {
        return Staffemail;
    }

    public void setStaffemail(String staffemail) {
        Staffemail = staffemail;
    }

    public String getStaffmobileno() {
        return Staffmobileno;
    }

    public void setStaffmobileno(String staffmobileno) {
        Staffmobileno = staffmobileno;
    }

    public String getStaffrole() {
        return Staffrole;
    }

    public void setStaffrole(String staffrole) {
        Staffrole = staffrole;
    }

    public String getStaffaddress() {
        return Staffaddress;
    }

    public void setStaffaddress(String staffaddress) {
        Staffaddress = staffaddress;
    }

    public String getStaffreportingto() {
        return Staffreportingto;
    }

    public void setStaffreportingto(String staffreportingto) {
        Staffreportingto = staffreportingto;
    }



}
