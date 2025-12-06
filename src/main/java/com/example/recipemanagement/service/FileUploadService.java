package com.example.recipemanagement.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file) {
        try {
            Map options = new HashMap();
            options.put("resource_type", "auto");

            return cloudinary.uploader().upload(file.getBytes(), options);

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }
}
