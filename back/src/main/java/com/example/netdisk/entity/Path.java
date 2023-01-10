package com.example.netdisk.entity;

import com.example.netdisk.common.entity.LogicEntity;
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
public class Path extends LogicEntity {

    @Column
    private String pathName;

    @OneToMany(mappedBy = "path",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"path"})
    private Set<Document> documents;

    @OneToMany(mappedBy = "path",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"path"})
    private Set<DocFolder> docFolders;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"paths"})
    private CommenUser commenUser;

    public Document addDoc(MultipartFile File){
        Document document=new Document();
        document.setPath(this);
//        System.out.println(File.getOriginalFilename());
        document.setDocumentName(File.getOriginalFilename());
//        System.out.println(Double.valueOf(File.getSize()));
        document.setSize(Double.valueOf(File.getSize())/1024.0);
//        System.out.println(File.getContentType());
        document.setType(File.getContentType());
        documents.add(document);
        return document;
    }

    public DocFolder addDocFile(String docFileName){
        DocFolder docFolder =new DocFolder();
        docFolder.setPath(this);
        docFolder.setDocFileName(docFileName);
        docFolders.add(docFolder);
        return docFolder;
    }

    public Document findDoc(String docName){
        AtomicReference<Document> result= new AtomicReference<>(null);
        documents.forEach(document ->{
            if(document.getDocumentName().equals(docName)){
                result.set(document);
            }
        });
        return result.get();
    }

    public DocFolder findDocFile(String docFileName){
        AtomicReference<DocFolder> result= new AtomicReference<>(null);
        docFolders.forEach(docFolder ->{
            if(docFolder.getDocFileName().equals(docFileName)){
                result.set(docFolder);
            }
        });
        return result.get();
    }


    public void deleteDoc(String docName){
        Document document =findDoc(docName);
        document.setIsDeleted(1);
    }

    public void deleteDocFile(String docFileName){
        DocFolder docFolder =findDocFile(docFileName);
        docFolder.setIsDeleted(1);
    }
}
