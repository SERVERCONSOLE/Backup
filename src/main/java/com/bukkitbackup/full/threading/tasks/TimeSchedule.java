/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkitbackup.full.threading.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Domenic Horner
 */
public class TimeSchedule {
    Calendar currentDate = Calendar.getInstance();
    SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
    String dateNow = formatter.format(currentDate.getTime());
    
    
}
