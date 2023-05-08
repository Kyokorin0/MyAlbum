package com.kyoko.myalbum.File;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author young
 * @create 2023/5/8 10:46
 * @Description
 */
public class ExifUtil {
    Metadata metadata = null;

    public ExifUtil(File file) throws ImageProcessingException, IOException {
        metadata = ImageMetadataReader.readMetadata(file);
    }

    public ExifUtil(String path) throws ImageProcessingException, IOException {
        File file = new File(path);
        metadata = ImageMetadataReader.readMetadata(file);
    }

    public ExifUtil(InputStream is) throws ImageProcessingException, IOException {
        metadata = ImageMetadataReader.readMetadata(is);
    }

    //获取Exif信息文件夹
    private Iterable<Directory> getDirectoryList() {
        Iterable<Directory> directories = metadata.getDirectories();
        return directories;
    }

    private List<Directory> getDirectory() {
        List<Directory> listDir = new ArrayList<Directory>();

        for (Iterator<Directory> iterator = getDirectoryList().iterator(); iterator.hasNext(); ) {
            Directory next = iterator.next();
            listDir.add(next);
        }
        return listDir;
    }

    //获取Directory对象下的Tag集合,getTags方法有重名
    private List<Collection<Tag>> getListTag() {
        List<Collection<Tag>> listTag = new ArrayList<>();
        for (Directory d : getDirectory()) {
            listTag.add(d.getTags());
        }
        return listTag;
    }

    //获取Exif分类文件夹的名称
    private String getExifDirectoryName(int type) {
        String directoryName = "File";
        switch (type) {
            case 1:
                directoryName = "Exif SubIFD";
                break;
            case 2:
                directoryName = "Exif IFD0";
                break;
            case 3:
                directoryName = "JFIF";
                break;
            case 4:
                directoryName = "JPEG";
                break;
            case 5:
                directoryName = "File";
            default:
                break;
        }
        return directoryName;
    }

    //通过TagKey获取指定键的Exif元数据的属性值
    public String getTagByKey(String tagKey, int type) {
        String tagValue = "";
        if (type > 5)
            return tagValue;
        List<Collection<Tag>> listTag = getListTag();
        boolean sign = true;
        for (Collection<Tag> tagColl : listTag) {
            if (!sign)
                break;
            for (Tag tag : tagColl) {
                if (tag.getTagName().equals(tagKey)
                        && tag.getDirectoryName().equals(getExifDirectoryName(type))) {
                    tagValue = tag.getDescription();
                    sign = false;
                    break;
                }
            }
        }
        return tagValue;
    }
    //按Directory组织所有tag
    public HashMap<String, HashMap<String,String>> getExifMap() {
        HashMap<String, HashMap<String,String>> dirMap = new HashMap<>();
        for(Directory d : getDirectory()){
            String dirName = d.getName();
            HashMap<String, String> map = new HashMap<>();
            for (Tag tag:d.getTags()){
                map.put(tag.getTagName(), tag.getDescription());
            }
            dirMap.put(dirName,map);
        }
        return dirMap;
    }

    //获取图片的所有Exif信息
    public HashMap<String, String> getAllTags() {
        HashMap<String, String> map = new HashMap<>();
        List<Collection<Tag>> listTag = getListTag();
        for (Collection<Tag> tagColl : listTag) {
            for (Tag tag : tagColl) {
                map.put(tag.getTagName(), tag.getDescription());
            }
        }
        return map;
    }

    //
    private String getExifDateTimeByKeyFromHashMap(String key) {
        HashMap<String, String> hash = getAllTags();
        return hash.get(key);
    }

    //获取图片Exif日期元数据
    public String getExifDateTime(boolean ishash) {
        String currTime = "";
        if (!ishash) {
            currTime = getTagByKey("Date/Time Original", 1);
            if (currTime != "") {
                currTime = currTime.split(" ")[0].replace(':', '-')
                        + currTime.split(" ")[1];
            } else {
                currTime = getTagByKey("File Modified Date", 5);
            }
        } else {
            //// Tue Jun 22 09:28:12 CST 2010
            currTime = getExifDateTimeByKeyFromHashMap("Date/Time Original");
            if (currTime == null || currTime.equals("")) {
                currTime = getExifDateTimeByKeyFromHashMap("File Modified Date");
            }
        }
        return currTime;
    }
    //获取Exif相机制造商名称
    public String getExifMake() {
        String make = "";
        make = getTagByKey("Make", 2);
        return make;
    }
    //获取Exif相机型号
    public String getExifModel() {
        String model = "";
        model = getTagByKey("Model", 2);
        return model;
    }
}


