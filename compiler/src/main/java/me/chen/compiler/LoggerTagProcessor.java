package me.chen.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import me.chen.annotation.TalkingData;

import static me.chen.compiler.Consts.METHOD_INJECT;
import static me.chen.compiler.Consts.TALKING_DATA_INJECT_IMPL;

@SupportedAnnotationTypes("me.chen.annotation.TalkingData")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class LoggerTagProcessor extends AbstractProcessor {

    private Messager messager;

    private HashMap<String, String> mAnnotationHashMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        mAnnotationHashMap = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TalkingData.class);
        if (elements == null || elements.size() == 0) {
            return true;
        }
        parseParams(elements);
        try {
            generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void generate() throws IOException {
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(
                ClassName.get(HashMap.class), TypeName.get(String.class), TypeName.get(String.class));
        ParameterSpec mapParameterSpec = ParameterSpec.builder(mapTypeName, "map").build();
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder(METHOD_INJECT)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(mapParameterSpec);

        String packageName = null;

        for (Map.Entry<String, String> entry : mAnnotationHashMap.entrySet()) {
            String qualifiedName = entry.getKey();
            packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
            String name = entry.getValue();
            injectMethodBuilder.addStatement("map.put($S,$S)", qualifiedName, name);
        }

        TypeSpec typeSpec = TypeSpec.classBuilder(TALKING_DATA_INJECT_IMPL)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(injectMethodBuilder.build())
                .build();
        JavaFile.builder(packageName, typeSpec).build().writeTo(processingEnv.getFiler());
    }

    private void parseParams(Set<? extends Element> elements) {
        for (Element element : elements) {
            TalkingData loggerTag = element.getAnnotation(TalkingData.class);
            String name = loggerTag.name();
            messager.printMessage(Diagnostic.Kind.NOTE, "tag = " + name);
            String qualifiedName = ((TypeElement) element).getQualifiedName().toString();

            messager.printMessage(Diagnostic.Kind.NOTE, "tag = " + name);
            mAnnotationHashMap.put(qualifiedName, name);
        }
    }
}
