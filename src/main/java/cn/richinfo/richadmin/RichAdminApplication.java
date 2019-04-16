package cn.richinfo.richadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//开启总的定时任务
@EnableScheduling
@ComponentScan(basePackages = {"cn.richinfo"})
public class RichAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(RichAdminApplication.class, args);
	}

}
