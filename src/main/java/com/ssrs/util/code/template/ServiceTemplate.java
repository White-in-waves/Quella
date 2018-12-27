package com.ssrs.util.code.template;

import com.baomidou.mybatisplus.service.IService;
import com.ssrs.util.code.domain.Generate;
import com.ssrs.util.code.util.CodeUtil;
import com.ssrs.util.code.util.GenerateUtil;
import com.ssrs.util.code.util.TemplateUtil;
import com.ssrs.util.commom.ToolUtil;

import java.nio.file.FileAlreadyExistsException;

/**
 * @author 小懒虫
 * @date 2018/10/25
 */
public class ServiceTemplate {

    /**
     * 生成需要导入的包
     */
    private static void genImport(Generate generate) {
        String tableEntity = generate.getBasic().getTableEntity();
        CodeUtil.importLine(TemplateUtil.getPath(generate) + ".model." + tableEntity);
        CodeUtil.importLine(IService.class);
    }

    /**
     * 生成类字段
     */
    private static void genClazzBody(Generate generate) {
        // 构建数据
//        String var = ToolUtil.lowerFirst(generate.getBasic().getTableEntity());
        String obj = generate.getBasic().getTableEntity();
        String filePath = ServiceTemplate.class.getResource("").getPath()
                + ServiceTemplate.class.getSimpleName() + ".code";

        // 生成Class部分
        String clazzTarget = TemplateUtil.getTemplate(filePath, "Class");
//        clazzTarget = clazzTarget.replace("#{var}", var);
        clazzTarget = clazzTarget.replace("#{obj}", obj);
        CodeUtil.append(clazzTarget).append(CodeUtil.lineBreak);
    }

    /**
     * 生成服务层模板
     */
    public static String generate(Generate generate) {
        CodeUtil.create();
        CodeUtil.setPackageName(TemplateUtil.getPackage(generate, "service"));
        TemplateUtil.genAuthor(generate);
        ServiceTemplate.genImport(generate);
        ServiceTemplate.genClazzBody(generate);

        // 生成文件
        String layer  ="I"+generate.getBasic().getTableEntity();
        generate.getBasic().setTableEntity(layer);
        String filePath = GenerateUtil.getJavaFilePath(generate, "service", "Service");
        try {
            GenerateUtil.generateFile(filePath, CodeUtil.save());
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        }
        return filePath;
    }
}
