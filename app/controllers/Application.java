package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

@With(ExcelControllerHelper.class)
public class Application extends Controller {

    public static void index() {
        List<DivaCalendar> calendars = 
            DivaCalendar.find("order by category").fetch();
        render(calendars);
    }

    public static void excel() {
        List<DivaCalendar> calendars = 
            DivaCalendar.find("order by category").fetch();
        request.format = "xls";
        String __FILE_NAME__ = "calendar.xls";
        render(__FILE_NAME__, calendars);
    }

}
