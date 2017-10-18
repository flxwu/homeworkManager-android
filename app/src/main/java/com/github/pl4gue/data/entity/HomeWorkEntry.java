package com.github.pl4gue.data.entity;

/**
 * @author David Wu (david10608@gmail.com)
 *         Created on 14.10.17.
 */

public class HomeWorkEntry {

    private String homeworkSubject;
    private String homework;
    private String homeworkEntryDate, homeworkDueDate;
    private String homeworkComments;

    public String getHomeworkSubject() {
        return homeworkSubject;
    }

    public void setHomeworkSubject(String homeworkSubject) {
        this.homeworkSubject = homeworkSubject;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public String getHomeworkEntryDate() {
        return homeworkEntryDate;
    }

    public void setHomeworkEntryDate(String homeworkEntryDate) {
        this.homeworkEntryDate = homeworkEntryDate;
    }

    public String getHomeworkDueDate() {
        return homeworkDueDate;
    }

    public void setHomeworkDueDate(String homeworkDueDate) {
        this.homeworkDueDate = homeworkDueDate;
    }

    public String getHomeworkComments() {
        return homeworkComments;
    }

    public void setHomeworkComments(String homeworkComments) {
        this.homeworkComments = homeworkComments;
    }
}
