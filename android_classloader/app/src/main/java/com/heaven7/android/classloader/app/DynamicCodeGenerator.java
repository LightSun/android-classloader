package com.heaven7.android.classloader.app;

import java.util.Map;

public final class DynamicCodeGenerator {

    private final String template;
    private final Map<String,String> holdMap;
    private final String extraCode;
    private final String imports;

    public DynamicCodeGenerator(String template, Map<String, String> holdMap, String extraCode, String imports) {
        this.template = template;
        this.holdMap = holdMap;
        this.extraCode = extraCode;
        this.imports = imports;
    }

    public String generate(){
        if(!template.contains("package ")){
            throw new IllegalStateException("you must define the statement of package.");
        }
        //,1 add extra code
        int i = template.indexOf(";");// find first package statement. eg: package com.xxx.xx;

        int idx = template.lastIndexOf("}");
        String finalTemplate = template.substring(0, idx) + extraCode + "}";
        // 2, replace
        for (Map.Entry<String,String> en : holdMap.entrySet()){
            String key = en.getKey();
            finalTemplate = finalTemplate.replace("$" + key, en.getValue());
        }
        finalTemplate = finalTemplate.substring(0, i + 1) + imports + finalTemplate.substring(i + 1);
        return finalTemplate;
    }
}
