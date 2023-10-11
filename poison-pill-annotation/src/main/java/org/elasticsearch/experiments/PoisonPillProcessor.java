package org.elasticsearch.experiments;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.Set;

import org.elasticsearch.common.Version;

public class PoisonPillProcessor extends AbstractProcessor {
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set,
                           RoundEnvironment roundEnv) {

        messager.printMessage(Diagnostic.Kind.OTHER, "Processing annotations");
        for (var annotatedElement : roundEnv.getElementsAnnotatedWith(PoisonPill.class)) {
  		    var annotation = annotatedElement.getAnnotation(PoisonPill.class);
              try {
                  var major = Integer.parseInt(annotation.expireVersion());
                  if (major <= Version.CURRENT.getMajor()) {
                      messager.printMessage(
                              Diagnostic.Kind.ERROR, "Poison pill", annotatedElement);
                  }
              }
              catch (NumberFormatException ignored) {
                  messager.printMessage(
                          Diagnostic.Kind.ERROR, "ExpireVersion must be an Integer", annotatedElement);
              }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(PoisonPill.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
