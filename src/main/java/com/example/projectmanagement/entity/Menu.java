package com.example.projectmanagement.entity;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private String text;
    private String icon;
    private List<Menu> children;
}
