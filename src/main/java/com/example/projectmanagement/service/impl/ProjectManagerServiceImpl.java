package com.example.projectmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.projectmanagement.entity.Menu;
import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.mapper.ProjectMapper;
import com.example.projectmanagement.service.ProjectManagerService;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectManagerServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectManagerService {
    private static String realPath;
    private static String sign = "\\";
    static {
            String path = System.getProperty("user.dir");
            int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
            int lastIndex = path.lastIndexOf(File.separator) + 1;

            realPath = path.substring(firstIndex, lastIndex)+"project"+ sign;
    }

    @Override
    public List<Menu> findProjectMenu(String projectNameEn) {
        if (!new File(realPath+projectNameEn).exists()) {
            return null;
        }
        List<Menu> menus = new ArrayList<>();
        getMenu(realPath+projectNameEn,menus);
        return menus;
    }

    @Override
    public String getCode(String path) throws Exception {
        File codeFile = new File(realPath + path);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(codeFile),"UTF-8"));

        StringBuilder sb = new StringBuilder();
        String s;
        while((s=bufferedReader.readLine()) != null){
            sb.append(s).append("\r\n");
        }
        bufferedReader.close();
        return sb.toString();
    }

    @Override
    public String editCode(String path, String code) throws Exception {
        File codeFile = new File(realPath + path);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(codeFile));
        bufferedWriter.write(code);
        bufferedWriter.flush();
        bufferedWriter.close();
        return null;
    }

    @Override
    public String addFile(String fileName, String path, String status) throws IOException {
        File codeFile = new File(realPath + path+sign+fileName);
        if (!codeFile.exists()){
            if(status.equals("1")){
                codeFile.mkdirs();
            }else{
                codeFile.createNewFile();
            }
        }
        return null;
    }

    @Override
    public String deleteFile(String currentPath) {
        File codeFile = new File(realPath + currentPath);
        if (codeFile.exists()){
            deletes(realPath + currentPath);
        }
        return null;
    }
    public void deletes(String fileName){
        File file = new File(fileName);
        File[] files = file.listFiles();
        // 如果目录为空，直接退出
        if (files != null && files.length>0) {
            // 遍历，删除目录下的所有文件
            for (File f : files) {
                if (f.isDirectory()) {
                    deletes(f.getAbsolutePath());
                }
                f.delete();
            }
        }
        file.delete();
    }
    public void getMenu(String fileName,List<Menu> menus) {
        File file = new File(fileName);
        File[] files = file.listFiles();
        // 如果目录为空，直接退出
        if (files != null) {
            // 遍历，目录下的所有文件
            for (File f : files) {
                Menu menu = new Menu();
                menu.setText(f.getName());
                menus.add(menu);
                if (f.isDirectory()) {
                    menu.setChildren(new ArrayList<>());
                    getMenu(f.getAbsolutePath(),menu.getChildren());
                }else{
                    menu.setIcon("none");
                }
            }
        }
    }
}
