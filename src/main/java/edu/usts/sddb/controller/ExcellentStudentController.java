package edu.usts.sddb.controller;

import edu.usts.sddb.entity.Advice;
import edu.usts.sddb.entity.ExcellentStudent;
import edu.usts.sddb.entity.pack.ObjectQuery;
import edu.usts.sddb.service.ExcelService;
import edu.usts.sddb.service.ExcellentStudentService;
import edu.usts.sddb.util.Excel.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("excellentStudent")
public class ExcellentStudentController {

    @Autowired
    ExcellentStudentService excellentStudentService;

    @Autowired
    ExcelService excelService;

    @RequestMapping("/find")
    @ResponseBody
    public ObjectQuery findExcellentStudentByPage(Boolean _search, String filters, int page, int rows)
            throws UnsupportedEncodingException {
        if (filters != null) {
            // 转码
            // filters = new String(filters.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println(filters);
        }
        return excellentStudentService.findByPage(_search, filters, page, rows);

    }

    @RequestMapping("/handle")
    @ResponseBody
    public String handleExcellentStudent(String oper, ExcellentStudent excellentStudent, String id[])
            throws UnsupportedEncodingException {
        String temp = excellentStudentService.handle(oper, excellentStudent, id);
        // 对传回的中文进行编码
        return URLEncoder.encode(temp, "UTF-8");
    }

    //-----------------------------------导入导出---------------------------------------------------------

    //先下载模版文件，再上传模版文件进行导入
    @RequestMapping("/downloadTemplate")
    @ResponseBody
    public void downloadTemplate(HttpServletResponse response) {
        HSSFWorkbook workbook = excelService.getTemplate("excellentStudent");
        ExcelUtil.downloadExcel(response, workbook, "excellentStudent.xls");
    }

    //导入excel
    @RequestMapping("/importExcel")
    @ResponseBody
    public String importExcel(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        String message = excellentStudentService.importExcel(request, response);
        return URLEncoder.encode(message, "UTF-8");
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletResponse response, Boolean _search, String filters, int page, int rows) {
        HSSFWorkbook workbook = excellentStudentService.export(_search, filters, page, rows);
        ExcelUtil.downloadExcel(response, workbook, "excellentStudent.xls");
    }

}
