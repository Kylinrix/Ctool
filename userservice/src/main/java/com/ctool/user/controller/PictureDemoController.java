package com.ctool.user.controller;

import com.ctool.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/11 20:16
 * @Email: Kylinrix@outlook.com
 * @Description: 本机的文件夹作为 图片服务器。 后期可以使用待框架的服务器专门处理头像与文件传输。
 */
@Controller
public class PictureDemoController {
    @Autowired
    ResourceLoader resourceLoader;


    private static final String mImagesPath = "/Users/lky/Desktop/testPic/";

    public static boolean upload(MultipartFile file, String path, String fileName){

        // 生成新的文件名为避免重复
        //String realPath = path + "/" + UUID.randomUUID().toString().replace("-", "")+fileName.substring(fileName.lastIndexOf("."));

        //使用原文件名 方便测试。
        String realPath = path  + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    @ResponseBody
    @RequestMapping(path={"/fileUpload"})
    public String upload(@RequestParam("fileName") MultipartFile file, Map<String, Object> map){

        // 要上传的目标文件存放路径
        String localPath =mImagesPath;
        // 上传成功或者失败的提示


        if (upload(file, localPath, file.getOriginalFilename())){
            // 上传成功，给出页面提示
            return JsonUtil.getJSONString(0);
        }else {
            return JsonUtil.getJSONString(1,"上传失败");

        }
        // 显示图片
        //map.put("msg", msg);
        //map.put("fileName", file.getOriginalFilename());

    }


    //本地显示图片方法，可升级为图片服务器直接由URL得到。
    @RequestMapping(method = RequestMethod.GET, value = "/getfile/{filename}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename,HttpServletResponse response,
                                     HttpServletRequest request) {
        try {
            //response.setContentType("image/jpeg");

            //因为不是在classpath中的文件，所以要加上file前缀
            //classpath即指target/classes/ 路径，即已编译的源文件。
            return ResponseEntity.ok(resourceLoader.getResource("file:"+mImagesPath+filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @RequestMapping(path = {"/testpic"})
    public String getPic(){
        return "picTest";
    }

}
