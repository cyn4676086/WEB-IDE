package com.example.projectmanagement.service;

import com.example.projectmanagement.entity.Menu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ProjectManagerService {

    List<Menu> findProjectMenu(String projectNameEn);

    String getCode(String path) throws Exception;

    String editCode(String path, String code) throws Exception ;

    String addFile(String fileName, String path, String status) throws IOException;

    String deleteFile(String currentPath);
}
