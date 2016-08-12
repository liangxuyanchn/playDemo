package controllers;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hamcrest.core.IsInstanceOf;

import models.Client;
import models.Person;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void show() {
    	Client client = new Client("127.0.0.1", "8080"); renderArgs.put("client", client);
    
    	renderArgs.put("client", client);
    	render();
    }

	public static void poi() {
		
		List<Person> personList=new ArrayList<Person>();
		Person p1=new Person();
		p1.id=11;
		p1.name="p1";
		
		Person p2=new Person();
		p2.id=12;
		p2.name="p2";
		
		Person p3=new Person();
		p3.id=13;
		p3.name="p3";
		
		personList.add(p1);
		personList.add(p2);
		personList.add(p3);
		
		String name="人口统计表";
		
		String[] arr1=new String[]{"用户名ID","姓名"};
		String[] arr2=new String[]{"id","name"};
		
		JsonConfig jsonConfig = new JsonConfig();
		
		//时间类型显示格式
    	jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
    	
    	//double类型显示格式
    	jsonConfig.registerJsonValueProcessor(Double.class, new JsonDoubleValueProcessor("##,##0.00"));
    	JSONArray arrList = JSONArray.fromObject(personList, jsonConfig);
    	  
		File file = ExcelUtils.export(name, arrList, arr1, arr2);
		
		renderBinary(file);
	}

    
}