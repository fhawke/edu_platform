package com.lsh.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现写操作

        //设置写入文件夹地址
        //String filename = "F:\\write.xlsx";

        //调用EasyExcel实现写操作
        //第一个参数文件名称，第二个参数实体类class
        //EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

        //读
        String filename = "F:\\write.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("lucy"+i);
            list.add(data);
        }
        return list;
    }
}
