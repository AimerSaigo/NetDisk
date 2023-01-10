package com.example.netdisk.controller;

import com.example.netdisk.common.controller.LogicController;
import com.example.netdisk.dao.CommenUserDao;
import com.example.netdisk.entity.CommenUser;
import com.example.netdisk.entity.Document;
import com.example.netdisk.entity.Path;
import com.example.netdisk.service.CommenUserService;
import com.example.netdisk.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "*")
@Api(tags = "普通用户实体的控制器")
@RestController
@RequestMapping("/commenUser")
public class CommenUserController extends LogicController<CommenUserService, CommenUserDao, CommenUser, Long> {
    public CommenUserController(@Autowired CommenUserService ss) {
        super(ss);
    }

    @ApiOperation("测试用方法")
    @GetMapping("/test")
    public String test () {
        return "OK";
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login (@ApiParam("用户名")String username, @ApiParam("用户名")String password) {
        return getService().login(username,password);
    }

    @ApiOperation("查询用户名")
    @GetMapping("/queryUsername")
    public String queryUsername (@ApiParam("token")String token) {

        return getService().queryUsername(token);

    }

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    @ResponseBody
    public Path UserUploadFile(@ApiParam("token")String token, @ApiParam("路径名") String pathName, @RequestParam MultipartFile File, HttpServletRequest request) throws IOException {
        return getService().UserUploadFile(token,pathName,File,request);
    }


    @ApiOperation("下载文件")
    @GetMapping("/downloadDoc")
    public String download(HttpServletResponse response,@ApiParam("token")String token, @ApiParam("路径名") String pathName, @ApiParam("文件名") String docName) throws Exception {
        return getService().download(token,pathName,docName,response);
    }

    @ApiOperation("新建文件夹")
    @GetMapping("/mkdir")
    public Path UserAdddocfolder(@ApiParam("token")String token,@ApiParam("路径名") String pathName, @ApiParam("文件夹名")String docFoldername){
        return getService().UserAdddocfolder(token, pathName, docFoldername);
    }

    @ApiOperation("删除文件")
    @GetMapping("/deldoc")
    public Path UserDeleteFile(@ApiParam("token")String token,@ApiParam("路径名") String pathName,@ApiParam("文件名") String filename){
        return getService().UserDeleteFile(token, pathName, filename);
    }

    @ApiOperation("删除文件夹")
    @GetMapping("/deldocFolder")
    public Path UserDeletedocFolder(@ApiParam("token")String token, @ApiParam("路径名")String pathName,@ApiParam("文件夹名") String docFoldername){
        return getService().UserDeletedocFolder(token, pathName, docFoldername);
    }

    @ApiOperation("获取目录文件")
    @GetMapping("/getdocFolder")
    public Path getPath(@ApiParam("token")String token,@ApiParam("路径名")String pathName){
        return getService().getPath(token, pathName);
    }

    @ApiOperation("初始化")
    @GetMapping("/reflash")
    public CommenUser commenUserInit(@ApiParam("用户ID")Long id){
        System.out.println(id);
        return getService().commenUserInit(id);
    }



}