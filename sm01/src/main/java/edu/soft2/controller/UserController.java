package edu.soft2.controller;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.soft2.pojo.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;

@Controller
@RequestMapping(value = "user")
public class UserController {
    //删除操作
    @RequestMapping(value = "/delete")
    public String delete(){
        System.out.println("----delete()----");
        return "hello";
    }

    //登录
    @RequestMapping(value = "login")
    public String login(User user, HttpSession session){
        System.out.println("----login()----");
        int flag = 1;//调用业务层,获取返回值
        if (flag == 1) {
            session.setAttribute("user",user);
            return "welcome";
        }
        return "hello";
    }

    @RequestMapping(value = "hello")
    public String hello(){
        System.out.println("----hello()----");
        return "hello";
    }
    @RequestMapping(value = "upload")
    public String upload(MultipartFile image, HttpServletRequest request) throws IOException {
        System.out.println("----upload()----");
        //获取文件输入流对象
        InputStream is = image.getInputStream();
        //获取文件名称
        String filename = image.getOriginalFilename();
        //设置上传路径
        String realPath = request.getServletContext().getRealPath("images");
        System.out.println("上传路径="+realPath);
        //设置上传server后的新名称
        String newname = doFilename(filename);
        OutputStream os = new FileOutputStream(new File(realPath,newname));
        int size = IOUtils.copy(is,os);//完成文件的拷贝大小(字节)
        System.out.println("上传"+filename+"到("+realPath+")完毕，共计"+size+"字节，上传后文件名为'"+newname+"'。");
        //资源释放(原则：先开后关，后开先关)
        os.close();is.close();
        return "welcome";
    }
//    private String doFilename(String filename){
//        //获取文件后缀名
//        String extension = FilenameUtils.getExtension(filename);
//        String uuid= UUID.randomUUID().toString();
//
//        return uuid+"."+extension;
//    }
    @RequestMapping(value = "/upload2",method = {RequestMethod.POST})
    public String upload2(MultipartFile[] image, HttpServletRequest request) throws IOException {
        System.out.println("----upload2()----");
        InputStream is = null;
        OutputStream os = null;
        for (MultipartFile imageFile:image) {
            System.out.println("UserController.upload2.foreach");
            //获取文件输入流对象
            is = imageFile.getInputStream();
            //获取文件名称
            String filename = imageFile.getOriginalFilename();
            //设置上传路径
            String realPath = request.getServletContext().getRealPath("/uploads");
            System.out.println("上传路径=" + realPath);
            //设置上传server后的新名称
            String newname = doFilename(filename);
            os = new FileOutputStream(new File(realPath, newname));
            int size = IOUtils.copy(is, os);//完成文件的拷贝大小(字节)
            System.out.println("上传" + filename + "到("+realPath+")完毕，共计" + size + "字节，上传后文件名为'" + newname + "'。");

        }
        os.close();
        is.close();
        return "welcome";
    }
    private String doFilename(String filename){
        //获取文件后缀名
        String extension = FilenameUtils.getExtension(filename);
        String uuid= UUID.randomUUID().toString();

        return uuid+"."+extension;
    }
}