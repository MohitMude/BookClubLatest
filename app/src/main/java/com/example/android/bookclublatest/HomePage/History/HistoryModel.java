package com.example.android.bookclublatest.HomePage.History;

public class HistoryModel
{
    private String bookname,isbn,issue_date,return_date,status,ism,url;

    public HistoryModel() {
    }

    public HistoryModel(String bookname, String isbn, String ism, String issue_date, String return_date, String status, String url) {
        this.bookname = bookname;
        this.isbn = isbn;
        this.issue_date = issue_date;
        this.return_date = return_date;
        this.status = status;
        this.ism = ism;
        this.url = url;
    }

    public String getIsmcode() {
        return ism;
    }

    public String getBookname() {
        return bookname;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public String getStatus() {
        return status;
    }

    public String getIsm() {
        return ism;
    }

    public String getUrl() {
        return url;
    }
}
