package com.sboard.service;

import com.sboard.dto.ArticleDTO;
import com.sboard.dto.FileDTO;
import com.sboard.entity.FileEntity;
import com.sboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
          if(!file.isEmpty()){
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
    public void downloadFile(){

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

    public List<FileDTO> selectFileAllByAno(int ano){return null;}
    public void updateFile(FileDTO fileDTO) {}
    public void deleteFile(int fno){}

}
