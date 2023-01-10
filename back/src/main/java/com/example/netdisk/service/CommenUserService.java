package com.example.netdisk.service;

import com.example.netdisk.common.service.LogicService;
import com.example.netdisk.dao.CommenUserDao;
import com.example.netdisk.dao.DocFileDao;
import com.example.netdisk.dao.DocumentDao;
import com.example.netdisk.dao.PathDao;
import com.example.netdisk.entity.CommenUser;
import com.example.netdisk.entity.Document;
import com.example.netdisk.entity.Path;
import com.example.netdisk.entity.User;
import com.example.netdisk.utils.JwtUtils;
import com.example.netdisk.utils.Result;
import io.jsonwebtoken.Claims;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class CommenUserService extends LogicService<CommenUserDao, CommenUser,Long> {
    public CommenUserService(@Autowired CommenUserDao dao) {
        super(dao);
    }


    @Resource
    private DocumentDao documentDao;
    @Resource
    private DocFileDao docFileDao;

    @Resource
    private PathDao pathDao;

    @Resource
    private CommenUserDao commenUserDao;

    private JwtUtils utils;
    public CommenUser findUserByToken(String token){
        Claims claims=JwtUtils.getClaimsByToken(token);
        Long id=Long.parseLong(claims.getSubject());
        CommenUser commenUser=getDAO().getReferenceById(id);
        return commenUser;
    }

    public Result login(String username,String password){
        password=User.getMD5password(password);
        System.out.println(password);
        CommenUser commenUser=commenUserDao.findByUsernameAndPassword(username,password);
        Result result=new Result();
        result.setCode(404);
        result.setToken("*******查无此人*******");
        if(commenUser!=null){
            result.setCode(200);
            result.setToken(JwtUtils.generateToken(commenUser.getId().toString()));
        }
        return result;
    }

    public String queryUsername(String token){
        Claims claims=JwtUtils.getClaimsByToken(token);
        Long id=Long.parseLong(claims.getSubject());
        CommenUser commenUser=getDAO().getReferenceById(id);
        System.out.println(commenUser);
        return "OK";
    }

    public Path UserAddFile(String token, String pathName, MultipartFile File) {
        CommenUser commenUser = findUserByToken(token);
        Path path = commenUser.addDocToPath(pathName,File);
        pathDao.save(path);
        return path;
    }

    public String up(String path, MultipartFile files, HttpServletRequest request) throws IOException {
        System.out.println(files.getOriginalFilename());
        System.out.println(files.getContentType());
        System.out.println(files.getSize());
        System.out.println(path);
        saveFile(files, path, request);
        return "上传成功";
    }

    public void saveFile(MultipartFile files, String path, HttpServletRequest request) throws IOException {
        String pathhead = request.getServletContext().getRealPath("/upload");
        path = "D:/SpringBootUpload" + path;
        File dir = new File(path);
        System.out.println(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(path + "/" + files.getOriginalFilename());
        System.out.println(file.getAbsolutePath());
        files.transferTo(file);
    }

    public Path UserUploadFile(String token, String pathName, MultipartFile File, HttpServletRequest request) throws IOException {
        CommenUser commenUser = findUserByToken(token);
        Path path = commenUser.addDocToPath(pathName, File);
        pathDao.save(path);
        up(pathName, File, request);
        return path;
    }


    public String download(String token, String pathName, String docfilename, HttpServletResponse response)throws Exception {
        CommenUser commenUser = findUserByToken(token);
        Document document = commenUser.getFile(pathName, docfilename);
        String path="D:/SpringbootUpload"+pathName+docfilename;
        System.out.println("path:"+path);
        File file=new File(path);
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename="+document.getDocumentName());
        System.out.println(file.length());
        System.out.println("——————————————————————————————————————————");

        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition","attachment;filename"+docfilename);
        byte[] readBytes= FileUtil.readAsByteArray(file);
        OutputStream os=response.getOutputStream();
        os.write(readBytes);
        return "下载成功";
    }



    public Document UserFindFile(String token, String pathName, String docfilename) {
        CommenUser commenUser = findUserByToken(token);
        Document document = commenUser.getFile(pathName, docfilename);
        return document;
    }


    public Path UserAdddocfolder(String token, String pathName, String docfoldername) {
        CommenUser commenUser = findUserByToken(token);
        Path path = commenUser.addDocFileToPath(pathName, docfoldername);
        pathDao.save(path);
        Path newPath = new Path();
        newPath.setPathName(pathName + docfoldername + "/");
        newPath.setCommenUser(commenUser);
        File dir = new File(newPath.getPathName());
        if (!dir.exists()) {
            dir.mkdir();
        }
        commenUser.addPath(newPath);
        pathDao.save(newPath);
        getDAO().save(commenUser);
        return path;
    }

    public Path UserDeleteFile(String token, String pathName, String filename) {
        CommenUser commenUser = findUserByToken(token);
        Path path = commenUser.deleteDocToPath(pathName, filename);
        pathDao.save(path);

        return path;
    }

    public Path UserDeletedocFolder(String token, String pathName, String docFoldername) {
        CommenUser commenUser = findUserByToken(token);
        Path path = commenUser.deleteDocFileToPath(pathName, docFoldername);
        pathDao.save(path);
        pathName = pathName + docFoldername + "/";
        commenUser.deletePath(pathName);
        getDAO().save(commenUser);

        return path;
    }


    public Path getPath(String token, String pathName) {
        CommenUser commenUser = findUserByToken(token);
        Path path = commenUser.findPath(pathName);
        return path;
    }

    public CommenUser commenUserInit(Long id) {
        CommenUser commenUser = getDAO().getReferenceById(id);
        Path path = new Path();
        path.setCommenUser(commenUser);
        path.setPathName("/" + commenUser.getUsername() + "/");
        commenUser.addPath(path);
        File dir = new File("D:/SpringBootUpload" + path.getPathName());
        if (!dir.exists()) {
            dir.mkdir();
        }
        getDAO().save(commenUser);
        return commenUser;
    }
}
