package com.sboard.service;

import com.sboard.dto.ArticleDTO;
import com.sboard.dto.FileDTO;
import com.sboard.entity.FileEntity;
import com.sboard.repository.FileRepository;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.context.RSocketPortInfoApplicationContextInitializer;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileService {
    private final FileRepository fileRepository;
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final ModelMapper modelMapper;

    //upload file
    public List<FileDTO> uploadFile(ArticleDTO articleDTO){

        //파일 시스템 경로 구하기
        File fileuploadpath = new File(uploadPath);
        if(!fileuploadpath.exists()){
            fileuploadpath.mkdirs();
        }

          String path=  fileuploadpath.getAbsolutePath();
            //파일 정보객체 불러오기
          List<MultipartFile> files = articleDTO.getFiles();
          //파일 fl
          List<FileDTO> fileDTOs = new ArrayList<>();
          for(MultipartFile file : files){
          if(file.getResource().exists()){
              String OName = file.getOriginalFilename();
                //확장자
              String ext = OName.substring(OName.lastIndexOf("."));
              String Sname= UUID.randomUUID().toString()+ext;

              //파일 저장
              try {
                  file.transferTo(new File(path,Sname));

                  FileDTO fileDTO  =  FileDTO.builder()
                          .oName(OName)
                          .sName(Sname)
                          .build();
                  fileDTOs.add(fileDTO);


              } catch (IOException e) {
                 log.error(e);
              }

          }

      } //for문 종료
        log.info(fileDTOs);
        return fileDTOs;
    }

    //download File
    public ResponseEntity<Resource> downloadFile(int fno) {
        Optional<FileEntity> optFile = fileRepository.findById(fno);

        FileEntity fileEntity = null;
        Resource resource =null;
        if (optFile.isPresent()) {
            fileEntity = optFile.get();
            fileEntity.setDownload();
            FileEntity savedFile = fileRepository.save(fileEntity);
            Path path = Paths.get(uploadPath + fileEntity.getSName());
            try {
                String ContentType = Files.probeContentType(path);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(
                        ContentDisposition.builder("attachment")
                                .filename(fileEntity.getOName(), StandardCharsets.UTF_8)
                                .build());
                headers.add(HttpHeaders.CONTENT_TYPE, ContentType);


                resource = new InputStreamResource(Files.newInputStream(path));

                return ResponseEntity.ok().headers(headers).body(resource);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }
        //java.nio.file
        //uploadPath ==
        return ResponseEntity.ok().body(resource);

    }


    public int insertFile(FileDTO fileDTOS) {
        FileEntity file = modelMapper.map(fileDTOS, FileEntity.class);
        if(file !=null){
            FileEntity savedfile = fileRepository.save(file);
            return savedfile.getFno();
        }
        return 0;
    }
    public FileDTO selectFileByFno(int fno){return null;}

    public FileDTO updateFileDownload(int fno){
        Optional<FileEntity> optFile = fileRepository.findById(fno);
        if (optFile.isPresent()) {
            FileEntity fileEntity = optFile.get();
            fileEntity.setDownload();
            FileEntity savedFile = fileRepository.save(fileEntity);
            return  modelMapper.map(savedFile, FileDTO.class);
        }
        return null;
    }

    public List<FileDTO> selectFileAllByAno(int ano){return null;}
    public void updateFile(FileDTO fileDTO) {}
    public void deleteFile(int fno){}

}
