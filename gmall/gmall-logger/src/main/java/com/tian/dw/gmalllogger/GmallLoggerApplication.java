package com.tian.dw.gmalllogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GmallLoggerApplication {
/*

java -cp ./gmall-logger-0.0.1-SNAPSHOT.jar:/opt/module/scala-2.11.8/lib/scala-library org.springframework.boot.loader.JarLauncher
java -Djava.ext.dirs=/opt/module/scala-2.11.8/lib -cp ./gmall-logger-0.0.1-SNAPSHOT.jar org.springframework.boot.loader.JarLauncher
 */
    public static void main(String[] args) {
        SpringApplication.run(GmallLoggerApplication.class, args);
    }

}
