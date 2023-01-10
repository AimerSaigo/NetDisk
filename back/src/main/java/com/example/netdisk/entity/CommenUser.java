package com.example.netdisk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = 0")
@DiscriminatorValue("0")
public class CommenUser extends User{

    public CommenUser(){
        super();
    }

    @Column
    protected String commenUserName;

    @OneToMany(mappedBy = "commenUser",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"commenUser"})
    private Set<Path> paths;

    public Path findPath(String pathName){
        AtomicReference<Path> result= new AtomicReference<>(null);
        paths.forEach(path ->{
            if(path.getPathName().equals(pathName)){
                result.set(path);
            }
        });
        return result.get();
    }

    public Path addDocToPath(String pathName, MultipartFile File){
        Path path=findPath(pathName);
        path.addDoc(File);
        return path;
    }

    public Path addDocFileToPath(String pathName,String docFolderName){
        Path path=findPath(pathName);
        path.addDocFile(docFolderName);
        return path;
    }

    public Path deleteDocToPath(String pathName,String documentName){
        Path path=findPath(pathName);
        path.deleteDoc(documentName);
        return path;
    }

    public Path deleteDocFileToPath(String pathName,String docFileName){
        Path path=findPath(pathName);
        path.deleteDocFile(docFileName);
        return path;
    }

    public  void deletePath(String pathname){
        Path path=findPath(pathname);
        path.setIsDeleted(1);
    }

    public Document getFile(String pathName,String docName){
        Path path=findPath(pathName);
        Document document=path.findDoc(docName);
        return document;
    }

    public void addPath(Path path){
        paths.add(path);
    }

}
