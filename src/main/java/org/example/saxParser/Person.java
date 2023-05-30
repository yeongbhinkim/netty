package org.example.saxParser;

public class Person {
    private int age;
    private String name;
    private String gender;
    private String role;
    public Person() {
    };
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "이름:"+name+" 나이:"+age+" 성별:"+gender+" 직책:"+role+"\n";
    }
}
