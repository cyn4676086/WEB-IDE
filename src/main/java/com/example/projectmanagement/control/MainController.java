package com.example.projectmanagement.control;

import cn.hutool.core.util.ZipUtil;
import com.example.projectmanagement.entity.Project;
import com.example.projectmanagement.entity.User;
import com.example.projectmanagement.service.ProjectService;
import com.example.projectmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;


@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;
    private static String realPath;
    private static String sign = "\\";
    static {
        String path = System.getProperty("user.dir");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;

        realPath = path.substring(firstIndex, lastIndex)+"project"+ sign;
    }

    @GetMapping("/toProjects/{id}")
    public String toMain(@PathVariable("id") Integer id, Model model) {
        User user = userService.getById(id);
        List<User> userlist = userService.list();
        List<Project> projects = projectService.list();
        Iterator<Project> it=projects.iterator();

        while (it.hasNext()){
            Project p= it.next();
            String usernames = p.getUsernames();
            String userids="-"+p.getUserids()+"-";
            if(!(userids.contains("-"+user.getId().toString()+"-"))){
                it.remove();
            }
            if (usernames != null && usernames.trim().length() > 0) {
                p.setUsernames(usernames.replaceAll("-", " "));
            }

        }
        //System.out.println(projects);
        model.addAttribute("user", user);
        model.addAttribute("list", userlist);
        model.addAttribute("projects", projects);
        return "IDE_projects";
    }

    @GetMapping("/toProjectdetail/{id}/{proId}/{projectNameEn}")
    public String toProjectdetail(@PathVariable("id") Integer id, @PathVariable("proId") Integer proId,@PathVariable("projectNameEn") String projectNameEn, Model model) {
        User user = userService.getById(id);
        Project pro = projectService.getById(proId);

        pro.setUsernames(pro.getUsernames().replaceAll("-", " "));
        pro.setXmbq(pro.getXmbq().replaceAll("-", " "));

        model.addAttribute("user", user);
        model.addAttribute("pro", pro);
        model.addAttribute("projectNameEn",projectNameEn);
        return "IDE_projectdetail";
    }

    @GetMapping("/toWebIDE/{id}/{proId}/{projectNameEn}")
    public String toWebIDE(@PathVariable("id") Integer id, @PathVariable("proId") Integer proId,@PathVariable("projectNameEn") String projectNameEn, Model model) {
        User user = userService.getById(id);
        Project pro = projectService.getById(proId);
        model.addAttribute("user", user);
        model.addAttribute("pro", pro);
        model.addAttribute("projectNameEn",projectNameEn);
        return "IDE_projecteditor";
    }

    @PostMapping("/saveProject")
    @ResponseBody
    public Map<String, String> saveProject(MultipartFile file,Project project) throws IOException {
        Map<String, String> resutl = new HashMap<>();
        String originalFilename = file.getOriginalFilename();
        File newFile = new File(realPath + originalFilename);
        String fileName = originalFilename.substring(0, originalFilename.indexOf("."));
        if (newFile.exists()){
            resutl.put("msg", "项目已存在");
            resutl.put("code", "500");
            return resutl;
        }else{
            file.transferTo(newFile);
            ZipUtil.unzip(realPath + originalFilename, realPath+fileName+sign);
            newFile.delete();
        }
        project.setProjectNameEn(fileName);
        project.setCreateby(userService.getById(project.getUserid()).getUsername());
        project.setCreated(new Date());
        project.setLastupdated(new Date());
        projectService.save(project);
        resutl.put("msg", "");
        resutl.put("code", "200");
        return resutl;
    }

    @GetMapping("/deletepro/{proId}/{id}")
    public String deletepro(@PathVariable("proId") Integer proId, @PathVariable("id") Integer id) {
        projectService.removeById(proId);
        return "redirect:/toProjects/" + id;
    }
}
