package com.example.projectmanagement.control;

import com.example.projectmanagement.entity.Menu;
import com.example.projectmanagement.service.ProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;


@Controller
@RequestMapping("/project")
public class ProjectManagerController {
    @Autowired
    private ProjectManagerService projectManagerService;

    @GetMapping("/test")
    @ResponseBody
    public String test() {

        String path = System.getProperty("user.dir");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        String sign = "\\";
        path = path.substring(firstIndex, lastIndex)+"project"+ sign;
        return null;
    }
    @GetMapping("/projectList")
    @ResponseBody
    public List<Menu> projectList(String projectNameEn) {
        return projectManagerService.findProjectMenu(projectNameEn);
    }

    @GetMapping("/getCode")
    @ResponseBody
    public String getCode(String path) throws Exception {
        return projectManagerService.getCode(path);
    }
    @PostMapping("/editCode")
    @ResponseBody
    public String editCode(String path,String code) throws Exception {
        return projectManagerService.editCode(path,code);
    }
    @PostMapping("/addFile")
    @ResponseBody
    public String status(String fileName,String parentFileName,String status) throws Exception {
        return projectManagerService.addFile(fileName,parentFileName,status);
    }
    @PostMapping("/deleteFile")
    @ResponseBody
    public String deleteFile(String currentPath) throws Exception {
        return projectManagerService.deleteFile(currentPath);
    }
}
